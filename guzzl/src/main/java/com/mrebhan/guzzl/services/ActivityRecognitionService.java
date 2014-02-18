package com.mrebhan.guzzl.services;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.mrebhan.guzzl.app.GuzzlApp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

// Intent Service that receives intent from activity recognition client to get the phones detected activity
public class ActivityRecognitionService extends IntentService{

	public static final String TAG = "ActivityRecognitionService";
	
	public ActivityRecognitionService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (ActivityRecognitionResult.hasResult(intent)) {
			ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
			DetectedActivity mostProbableActivity = result.getMostProbableActivity();
			int activityType = mostProbableActivity.getType();
            Log.d(TAG, Integer.toString(activityType));
			if(activityType == DetectedActivity.IN_VEHICLE) GuzzlApp.STATE_IN_VEHICLE = true;
            else GuzzlApp.STATE_IN_VEHICLE = false;
			Log.d(TAG, Boolean.toString(GuzzlApp.STATE_IN_VEHICLE));
			
		}
		
	}

	
	
	

}
