package com.zoostudio.android.image;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class ZooImageDishBorder extends SmartImageView {

	public ZooImageDishBorder(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ZooImageDishBorder(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ZooImageDishBorder(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
}
