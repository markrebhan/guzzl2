package com.mrebhan.guzzl.activities;

import android.os.Bundle;

import com.mrebhan.guzzl.utils.FragmentTransactions;

/**
 * Created by markrebhan on 2/9/14.
 *
 * This activity subclass handles the info bar that lives directly below the actionbar,
 *
 */
public class InfoBar extends LocationActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransactions.replaceInfoBar(this);
    }
}
