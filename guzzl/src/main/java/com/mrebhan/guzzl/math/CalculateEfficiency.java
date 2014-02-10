package com.mrebhan.guzzl.math;

/**
 * calculate the fuel efficiency type given time and distance traveled
 * Created by markrebhan on 2/9/14.
 */
public class CalculateEfficiency {
    // distance in meters
    // time in milliseconds
    public static boolean highwayOrCity(double distanceTraveled, double timeTaken){
        boolean isHighway;

        double averageVelocity = distanceTraveled / timeTaken; // in m/s

        // based on average velocity, anything > 20/ms is highway and anything < 20 m/s is city
        if (averageVelocity >= 20) isHighway = true;
        else isHighway = false;

        return  isHighway;
    }

}
