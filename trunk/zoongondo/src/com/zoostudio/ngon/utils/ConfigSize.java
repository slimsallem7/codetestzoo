package com.zoostudio.ngon.utils;

import android.content.res.Resources;

import com.zoostudio.ngon.R;

public class ConfigSize {
	public static int SIZE_AVATAR;
	public static int SIZE_THUMB;
	public static int SIZE_BORDER;
	public static int SIZE_BORDER_WHITE;
	public static int SIZE_PICKER;
	private Resources resources;

	public void loadResources() {
		SIZE_PICKER = (int) resources.getDimension(R.dimen.size_picker);
		SIZE_AVATAR = (int) resources.getDimension(R.dimen.size_avatar);
		SIZE_THUMB =  resources.getDimensionPixelOffset(R.dimen.size_thumb);
		SIZE_BORDER = resources.getDimensionPixelOffset(R.dimen.size_border);
		SIZE_BORDER_WHITE = resources.getDimensionPixelOffset(R.dimen.size_border_white);
	}

	public ConfigSize(Resources resources) {
		this.resources = resources;
	}
}
