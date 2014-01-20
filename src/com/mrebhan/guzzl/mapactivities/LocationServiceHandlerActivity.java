package com.mrebhan.guzzl.mapactivities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.mrebhan.guzzl.app.GuzzlApp;
import com.mrebhan.guzzl.receivers.ReceiverNoGPS;
import com.mrebhan.guzzl.receivers.RecieverNoNetwork;
import com.mrebhan.guzzl.services.LocationService;
import com.mrebhan.guzzl.services.LocationService.LocationBinder;

/*
 * This class handles binding to Location service
 */
public class LocationServiceHandlerActivity extends BaseMapActivity{
	
	public static final String TAG = "LocationServiceHandlerActivity";

	Intent serviceIntent;
	LocationService mService;
	boolean mBound = false;
	BroadcastReceiver receiverNoGPS;
	BroadcastReceiver receiverNoNetwork;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		receiverNoGPS = new ReceiverNoGPS();
		registerReceiver(receiverNoGPS, new IntentFilter(
				GuzzlApp.ACTION_NO_GPS_RECIEVER));
		receiverNoNetwork = new RecieverNoNetwork();
		registerReceiver(receiverNoNetwork, new IntentFilter(
				GuzzlApp.ACTION_NO_NETWORK_RECIEVER));
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onstart");
		// stop service with no binding
		if (serviceIntent != null) stopService(serviceIntent);
		// bind to LocationService
		Intent intent = new Intent(this, LocationService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

	}
	
	@Override
	protected void onStop() {
		super.onStop();
		// Unbind from the service
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
		
		// Start location service with no binding associated with it
		serviceIntent = new Intent(this, LocationService.class);
		startService(serviceIntent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// stop service with no binding
		if (serviceIntent != null) stopService(serviceIntent);
		unregisterReceiver(receiverNoGPS);
		unregisterReceiver(receiverNoNetwork);
	}
	
	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			LocationBinder binder = (LocationBinder) service;
			mService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			mBound = false;
		}
	};

}
