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

    @Override
    protected void onResume() {
        super.onResume();
        // send a broadcast to let the Location Service know to update location every second since the activity is in view
        sendBroadcast(new Intent(GuzzlApp.ACTION_CHANGE_LOCATION_REFRESH).putExtra(GuzzlApp.EXTRA_ON_ACTIVITY, true));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // send a broadcast to let the Location Service know to update location every 30 seconds since the activity is no longer in view
        sendBroadcast(new Intent(GuzzlApp.ACTION_CHANGE_LOCATION_REFRESH).putExtra(GuzzlApp.EXTRA_ON_ACTIVITY, false));
    }

    @Override
	protected void onDestroy() {
		super.onDestroy();
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

}
