package com.zoostudio.android.image;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;

public class ZooAvatarWebImage extends WebImage {
	private int size;

	public ZooAvatarWebImage(String url, int size) {
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
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
			is.close();
			bif.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
