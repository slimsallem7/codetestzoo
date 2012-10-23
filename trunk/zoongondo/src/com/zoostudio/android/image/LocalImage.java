package com.zoostudio.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.support.v4.content.Loader;
import android.util.Log;

import com.test.cache.CacheableBitmapWrapper;
import com.zoostudio.ngon.utils.ConfigSize;

public class LocalImage implements SmartImage {

	private static final int IMG_BUFFER_LEN = 256;
	private long idMedia;
	private String pathMedia;
	private volatile boolean cancel;
	private int targetSize;
	private LocalCacheImage localImageCache;

	public LocalImage(long idMedia) {
		this.idMedia = idMedia;
	}

	public LocalImage(String pathMedia) {
		this.pathMedia = pathMedia;
	}

	@Override
	public CacheableBitmapWrapper getWrap(Context context) {
		// Don't leak context
		localImageCache = LocalCacheImage.getInstance(context);
		// Try getting bitmap from cache first
		CacheableBitmapWrapper wrapper = null;
		wrapper = localImageCache.getBitmapFromMemory(pathMedia);
		if (null == wrapper) {
			wrapper = getBitmapFromLocal(pathMedia);
			if (null != wrapper) {
				localImageCache.cacheBitmapToMemory(pathMedia, wrapper);
			}
		}
		return wrapper;
	}

	public void cancel() {
		cancel = true;
	}

	private CacheableBitmapWrapper getBitmapFromLocal(String pathMedia) {
		Bitmap bitmap = null;
		Options options = new Options();
		options.inJustDecodeBounds = true;
		if (cancel)
			return null;
		BitmapFactory.decodeFile(pathMedia, options);
		if (cancel)
			return null;
		// Only scale if we need to
		// (16384 buffer for img processing)
		targetSize = ConfigSize.WIDTH_SCREEN / 3;
		Boolean scaleByHeight = Math.abs(options.outHeight - targetSize) >= Math
				.abs(options.outWidth - targetSize);
		if (options.outHeight * options.outWidth * 2 >= 16384) {
			// Load, scaling to smallest power of 2 that'll get it <= desired
			// dimensions
			double sampleSize = scaleByHeight ? options.outHeight / targetSize
					: options.outWidth / targetSize;
			options.inSampleSize = (int) Math.pow(2d,
					Math.floor(Math.log(sampleSize) / Math.log(2d)));
		}
		// Do the actual decoding
		if (cancel)
			return null;
		options.inJustDecodeBounds = false;
		options.inTempStorage = new byte[IMG_BUFFER_LEN];
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		bitmap = BitmapFactory.decodeFile(pathMedia, options);
		if (null == bitmap) {
			Log.e(this.getClass().getName(), "WTF");
			return null;
		}
		CacheableBitmapWrapper wrapper = new CacheableBitmapWrapper(""
				+ pathMedia, bitmap);
		return wrapper;
	}
}
