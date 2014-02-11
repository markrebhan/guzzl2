package com.mrebhan.guzzl.fragments;

import java.security.Guard;

import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.drawables.FuelGageBitmap;
import com.mrebhan.guzzl.math.*;
import com.mrebhan.guzzl.interfacesgeneral.*;
import com.mrebhan.guzzl.app.*;
import com.mrebhan.guzzl.utils.*;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class FuelGuageFragment extends Fragment {

    public final static String TAG = "FuelGuageFragment";

    private TextView textView;
    private SeekBar seekBar;

    //As this is the exit back to the main part of the activity, run the onStateShowRange to change and values for range if fuel levels were modified
    OnStateShowRangeOnMapChanged onStateShowRangeOnMapChanged;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onStateShowRangeOnMapChanged = (OnStateShowRangeOnMapChanged) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onStateShowRangeOnMapChanged Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fuel_reading, container, false);

        seekBar = (SeekBar) view.findViewById(R.id.seekbar_fuel_guage);
        onProgressChange();

        textView = (TextView) view.findViewById(R.id.text_fuel_gauge);
        textView.setText("Full");

        Button buttonOkay = (Button) view.findViewById(R.id.button_ok_car_fuel_reading);
        buttonOkay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkay();
                onStateShowRangeOnMapChanged.onShowRangeOnMap();
            }
        });

        Button buttonCancel = (Button) view.findViewById(R.id.button_cancel_fuel_reading);
        buttonCancel.setOnClickListener(new CancelCurrentFragment(getActivity()));

        return view;
    }

    public void onOkay() {

        // get seekbar progress and max
        float amount = seekBar.getProgress();
        float total = seekBar.getMax();

        // get the total amount of fuel from preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(GuzzlApp.PREFERENCE_MAIN, 0);
        int fuel_size = sharedPreferences.getInt(GuzzlApp.PREFERENCE_FUEL_SIZE, 0);
        float fuelRemaining = (amount / total) * fuel_size;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // calculate the amount and put into SharedPreferences
        Log.d(TAG, Float.toString((amount / total) * fuel_size));
        editor.putFloat(GuzzlApp.PREFERENCE_FUEL_REMAINING, fuelRemaining);
        editor.putFloat(GuzzlApp.PREFERENCE_RANGE, (float) fuelRemaining * sharedPreferences.getInt(GuzzlApp.PREFERENCE_MPG_HW, 0));
        editor.commit();
        // Remove this fragment and all of backstack
        FragmentTransactions.removeAllFragments(getActivity());
        // add back infobar
        FragmentTransactions.replaceInfoBar(getActivity());


    }

    public void onProgressChange() {

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                textView.setText(FuelFractions.reduceFractions(progress, 16));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

        });
    }


}
