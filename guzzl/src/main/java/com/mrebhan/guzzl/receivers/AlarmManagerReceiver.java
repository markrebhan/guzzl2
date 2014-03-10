package com.mrebhan.guzzl.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mrebhan.guzzl.app.GuzzlApp;
import com.mrebhan.guzzl.services.LocationService;
import com.mrebhan.guzzl.services.UpdateMileage;

/**
 * Created by markrebhan on 2/13/14.
 * This broadcast receiver ensures that all services are running every 5 minutes
 * This is broadcast is received on phone bootup or app start to ensure that stats are always being updated in the background.
*/
public class AlarmManagerReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("AlarmManagerReceiver", "onReceive");

        Intent intentLocation = new Intent(GuzzlApp.ACTION_START_LOCATION_SERVICE);
        Intent intentMileage = new Intent(GuzzlApp.ACTION_START_UPDATE_MILEAGE);

        PendingIntent pendingIntent1 = PendingIntent.getService(context,-1,intentLocation,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getService(context,-1,intentMileage,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent1);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent2);

    }
}
