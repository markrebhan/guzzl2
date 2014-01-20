package com.mrebhan.guzzl.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mrebhan.guzzl.mapactivities.EnableGPSDialog;

public class RecieverNoNetwork extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO add something here
		Log.d("NoNetwork", "No Network");
		//EnableGPSDialog enableGPSDialog = new EnableGPSDialog();
		//enableGPSDialog.showGPSAlert();
	}

}
