package com.mrebhan.guzzl.receivers;

import com.mrebhan.guzzl.mapactivities.EnableGPSDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverNoGPS extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("NOGPS", "No GPS");
		//EnableGPSDialog enableGPSDialog = new EnableGPSDialog();
		//enableGPSDialog.showGPSAlert();
		//TODO MOVE THIS SHIT just need to make a call to an activity to push fragment
	}

}
