package com.zoostudio.android.image;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;

public class ZooScaleAbleWebImage extends WebImage {
	private int size;

	public ZooScaleAbleWebImage(String url, int size) {
		super(url);
		this.size = size;
	}

	@Override
	protected Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			InputStream is = conn.getInputStream();
			BufferedInputStream bif = new BufferedInputStream(is);
			Options options = new Options();
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeStream(bif);
			Matrix matrix = new Matrix();
			matrix.postScale(bitmap.getWidth()/size, bitmap.getHeight()/size);
			bitmap = Bitmap.createScaledBitmap(bitmap, size, size, false);
			is.close();
			bif.close();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
