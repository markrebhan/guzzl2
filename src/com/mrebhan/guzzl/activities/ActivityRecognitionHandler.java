package com.mrebhan.guzzl.activities;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognitionClient;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mrebhan.guzzl.services.*;

public class ActivityRecognitionHandler extends MapActivityMenu{
	
	public static final int DETECTION_INTERVAL_MILLI_ACTIVE = 30000;
	
	private PendingIntent mPendingIntent;
	private ActivityRecognitionClient mActivityRecognitionClient;
	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		mActivityRecognitionClient = new ActivityRecognitionClient(this, new ConnectionCallbacks() {
			
			@Override
			public void onDisconnected() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onConnected(Bundle connectionHint) {
				
				mPendingIntent = PendingIntent.getService(context, 0, new Intent(context, ActivityRecognitionService.class), PendingIntent.FLAG_UPDATE_CURRENT);
				mActivityRecognitionClient.requestActivityUpdates(DETECTION_INTERVAL_MILLI_ACTIVE, mPendingIntent);
				
			}
		}, new OnConnectionFailedListener() {
			
			@Override
			public void onConnectionFailed(ConnectionResult result) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	
	
}
