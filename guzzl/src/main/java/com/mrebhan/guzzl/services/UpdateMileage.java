package com.mrebhan.guzzl.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.activities.InfoBar;
import com.mrebhan.guzzl.app.GuzzlApp;
import com.mrebhan.guzzl.asynctasks.AsyncTaskUpdateMetrics;
import com.mrebhan.guzzl.interfacesgeneral.UpdateInfoBar;
import com.mrebhan.guzzl.math.*;
import com.mrebhan.guzzl.utils.FragmentTransactions;

import java.util.HashMap;

public class UpdateMileage extends Service {

    public static final String TAG = "Update Mileage";

    LocationReceiver receiver;

    long currentTime;
    long previousTime = 0;
    double timeElapsed = 0;
    public static double totalTimeElapsed = 600; // set to 10 minutes so the notification appears the first time with no time limit

    @Override
    public void onCreate() {
        // register a new listener
        receiver = new LocationReceiver();
        registerReceiver(receiver, new IntentFilter(
                GuzzlApp.ACTION_LOCATION_RECIEVER));
        super.onCreate();
    }

    // make the service sticky so it starts back up properly after the main process is destroyed
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (receiver != null)
            unregisterReceiver(receiver);
        super.onDestroy();
    }

    // inner class that defines action to be taken when a location broadcast is
    // received
    // Duplicate code in locationActivity
    class LocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // get extras from broadcast
            Bundle extras = intent.getExtras();
            double[] loc = extras.getDoubleArray(GuzzlApp.EXTRA_COORDINATES);

            // if it is determined we are in a vehicle when a location is received, update mileage
            if(GuzzlApp.STATE_IN_VEHICLE){
            // calculate time elapsed to help determine fuel efficiency
            currentTime = System.currentTimeMillis();
            if (previousTime != 0)
                timeElapsed = (double) (currentTime - previousTime) / 1000L; //to seconds
            previousTime = currentTime;

            // create a new asynctask and execute
            AsyncTaskUpdateMetrics asyncTaskUpdateMetrics = new AsyncTaskUpdateMetrics(context);
            asyncTaskUpdateMetrics.execute(loc[0], loc[1], timeElapsed);
            }

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
