package com.sakthisugars.salesandmarketing;

/**
 * Created by singapore on 23-07-2016.
 */

import android.Manifest;
import android.app.Notification;
// for level below 11
import android.content.pm.PackageManager;
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

public class LocationService extends Service {
    private NotificationManager mNM;
    private int NOTIFICATION = R.string.local_service_started;
    private LocationManager mgr;
    private Locationer gps_locationer, network_locationer;
    public static int START;
    private NotificationCompat.Builder builder;
    private Location location;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        android.os.Debug.waitForDebugger();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Display a notification about us starting.  We put an icon in the status bar.
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        gps_locationer = new Locationer(getBaseContext());
        network_locationer = new Locationer(getBaseContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                START=1;
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
                    location= mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (providerFine != null) {
                    mgr.requestLocationUpdates(providerFine, 2000, 0, gps_locationer);
                    location=mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                showNotification("Latitude: "+location.getLatitude()+"\n Longitude: "+ location.getLongitude());            }
        },3000);
        return 0;
    }



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
}
