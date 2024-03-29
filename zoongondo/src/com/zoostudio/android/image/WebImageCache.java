package com.zoostudio.android.image;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.test.cache.CacheableBitmapWrapper;

public class WebImageCache {
	private static final String DISK_CACHE_PATH = "/web_image_cache/";

	private static final String TAG = "WebImageCache";

	private LruCache<String, CacheableBitmapWrapper> lruCache;
	private String diskCachePath;
	private boolean diskCacheEnabled = false;
	private ExecutorService writeThread;

	private Context appContext;

	public WebImageCache(Context context) {
		// Set up in-memory cache store
		// memoryCache = new ConcurrentHashMap<String,
		// SoftReference<CacheableBitmapWrapper>>();
		// OutOfMemory exception.
		final int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = 1024 * 1024 * memClass /6;
		lruCache = new LruCache<String, CacheableBitmapWrapper>(cacheSize);
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

	public CacheableBitmapWrapper get(final String url) {
		CacheableBitmapWrapper wrapper = null;

		// Check for image in memory
		wrapper = getBitmapFromMemory(url);

		// Check for image on disk cache
		if (wrapper == null) {
			Log.e("WebImageCache", url + " = NULL");
			wrapper = getBitmapFromDisk(url);

			// Write bitmap back into memory cache
			if (wrapper != null) {
				lruCache.put(url, wrapper);
			}
		}

		return wrapper;
	}

	public void put(String url, CacheableBitmapWrapper wrapper) {
		lruCache.put(url, wrapper);
		cacheBitmapToDisk(url, wrapper);
	}

	private void cacheBitmapToDisk(String url, CacheableBitmapWrapper wrapper) {
		writeThread.execute(new CacheToDisk(url, wrapper));
	}

	public void remove(String url) {
		if (url == null) {
			return;
		}
		// Log.e("WebImageCache", "Remove " + url + " Khoi memory");
		// Remove from memory cache
		// lruCache.remove(url);
	}

	public void clearMemory() {
		// if (null != lruCache)
		// lruCache.evictAll();
	}

	public void clear() {
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

	private class CacheToDisk implements Runnable {
		private String url;
		private CacheableBitmapWrapper wrapper;

		public CacheToDisk(String url, CacheableBitmapWrapper wrapper) {
			this.url = url;
			this.wrapper = wrapper;
			this.wrapper.setCached(true);
		}

		@Override
		public void run() {
			if (diskCacheEnabled) {
				BufferedOutputStream ostream = null;
				try {
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					Log.e(TAG, "cacheBitmapToDisk url =" + url);
					ostream = new BufferedOutputStream(new FileOutputStream(
							new File(diskCachePath, getCacheKey(url))),
							2 * 1024);
					// bitmap.compress(CompressFormat.PNG, 100, ostream);
					wrapper.getBitmap()
							.compress(CompressFormat.PNG, 100, bytes);
					ostream.write(bytes.toByteArray());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (ostream != null) {
							ostream.flush();
							ostream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			wrapper.setCached(false);
		}

	}

	private CacheableBitmapWrapper getBitmapFromMemory(String url) {
		CacheableBitmapWrapper wrapper = null;
		wrapper = lruCache.get(url);
		if (null == wrapper || wrapper.getBitmap().isRecycled()) {
			lruCache.remove(url);
			return null;
		}
		return wrapper;
	}

	private CacheableBitmapWrapper getBitmapFromDisk(String url) {
		CacheableBitmapWrapper wrapper = null;
		Bitmap bitmap = null;
		if (diskCacheEnabled) {
			String filePath = getFilePath(url);
			File file = new File(filePath);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(filePath);
				if (null == bitmap) {
					Log.e(TAG, "Khong the lay  url tu Disk =" + url);
					return null;
				}
				Log.e(TAG, "Url tu Disk =" + url);
				wrapper = new CacheableBitmapWrapper(getCacheKey(url), bitmap);
			} else {
				Log.e(TAG, "Khong the lay tu Disk =" + url);
			}
		}
		return wrapper;
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