package com.mrebhan.guzzl.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.app.GuzzlApp;
import com.mrebhan.guzzl.interfacesgeneral.UpdateInfoBar;

/**
 * Created by markrebhan on 2/9/14.
 */
public class InfoBarFragment extends Fragment implements UpdateInfoBar {


    TextView text;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infobar, container, false);

        text = (TextView) view.findViewById(R.id.info_text);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_fuel_gauge);

        //initialize values oncreate;
        updateValues();

        return view;
    }

    // This interface callback is called when the broadcast is received.
    @Override
    public void updateValues() {
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(GuzzlApp.PREFERENCE_MAIN, 0);
            float fuelRemaining = sharedPreferences.getFloat(GuzzlApp.PREFERENCE_FUEL_REMAINING, 0);
            int fuelSize = sharedPreferences.getInt(GuzzlApp.PREFERENCE_FUEL_SIZE, 0);
            float range = sharedPreferences.getFloat(GuzzlApp.PREFERENCE_RANGE, 0);
            float progress = (fuelRemaining / fuelSize) * 100;
            progressBar.setProgress((int) progress);

            String rangeS = Float.toString(range);
            String[] rangeSA = rangeS.split(".");

            text.setText(Float.toString(range));
        }

    }


}
