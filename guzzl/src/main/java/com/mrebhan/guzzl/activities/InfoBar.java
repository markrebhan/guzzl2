package com.mrebhan.guzzl.activities;

import android.os.Bundle;
import android.util.Log;

import com.mrebhan.guzzl.utils.FragmentTransactions;

/**
 * Created by markrebhan on 2/9/14.
 *
 * This activity subclass handles the info bar that lives directly below the actionbar,
 *
 */
public class InfoBar extends LocationActivity{

    public static final String TAG = "InfoBar";

    public static final String STATE_INFOBAR_VISIBLE = "InfoBar Visible";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || savedInstanceState.getBoolean(STATE_INFOBAR_VISIBLE)){
            FragmentTransactions.replaceInfoBar(this);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        boolean isInfoBarVisible;
        // if there are no fragments in the backstack (fuel guages or new cars), then set this fragment to be true
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) isInfoBarVisible = true;
        else isInfoBarVisible = false;

        outState.putBoolean(STATE_INFOBAR_VISIBLE, isInfoBarVisible);
    }
}
