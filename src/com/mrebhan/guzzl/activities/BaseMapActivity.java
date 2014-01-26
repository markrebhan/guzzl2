package com.mrebhan.guzzl.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.fragments.MapsFragment;

/*
 * This base map activity displays the maps from google API,
 * defines GoogleMap object and sets options for Map
 *
 */
public class BaseMapActivity extends FragmentActivity {

	GoogleMap googleMap;
	MapFragment mapFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_map);
		findFragment();
		
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
