package com.zoostudio.service.impl;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.zoostudio.exception.ZooLocationException;
import com.zoostudio.service.impl.NgonLocation.LocalBinder;

public class NgonLocationManager {
	private static NgonLocationManager instance;
	private boolean mBound;
	private NgonLocation mNgonLocation;
	private Activity activity;
	private OnNgonServiceListener mServiceListener;
	private int type;
	public final static int AUTO = 0;
	public final static int MANUAL = 1;
	private int mCurrentType = AUTO;
	
	public static NgonLocationManager getInstance(Activity activity){
		if(null == instance){
			instance = new NgonLocationManager(activity);
		}
		return instance;
	}
	
	public NgonLocationManager(Activity activity) {
		this.activity = activity;
	}
	public void resetActivity(Activity activity){
		this.activity = activity;
	}

	public Location getCurrentLocation() throws ZooLocationException{
		if(null == mNgonLocation) throw new ZooLocationException("Service  null");
		Location location = mNgonLocation.getCurrentLocation();
		if(null!= location){
			return location;
		}
		else{
			throw new ZooLocationException("lastKnownLocation null");
		}
	}
	
	/**
	 * Chay service theo GPS hoac 3G/Wifi
	 * @param type SERVICE_GPS hoac SERVICE_PROVIDER
	 * @return
	 */
	public boolean bindNgonService(int type) {
		this.type = type;
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(!mBound){
					Intent intent = new Intent(activity, NgonLocation.class);
					boolean rs = activity.getApplicationContext().bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
					
					if(rs == false){
						mServiceListener.onErrorStartService();
					}
				}else{
					mServiceListener.onReadyService();
				}
			}
		}).start();
		
		return false;
	}
	
	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			mNgonLocation = binder.getService();
			mBound = true;
			mNgonLocation.requestLocation(NgonLocationManager.this.type);
			mServiceListener.onReadyService();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	public void setCurrentLocation(Location location) {
		mNgonLocation.setCurrentLocation(location);
	}

	public boolean isLocationReady() {
		return mBound;
	}
	
	public boolean isGPSEnable() {
		return mNgonLocation.isGpsEnable();
	}
	
	public void unBind() {
		if (mBound) {
			Log.i("LocationService","UNBIND");
			mNgonLocation.unRegisterUpdateLocation();
			activity.getApplicationContext().unbindService(mConnection);
			mBound = false;
		}
	}


	public boolean isBounded() {
		return mBound;
	}
	
	public interface OnNgonServiceListener{
		public void onReadyService();
		public void onErrorStartService();
	}

	public void setOnServiceListener(OnNgonServiceListener listener) {
		this.mServiceListener = listener;
		
	}

	public int typeService() {
		return this.type;
	}

	public boolean isNetWorkEnable() {
		return mNgonLocation.isNetworkEnable();
	}
	
	public void changeLocationToManual(){
		if(mCurrentType == MANUAL) return;
		mCurrentType = MANUAL;
		mNgonLocation.unRequestLocation();
	}
	
	public void changeLocationToAuto(){
		if(mCurrentType == AUTO) return;
		mCurrentType = AUTO;
		mNgonLocation.setCurrentLocationByLastLocation();
	}
	
	public void setCurrentLocationByManual(Location location){
		mNgonLocation.setCurrentLocation(location);
	}

	public boolean isAuto() {
		return mCurrentType == AUTO ? true : false;
	}

	public void resetAll() {
		mBound = false;
//		instance = null;
	}

	public boolean isManual() {
		return mCurrentType == MANUAL ? true : false;
	}

	public void notifiChangeLocation() {
		mNgonLocation.notifyChangeLocation();
	}

	public void requestLocation(NgonLocationListener listener) {
		mNgonLocation.setLocationListener(listener);
		mNgonLocation.requestGetLocaiton();
	}
}
