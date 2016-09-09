package com.sakthisugars.salesandmarketing;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 7/25/2016.
 */
public class Payment_Approval extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Button save, cancel, sms;
    GPSTracker gpsTracker;
    TextView txtlatitude,txtlongtitude;
    EditText cus_name,area, pincode, executivenumber, company_number, customer_number;
    String customer_no, company_no, executive_no, customer_name, name, ph_number;
    ArrayList<String> values = new ArrayList<String>();
    String CHAR_LIST;
    private static final int RANDOM_STRING_LENGTH =5;
    DrawerLayout drawer;
    Database_handler database_handler;
    SQLiteDatabase db;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_approval_drawer);
        Intent intentval = getIntent();
        userid = intentval.getStringExtra("userid");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //...............................................................................................................
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        sms = (Button) findViewById(R.id.sms);
        cus_name = (EditText) findViewById(R.id.cus_name);
        executivenumber = (EditText) findViewById(R.id.employee_no);
        customer_number = (EditText) findViewById(R.id.cus_no);
        company_number = (EditText) findViewById(R.id.company_no);
        txtlatitude = (TextView) findViewById(R.id.cus_latitude);
        txtlongtitude = (TextView) findViewById(R.id.cus_lontitude);
        customer_name = cus_name.getText().toString();
        customer_no = customer_number.getText().toString();
        company_no = company_number.getText().toString();
        executive_no = executivenumber.getText().toString();
         ActivityCompat.requestPermissions(Payment_Approval.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        gpsTracker = new GPSTracker(Payment_Approval.this);
        gpsTracker.getLocation();
        if (!gpsTracker.canGetLocation) {
            gpsTracker.showSettingsAlert();
        } else{
            float loc_lat = (float) (gpsTracker.location.getLatitude());
            float loc_long = (float) (gpsTracker.location.getLongitude());
            txtlongtitude.setText(String.valueOf(loc_long));
            txtlatitude.setText(String.valueOf(loc_lat));
        }
        //  int loc_lat = (int) gpsTracker.location.getLatitude();
        // Toast.makeText(Payment_Approval.this, message, Toast.LENGTH_LONG).show();
        //int loc_long = (int)gpsTracker.location.getLongitude();
        //  Toast.makeText(Payment_Approval.this, message, Toast.LENGTH_LONG).show();
        // txtlatitude.setText(loc_lat);
        // txtlongtitude.setText(loc_long);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                  /*  ActivityCompat.requestPermissions(Payment_Approval.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                gpsTracker = new GPSTracker(Payment_Approval.this);
                gpsTracker.getLocation();
                if (!gpsTracker.canGetLocation) {
                    gpsTracker.showSettingsAlert();
                } else{
                   float loc_lat = (float) (gpsTracker.location.getLatitude());
                   float loc_long = (float) (gpsTracker.location.getLongitude());
                   txtlongtitude.setText(String.valueOf(loc_long));
                   txtlatitude.setText(String.valueOf(loc_lat));
                }*/
                    String na = "haliff";
                    String no = "12346561234";
                    name = na.substring(1, 3);
                    ph_number = no.substring(1, 3);
                    CHAR_LIST = name + ph_number + "qw678eriQWERTplkvbnmYUIjxcSDFGHhgfdsazJKLZXCVBNM1234tyuPA590";
                    String message = generateRandomString();
                    String msg="Your OTP is "+message+".Thank you for purchase.";
                   Toast.makeText(Payment_Approval.this,msg, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(Payment_Approval.this, "enter correct value", Toast.LENGTH_LONG).show();
                }
            }

        });
       /* sms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    // String name = customer_name.substring(0, 3);
                    // String ph_number = customer_no.substring(0, 3);
                    // CHAR_LIST =name+ph_number;
                    // SmsManager sms = SmsManager.getDefault();
                    // String message="hello";
                    String na = "JSHDFkjhjh";
                    String no = "1234df65612ASM34";
                    name = na.substring(1, 3);
                    ph_number = no.substring(1, 3);
                    CHAR_LIST = name + ph_number + "qw678eriQWERToplkvbnmYUIjxcSDFGHhgfdsazJKLZXCVBNM1234tyuOPA590";
                    String message = generateRandomString();
                    String msg="Your OTP is"+message+"Thank you for purchase";
                    String[] numbers = new String[]{customer_no, company_no, executive_no};
                    for (String number : numbers) {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(number, null, msg, null, null);
                        Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(Payment_Approval.this, "SMS faild , please try again later!", Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }
    boolean flag = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(flag){
                super.onBackPressed();
            }else{
                Toast.makeText(this,"Press Back Again to Exit",Toast.LENGTH_LONG).show();
                flag=true;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.customerpage:
                finish();
                Intent intent2 = new Intent(Payment_Approval.this, CustomerPage.class);
                intent2.putExtra("userid", userid);
                startActivity(intent2);
               // startActivity(new Intent(Payment_Approval.this,SchemeSelection.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * This method generates random string
     *
     * @return
     */
    public String generateRandomString() {

        StringBuffer randStr = new StringBuffer();
        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    /**
     * This method generates random numbers
     *
     * @return int
     */
    private int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }


    public  class GPSTracker extends Service implements LocationListener {

        private final Context mContext;

        //Activity for requesting permission
        private Activity activity;

        //Request code for the permission
        int REQUEST_CODE= 100;
        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        public double latitude; // latitude
        public double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60* 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPSTracker(Context context) {
            this.mContext = context;
            this.activity=Payment_Approval.this;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ContextCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    turnGPSOn();
                    getLocation();
                    stopUsingGPS();
                }
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    Toast.makeText(mContext,"Location is not Available without the Permission",Toast.LENGTH_LONG).show();
                }
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            }else {
                try {
                    turnGPSOn();
                    getLocation();
                    stopUsingGPS();
                }catch (SecurityException e){
                    e.printStackTrace();

                }
            }

        }

        public void getLocation() throws  SecurityException {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }

                    }
                    stopUsingGPS();
                }
            }

        }


        /**
         * Stop using GPS listener Calling this function will stop using GPS in your
         * app.
         * */
        public void stopUsingGPS() throws SecurityException {
            if (locationManager != null) {

                locationManager.removeUpdates(GPSTracker.this);
                turnGPSOff();
            }
        }

        /**
         * Function to show settings alert dialog On pressing Settings button will
         * launch Settings Options
         * */
        public void showSettingsAlert() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting DialogHelp Title
            alertDialog.setTitle("GPS settings");

            // Setting DialogHelp Message
            alertDialog
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            mContext.startActivity(intent);
                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });

            // Showing Alert Message

            alertDialog.show();
        }

        @Override
        public void onLocationChanged(Location location){
            float bestAccuracy = -1f;
            if (location.getAccuracy() != 0.0f && (location.getAccuracy() < bestAccuracy) || bestAccuracy == -1f) {
                try {
                    locationManager.removeUpdates(this);
                }catch(SecurityException e){
                    e.printStackTrace();
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                }
            }
            bestAccuracy = location.getAccuracy();
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0){
            gpsTracker.getLocation();
        }else{
            Toast.makeText(this,"Location Service Denied",Toast.LENGTH_LONG).show();
        }

    }

    //turn on the gps
    public void turnGPSOn() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        this.sendBroadcast(intent);

        String provider = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        // if(!provider.contains("gps")){ //if gps is disabled
        final Intent poke = new Intent();
        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
        poke.setData(Uri.parse("3"));
        this.sendBroadcast(poke);


        //}
    }

    // turn off the gps
    public void turnGPSOff() {
        String provider = android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.sendBroadcast(poke);
        }
    }

}
