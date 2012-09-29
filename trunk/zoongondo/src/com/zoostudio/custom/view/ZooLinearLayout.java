package com.zoostudio.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class ZooLinearLayout extends LinearLayout {
	private boolean mTouchAble = true;

	public ZooLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Do not allow touch events.
		return !mTouchAble;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return !mTouchAble;
	}

	public void changeTouchAble() {
		if (mTouchAble) {
			mTouchAble = false;
		} else {
			mTouchAble = true;
		}
	}
}
