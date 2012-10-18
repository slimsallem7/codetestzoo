package com.zoostudio.android.image;

import com.test.cache.CacheableBitmapWrapper;

import android.content.Context;
import android.graphics.Bitmap;

public class LocalImage implements SmartImage {

	private long idMedia;
	private static LocalCacheImage localImageCache;

	public LocalImage(long idMedia) {
		this.idMedia = idMedia;
	}

	@Override
	public CacheableBitmapWrapper getWrap(Context context) {
		// Don't leak context
		if (localImageCache == null) {
			localImageCache = new LocalCacheImage(context);
		}
		// Try getting bitmap from cache first
		CacheableBitmapWrapper wrapper=null;
		Bitmap bitmap = null;
		if (idMedia != -1) {
			bitmap = localImageCache.get(idMedia);
			if (bitmap != null) {
				// bitmap = localIma     geCache.get(-1);
				wrapper = new CacheableBitmapWrapper(""+idMedia, bitmap);
			}
		}

		return wrapper;
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
