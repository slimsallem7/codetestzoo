package com.zoostudio.android.image;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
			bitmap = BitmapFactory.decodeStream(bif);
			bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
			is.close();
			bif.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
