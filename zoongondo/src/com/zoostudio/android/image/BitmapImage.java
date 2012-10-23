package com.zoostudio.android.image;

import android.content.Context;

import com.test.cache.CacheableBitmapWrapper;

public class BitmapImage implements SmartImage {
    private CacheableBitmapWrapper bitmap;

    public BitmapImage(CacheableBitmapWrapper bitmap) {
        this.bitmap = bitmap;
    }

    public CacheableBitmapWrapper getWrap(Context context) {
        return bitmap;
    }

	@Override
	public void cancel() {
		
	}
}