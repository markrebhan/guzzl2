package com.mrebhan.guzzl.utils;

import android.content.Context;

/** This class helps determine the actual number of screen pixels required for an object on the UI
 * Created by markrebhan on 2/16/14.
 */
public class PixelConverter {
    public static int calculatePixels(Context context, int baseLinePixels){
        float density = context.getResources().getDisplayMetrics().density;
        float pixels = (float) baseLinePixels * density;
        return (int) pixels;
    }
}
