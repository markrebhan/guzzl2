package com.mrebhan.guzzl.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


import com.google.android.gms.maps.model.LatLng;
import com.mrebhan.guzzl.app.GuzzlApp;
import com.mrebhan.guzzl.interfacesgeneral.UpdateInfoBar;
import com.mrebhan.guzzl.math.*;
import com.mrebhan.guzzl.utils.FragmentTransactions;

public class UpdateMileage extends Service {

    public static final String TAG = "Update Mileage";

    LocationReceiver receiver;
    LatLng currentPosition;
    LatLng previousPosition;

    long currentTime;
    long previousTime = 0;
    double timeElapsed = 0;

    UpdateInfoBar updateInfoBar;


    @Override
    public void onCreate() {
        // register a new listener
        receiver = new LocationReceiver();
        registerReceiver(receiver, new IntentFilter(
                GuzzlApp.ACTION_LOCATION_RECIEVER));
        super.onCreate();
        // bind the interface to the fragment
        updateInfoBar = (UpdateInfoBar) FragmentTransactions.infoBar;
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

            // calculate time elapsed to help determine fuel efficiency
            currentTime = System.currentTimeMillis();
            if (previousTime != 0)
                timeElapsed = (double) (currentTime - previousTime) / 1000L; //to seconds
            previousTime = currentTime;

            // create a new asynctask and execute
            AsyncTaskUpdateMetrics asyncTaskUpdateMetrics = new AsyncTaskUpdateMetrics();
            asyncTaskUpdateMetrics.execute(loc[0], loc[1], timeElapsed);


            Log.d(TAG, "onReceive");
        }
    }

    class AsyncTaskUpdateMetrics extends AsyncTask<Double, Void, Void> {

        @Override
        protected Void doInBackground(Double... doubles) {

            // grab the input args and name them to something readable
            double latitude = doubles[0];
            double longitude = doubles[1];
            double timeElapsed = doubles[2];

            currentPosition = new LatLng(latitude, longitude);
            double distanceTraveled = 0;
            double fuelConsumed = 0;
            if (previousPosition != null) {
                distanceTraveled = DistanceBetweenPreviousTwoPoints.distance(previousPosition, currentPosition); // in meters
                boolean isHighway = CalculateEfficiency.highwayOrCity(distanceTraveled, timeElapsed);
                // calculate fuel consumed based on distance traveled and fuelEfficency
                fuelConsumed = FuelRemaining.CalculateFuelConsumed(distanceTraveled, getFuelEfficiency(isHighway));

                updateSharedPreferences(distanceTraveled, fuelConsumed); // add value to odometer
            }
            previousPosition = currentPosition;
            return null;
        }

        // after execution, update the info bar Fragment static object
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateInfoBar.updateValues();
            Log.d(TAG, "onPostExecute");
        }
    }

    private void updateSharedPreferences(double newDistance, double fuelConsumed) {
        SharedPreferences sharedPreferences = getSharedPreferences(GuzzlApp.PREFERENCE_MAIN, 0);
        float current = sharedPreferences.getFloat(GuzzlApp.PREFERENCE_ODOMETER, 0);
        float currentFuel = sharedPreferences.getFloat(GuzzlApp.PREFERENCE_FUEL_REMAINING, 0);

        // add to the odometer
        float result = (float) (current + newDistance);
        // subtract for current fuel
        float resultFuel = (float) (currentFuel - fuelConsumed);
        // ensure that the fuel never goes below 0
        if (resultFuel < 0) resultFuel = 0;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(GuzzlApp.PREFERENCE_ODOMETER, result);
        editor.putFloat(GuzzlApp.PREFERENCE_FUEL_REMAINING, resultFuel);
        // put in the total range remaining for the gas tank based on current fuel reading times highway MPG
        editor.putFloat(GuzzlApp.PREFERENCE_RANGE, (float) resultFuel * sharedPreferences.getInt(GuzzlApp.PREFERENCE_MPG_HW, 0)); // in miles
        editor.commit();
    }

    private int getFuelEfficiency(boolean isHighway) {
        SharedPreferences sharedPreferences = getSharedPreferences(GuzzlApp.PREFERENCE_MAIN, 0);
        if (isHighway) {
            return sharedPreferences.getInt(GuzzlApp.PREFERENCE_MPG_HW, 0);
        } else {
            //city
            return sharedPreferences.getInt(GuzzlApp.PREFERENCE_MPG_CITY, 0);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
