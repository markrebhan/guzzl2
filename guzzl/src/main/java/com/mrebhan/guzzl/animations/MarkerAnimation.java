package com.mrebhan.guzzl.animations;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.mrebhan.guzzl.math.LatLngInterpolator;

public class MarkerAnimation {
	
	public static final String TAG = "MarkerAnimation";

	//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static LatLng animateMarkerHC(final Marker marker, final double bearing, final double velocity){
		final LatLng startPosition = marker.getPosition();
        // get the position that the marker will be in 1 second.
        final LatLng finalPosition = LatLngInterpolator.finalPosition(startPosition, bearing, velocity);
		// create new value animator object and implement the update listener to calculate new positions
		// as animation is running.
		ValueAnimator valueAnimator = new ValueAnimator();
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float fraction = animation.getAnimatedFraction();
				LatLng newposition = LatLngInterpolator.interpolate(fraction, startPosition, finalPosition);
				marker.setPosition(newposition);
			}
		});
		
		valueAnimator.setFloatValues(0,1); // default values
		// start the animation and set to 1 second to cancel out s in velocity
		valueAnimator.setDuration(1000).start();

        return finalPosition;
	}
	
}
