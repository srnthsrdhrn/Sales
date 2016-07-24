package com.sakthisugars.salesandmarketing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by singapore on 25-06-2016.
 */
public  class NetworkCheck extends BroadcastReceiver {
    public static boolean Connected = false;
    public NetworkCheck(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                Connected = true;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                Connected = true;
        } else
            Connected = false;

        if (Connected){

         }
        else {

        }
    }
}
