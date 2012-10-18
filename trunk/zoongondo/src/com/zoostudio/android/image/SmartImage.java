package com.zoostudio.android.image;

import com.test.cache.CacheableBitmapWrapper;

import android.content.Context;
import android.graphics.Bitmap;

public interface SmartImage {
    public CacheableBitmapWrapper getWrap(Context context);
}