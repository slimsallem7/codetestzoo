package com.zoostudio.ngon.utils;

import android.content.Context;
import android.view.WindowManager;

public class ScreenUtil {
	private static ScreenUtil instance;

	public static ScreenUtil getIntance(Context context) {
		if (null == instance) {
			instance = new ScreenUtil(context);
		}
		return instance;
	}

	private int width;

	ScreenUtil(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		width = manager.getDefaultDisplay().getWidth();
	}

	public int getWidth() {
		return width;
	}
}
