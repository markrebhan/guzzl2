package com.mrebhan.guzzl.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.app.GuzzlApp;
import com.mrebhan.guzzl.interfacesgeneral.OnStateShowRangeOnMapChanged;
import com.mrebhan.guzzl.interfacesgeneral.UpdateInfoBar;

import java.util.zip.GZIPInputStream;

/**
 * Created by markrebhan on 2/9/14.
 */
public class InfoBarFragment extends Fragment implements UpdateInfoBar {


    TextView text;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    OnStateShowRangeOnMapChanged onStateShowRangeOnMapChanged;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(GuzzlApp.PREFERENCE_MAIN,0);
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infobar, container, false);

        text = (TextView) view.findViewById(R.id.info_text);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_fuel_gauge);

        //initialize values oncreate;
        updateValues();

        ImageButton imageButton_fuel = (ImageButton) view.findViewById(R.id.image_fuel);
        imageButton_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeFuelToast();
            }
        });

        ImageButton imageButton_range = (ImageButton) view.findViewById(R.id.image_range);
        imageButton_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change the state of showing range on map
                if (GuzzlApp.STATE_SHOW_RANGE_ON_MAP == false) {GuzzlApp.STATE_SHOW_RANGE_ON_MAP = true;}
                else {GuzzlApp.STATE_SHOW_RANGE_ON_MAP = false;}
                // no need to pass as the state is global
                onStateShowRangeOnMapChanged.onShowRangeOnMap();
            }
        });

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

    //Make toast of total fuel remaining
    //TODO move if other classes implement this method
    private void makeFuelToast(){
        String fuelRemaining = Float.toString(sharedPreferences.getFloat(GuzzlApp.PREFERENCE_FUEL_REMAINING, 0)); // in gallons by default
        Toast toast = Toast.makeText(getActivity(), fuelRemaining + " " + getResources().getString(R.string.gal), Toast.LENGTH_LONG);
        toast.show();

    }



}
