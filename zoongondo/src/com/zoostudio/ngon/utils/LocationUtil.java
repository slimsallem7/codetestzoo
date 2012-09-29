package com.zoostudio.ngon.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.zoostudio.adapter.item.InfoAddress;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;

public class LocationUtil {
	private OnAddressChanged listener;
	private OnLocationRecevier mLocationListener;
	private OnAddressSuggestListener mSuggListener;
	
	public void getAddress(final Context context, final Location location) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Locale.setDefault(new Locale("vi"));
				Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
				try {
					List<android.location.Address> addresses = geoCoder
							.getFromLocation(location.getLatitude(),
									location.getLongitude(), 1);
					String newAddress = "";
					StringBuilder builder = new StringBuilder(1024);
					if (addresses.size() > 0) {
						for (int i = 0; i < addresses.get(0)
								.getMaxAddressLineIndex(); i++)
							builder.append(addresses.get(0).getAddressLine(i))
									.append(", ");
					}
					newAddress = builder.toString();
					newAddress = newAddress.trim();
					newAddress.substring(0, newAddress.length() - 3);
					if (listener != null)
						listener.onAddressChange(newAddress);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void getAddress(final Context context, final double lat,
			final double longtitude) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Locale.setDefault(new Locale("vi"));
				Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
				try {
					List<android.location.Address> addresses = geoCoder
							.getFromLocation(lat, longtitude, 5);
					String newAddress = "";
					StringBuilder builder = new StringBuilder(1024);
					if (addresses.size() > 0) {
						for (int j = 0, m = addresses.get(0)
								.getMaxAddressLineIndex(); j < m; j++) {
							builder.append(addresses.get(0).getAddressLine(j))
									.append(", ");
						}
						newAddress = builder.toString();
						builder.delete(0, builder.length());
						newAddress = newAddress.trim();
						listener.onAddressChange(newAddress);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void setOnAddressChangedListener(OnAddressChanged listener) {
		this.listener = listener;
	}

	public void getLongLat(final Context context, final String address) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Locale.setDefault(new Locale("vi"));
				Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
				try {
					List<android.location.Address> addresses = geoCoder
							.getFromLocationName(address, 5);
					if (addresses.size() > 0) {
						
					}
					android.location.Address location = addresses.get(0);
					location.getLatitude();
					location.getLongitude();

					GeoPoint point = new GeoPoint(
							(int) (location.getLatitude() * 1E6),
							(int) (location.getLongitude() * 1E6));
					if (mLocationListener != null)
						mLocationListener.onLocationRecevier(point);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void getSuggestAddress(final Context context, final String address){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Locale.setDefault(new Locale("vi","VN"));
				Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
				try {
					ArrayList<InfoAddress> results = new ArrayList<InfoAddress>();
					List<android.location.Address> addresses = geoCoder
							.getFromLocationName(address, 5);
					if (addresses.size() > 0) {
						String newAddress = "";
						
						StringBuilder builder = new StringBuilder(1024);
						
						for(int i=0,n = addresses.size();i<n;i++){
							InfoAddress infoAddress = new InfoAddress();
							android.location.Address itm = addresses.get(i);
							for (int j = 0, m = itm
									.getMaxAddressLineIndex(); j < m; j++) {
								
								builder.append(itm.getAddressLine(j))
										.append(", ");
								
							}
							
							newAddress = builder.toString();
							builder.delete(0, builder.length());
							newAddress = newAddress.trim();
							newAddress = parseAddress(newAddress);
							infoAddress.setAddress(newAddress);
							
							if(itm.hasLatitude() && itm.hasLongitude()){
								infoAddress.setLatitude(itm.getLatitude());
								infoAddress.setLongtitude(itm.getLongitude());
							}else{
								infoAddress.hasLongLat(false);
							}
							results.add(infoAddress);
						}
						
						mSuggListener.onAddressSuggestRecever(results);
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void setOnLocationRecevier(OnLocationRecevier selectLocationManual) {
		this.mLocationListener = selectLocationManual;
	}

	public static String parseAddress(String address) {
		String newAdd = "";
		for (int i = address.length() - 1; i > 0; i++) {
			if (address.charAt(i) == ',') {
				newAdd = address.substring(0, i);
				break;
			}
		}
		return newAdd;
	}
	
	public void setOnAddressSuggListener(OnAddressSuggestListener listener){
		this.mSuggListener = listener;
	}
}
