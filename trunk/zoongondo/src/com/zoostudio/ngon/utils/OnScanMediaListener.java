package com.zoostudio.ngon.utils;

import java.util.ArrayList;

import com.zoostudio.adapter.item.MediaItem;

public interface OnScanMediaListener {
	public void onScanFinished(ArrayList<MediaItem> ids);
}
