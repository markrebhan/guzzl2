package com.mrebhan.guzzl.activities;

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
import com.mrebhan.guzzl.fragments.EnableGPSDialog;
import com.mrebhan.guzzl.services.LocationService;
import com.mrebhan.guzzl.services.LocationService.LocationBinder;

/*
 * This class handles binding to Location service
 */
public class LocationServiceHandlerActivity extends MapActivityMenu {

	public static final String TAG = "LocationServiceHandlerActivity";

	Intent serviceIntent;
	LocationService mService;
	boolean mBound = false;
	BroadcastReceiver receiverNoGPS;
	BroadcastReceiver receiverNoNetwork;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		receiverNoNetwork = new RecieverNoNetwork();
		registerReceiver(receiverNoNetwork, new IntentFilter(
				GuzzlApp.ACTION_NO_NETWORK_RECIEVER));
	}

	// on start, stop the location service with background state and bind it
	// with foreground state,
	// this is the only place we stop then start the service so in case the user
	// make change or
	// turns the screen or moves away from app while no service, it attempts to
	// kick back on
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onstart");
		// stop service with no binding
		if (serviceIntent != null)
			stopService(serviceIntent);
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
		if (serviceIntent != null)
			stopService(serviceIntent);
		unregisterReceiver(receiverNoNetwork);
	}


	// make sure there is only one object instantiated from broadcasts
	static EnableGPSDialog enableGPSDialog;
	// this inner receiver class displays a error pop up if no network is
	// detected
	public class RecieverNoNetwork extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO add something here
			Log.d("NoNetwork", "No Network");
			if (enableGPSDialog == null) {
				enableGPSDialog = new EnableGPSDialog();
				enableGPSDialog.show(getFragmentManager(), "No Network");
			}
		}

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
