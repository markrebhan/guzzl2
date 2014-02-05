package com.mrebhan.guzzl.math;

public class FuelFractions {

	public static String reduceFractions(int num, int den){
		
		String out = "";
		int num2 = num;
		int den2 = den;
		
		if(num == 0) out = "Empty";
		else if(num == den) out = "Full";
		else{
			boolean isReduced = false;
			while(!isReduced){
				if(num2 % 2 == 0 && den2 % 2 == 0){
					num2 = num2 / 2;
					den2 = den2 / 2;
				}
				else {
					out = Integer.toString(num2) + "/" + Integer.toString(den2) + " Full";
					isReduced = true;
				}
			}
		}
		
		return out;
		
	}
	
}
