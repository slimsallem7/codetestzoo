package com.zoostudio.ngon.utils;

import android.content.res.Resources;
import android.util.Log;

import com.zoostudio.ngon.R;

public class ConfigSize {
	public static int SIZE_AVATAR;
	private Resources resources;

	public void loadResources() {
		SIZE_AVATAR = (int) resources.getDimension(R.dimen.size_avatar);
		Log.e("ConfigSize","Size = " + SIZE_AVATAR);
	}

	public ConfigSize(Resources resources) {
		this.resources = resources;
	}
}
