package com.mrebhan.guzzl.utils;

import java.text.DecimalFormat;

/** This class builds a formatted string for all values that are displayed in the UI
 * Created by markrebhan on 2/14/14.
 */
public class DecimalFormatter {
    public static String decimalFormatter(double value){
        // thr @ symbolizes number of sig figs.
        if (value >= 10.0d){
            DecimalFormat dec = new DecimalFormat("@@@");
            return  dec.format(value);
        }
        else if(value >= 1.0d){
            DecimalFormat dec = new DecimalFormat("@@");
            return  dec.format(value);
        }
        else{
            DecimalFormat dec = new DecimalFormat("@");
            return  dec.format(value);
        }

    }
}
