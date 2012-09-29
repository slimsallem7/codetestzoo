package com.zoostudio.custom.view;

import com.zoostudio.custom.view.ZooHorizontalScrollView.SizeCallback;

import android.util.Log;
import android.view.View;

public class SizeCallbackForMenu implements SizeCallback {
	int btnWidth;
	View btnSlide;
	private OnWidthOfButtonChanged buttonChanged;

	public SizeCallbackForMenu(View btnSlide,
			OnWidthOfButtonChanged buttonChanged) {
		super();
		this.btnSlide = btnSlide;
		this.buttonChanged = buttonChanged;
	}

	@Override
	public void onGlobalLayout() {
		btnWidth = btnSlide.getMeasuredWidth();
		btnWidth = View.MeasureSpec.getSize(btnWidth);
		buttonChanged.onWidthChange(btnWidth);
		Log.i("Fuck", "Width Button = " + btnWidth);
	}

	@Override
	public void getViewSize(int idx, int w, int h, int[] dims) {
		dims[0] = w;
		dims[1] = h;
		final int menuIdx = 0;
		if (idx == menuIdx) {
			// dims[0] = w - btnWidth;
		}
	}
}
