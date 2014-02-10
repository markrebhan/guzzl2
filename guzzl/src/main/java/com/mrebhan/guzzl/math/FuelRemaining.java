package com.mrebhan.guzzl.math;

import com.mrebhan.guzzl.app.GuzzlApp;

/**
 * calculate the amount of fuel remaining for update service
 * Created by markrebhan on 2/9/14.
 */
public class FuelRemaining {

    // distance traveled in meters
    // fuelRemaining in gallons
    // efficiency in miles per gallon
    // return the amount of fuel consumed
    public static double CalculateFuelConsumed(double distanceTraveled, int efficiency){
        double fuelConsumed = 0;

        double milesTraveled = MetricConversion.metersToMiles(distanceTraveled);
        // miles/1 * gallons/miles = gallons
        fuelConsumed = (double) milesTraveled / efficiency;

        return  fuelConsumed;
    }



}
