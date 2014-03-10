package com.mrebhan.guzzl.math;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/** this class will calculate the LatLng vales of 4 points of circle
 * Created by markrebhan on 3/1/14.
 */
public class LatLngBoundsCircle {


    // return a hashmap of the bounds of the circle given the current position and radius of circle
    public static List<LatLng> findLatlng(LatLng currentPosition, double radius){ //radius in meters

        List<LatLng> points = new ArrayList<LatLng>();

        // find the distance in meters of 1 degree of lat and long
        double latPerOneDegree = 111000d;
        double longPerOneDegree = Math.cos(Math.toRadians(currentPosition.latitude)) * latPerOneDegree;

        points.add(new LatLng(currentPosition.latitude + (radius/latPerOneDegree), currentPosition.longitude));
        points.add(new LatLng(currentPosition.latitude - (radius/latPerOneDegree), currentPosition.longitude));
        points.add(new LatLng(currentPosition.latitude, currentPosition.longitude + (radius/longPerOneDegree)));
        points.add(new LatLng(currentPosition.latitude, currentPosition.longitude - (radius/longPerOneDegree)));

        return  points;
    }
}


