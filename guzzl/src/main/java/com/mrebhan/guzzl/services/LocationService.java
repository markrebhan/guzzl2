package com.mrebhan.guzzl.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.mrebhan.guzzl.app.GuzzlApp;

/* 
 * This service implements LocationListener to retrieve current location
 */
public class LocationService extends Service implements LocationListener,
        GooglePlayServicesClient.OnConnectionFailedListener, GooglePlayServicesClient.ConnectionCallbacks{

	public static final String TAG = "LocationService";

	static final String provider = LocationManager.GPS_PROVIDER;
	private LocationManager locationManager;
	private Location location;
	boolean isNetworkEnabled;

    ActivityRecognitionClient activityRecognitionClient;
    PendingIntent pendingIntent;

    // This Broadcast Receiver changes the value of time of refresh
    BroadcastReceiver receiverChangeRefreshTime;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       initializeMap();
       return START_STICKY;
    }

    @Override
	public void onCreate() {
		super.onCreate();
        initializeMap();
        receiverChangeRefreshTime = new ReceiverChangeRefreshTime();
        registerReceiver(receiverChangeRefreshTime, new IntentFilter(GuzzlApp.ACTION_CHANGE_LOCATION_REFRESH));

        int response = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (response == ConnectionResult.SUCCESS){
            activityRecognitionClient = new ActivityRecognitionClient(this, this, this);
            activityRecognitionClient.connect();
        }
        else {
            Toast.makeText(this, "Please Install Google Play Services.", Toast.LENGTH_LONG).show();
        }

	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Location Service Stopped.");
        locationManager.removeUpdates(this);
        unregisterReceiver(receiverChangeRefreshTime);

        if (activityRecognitionClient != null){
            activityRecognitionClient.removeActivityUpdates(pendingIntent);
            activityRecognitionClient.disconnect();
        }
    }

    private  void initializeMap(){
        Log.d(TAG, "Location Service Started from new");

        // initialize system GPS service to retrieve location info
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isNetworkEnabled) {
            // if no network enabled show a dialog indicating so
            sendBroadcast(new Intent (GuzzlApp.ACTION_NO_NETWORK_RECIEVER));
        } else {
            // get the last known location if unable to find new one
            location = locationManager.getLastKnownLocation(provider);
            // initialize updates with foreground if map initialized
            setLocationUpdate(GuzzlApp.MIN_TIME_LOCATION_REFRESH_ACTIVE);

            // Manually call onLocationChanged to immediately display last known
            // location
            if (location != null)
                onLocationChanged(location);
        }
    }

	// call to update requestLocationUpdates minTime
	public void setLocationUpdate(int minTime) {
        Log.d(TAG, "New Refresh Rate =:" + Integer.toString(minTime));
		locationManager.requestLocationUpdates(provider, minTime, GuzzlApp.MIN_DISTANCE,
				this);
	}

	@Override
	public void onLocationChanged(final Location location) {
		new Thread(){
			public void run(){


                double[] latlong = new double[4];

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
		}.start();

	}

    public  class ReceiverChangeRefreshTime extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isActivity = intent.getBooleanExtra(GuzzlApp.EXTRA_ON_ACTIVITY, true);
            if(isActivity) {
                setLocationUpdate(GuzzlApp.MIN_TIME_LOCATION_REFRESH_ACTIVE);
                activityRecognitionClient.requestActivityUpdates(GuzzlApp.MIN_TIME_LOCATION_REFRESH_ACTIVE, pendingIntent);
            }
            else{
                setLocationUpdate(GuzzlApp.MIN_TIME_LOCATION_REFRESH_INACTIVE);
                activityRecognitionClient.requestActivityUpdates(GuzzlApp.MIN_TIME_LOCATION_REFRESH_INACTIVE, pendingIntent);
            }
        }
    }

    // Connection Callbacks
    @Override
    public void onConnected(Bundle bundle) {
        Intent intent = new Intent(this, ActivityRecognitionService.class);
        pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        activityRecognitionClient.requestActivityUpdates(GuzzlApp.MIN_TIME_LOCATION_REFRESH_ACTIVE, pendingIntent);
    }

    //OnConnectionFailedListener
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Service Failed.", Toast.LENGTH_LONG).show();
    }

    // Connection Callbacks
    @Override
    public void onDisconnected() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
