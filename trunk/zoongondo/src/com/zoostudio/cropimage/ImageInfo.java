package com.zoostudio.cropimage;

import java.io.IOException;

import android.media.ExifInterface;

public class ImageInfo {
	public static final int LEFT_LANS = 0;
	public static final int RIGHT_LANS = 1;
	public static final int TOP_PORT = 2;
	public static final int BOTTOM_PORT = 3;
	private String mDateTime;
	private String mFlash;
	private String mGpsLat;
	private int mOrientation;
	public ImageInfo() {
		mOrientation  = ExifInterface.ORIENTATION_NORMAL;
	}
	public ImageInfo(String filename) {
		try {
			ExifInterface exif = new ExifInterface(filename);
			getExif(exif);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getExif(ExifInterface exif) {
		mDateTime = getTagString(ExifInterface.TAG_DATETIME, exif);
		mFlash = getTagString(ExifInterface.TAG_FLASH, exif);
		mGpsLat = getTagString(ExifInterface.TAG_GPS_LATITUDE, exif);
		mOrientation = Integer.parseInt(getTagString(ExifInterface.TAG_ORIENTATION, exif));
	}

	private String getTagString(String tag, ExifInterface exif) {
		return exif.getAttribute(tag);
	}

	public String getDateTime() {
		return mDateTime;
	}

	/**
	 * @return the mFlash
	 */
	public String getFlash() {
		return mFlash;
	}

	/**
	 * @return the mGpsLat
	 */
	public String getGpsLat() {
		return mGpsLat;
	}

	public int getOrientation() {
		return mOrientation;
	}

}
