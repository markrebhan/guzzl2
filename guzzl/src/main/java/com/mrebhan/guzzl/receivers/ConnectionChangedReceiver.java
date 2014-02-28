package com.mrebhan.guzzl.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mrebhan.guzzl.app.GuzzlApp;

/** This broadcast receiver will notify the app if there is a loss in connection
 * Created by markrebhan on 2/27/14.
 */
public class ConnectionChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // if we cannot obtain network info, then send a message to activity
        if(networkInfo == null){
            context.sendBroadcast(new Intent(GuzzlApp.ACTION_NO_NETWORK_RECEIVER));
        }
    }
}
