package com.mrebhan.guzzl.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.app.GuzzlApp;

/**
 * Created by markrebhan on 3/7/14.
 */
// extend FuelGaugeFragment class
public class FuelRefillFragment extends FuelGuageFragment {

    static float calculatedfuelFillAmount;
    static float actualFuelAmount;
    static EditText textFuelAdded;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // make the items in the view visible for refueling
        Button buttonAdd = (Button) view.findViewById(R.id.button_plus);
        Button buttonSub = (Button) view.findViewById(R.id.button_minus);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layout_fuel_amount_added);

        buttonAdd.setVisibility(View.VISIBLE);
        buttonSub.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);

        textFuelAdded = (EditText) view.findViewById(R.id.edittext_fuel_level);
        textFuelAdded.setText(Float.toString(getFuelAmountRefill()));
        textFuelAdded.addTextChangedListener(new OnTextChanged());

        buttonAdd.setOnClickListener(new OnAddClick());
        buttonSub.setOnClickListener(new OnSubClick());

        return super.view;
    }

    // get the amount of fuel calculated to fill tank
    public float getFuelAmountRefill(){
        float fuelRemaining = super.sharedPreferences.getFloat(GuzzlApp.PREFERENCE_FUEL_REMAINING, 0);
        int fuelSize = super.sharedPreferences.getInt(GuzzlApp.PREFERENCE_FUEL_SIZE, 0);
        calculatedfuelFillAmount = fuelSize - fuelRemaining;
        actualFuelAmount = calculatedfuelFillAmount;
        return calculatedfuelFillAmount;
    }

    // The following classes are implementations of onclicklisteners for + and - and increment the value in the edittext.
    static class OnAddClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            actualFuelAmount += 0.1;
            textFuelAdded.setText(Float.toString(actualFuelAmount));
        }
    }

    static class OnSubClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            actualFuelAmount -= 0.1;
            textFuelAdded.setText(Float.toString(actualFuelAmount));
        }
    }

    // the class implements textwatcher interface which we can set values as text is being changed
    static class OnTextChanged implements TextWatcher{


        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            String text = textFuelAdded.getText().toString();
            // try to convert the text to a float value
            try {
                actualFuelAmount = Float.parseFloat(text);
            }
            catch (Exception e){
                Log.d(TAG, "error parsing String to a Float value");
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}
