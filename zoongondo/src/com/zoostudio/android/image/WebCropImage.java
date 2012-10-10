package com.zoostudio.android.image;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.zoostudio.ngon.utils.ScreenUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class WebCropImage extends WebImage {

	private int reqWidth;

	public WebCropImage(String url) {
		super(url);
	}
	
	protected Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			InputStream is = conn.getInputStream();
			BufferedInputStream buf = new BufferedInputStream(is);
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(buf, null, options);
			options.inSampleSize = calculateInSampleSize(options, reqWidth);
			is.close();
			buf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}
	
	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth) {
		// Raw height and width of image
		final int width = options.outWidth;

		int scale = 1;
		while (width / scale / 2 >= reqWidth)
			scale *= 2;

		return scale;
	}
}
