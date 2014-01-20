package com.mrebhan.guzzl.math;

import com.google.android.gms.maps.model.LatLng;

public class LatLngInterpolator {
	
	public static final String TAG = "LatLngInerpolator";
	// given the latitude l, bearing b in degrees, velocity v in m/s, find new lat/long
	public static LatLng interpolate(float fraction, LatLng l, double b, double v){
		
		
		// find change in x and y coordinates based on velocity vector v
		// Note that sine and cosine are calculated in radians
		double x = v * Math.cos(Math.toRadians(b));
		double y = v * Math.sin(Math.toRadians(b));
		// find the distance in meters of 1 degree of lat and long
		double latPerOneDegree = 111000d;
		double longPerOneDegree = Math.cos(Math.toRadians(l.latitude)) * latPerOneDegree;
		// divide velocity by latperonedegree and multiply by fraction complete
		double lat = l.latitude + (x/latPerOneDegree * fraction);
		double lng = l.longitude + (y/longPerOneDegree * fraction);
				
		return new LatLng(lat, lng);
		
	}

}
