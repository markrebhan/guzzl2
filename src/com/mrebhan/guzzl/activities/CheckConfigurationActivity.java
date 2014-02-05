package com.mrebhan.guzzl.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.mrebhan.guzzl.app.*;
import com.mrebhan.guzzl.utils.*;

public class CheckConfigurationActivity extends BaseMapActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPreferences = getSharedPreferences(GuzzlApp.PREFERENCE_MAIN, 0);
		// if shared preferences are not found, open up the replace car fragment
		if(sharedPreferences.getInt(GuzzlApp.PREFERENCE_MPG_HW, 0) == 0) FragmentTransactions.replaceCarPicker(this);
	}

	
	
}
