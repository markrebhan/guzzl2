package com.mrebhan.guzzl.interfaces;

/* 
 * This interface is used as a callback from the Location service to relay position back to activity
 */
public interface MapOnLocationChanged {
	public void mapOnLocationChanged(double lat, double lon);
}
