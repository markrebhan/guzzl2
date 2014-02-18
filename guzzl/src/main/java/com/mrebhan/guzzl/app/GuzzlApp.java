package com.mrebhan.guzzl.app;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.mrebhan.guzzl.services.*;

public class GuzzlApp extends Application implements OnSharedPreferenceChangeListener{
	
	// Global variables
	public static final String ACTION_LOCATION_RECIEVER = "com.mrebhan.guzzl.locationReciever";
	public static final String ACTION_NO_GPS_RECIEVER = "com.mrebhan.guzzl.receivers.ReceiverNoGPS";
	public static final String ACTION_NO_NETWORK_RECIEVER = "com.mrebhan.guzzl.receivers.ReceiverNoNetwork";
    public static final String ACTION_START_ALARMMANAGER = "com.mrebhan.guzzl.AlarmManager";
    public static final String ACTION_CHANGE_LOCATION_REFRESH = "com.guzzl.LocationRefresh";
	
	public static final String EXTRA_COORDINATES = "com.mrebhan.guzzl.coordinates";
	public static final String EXTRA_ON_ACTIVITY = "com.mrebhan.guzzl.onActivity";

	public static final String PREFERENCE_MAIN = "preference_main";
	public static final String PREFERENCE_MPG_HW = "preference_mpg_hw";
	public static final String PREFERENCE_MPG_CITY = "preference_mpg_city";
	public static final String PREFERENCE_FUEL_SIZE = "preference_fuel_city";
	public static final String PREFERENCE_FUEL_REMAINING = "preference_fuel_remaining";
	public static final String PREFERENCE_ODOMETER = "preference_odometer"; // in meters
    public static final String PREFERENCE_RANGE = "preference_range";

    public static boolean STATE_SHOW_RANGE_ON_MAP = false;
    public static boolean STATE_IN_VEHICLE = false;
    public static LatLng STATE_CURRENT_POSITION;
    public static LatLng STATE_PREVIOUS_POSITION;

    // Define constants for length of refresh
    public static final int MIN_TIME_LOCATION_REFRESH_ACTIVE = 100; // in ms
    public static final int MIN_TIME_LOCATION_REFRESH_INACTIVE = 30000; // in ms
    public static final int MIN_DISTANCE = 5; // in meters



	
	public SharedPreferences sharedPreferences;
	
	// override application oncreate to set up sharedpreference listeners
	@Override
	public void onCreate() {
		super.onCreate();
		sharedPreferences = getSharedPreferences(PREFERENCE_MAIN, 0);
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		// set up alarmManager

		
	}
	
	

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		this.sharedPreferences = sharedPreferences;
		
	}
	
	/**
	 * Now we can make calls to sharedPreference interface to save primitive data about the application
	 */

	
	

}
