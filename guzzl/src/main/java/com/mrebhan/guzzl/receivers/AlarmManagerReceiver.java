package com.mrebhan.guzzl.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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


        long interval = 30000; // in ms

        Intent intentLocation = new Intent(context, LocationService.class);
        Intent intentMileage = new Intent(context, UpdateMileage.class);

        PendingIntent pendingIntent1 = PendingIntent.getService(context,0,intentLocation,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getService(context,0,intentMileage,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis(), interval, pendingIntent1);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis(), interval, pendingIntent2);

    }
}
