package com.zoostudio.android.image;

import com.test.cache.CacheableBitmapWrapper;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images;
import android.support.v4.util.LruCache;

public class LocalCacheImage {
	private LruCache<String, CacheableBitmapWrapper> memoryCache;
	private Context appContext;

	public LocalCacheImage(Context context) {
		final int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = 1024 * 1024 * memClass / 8;
		memoryCache = new LruCache<String, CacheableBitmapWrapper>(cacheSize);
		// Set up disk cache store
		appContext = context;
	}

	public CacheableBitmapWrapper get(final long idMedia) {
		CacheableBitmapWrapper wrapper = null;

		// Check for image in memory
		wrapper = getBitmapFromMemory(""+idMedia);

		// Check for image on disk cache
		if (wrapper == null) {
			wrapper = getBitmapFromLocal(idMedia);
			// Write bitmap back into memory cache
			if (wrapper != null) {
				cacheBitmapToMemory(""+idMedia, wrapper);
			}
		}

		return wrapper;
	}

	public void put(long idMedia, CacheableBitmapWrapper bitmap) {
		cacheBitmapToMemory(""+idMedia, bitmap);
	}

	public void remove(String idMedia) {
		// Remove from memory cache
		memoryCache.remove(idMedia);
	}

	public void clear(Long[] keys) {
		// Remove everything from memory cache
		memoryCache.evictAll();
	}

	private void cacheBitmapToMemory(String idMedia, CacheableBitmapWrapper bitmap) {
		memoryCache.put(idMedia, bitmap);
	}

	private CacheableBitmapWrapper getBitmapFromMemory(String idMedia) {
		CacheableBitmapWrapper wrapper = memoryCache.get(idMedia);
		if (null == wrapper || wrapper.getBitmap().isRecycled())
			return null;
		return wrapper;
	}

	private CacheableBitmapWrapper getBitmapFromLocal(long idMedia) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		bitmap = Images.Thumbnails.getThumbnail(
				appContext.getContentResolver(), idMedia,
				Images.Thumbnails.MINI_KIND, options);
		CacheableBitmapWrapper wrapper = new CacheableBitmapWrapper(""+idMedia, bitmap);
		return wrapper;
	}

}
