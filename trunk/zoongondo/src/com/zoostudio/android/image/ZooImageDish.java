package com.zoostudio.android.image;

import com.zoostudio.ngon.utils.ConfigSize;

import android.content.Context;
import android.util.AttributeSet;

public class ZooImageDish extends ZooImageThumb {

	public ZooImageDish(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ZooImageDish(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ZooImageDish(Context context) {
		super(context);
	}
	
	@Override
	public void setImageUrl(String url) {
		setImage(new ZooScaleAbleWebImage(url, ConfigSize.SIZE_THUMB));
	}
}
