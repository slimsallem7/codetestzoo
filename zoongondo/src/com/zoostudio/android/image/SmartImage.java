package com.zoostudio.android.image;

import android.content.Context;

import com.test.cache.CacheableBitmapWrapper;

public interface SmartImage {
    public CacheableBitmapWrapper getWrap(Context context);
}