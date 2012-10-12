package com.zoostudio.ngon.task.callback;

import java.util.ArrayList;

import com.zoostudio.adapter.item.SpotItem;

public interface OnSpotItemReceiver {
	public void onDataReceiver(ArrayList<SpotItem> data);
	public void onError(int errorCode);
}
