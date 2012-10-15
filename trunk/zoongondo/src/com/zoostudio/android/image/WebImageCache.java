package com.zoostudio.android.image;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.zoostudio.ngon.R;

public class WebImageCache {
	private static final String DISK_CACHE_PATH = "/web_image_cache/";

	// private ConcurrentHashMap<String, SoftReference<Bitmap>> memoryCache;
	private LruCache<String, Bitmap> lruCache;
	private String diskCachePath;
	private boolean diskCacheEnabled = false;
	private ExecutorService writeThread;

	private Context appContext;

	public WebImageCache(Context context) {
		// Set up in-memory cache store
		// memoryCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();
		// OutOfMemory exception.
		final int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = 1024 * 1024 * memClass / 8;
		lruCache = new LruCache<String, Bitmap>(cacheSize);
		// Set up disk cache store
		appContext = context.getApplicationContext();

		diskCachePath = appContext.getCacheDir().getAbsolutePath()
				+ DISK_CACHE_PATH;

		File outFile = new File(diskCachePath);
		outFile.mkdirs();

		diskCacheEnabled = outFile.exists();

		// Set up threadpool for image fetching tasks
		writeThread = Executors.newSingleThreadExecutor();
	}

	public Bitmap get(final String url) {
		Bitmap bitmap = null;

		// Check for image in memory
		bitmap = getBitmapFromMemory(url);

		// Check for image on disk cache
		if (bitmap == null) {
			bitmap = getBitmapFromDisk(url);

			// Write bitmap back into memory cache
			if (bitmap != null) {
				cacheBitmapToMemory(url, bitmap);
			}
		}

		return bitmap;
	}

	public void put(String url, Bitmap bitmap) {
		cacheBitmapToMemory(url, bitmap);
		cacheBitmapToDisk(url, bitmap);
	}

	public Bitmap getImageBorder(String id) {
		Bitmap bitmap = null;
		// Check for image in memory
		bitmap = getBitmapFromMemory(id);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeStream(appContext.getResources()
					.openRawResource(R.drawable.ic_border_circle));
			if (bitmap != null) {
				cacheBitmapToMemory(id, bitmap);
			}
		}
		return bitmap;
	}

	public void remove(String url) {
		if (url == null) {
			return;
		}

		// Remove from memory cache
		// memoryCache.remove(getCacheKey(url));
		lruCache.remove(getCacheKey(url));

		// Remove from file cache
		File f = new File(diskCachePath, url);
		if (f.exists() && f.isFile()) {
			f.delete();
		}
	}

	public void clearMemory() {
		if (null != lruCache)
			lruCache.evictAll();
	}

	public void clear() {
		// Remove everything from memory cache
		// memoryCache.clear();
		lruCache.evictAll();
		// Remove everything from file cache
		File cachedFileDir = new File(diskCachePath);
		if (cachedFileDir.exists() && cachedFileDir.isDirectory()) {
			File[] cachedFiles = cachedFileDir.listFiles();
			for (File f : cachedFiles) {
				if (f.exists() && f.isFile()) {
					f.delete();
				}
			}
		}
	}

	private void cacheBitmapToMemory(final String url, final Bitmap bitmap) {
		// memoryCache.put(getCacheKey(url), new SoftReference<Bitmap>(bitmap));
		lruCache.put(getCacheKey(url), bitmap);
	}

	private void cacheBitmapToDisk(final String url, final Bitmap bitmap) {
		writeThread.execute(new Runnable() {
			@Override
			public void run() {
				if (diskCacheEnabled) {
					BufferedOutputStream ostream = null;
					try {
						ostream = new BufferedOutputStream(
								new FileOutputStream(new File(diskCachePath,
										getCacheKey(url))), 2 * 1024);
						bitmap.compress(CompressFormat.PNG, 100, ostream);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						try {
							if (ostream != null) {
								ostream.flush();
								ostream.close();
							}
						} catch (IOException e) {
						}
					}
				}
			}
		});
	}

	private Bitmap getBitmapFromMemory(String url) {
		Bitmap bitmap = null;
		// SoftReference<Bitmap> softRef = memoryCache.get(getCacheKey(url));
		bitmap = lruCache.get(getCacheKey(url));
		// if (softRef != null) {
		// bitmap = softRef.get();
		// }

		return bitmap;
	}

	private Bitmap getBitmapFromDisk(String url) {
		Bitmap bitmap = null;
		if (diskCacheEnabled) {
			String filePath = getFilePath(url);
			File file = new File(filePath);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(filePath);
			}
		}
		return bitmap;
	}

	private String getFilePath(String url) {
		return diskCachePath + getCacheKey(url);
	}

	private String getCacheKey(String url) {
		if (url == null) {
			throw new RuntimeException("Null url passed in");
		} else {
			return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
		}
	}
}