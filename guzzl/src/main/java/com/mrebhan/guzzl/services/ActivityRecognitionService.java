package com.mrebhan.guzzl.services;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ActivityRecognitionService extends IntentService{

	public static final String TAG = "ActivityRecognitionService";
	
	public ActivityRecognitionService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (ActivityRecognitionResult.hasResult(intent)) {
			boolean inVehicle = false;
			ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
			DetectedActivity mostProbableActivity = result.getMostProbableActivity();
			int activityType = mostProbableActivity.getType();
			if(activityType == DetectedActivity.IN_VEHICLE) inVehicle = true;
			Log.d(TAG, Boolean.toString(inVehicle));
			
		}
		
	}

	
	
	

}
