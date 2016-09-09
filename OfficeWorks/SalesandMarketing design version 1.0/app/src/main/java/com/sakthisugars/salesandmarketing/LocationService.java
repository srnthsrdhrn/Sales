package com.sakthisugars.salesandmarketing;

/**
 * Created by singapore on 23-07-2016.
 */

import android.Manifest;
import android.app.Notification;
// for level below 11
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.widget.Toast;
import android.os.Process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationService extends Service {
    private NotificationManager mNM;
    private int NOTIFICATION = R.string.local_service_started;
    private LocationManager mgr;
    private Locationer gps_locationer, network_locationer;
    public static int START;
    private NotificationCompat.Builder builder;
    private Location location;
    private Handler mHandler;
    private Database_handler database_handler;
    SQLiteDatabase db;
    //private String Url = "http://192.168.1.28:900/Android.svc/SaveEmployeeStartOver?UserId=DEVELOP&Longitude=45.133&Latitude=17.188&EntryDateTime=2016-08-09%2010:45:01.253";
    long mInterval;
    String userid;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Display a notification about us starting.  We put an icon in the status bar.
        //android.os.Debug.waitForDebugger();
        userid= intent.getExtras().getString("userid");
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        gps_locationer = new Locationer(getBaseContext());
        network_locationer = new Locationer(getBaseContext());
        database_handler= new Database_handler(this,Database_handler.DATABASE_NAME,null,Database_handler.DATABASE_VERSION);
        db=database_handler.getWritableDatabase();
        mHandler = new Handler();
        mStatusChecker.run();
        String[] time = (database_handler.GetSettings(db, Database_handler.SETTINGS_LOCATION_CHECK_TIMER)).split(";");
        int hour = Integer.parseInt(time[0]);
        int minutes= Integer.parseInt(time[1]);
        mInterval = hour*60*60+minutes*60;

        return 0;
    }


    final Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {

                START = 1;
                Criteria criteria = new Criteria();
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(false);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String providerFine = mgr.getBestProvider(criteria, true);
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String providerCoarse = mgr.getBestProvider(criteria, true);

                if (providerCoarse != null) {
                    if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mgr.requestLocationUpdates(providerCoarse, 30000, 5, network_locationer);
                    location = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (providerFine != null&&(mgr.getAllProviders().contains(LocationManager.GPS_PROVIDER))) {
                    mgr.requestLocationUpdates(providerFine, 2000, 0, gps_locationer);
                    location = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }finally {
                if(location!=null) {
                    double lat = location.getLatitude() % 0.00001;
                    lat = location.getLatitude() - lat;
                    double longt = location.getLongitude() % 0.00001;
                    longt = location.getLongitude() - longt;
                    showNotification("Lat: "+ lat+"Long: "+ longt);
                    Date date = new Date();
                    database_handler.storeLocation(db, lat + "", longt + "", date.getTime() + "");
                    Date date1 = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
                    String date_text = simpleDateFormat.format(date1);
            String url = "http://192.168.1.28:900/Android.svc/SaveEmployeeStartOver?UserId="+userid+"&Longitude="+location.getLongitude()+"&Latitude="+location.getLatitude()+"&EntryDateTime="+date_text;
                    new StoreLocation().execute(url);
                }
                mHandler.postDelayed(mStatusChecker,mInterval);
            }
        }
    };
    @Override
    public void onDestroy() {
        START=0;
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);
        mgr.removeUpdates(gps_locationer);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mgr.removeUpdates(network_locationer);
        // Tell the user we stopped.
        mHandler.removeCallbacks(mStatusChecker);
        Toast.makeText(this, "local service is stopped", Toast.LENGTH_SHORT).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification(String text) {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Login.class), 0);

        builder = new NotificationCompat.Builder(getBaseContext())
                .setContentTitle("You are being tracked...")
                .setContentText(text)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentIntent(contentIntent)
                .setOngoing(true);

        Notification notification = builder.getNotification();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }
    private class StoreLocation extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {

                    String string = "";
                    try {
                        URL url = new URL(params[0]);
                        try {
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setConnectTimeout(10000);
                            httpURLConnection.setReadTimeout(10000);
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                StringBuilder stringBuilder = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    stringBuilder.append(line);
                                    stringBuilder.append("\n");
                                }
                                string = stringBuilder.toString();
                                reader.close();
                                inputStream.close();
                            }
                            httpURLConnection.disconnect();
                        } catch (SocketTimeoutException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}


