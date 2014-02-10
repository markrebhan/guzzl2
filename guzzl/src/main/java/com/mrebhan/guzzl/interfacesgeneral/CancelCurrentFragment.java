package com.mrebhan.guzzl.interfacesgeneral;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

// common action to take when cancel button is pressed on fragment
public class CancelCurrentFragment implements OnClickListener{

	private FragmentActivity activity;
	
	public CancelCurrentFragment(FragmentActivity activity){
		this.activity = activity;
	}
	
	@Override
	public void onClick(View v) {
		activity.getSupportFragmentManager().popBackStack();
	}

}
