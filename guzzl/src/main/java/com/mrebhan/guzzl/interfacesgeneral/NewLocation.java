package com.mrebhan.guzzl.interfacesgeneral;

import com.google.android.gms.maps.model.LatLng;

public interface NewLocation {
	public void onLocationChanged(LatLng latLng, double bearing, double velocity);
}
