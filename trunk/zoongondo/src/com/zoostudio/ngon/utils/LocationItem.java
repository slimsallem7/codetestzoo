package com.zoostudio.ngon.utils;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.FloatMath;
import android.util.Log;

public class LocationItem implements Parcelable {

    private double longtitude;
    private double latitude;

    public LocationItem(double longtitude, double latitude) {
        setLatitude(latitude);
        setLongtitude(longtitude);
    }
    
    public LocationItem(Parcel in) {
    	readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
		latitude = in.readDouble();
		longtitude = in.readDouble();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(latitude);
		dest.writeDouble(longtitude);
	}

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double distanceTo(Location location) {
        return distanceTo(location.getLongitude(), location.getLatitude());
    }

    public double distanceTo(double lon, double lat) {
//    	float results[] = new float[3];
//    	Location.distanceBetween(latitude, longtitude, lat, lon, results);
    	float pk = (float) (180 / 3.14169);
		float a1 = (float) (latitude / pk);
		float a2 = (float) (longtitude / pk);
		
		float b1 = (float) (lat / pk);
		float b2 = (float) (lon / pk);

		float t1 = FloatMath.cos(a1) * FloatMath.cos(a2) * FloatMath.cos(b1)
				* FloatMath.cos(b2);
		float t2 = FloatMath.cos(a1) * FloatMath.sin(a2) * FloatMath.cos(b1)
				* FloatMath.sin(b2);
		float t3 = FloatMath.sin(a1) * FloatMath.sin(b1);
		double tt = Math.acos(t1 + t2 + t3);
		
		Log.i("LocationConvert","Distance = " + 6399.592 * tt);
		return 6399.592 * tt;
  
    }

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<LocationItem> CREATOR =
			new Parcelable.Creator<LocationItem>() {
		public LocationItem createFromParcel(Parcel in) {
			return new LocationItem(in);
		}

		public LocationItem[] newArray(int size) {
			return new LocationItem[size];
		}
	};
}
