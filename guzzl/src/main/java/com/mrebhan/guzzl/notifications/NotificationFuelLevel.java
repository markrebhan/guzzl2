package com.mrebhan.guzzl.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.activities.InfoBar;
import com.mrebhan.guzzl.services.LocationService;
import com.mrebhan.guzzl.services.ResetNotificationTimer;
import com.mrebhan.guzzl.utils.DecimalFormatter;

/**
 * Created by markrebhan on 2/13/14.
 */
public class NotificationFuelLevel {

    Context context;

    public  NotificationFuelLevel(Context context){
        this.context = context;
    }

    // build the notification to be displayed if fuel levels are low
    public void buildNotification(float currentRange){
        // Prepare the intent if the notification is selected
        // Make sure the activity is set to whatever the launch activity is
        Intent intent = new Intent(context, InfoBar.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        // build pending intent for when the notification is removed by user so we can set time counter to 0;
        Intent intent1 = new Intent(context, ResetNotificationTimer.class);
        PendingIntent pendingIntent1 = PendingIntent.getService(context, 0, intent1, 0);

        // Build out the notification
        Notification.Builder notification = new Notification.Builder(context);
        notification.setSmallIcon(R.drawable.ic_launcher).setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(String.format(context.getResources().getString(R.string.notification_text), DecimalFormatter.decimalFormatter(currentRange)))
                .setContentIntent(pendingIntent).setDeleteIntent(pendingIntent1);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);



        if (Build.VERSION.SDK_INT >= 16){
            notificationManager.notify(0,notification.build());
            // create an artificial backstack for the started activity to ensure clicking on back will return to home
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(InfoBar.class);
            taskStackBuilder.addNextIntent(intent);
        }
        else notificationManager.notify(0, notification.getNotification());
    }
}
