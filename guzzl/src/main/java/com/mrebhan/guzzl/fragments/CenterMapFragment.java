package com.mrebhan.guzzl.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.interfacesgeneral.OnMapCenterClick;

/** This Fragment holds a button for the user to click to recenter the map
 * Created by markrebhan on 2/17/14.
 */
public class CenterMapFragment extends Fragment{


    OnMapCenterClick onMapCenterClick;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            onMapCenterClick = (OnMapCenterClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onMapCenterClick Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center_map, container, false);
        Button button = (Button) view.findViewById(R.id.button_centerMap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call the interface to send signal to parent Activity
                onMapCenterClick.OnCenterMapClick();
            }
        });
        return  view;
    }
}
