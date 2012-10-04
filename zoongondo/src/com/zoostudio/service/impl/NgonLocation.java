package com.zoostudio.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.FloatMath;
import android.util.Log;
import android.widget.Toast;

public class NgonLocation extends Service {
	private LocationManager mLocationManager;
	private Location mCurrentLocation;
	private Location mCurrentLocationAuto;
	private boolean mGpsEnabled = false;
	private boolean mNetworkEnable = false;

	// private OnLocationChangeListener mLocationListener;
	private NgonLocationListener mLocationListener;
	public final static int GPS_OFF = 0;
	public final static int NETWORK_OFF = 1;

	public static final int REQUEST_GPS = 0;
	public static final int REQUEST_PROVIDER = 1;

	public int mErrorCode = 2;
	private final IBinder mBinder = new LocalBinder();
	private Handler mHandler;
	private int mCurrentTypeRequest;

	private long mLastCheck = 0;
	/**
	 * Store last location get from WIFI
	 */
	private Location mLastNetLocation = null;
	/**
	 * Store last location get from GPS
	 */
	private Location mLastGPSLocation = null;

	// public void setOnLocationChangeListener(OnLocationChangeListener
	// mListener) {
	// this.mLocationListener = mListener;
	// }

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("NgonLocation", "onStart");
		mHandler = new Handler();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public boolean isReady() {
		return (null == mCurrentLocation) ? false : true;
	}

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		public NgonLocation getService() {
			return NgonLocation.this;
		}
	}

	public void setCurrentLocation(Location location) {
		this.mCurrentLocation = location;
	}

	private void requestGPS() {
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				0, 0, locationListenerGps);
	}

	private void requestNetwork() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Log.i("NgonLocation", "Request Network");
				mLocationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 5000, 0,
						locationListenerNetwork);
			}
		});
	}

	public void unRegisterUpdateLocation() {
		if (null != locationListenerGps) {
			mLocationManager.removeUpdates(locationListenerGps);
		}
		if (null != locationListenerNetwork) {
			mLocationManager.removeUpdates(locationListenerNetwork);
		}
	}

	private void initServices() {
		try {
			mGpsEnabled = mLocationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			mNetworkEnable = mLocationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean validLocation(Context context) {
		if (mGpsEnabled || mNetworkEnable) {
			return true;
		} else if (!mNetworkEnable) {
			mErrorCode = 1;
			return false;
		} else if (!mGpsEnabled) {
			mErrorCode = 0;
			return false;
		}
		return false;
	}

	LocationListener locationListenerGps = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			boolean rs = isBetterLocation(location, mCurrentLocation);
			if (rs) {
				mCurrentLocation = location;
				if (mLocationListener != null) {
					mLocationListener.onLocationReceiver(mCurrentLocation);
					Log.i("NgonLocation", "onLocationChange");
				}
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "Disable",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "Enable",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			boolean rs = isBetterLocation(location, mCurrentLocation);
			if (rs) {
				mCurrentLocation = location;
				if (mLocationListener != null) {
					Log.e(getClass().getName(), "current mLocationListener:"
							+ mLocationListener.toString());

					mLocationListener.onLocationReceiver(location);
				} else {
					Log.e(getClass().getName(),
							"Error@onLocationChanged: mLocationListener is null");
				}
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(),
					"LocationListenerNetwork - Disable", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(),
					"LocationListenerNetwork - Enabled ", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Toast.makeText(getApplicationContext(),
					"onStatusChanged -  " + status, Toast.LENGTH_SHORT)
					.show();
		}
	};

	public void requestLocation(int type) {
		//
		Log.i("NgonLocation","get Location type");
		if (mLocationManager == null)
			mLocationManager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
		initServices();

		if (mGpsEnabled)
			mLastGPSLocation = mLocationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (mNetworkEnable)
			mLastNetLocation = mLocationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		// if there are both values use the latest one
		if (mLastGPSLocation != null && mLastNetLocation != null) {
			if (mLastGPSLocation.getTime() > mLastNetLocation.getTime()) {
				mCurrentLocation = mLastGPSLocation;
				Log.i(getClass().getName(), "getLastLocation:"
						+ mCurrentLocation.toString());
			} else {
				mCurrentLocation = mLastNetLocation;
				Log.i(getClass().getName(), "getLastLocation:"
						+ mCurrentLocation.toString());
			}
		} else if (mLastGPSLocation != null) {
			mCurrentLocation = mLastGPSLocation;
			Log.i(getClass().getName(),
					"getLastLocation:" + mCurrentLocation.toString());
		} else if (mLastNetLocation != null) {
			mCurrentLocation = mLastNetLocation;
			Log.i(getClass().getName(),
					"getLastLocation:" + mCurrentLocation.toString());
		}
		Log.i("NgonLocation","get Type Location done");
		startThread(type);
	}

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// public interface OnLocationChangeListener {
	// public void onStatusChange(int status);
	//
	// public void onLocationChange(Location location);
	//
	// public void onLocationError(int errorCode);
	// }

	// public void removeLocationListener(OnLocationChangeListener listener) {
	// if (mLocationListener == listener) {
	// Log.e(getClass().getName(), "remove mLocationListener: "
	// + mLocationListener.toString());
	// mLocationListener = null;
	// }
	// }

	public Location getCurrentLocation() {
		return mCurrentLocation;
	}

	public void startThread(int requestType) {
		if (requestType == REQUEST_GPS) {
			mCurrentTypeRequest = REQUEST_GPS;
			requestGPS();
		} else if (requestType == REQUEST_PROVIDER) {
			mCurrentTypeRequest = REQUEST_PROVIDER;
			requestNetwork();
		}
	}

	public boolean isGpsEnable() {
		try {
			mGpsEnabled = mLocationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		return mGpsEnabled;
	}


	private static final int TWO_MINUTES = 1000 * 60 * 2;;

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		double distance = gps2m((float) location.getLatitude(),
				(float) location.getLongitude(),
				(float) currentBestLocation.getLatitude(),
				(float) currentBestLocation.getLongitude());

		if (distance < LocationConfig.getInstance().minDistance) {
			return false;
		}

		DateFormat format = new SimpleDateFormat("MMddyyHHmmss");
		try {
			Date date = format.parse("" + currentBestLocation.getTime());
			Date date1 = format.parse("" + location.getTime());
			Log.i("NgonLocation",
					"Date currentBestLocation = " + date.toLocaleString());
			Log.i("NgonLocation", "Date location = " + date1.toLocaleString());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy

		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	private double gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
		float pk = (float) (180 / 3.14169);

		float a1 = lat_a / pk;
		float a2 = lng_a / pk;
		float b1 = lat_b / pk;
		float b2 = lng_b / pk;

		float t1 = FloatMath.cos(a1) * FloatMath.cos(a2) * FloatMath.cos(b1)
				* FloatMath.cos(b2);
		float t2 = FloatMath.cos(a1) * FloatMath.sin(a2) * FloatMath.cos(b1)
				* FloatMath.sin(b2);
		float t3 = FloatMath.sin(a1) * FloatMath.sin(b1);
		double tt = Math.acos(t1 + t2 + t3);

		return 6371000 * tt;
	}

	public boolean isNetworkEnable() {
		try {
			mNetworkEnable = mLocationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception e) {
			e.printStackTrace();
			mNetworkEnable = false;
		}
		return mNetworkEnable;
	}

	public void unRequestLocation() {
		Log.i("NgonLocation", "unRequestLocation");
		mCurrentLocationAuto = mCurrentLocation;
		if (mCurrentTypeRequest == REQUEST_GPS) {
			mLocationManager.removeUpdates(locationListenerGps);
		}

		if (mCurrentTypeRequest == REQUEST_PROVIDER) {
			mLocationManager.removeUpdates(locationListenerNetwork);
		}
	}

	public void notifyChangeLocation() {
		mLocationListener.onLocationReceiver(mCurrentLocation);
	}

	public void setCurrentLocationByLastLocation() {
		mCurrentLocation = mCurrentLocationAuto;
	}

	public void setLocationListener(NgonLocationListener listener) {
		this.mLocationListener = listener;
	}

	public void requestGetLocaiton() {
		Log.i("NgonLocation","requestLocation");
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (mCurrentLocation != null) {
					if (mLocationListener != null) {
						mLocationListener.onLocationReceiver(mCurrentLocation);
					}
				} else {
					if (mLocationListener != null) {
						mLocationListener.onGettingLocation();
					}
				}
			}
		}).start();
	}
}