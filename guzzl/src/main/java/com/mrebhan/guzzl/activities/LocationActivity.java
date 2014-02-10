package com.mrebhan.guzzl.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mrebhan.guzzl.animations.MarkerAnimation;
import com.mrebhan.guzzl.app.GuzzlApp;
import com.mrebhan.guzzl.services.*;

/*
 * This Class Finds and Displays current position on Map 
 * and receives updates from service to update the position
 * as it changes.
 */

public class LocationActivity extends LocationServiceHandlerActivity implements
		OnMapClickListener, OnMarkerClickListener {

	public static final String TAG = "LocationActivity";

	BroadcastReceiver receiver;

	// initialize marker that we will draw on the map
	Marker marker = null;

	protected final String zoomS = "zoom";
	protected final String centerMapS = "centerMap";

	LocationManager locationManager;
	Location location;

	protected LatLng currentPosition;
	protected double bearing;
	protected double velocity;

	protected float zoom = 15f;
	protected boolean centerMap = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			zoom = savedInstanceState.getFloat(zoomS);
			centerMap = savedInstanceState.getBoolean(centerMapS);
		} catch (Exception e) {
			Log.d(TAG, "No saved instance.");
		}

	}
	@Override
	protected void onResume() {
		// register a new listener
		receiver = new LocationReceiver();
		registerReceiver(receiver, new IntentFilter(
				GuzzlApp.ACTION_LOCATION_RECIEVER));
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (receiver != null)
			unregisterReceiver(receiver);
	}

	// Save map location information in case activity gets destroyed and
	// recreated (screen rotation)
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(centerMapS, centerMap);
		outState.putFloat(zoomS, googleMap.getCameraPosition().zoom);
	}

	public void initializeMarker(LatLng latLng) {
		// initialize a new marker and set the position and options
		marker = googleMap.addMarker(new MarkerOptions().position(latLng));
		if (centerMap)
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
		// initialize map listeners
		googleMap.setOnMapClickListener(this);
		googleMap.setOnMarkerClickListener(this);
	}
	
	public void locationChanged(double[] loc) {
		currentPosition = new LatLng(loc[0], loc[1]);
		bearing = loc[2];
		velocity = loc[3];
		// if the marker is null, create a new marker and position it to the
		// current position
		if (marker == null) {
			initializeMarker(currentPosition);
		} else {
			marker.setPosition(currentPosition);
			// center map over current position
			if (centerMap)
				googleMap.moveCamera(CameraUpdateFactory
						.newLatLng(currentPosition));
		}

		// animate the marker given current bearing and velocity
		MarkerAnimation.animateMarkerHC(marker, googleMap, bearing, velocity);
		
		// TEMP
		SharedPreferences sharedPreferences = getSharedPreferences(GuzzlApp.PREFERENCE_MAIN, 0);
		float current = sharedPreferences.getFloat(GuzzlApp.PREFERENCE_ODOMETER, 0);
		setTitle(Float.toString(current));

	}


	// inner class that defines action to be taken when a location broadcast is
	// received
	class LocationReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// get extras from broadcast
			Bundle extras = intent.getExtras();
			double[] loc = extras.getDoubleArray(GuzzlApp.EXTRA_COORDINATES);
			locationChanged(loc);
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		// turn off centering map on location refresh
		centerMap = false;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// set centerMap to true when clicked (centered by default)
		centerMap = true;
		return false;
	}
}
