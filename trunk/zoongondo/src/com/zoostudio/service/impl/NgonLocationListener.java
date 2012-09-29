package com.zoostudio.service.impl;

import android.location.Location;

public interface NgonLocationListener {
	public void onGettingLocation();
	public void onFailGetLocation();
	public void onLocationReceiver(Location location);
}
