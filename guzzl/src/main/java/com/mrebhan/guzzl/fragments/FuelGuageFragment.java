package com.mrebhan.guzzl.fragments;

import java.security.Guard;

import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.drawables.FuelGageBitmap;
import com.mrebhan.guzzl.math.*;
import com.mrebhan.guzzl.interfacesgeneral.*;
import com.mrebhan.guzzl.app.*;
import com.mrebhan.guzzl.utils.*;

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

public class FuelGuageFragment extends Fragment{

	public final static String TAG = "FuelGuageFragment";
	
	private TextView textView;
	private Button buttonOkay;
	private SeekBar seekBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fuel_reading, container, false);
		
		seekBar = (SeekBar) view.findViewById(R.id.seekbar_fuel_guage);
		onProgressChange();
		
		textView = (TextView) view.findViewById(R.id.text_fuel_gauge);
		textView.setText("Full");
		
		buttonOkay = (Button) view.findViewById(R.id.button_ok_car_fuel_reading);
		onOkay();
		
		Button buttonCancel = (Button) view.findViewById(R.id.button_cancel_fuel_reading);
		buttonCancel.setOnClickListener(new CancelCurrentFragment(getActivity()));
		
		return view;
	}
	
	public void onOkay(){
		buttonOkay.setOnClickListener(new OnClickListener() {
			// onclick, get the progress of the seekbar and calculate the amount of fuel left in gallons
			@Override
			public void onClick(View v) {
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
                editor.putFloat(GuzzlApp.PREFERENCE_RANGE, (float) fuelRemaining * sharedPreferences.getInt(GuzzlApp.PREFERENCE_MPG_HW,0));
				editor.commit();
				// Remove this fragment and all of backstack
				FragmentTransactions.removeAllFragments(getActivity());
			}
		});
	}
	
	public void onProgressChange(){
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				textView.setText(FuelFractions.reduceFractions(progress, 16));
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
		});
	}

	
	
}
