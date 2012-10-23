package com.zoostudio.android.image;

import android.content.Context;
import android.util.AttributeSet;

import com.test.cache.CacheableBitmapWrapper;

public class ZooAjustImageView extends SmartImageView {
	
	public ZooAjustImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public ZooAjustImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ZooAjustImageView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		// setScaleType(ScaleType.CENTER_CROP);
		// setLayoutParams(new
		// RelativeLayout.LayoutParams(NgonGridMediaAdapter.SIZE,
		// NgonGridMediaAdapter.SIZE));
	}

	@Override
	public void setImageWrap(CacheableBitmapWrapper wrapper) {
		setImageBitmap(wrapper.getBitmap());
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (null != mDisplayedBitmapWrapper) {
			mDisplayedBitmapWrapper.setBeingUsed(false);
			mDisplayedBitmapWrapper = null;
		}
	}

	@Override
	protected void resetCachedDrawable() {
	}

	// Helpers to set image by URL
	public void setImageId(long idMedia) {
		setImage(new LocalImage(idMedia));
	}

	// Helpers to set image by URL
	public void setImagePath(String pathMedia) {
		setImage(new LocalImage(pathMedia));
	}

	public void setImageId(long idMedia, int fallbackResId) {
		setImage(new LocalImage(idMedia), fallbackResId);
	}

}
