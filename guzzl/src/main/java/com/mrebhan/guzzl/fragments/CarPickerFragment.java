package com.mrebhan.guzzl.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.app.GuzzlApp;
import com.mrebhan.guzzl.interfacesgeneral.CancelCurrentFragment;
import com.mrebhan.guzzl.utils.*;

public class CarPickerFragment extends Fragment {

	public static final String TAG = "CarPickerFragment";
	public static final String SEEKBAR_PROGRESS = "seekbar_progress";
	private static View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_car_manual, container, false);
		Button buttonCancel = (Button) view.findViewById(R.id.button_cancel_car_picker_manual);
		Button buttonOkay = (Button) view.findViewById(R.id.button_ok_car_picker_manual);
		buttonCancel.setOnClickListener(new CancelCurrentFragment(getActivity()));
		
		buttonOkay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onOkay();
				
			}
		});
		
		int [] in = new int[3];
		// Grab the saved state values of the seekbar if an entry exists or grab values from preferences if they exist when the the first
		// load occurs
		if(savedInstanceState != null && savedInstanceState.getIntArray(SEEKBAR_PROGRESS) != null){
			in = savedInstanceState.getIntArray(SEEKBAR_PROGRESS);
		}
		else
		{
			SharedPreferences sharedPreferences = getActivity().getSharedPreferences(GuzzlApp.PREFERENCE_MAIN, 0);
			in[0] = sharedPreferences.getInt(GuzzlApp.PREFERENCE_MPG_HW, 0);
			in[1] = sharedPreferences.getInt(GuzzlApp.PREFERENCE_MPG_CITY, 0);
			in[2] = sharedPreferences.getInt(GuzzlApp.PREFERENCE_FUEL_SIZE, 0);
		}
		
		// call the method below to initialize 3 seekbar fragments
		passInObjectSpecificParameters(100, R.string.mpg_hw, R.string.mpg, R.id.slider_mpg_highway, in[0]);
		passInObjectSpecificParameters(80, R.string.mpg_city, R.string.mpg, R.id.slider_mpg_city, in[1]);
		passInObjectSpecificParameters(30, R.string.fuel_size, R.string.gal, R.id.slider_fuel_size, in[2]);

		return view;
	}

	// save values the seekbars are currently set to in order to restore in case the fragment is recreated
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		int [] out = getSeekBarData();
		outState.putIntArray(SEEKBAR_PROGRESS, out);
	}
	
	private void passInObjectSpecificParameters(int max, int stringLabel,
			int stringValueType, int frameLayout, int progress) {
		// Create a bundle to pass in id's of object specific values
		Bundle args = new Bundle();
		args.putInt(SeekBarFragment.SEEK_MAX, max);
		args.putInt(SeekBarFragment.SEEK_LABEL, stringLabel);
		args.putInt(SeekBarFragment.SEEK_VALUE_TYPE, stringValueType);
		args.putInt(SeekBarFragment.SEEK_PROGRESS_IN, progress);
		SeekBarFragment fragment = new SeekBarFragment();
		fragment.setArguments(args);
		FragmentTransaction ft = getChildFragmentManager().beginTransaction()
				.replace(frameLayout, fragment);
		ft.commit();
	}
	
	// on click listener defined in XML
	private void onOkay(){
		// Save all the values into shared preferences
		int [] out = getSeekBarData();
		SharedPreferences settings = getActivity().getSharedPreferences(GuzzlApp.PREFERENCE_MAIN, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(GuzzlApp.PREFERENCE_MPG_HW, out[0]);
		editor.putInt(GuzzlApp.PREFERENCE_MPG_CITY, out[1]);
		editor.putInt(GuzzlApp.PREFERENCE_FUEL_SIZE, out[2]);
		editor.commit();
		FragmentTransactions.replaceFuelGauge(getActivity());
	}
	
	private int[] getSeekBarData(){
		SeekBar seekBar1 = (SeekBar) view.findViewById(R.id.slider_mpg_highway).findViewById(R.id.slider_seek_bar);
		SeekBar seekBar2 = (SeekBar) view.findViewById(R.id.slider_mpg_city).findViewById(R.id.slider_seek_bar);
		SeekBar seekBar3 = (SeekBar) view.findViewById(R.id.slider_fuel_size).findViewById(R.id.slider_seek_bar);
		int [] seekBarValues = {seekBar1.getProgress(), seekBar2.getProgress(), seekBar3.getProgress()};
		return seekBarValues;
	}
}
