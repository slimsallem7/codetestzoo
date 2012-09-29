package com.zoostudio.adapter.item;

public class InfoAddress {
	private String mAddress;
	private double mLongtitude;
	private double mLatitude;
	private boolean mHasLongLat;
	
	public InfoAddress() {
	}
	
	public InfoAddress(String mAddress, double mLongtitude, double mLatitude) {
		super();
		this.setAddress(mAddress);
		this.setLongtitude(mLongtitude);
		this.setLatitude(mLatitude);
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public double getLongtitude() {
		return mLongtitude;
	}

	public void setLongtitude(double mLongtitude) {
		this.mLongtitude = mLongtitude;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}
	
	public String toString(){
		return this.mAddress;
	}

	public void hasLongLat(boolean b) {
		this.mHasLongLat = b;
	}
	public boolean hasLongLat(){
		return mHasLongLat;
	}
	
}
