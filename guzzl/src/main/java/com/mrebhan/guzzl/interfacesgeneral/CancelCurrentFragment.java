package com.mrebhan.guzzl.interfacesgeneral;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.mrebhan.guzzl.utils.FragmentTransactions;

// common action to take when cancel button is pressed on fragment
public class CancelCurrentFragment implements OnClickListener{

	private FragmentActivity activity;
	
	public CancelCurrentFragment(FragmentActivity activity){
		this.activity = activity;
	}
	
	@Override
	public void onClick(View v) {
		activity.getSupportFragmentManager().popBackStack();
        // add the infobar fragment back if there is not replace car fragment or fuel gauge fragment
        if (activity.getSupportFragmentManager().getBackStackEntryCount() <= 1) FragmentTransactions.replaceInfoBar(activity);

	}

}
