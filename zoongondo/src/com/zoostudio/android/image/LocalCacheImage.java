package com.zoostudio.android.image;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images;
import android.support.v4.util.LruCache;

public class LocalCacheImage {
//	private ConcurrentHashMap<Long, SoftReference<Bitmap>> memoryCache;
	private LruCache<Long, Bitmap> memoryCache;
	private Context appContext;

	public LocalCacheImage(Context context) {
		// Set up in-memory cache store
//		memoryCache = new ConcurrentHashMap<Long, SoftReference<Bitmap>>();
		final int memClass = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = 1024 * 1024 * memClass / 8;
		memoryCache = new LruCache<Long, Bitmap>(cacheSize);
		// Set up disk cache store
		appContext = context;
	}

	public Bitmap get(final long idMedia) {
		Bitmap bitmap = null;

		// Check for image in memory
		bitmap = getBitmapFromMemory(idMedia);

		// Check for image on disk cache
		if (bitmap == null) {
			bitmap = getBitmapFromLocal(idMedia);
			// Write bitmap back into memory cache
			if (bitmap != null) {
				cacheBitmapToMemory(idMedia, bitmap);
			}
		}

		return bitmap;
	}

	public void put(long idMedia, Bitmap bitmap) {
		cacheBitmapToMemory(idMedia, bitmap);
	}

	public void remove(long idMedia) {
		if (idMedia == -1) {
			return;
		}

		// Remove from memory cache
		memoryCache.remove(idMedia);
	}

	public void clear(Long[] keys) {
		// Remove everything from memory cache
		memoryCache.evictAll();
	}

	private void cacheBitmapToMemory(final long idMedia, final Bitmap bitmap) {
		memoryCache.put(idMedia, bitmap);
	}

	private Bitmap getBitmapFromMemory(long idMedia) {
		Bitmap bitmap = memoryCache.get(idMedia);
		return bitmap;
	}

	private Bitmap getBitmapFromLocal(long idMedia) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		bitmap = Images.Thumbnails.getThumbnail(
				appContext.getContentResolver(), idMedia,
				Images.Thumbnails.MINI_KIND, options);
		return bitmap;
	}

}
