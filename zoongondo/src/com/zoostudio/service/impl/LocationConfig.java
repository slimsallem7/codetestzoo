package com.zoostudio.service.impl;

public class LocationConfig {
	private static LocationConfig instance;
	public int minDistance = 0;
	
	public static LocationConfig getInstance() {
		if(null == instance){
			instance = new LocationConfig();
		}
		return instance;
	}
	
	public LocationConfig() {
		
	}
	
	public void saveToReferenece(){
		
	}
	
}
