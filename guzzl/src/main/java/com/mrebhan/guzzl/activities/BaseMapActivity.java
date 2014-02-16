package com.mrebhan.guzzl.activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.app.GuzzlApp;
import com.mrebhan.guzzl.fragments.MapsFragment;
import com.mrebhan.guzzl.services.UpdateMileage;

/*
 * This base map activity displays the maps from google API,
 * defines GoogleMap object and sets options for Map
 *
 */
public class BaseMapActivity extends FragmentActivity {

	GoogleMap googleMap;
	MapFragment mapFragment;

    // get shared preferences for all subactivities
    SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_map);
		
		startService(new Intent(this, UpdateMileage.class));

        sharedPreferences = getSharedPreferences(GuzzlApp.PREFERENCE_MAIN,0);

        Log.d("GuzzlApp", "App Created");
        sendBroadcast(new Intent(GuzzlApp.ACTION_START_ALARMMANAGER));
		
		// check for google play 
		int googlePlay = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (googlePlay == ConnectionResult.SUCCESS) findFragment();
	}

	// Initialize googleMap variable in code
	public void findFragment() {

		if (googleMap == null) {
			// find the fragment containing the map in the base activity
			Fragment fragmentMap = (MapsFragment) getFragmentManager()
					.findFragmentById(R.id.fragment_map);
			// find the map in the mapfragment to find it
			mapFragment = (MapFragment) fragmentMap.getFragmentManager()
					.findFragmentById(R.id.map);
			// initialize map
			googleMap = mapFragment.getMap();
			setupMap();
		}

	}
	
	public void setupMap(){
		// remove zoom buttons from Maps UI
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		// allow tilt gestures
		googleMap.getUiSettings().setTiltGesturesEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(true);
	}

}
