package com.mrebhan.guzzl.fragments;

import com.mrebhan.guzzl.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CarPickerFragment extends Fragment{

	public static final String TAG = "CarPickerFragment";
	// The argument key for the page number this fragment represents
	static final String ARG_PAGE = "page";
	
	// The fragments page number
	private int mPageNumber;
	
	/**
	 * Factory constructor method for this fragment to construct a new fragment given the page number
	 */
	public static CarPickerFragment create(int pageNumber){
		CarPickerFragment fragment = new CarPickerFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	// Generic constructor when this fragment is first intialized
	public CarPickerFragment() {}
	
	// set page number equal to whatever page number is passed into fragment object
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On Create!");
        mPageNumber = getArguments().getInt(ARG_PAGE);
        
    }
	
	public void destroy(){
		onDestroy();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		 Log.d(TAG, "On Destroy!");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// If page number is 1 return manual setup else return main setup
		if(mPageNumber == 1) return inflater.inflate(R.layout.fragment_car_manual, container, false);
		else return inflater.inflate(R.layout.fragment_car_picker, container, false);
	}
	
	
	
	/**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

}
