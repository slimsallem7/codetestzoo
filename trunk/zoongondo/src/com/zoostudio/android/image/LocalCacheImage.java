package com.zoostudio.android.image;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.util.LruCache;

import com.test.cache.CacheableBitmapWrapper;

public class LocalCacheImage {
	ConcurrentHashMap<String, WeakReference<CacheableBitmapWrapper>> concurrentHashMap;
//	private LruCache<String, CacheableBitmapWrapper> memoryCache;
	private static LocalCacheImage cacheImage;

	public static LocalCacheImage getInstance(Context context) {
		if (null == cacheImage) {
			synchronized (LocalCacheImage.class) {
				cacheImage = new LocalCacheImage(context);
			}
		}
		return cacheImage;
	}

	public LocalCacheImage(Context context) {
		final int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		final int cacheSize = 1024 * 1024 * memClass / 8;
//		memoryCache = new LruCache<String, CacheableBitmapWrapper>(cacheSize);
		concurrentHashMap = new ConcurrentHashMap<String, WeakReference<CacheableBitmapWrapper>>();
	}

	public CacheableBitmapWrapper get(final String pathMedia) {
		CacheableBitmapWrapper wrapper = null;
		// Check for image in memory
		wrapper = getBitmapFromMemory(pathMedia);
		return wrapper;
	}

	public void remove(String idMedia) {
		// Remove from memory cache
		// memoryCache.remove(idMedia);
	}

	public void clear(Long[] keys) {
		// Remove everything from memory cache
		// memoryCache.evictAll();
	}

	public void cacheBitmapToMemory(String idMedia,
			CacheableBitmapWrapper bitmap) {
		WeakReference<CacheableBitmapWrapper> reference = new WeakReference<CacheableBitmapWrapper>(
				bitmap);
		concurrentHashMap.put(idMedia, reference);
		// memoryCache.put(idMedia, bitmap);
	}

	public CacheableBitmapWrapper getBitmapFromMemory(String idMedia) {
		// CacheableBitmapWrapper wrapper = memoryCache.get(idMedia);
		CacheableBitmapWrapper wrapper = null;
		WeakReference<CacheableBitmapWrapper> reference = concurrentHashMap
				.get(idMedia);
		if (reference != null) {
			wrapper = reference.get();
		} else {
			return null;
		}
		if (null== wrapper ||wrapper.getBitmap().isRecycled())
			return null;
		return wrapper;
	}

}
