package com.mrebhan.guzzl.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.mrebhan.guzzl.app.GuzzlApp;

/* 
 * This service implements LocationListener to retrieve current location
 */
public class LocationService extends Service implements LocationListener {

	public static final String TAG = "LocationService";

	// Define constants for length of refreshs
	public static final int MINTIME_ACTIVITY_BOUND = 100;
	public static final int MINTIME_BACKGROUND = 300000;
	public static final int MINDISTANCE = 5; // in meters

	static final String provider = LocationManager.GPS_PROVIDER;
	private LocationManager locationManager;
	Location location;
	private double[] latlong = new double[4];
	boolean isGPSEnabled, isNetworkEnabled;

	private final IBinder mBinder = new LocationBinder();

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d(TAG, "Location Service Started.");
		
		// initialize system GPS service to retrieve location info
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		// getting GPS status
		isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// getting network status
		isNetworkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isNetworkEnabled) {
			// if no network enabled show a dialog indicating so
			sendBroadcast(new Intent (GuzzlApp.ACTION_NO_NETWORK_RECIEVER));
		} else if (!isGPSEnabled) {
			sendBroadcast(new Intent (GuzzlApp.ACTION_NO_GPS_RECIEVER));
		} else {
			// get the last known location if unable to find new one
			location = locationManager.getLastKnownLocation(provider);
			// initialize updates with background refresh times
			setLocationUpdate(MINTIME_BACKGROUND);

			// Manually call onLocationChanged to immediately display last known
			// location
			if (location != null)
				onLocationChanged(location);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Location Service Stopped.");
		locationManager.removeUpdates(this);
	}

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocationBinder extends Binder {
		public LocationService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return LocationService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		if (isGPSEnabled || isNetworkEnabled)
			setLocationUpdate(MINTIME_ACTIVITY_BOUND);
		Log.d(TAG, "service bound to activity");
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// change setting to update less frequently when service not bound to
		// activity
		setLocationUpdate(MINTIME_BACKGROUND);
		Log.d(TAG, "service unbound to activity");
		return super.onUnbind(intent);

	}

	// call to update requestLocationUpdates minTime
	public void setLocationUpdate(int minTime) {
		locationManager.requestLocationUpdates(provider, minTime, MINDISTANCE,
				this);
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, location.getLatitude() + " " + location.getLongitude());
		latlong[0] = location.getLatitude();
		latlong[1] = location.getLongitude();
		// get bearing so we can point marker in the correct direction
		// get speed and bearing for animation
		latlong[2] = location.getBearing();
		// in m/s
		latlong[3] = location.getSpeed();
		// send a broadcast of the new location along with extra coordinates
		sendBroadcast(new Intent(GuzzlApp.ACTION_LOCATION_RECIEVER).putExtra(
				GuzzlApp.EXTRA_COORDINATES, latlong));

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
}
