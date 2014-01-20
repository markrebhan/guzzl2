package com.mrebhan.guzzl.app;

import android.app.Application;

public class GuzzlApp extends Application{
	
	// Global variables
	public static final String ACTION_LOCATION_RECIEVER = "com.mrebhan.guzzl.locationReciever";
	public static final String ACTION_NO_GPS_RECIEVER = "com.mrebhan.guzzl.receivers.ReceiverNoGPS";
	public static final String ACTION_NO_NETWORK_RECIEVER = "com.mrebhan.guzzl.receivers.ReceiverNoNetwork";
	
	public static final String EXTRA_COORDINATES = "com.mrebhan.guzzl.coordinates";

	
	

}
