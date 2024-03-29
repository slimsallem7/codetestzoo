package com.zoostudio.ngon.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class ImageUtil {
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;

		int scale = 1;
		while (width / scale / 2 >= reqWidth && height / scale / 2 >= reqHeight)
			scale *= 2;

		return scale;
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth) {
		final int width = options.outWidth;

		int scale = 1;
		while (width / scale / 2 >= reqWidth)
			scale *= 2;

		return scale;
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.ARGB_8888;
		return BitmapFactory.decodeResource(res, resId, options);
	}
	public static String makeFullPathImage(String url){
		return com.zoostudio.ngon.Config.URL_IMAGE + url;
	}
}
