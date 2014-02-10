package com.mrebhan.guzzl.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mrebhan.guzzl.R;

public class SeekBarFragment extends Fragment{
	
	private View view;
	private EditText text;
	private SeekBar seekBar;
	
	public static final String SEEK_MAX = "max";
	public static final String SEEK_LABEL = "seek_label";
	public static final String SEEK_VALUE_TYPE = "seek_value_type";
	public static final String SEEK_PROGRESS_IN = "seek_progress_in";

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_slider, container, false);
		text = (EditText) view.findViewById(R.id.slider_value);
		
		// Set the text of the label based on passed in value from bundle
		TextView label = (TextView) view.findViewById(R.id.slider_label);
		label.setText(getArguments().getInt(SEEK_LABEL));
		
		// Set the text of the value type based on passed in value from label
		TextView valueType = (TextView) view.findViewById(R.id.slider_value_type);
		valueType.setText(getArguments().getInt(SEEK_VALUE_TYPE));
		
		// Set seekbar max depending on what was passed in the bundle on fragment object initialization
		setSeekBar(getArguments().getInt(SEEK_MAX), getArguments().getInt(SEEK_PROGRESS_IN));
		
		editTextCallBack();
		//clearTextOnFocus();
		return view;
	}

	/*
	 * This method initializes the seekbar object and sets up the listeners
	 */
	public void setSeekBar(int max, int progress){
		seekBar = (SeekBar) view.findViewById(R.id.slider_seek_bar);
		seekBar.setMax(max);
		seekBar.setProgress(progress);
		text.setText(Integer.toString(progress));
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Here, set the edittext view with the current value
				text = (EditText) view.findViewById(R.id.slider_value);
				text.setText(Integer.toString(progress));
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
		});
	}
	
	public void editTextCallBack(){
		
		text.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() > 0){
					text.setSelection(s.length());
					seekBar.setProgress(Integer.parseInt(s.toString()));
				}
				else{
					seekBar.setProgress(0);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
	}
	
	// Delete value on focus
	public void clearTextOnFocus(){
		text.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) text.setText("");
			}
		});
	}
	
	
	
	

}
