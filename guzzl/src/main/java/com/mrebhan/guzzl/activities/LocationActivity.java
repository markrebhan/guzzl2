package com.mrebhan.guzzl.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.animations.MarkerAnimation;
import com.mrebhan.guzzl.app.GuzzlApp;
import com.mrebhan.guzzl.fragments.CenterMapFragment;
import com.mrebhan.guzzl.interfacesgeneral.OnMapCenterClick;
import com.mrebhan.guzzl.interfacesgeneral.OnStateShowRangeOnMapChanged;
import com.mrebhan.guzzl.math.MetricConversion;
import com.mrebhan.guzzl.utils.FragmentTransactions;

/*
 * This Class Finds and Displays current position on Map 
 * and receives updates from service to update the position
 * as it changes.
 */

public class LocationActivity extends LocationServiceHandlerActivity implements
        OnMapClickListener, OnMarkerClickListener, OnStateShowRangeOnMapChanged, OnMapCenterClick {

    public static final String TAG = "LocationActivity";
    public static final int CAMERA_ANIMATION_TIME = 1000; // in ms

    BroadcastReceiver receiver;

    // initialize marker and circle that we will draw on the map
    Marker marker = null;
    Circle circle = null;

    protected final String zoomS = "zoom";
    protected final String centerMapS = "centerMap";
    protected LatLng currentPosition;
    protected double bearing;
    protected double velocity;

    protected float zoom = 15f;
    protected boolean centerMap = true;

    Fragment centerMapFragment = new CenterMapFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            zoom = savedInstanceState.getFloat(zoomS);
            centerMap = savedInstanceState.getBoolean(centerMapS);
        } catch (Exception e) {
            Log.d(TAG, "No saved instance.");
        }

    }

    @Override
    protected void onResume() {
        // register a new listener
        receiver = new LocationReceiver();
        registerReceiver(receiver, new IntentFilter(
                GuzzlApp.ACTION_LOCATION_RECIEVER));
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null)
            unregisterReceiver(receiver);
    }

    // Save map location information in case activity gets destroyed and
    // recreated (screen rotation)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(centerMapS, centerMap);
        outState.putFloat(zoomS, googleMap.getCameraPosition().zoom);

    }

    public void initializeMarker(LatLng latLng) {
        // initialize a new marker and set the position and options
        marker = googleMap.addMarker(new MarkerOptions().position(latLng));
        if (centerMap)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
        // initialize map listeners
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(this);
    }

    public void locationChanged(double[] loc) {
        currentPosition = new LatLng(loc[0], loc[1]);
        bearing = loc[2];
        velocity = loc[3];
        // if the marker is null, create a new marker and position it to the
        // current position
        if (marker == null) {
            initializeMarker(currentPosition);
        } else {
            marker.setPosition(currentPosition);
        }

        // animate the marker given current bearing and velocity
        LatLng finalPosition = MarkerAnimation.animateMarkerHC(marker, bearing, velocity);

        // given the calculated final position, animate the camera to that position if center map is true
        if (centerMap) {
            // start from current position in case position has moved
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
            CameraPosition c = new CameraPosition.Builder().target(finalPosition).bearing((float) bearing)
                    .zoom(googleMap.getCameraPosition().zoom).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(c), CAMERA_ANIMATION_TIME, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onCancel() {

                }
            });
        }

        // create circle or update
        onShowRangeOnMap();
    }

    // implement onStateShowRangeOnMap
    // Do nested ifs to solve issue for configuration change when the circle will be redrawn
    @Override
    public void onShowRangeOnMap() {
        if (GuzzlApp.STATE_SHOW_RANGE_ON_MAP) {
            if (circle == null) {
                circle = googleMap.addCircle(new CircleOptions().center(currentPosition).radius(MetricConversion.milesToMeters(sharedPreferences.getFloat(GuzzlApp.PREFERENCE_RANGE, 0) * 0.8d)).fillColor(R.color.opaque_black));

            } else {
                circle.setCenter(currentPosition);
                circle.setRadius(MetricConversion.milesToMeters(sharedPreferences.getFloat(GuzzlApp.PREFERENCE_RANGE, 0) * 0.8d));
            }
        } else {
            if (circle != null) {
                circle.remove();
                circle = null;
            }
        }
    }

    public void updateRange() {


    }

    // inner class that defines action to be taken when a location broadcast is
    // received
    class LocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // get extras from broadcast
            Bundle extras = intent.getExtras();
            double[] loc = extras.getDoubleArray(GuzzlApp.EXTRA_COORDINATES);
            locationChanged(loc);
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        // turn off centering map on location refresh
        centerMap = false;
        // add a fragment to show button to go back to map center
        FragmentTransactions.replaceCenterMap(this, centerMapFragment);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // set centerMap to true when clicked (centered by default)
        centerMap = true;
        return false;
    }

    @Override
    public void OnCenterMapClick() {
        // on fragment center click callback, center the map and remove fragment
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(currentPosition));
        centerMap = true;
        FragmentTransactions.removeCenterMap(this, centerMapFragment);
    }
}
