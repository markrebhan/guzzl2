package com.mrebhan.guzzl.math;

import com.google.android.gms.maps.model.LatLng;

// in meters
public class DistanceBetweenPreviousTwoPoints {

	public static double distance(LatLng start, LatLng end) {
		double startLat = start.latitude;
		double startLong = start.longitude;
		double endLat = end.latitude;
		double endLong = end.longitude;
		// find the distance in meters of 1 degree of lat and long
		double latPerOneDegree = 111000d; //meters
		double averageLat = (endLat + startLat) / 2;
		double longPerOneDegree = Math.cos(Math.toRadians(averageLat))
				* latPerOneDegree; //meters
		
		// find the change distance for both x and y components
		double deltaLat = Math.abs((endLat - startLat) * latPerOneDegree);
		double deltaLong = Math.abs((endLong - startLong) * longPerOneDegree);
		
		// use pythagorean theorem to find tangent distance
		double distanceSquared = deltaLat * deltaLat + deltaLong * deltaLong;
		double distance = Math.sqrt(distanceSquared);
		return distance; // in meters
	}

}
