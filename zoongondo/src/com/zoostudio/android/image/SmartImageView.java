package com.zoostudio.android.image;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.test.cache.CacheableBitmapWrapper;
import com.zoostudio.ngon.utils.ImageUtil;

public class SmartImageView extends ImageView {
	private static final int LOADING_THREADS = 4;
	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(LOADING_THREADS);

	private SmartImageTask currentTask;
	private int reqWidth;
	private int reqHeight;
	private boolean resize;
	private CacheableBitmapWrapper mDisplayedBitmapWrapper;

	public SmartImageView(Context context) {
		super(context);
	}

	public SmartImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// Helpers to set image by URL
	public void setImageUrl(String url) {
		setImage(new WebImage(url));
	}

	public void setImageUrl(String url, final Integer fallbackResource) {
		setImage(new WebImage(url), fallbackResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(new WebImage(url), fallbackResource, loadingResource);
	}

	// Set image using SmartImage object
	public void setImage(final SmartImage image) {
		setImage(image, null, null);
	}

	public void setImage(final SmartImage image, final Integer fallbackResource) {
		setImage(image, fallbackResource, fallbackResource);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource, final Integer loadingResource) {
		// Set a loading resource
		if (loadingResource != null) {
			setImageResource(loadingResource);
		}

		// Cancel any existing tasks for this image view
		if (currentTask != null) {
			currentTask.cancel();
			currentTask = null;
		}

		// Set up the new task
		currentTask = new SmartImageTask(getContext(), image);
		currentTask
				.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
					@Override
					public void onComplete(CacheableBitmapWrapper bitmap) {
						if (bitmap != null) {
							setImageWrap(bitmap);
						} else {
							// Set fallback resource
							if (fallbackResource != null) {
								setImageResource(fallbackResource);
							}
						}
					}
				});
		// Run the task in a threadpool
		threadPool.execute(currentTask);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if (null == bm) {
			if (currentTask != null)
				currentTask.cancel();
			resetCachedDrawable();
		}
		super.setImageBitmap(bm);
	}

	public void setImageWrap(CacheableBitmapWrapper wrapper) {
		if (null != wrapper && wrapper.hasValidBitmap()) {
			wrapper.setBeingUsed(true);
			wrapper.getImageRef();
			setImageBitmap(wrapper.getBitmap());
		} else {
			resetCachedDrawable();
		}

		// Finally, set our new BitmapWrapper
		mDisplayedBitmapWrapper = wrapper;
	}

	public static void cancelAllTasks() {
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}

	/**
	 * Nen su dung ham nay khi anh qua to
	 * 
	 * @param resId
	 * @param resize
	 */
	public void setImageResource(int resId) {
		if (!resize) {
			super.setImageResource(resId);
		} else {
			Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(
					getResources(), resId, reqWidth, reqHeight);
			Log.i("Bitmap", "Width = " + bitmap.getWidth() + " | height = "
					+ bitmap.getHeight());
			this.setImageBitmap(bitmap);
		}
	}

	public void setSizeImage(int width, int height) {
		this.reqWidth = width;
		this.reqHeight = height;
		this.resize = true;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.e(VIEW_LOG_TAG, "onDetachedFromWindow");
		resetCachedDrawable();
	}

	/**
	 * Called when the current cached bitmap has been removed. This method will
	 * remove the displayed flag and remove this objects reference to it.
	 */
	private void resetCachedDrawable() {
		if (null != mDisplayedBitmapWrapper) {
			mDisplayedBitmapWrapper.setBeingUsed(false);
			mDisplayedBitmapWrapper = null;
		}
	}
}