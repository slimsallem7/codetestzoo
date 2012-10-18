package com.zoostudio.ngon.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.WindowManager;

import com.zoostudio.ngon.R;

public class ConfigSize {
	public static int SIZE_AVATAR;
	public static int SIZE_THUMB;
	public static int SIZE_BORDER;
	public static int SIZE_BORDER_WHITE;
	public static int SIZE_PICKER;
	public static int WIDTH_SCREEN;
	private Resources resources;

	public void loadResources() {
		SIZE_PICKER = (int) resources.getDimension(R.dimen.size_picker);
		SIZE_AVATAR = (int) resources.getDimension(R.dimen.size_avatar);
		SIZE_THUMB =  resources.getDimensionPixelOffset(R.dimen.size_thumb);
		SIZE_BORDER = resources.getDimensionPixelOffset(R.dimen.size_border);
		SIZE_BORDER_WHITE = resources.getDimensionPixelOffset(R.dimen.size_border_white);
	}

	public ConfigSize(Context context,Resources resources) {
		this.resources = resources;
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		WIDTH_SCREEN = manager.getDefaultDisplay().getWidth();
	}
}
