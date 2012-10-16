package com.zoostudio.android.image;

import android.content.Context;
import android.graphics.Bitmap;

public class LocalImage implements SmartImage {

	private long idMedia;
	private static LocalCacheImage localImageCache;

	public LocalImage(long idMedia) {
		this.idMedia = idMedia;
	}

	@Override
	public Bitmap getBitmap(Context context) {
		// Don't leak context
		if (localImageCache == null) {
			localImageCache = new LocalCacheImage(context);
		}
		// Try getting bitmap from cache first
		Bitmap bitmap = null;
		if (idMedia != -1) {
			bitmap = localImageCache.get(idMedia);
			if (bitmap == null) {
				// bitmap = localIma     geCache.get(-1);
			}
		}

		return bitmap;
	}

	public static void removeFromCache(long idMedia) {
		if (localImageCache != null) {
			localImageCache.remove(idMedia);
		}
	}

	public static void clearMemory(Long[] keys) {
		if (null != localImageCache)
			localImageCache.clear(keys);
	}
}
