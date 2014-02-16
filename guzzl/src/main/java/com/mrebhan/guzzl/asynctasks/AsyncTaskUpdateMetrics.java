package com.mrebhan.guzzl.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.mrebhan.guzzl.app.GuzzlApp;
import com.mrebhan.guzzl.interfacesgeneral.UpdateInfoBar;
import com.mrebhan.guzzl.math.CalculateEfficiency;
import com.mrebhan.guzzl.math.DistanceBetweenPreviousTwoPoints;
import com.mrebhan.guzzl.math.FuelRemaining;
import com.mrebhan.guzzl.notifications.NotificationFuelLevel;
import com.mrebhan.guzzl.services.UpdateMileage;
import com.mrebhan.guzzl.utils.FragmentTransactions;

import java.util.HashMap;

/**
 * This async task calculates latest info on fuel, distance traveled, range remaining and displays on
 * info fragment
 * Created by markrebhan on 2/13/14.
 */
public class AsyncTaskUpdateMetrics extends AsyncTask<Double, Void, Void> {
    public static final String TAG = "AsyncTaskUpdateMetrics";

    Context context;
    UpdateInfoBar updateInfoBar;

    public AsyncTaskUpdateMetrics(Context context){
        this.context = context;
    }

    SharedPreferences sharedPreferences;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // initialize items used within the context of async cycle
        sharedPreferences = context.getSharedPreferences(GuzzlApp.PREFERENCE_MAIN, 0);
        updateInfoBar = (UpdateInfoBar) FragmentTransactions.infoBar;
    }

    @Override
    protected Void doInBackground(Double... doubles) {

        // grab the input args and name them to something readable
        double latitude = doubles[0];
        double longitude = doubles[1];
        double timeElapsed = doubles[2];

        GuzzlApp.STATE_CURRENT_POSITION = new LatLng(latitude, longitude);
        double distanceTraveled = 0;
        double fuelConsumed = 0;
        if (GuzzlApp.STATE_PREVIOUS_POSITION != null) {
            distanceTraveled = DistanceBetweenPreviousTwoPoints.distance(GuzzlApp.STATE_PREVIOUS_POSITION, GuzzlApp.STATE_CURRENT_POSITION); // in meters
            boolean isHighway = CalculateEfficiency.highwayOrCity(distanceTraveled, timeElapsed);
            // calculate fuel consumed based on distance traveled and fuelEfficency
            fuelConsumed = FuelRemaining.CalculateFuelConsumed(distanceTraveled, getFuelEfficiency(isHighway));

            HashMap<String, Float> results = calculateNewValues(distanceTraveled, fuelConsumed, timeElapsed);
            updateSharedPreferences(results); // add value to odometer
        }
        GuzzlApp.STATE_PREVIOUS_POSITION = GuzzlApp.STATE_CURRENT_POSITION;
        return null;
    }

    // after execution, update the info bar Fragment static object
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(updateInfoBar != null) updateInfoBar.updateValues();
        Log.d(TAG, "onPostExecute");
    }


    private void updateSharedPreferences(HashMap<String, Float> results) {


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(GuzzlApp.PREFERENCE_ODOMETER, results.get("result"));
        editor.putFloat(GuzzlApp.PREFERENCE_FUEL_REMAINING, results.get("resultFuel"));
        // put in the total range remaining for the gas tank based on current fuel reading times highway MPG
        editor.putFloat(GuzzlApp.PREFERENCE_RANGE, results.get("currentRange")); // in miles
        editor.commit();
    }


    private HashMap<String, Float> calculateNewValues(double newDistance, double fuelConsumed, double timeElapsed){
        float current = sharedPreferences.getFloat(GuzzlApp.PREFERENCE_ODOMETER, 0);
        float currentFuel = sharedPreferences.getFloat(GuzzlApp.PREFERENCE_FUEL_REMAINING, 0);

        // add to the odometer
        float result = (float) (current + newDistance);
        // subtract for current fuel
        float resultFuel = (float) (currentFuel - fuelConsumed);
        // ensure that the fuel never goes below 0
        if (resultFuel < 0) resultFuel = 0;
        float currentRange = resultFuel * sharedPreferences.getInt(GuzzlApp.PREFERENCE_MPG_HW, 0);//in miles

        // send a notification if the amount of range remaining is less than 50 create a fragment to create the notification
        // send notification every 10 minutes as to not bombard the user
        UpdateMileage.totalTimeElapsed += timeElapsed;
        Log.d(TAG, Double.toString(UpdateMileage.totalTimeElapsed));
        if(currentRange < 50.0f && UpdateMileage.totalTimeElapsed >= 600 /*in seconds*/){
            // create a notification object to create a notification
            NotificationFuelLevel notificationFuelLevel = new NotificationFuelLevel(context);
            notificationFuelLevel.buildNotification(currentRange);
        }

        HashMap<String, Float> resultsMap = new HashMap<String, Float>();
        resultsMap.put("result", (float) result);
        resultsMap.put("resultFuel", (float) resultFuel);
        resultsMap.put("currentRange", (float) currentRange);
        return resultsMap;
    }

    private int getFuelEfficiency(boolean isHighway) {
        if (isHighway) {
            return sharedPreferences.getInt(GuzzlApp.PREFERENCE_MPG_HW, 0);
        } else {
            //city
            return sharedPreferences.getInt(GuzzlApp.PREFERENCE_MPG_CITY, 0);
        }
    }
}
