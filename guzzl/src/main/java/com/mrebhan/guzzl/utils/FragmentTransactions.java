package com.mrebhan.guzzl.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.fragments.*;

/**
 * This class houses the applications various fragment transactions statically
 *
 */
public class FragmentTransactions {

	public static void replaceCarPicker(FragmentActivity activity){
		// Pop the carpicker fragment to select/mofify car settings
		Fragment fragment = new CarPickerFragment();
		FragmentManager fm = activity.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction().replace(R.id.frameLayout_main, fragment);
		fm.popBackStackImmediate(); 
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public static void replaceFuelGauge(FragmentActivity activity){
		// Pop the carpicker fragment to select/mofify car settings
		Fragment fragment = new FuelGuageFragment();
		FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_main, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public static void removeAllFragments(FragmentActivity activity){
		FragmentManager fm = activity.getSupportFragmentManager();
		// pop previous fragment so the back button does not open them back up by popping 0th entry and with constant
		fm.popBackStack(fm.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // replace back infobar once other fragments have been removed;
        replaceInfoBar(activity);
		
	}

    public static Fragment infoBar;

    public static void replaceInfoBar(FragmentActivity activity){
        infoBar = new InfoBarFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_infoBar, infoBar).commit();
    }

    public static void removeInfoBar(FragmentActivity activity){
        activity.getSupportFragmentManager().beginTransaction().remove(infoBar).commit();
    }
	
}
