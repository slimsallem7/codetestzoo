package com.zoostudio.android.image;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WebImage implements SmartImage {
	protected static final int CONNECT_TIMEOUT = 20000;
	protected static final int READ_TIMEOUT = 10000;

	public static WebImageCache webImageCache;

	protected String url;

	public WebImage(String url) {
		this.url = url;
	}


	public Bitmap getBitmap(Context context) {
		// Don't leak context
		if (webImageCache == null) {
			webImageCache = new WebImageCache(context);
		}

		// Try getting bitmap from cache first
		Bitmap bitmap = null;
		if (url != null) {
			bitmap = webImageCache.get(url);
			if (bitmap == null) {
				bitmap = getBitmapFromUrl(url);
				if (bitmap != null) {
					webImageCache.put(url, bitmap);
				}
			}
		}

		return bitmap;
	}

	public Bitmap getBorderBitmap(Context context) {
		// Don't leak context
		if (webImageCache == null) {
			webImageCache = new WebImageCache(context);
		}
		// Try getting bitmap from cache first
		Bitmap bitmap = null;
		if (url != null) {
			bitmap = webImageCache.getImageBorder(url);
		}

		return bitmap;
	}

	protected Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			InputStream is = conn.getInputStream();
			BufferedInputStream buf = new BufferedInputStream(is);			
			bitmap = BitmapFactory.decodeStream(buf);
			is.close();
			buf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static void removeFromCache(String url) {
		if (webImageCache != null) {
			webImageCache.remove(url);
		}
	}
	
}