package com.zoostudio.android.image;

import android.content.Context;

import com.test.cache.CacheableBitmapWrapper;

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
		if (idMedia != -1) {
			wrapper = localImageCache.get(idMedia);
		}

		return wrapper;
	}

	public static void removeFromCache(long idMedia) {
		if (localImageCache != null) {
			localImageCache.remove(""+idMedia);
		}
	}

	public static void clearMemory(Long[] keys) {
		if (null != localImageCache)
			localImageCache.clear(keys);
	}
}
