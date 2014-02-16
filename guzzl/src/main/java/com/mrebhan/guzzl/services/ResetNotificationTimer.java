package com.mrebhan.guzzl.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * This service resets the global timeelapsed timer for creating a new notification
 * Created by markrebhan on 2/13/14.
 */
public class ResetNotificationTimer extends IntentService {

    // required default constructor for IntentService
    public ResetNotificationTimer() {
        super("ResetNotificationTimer");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("ResetNotification", "start");
        UpdateMileage.totalTimeElapsed = 0;
    }
}
