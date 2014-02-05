package com.mrebhan.guzzl.math;

public class MetricConversion {

	public static double milesToMeters(double miles){
		return miles * 1609.34d;
	}
	
	public static double gallonToLiters(double gallons){
		return gallons * 3.78541d;
	}
	
	public static double metersToMiles(double meters){
		return meters * 0.000621371d;
	}
	
	public static double litersToGallons(double liters){
		return liters * 0.264172d;
	}
}
