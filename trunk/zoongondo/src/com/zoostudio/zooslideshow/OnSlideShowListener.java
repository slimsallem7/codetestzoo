package com.zoostudio.zooslideshow;

import java.util.ArrayList;

import com.zoostudio.adapter.item.PhotoItem;

public interface OnSlideShowListener {
	public void onTakePhoto();
	public void onSelectFullImage();
	public void onListThumbClicked(ArrayList<PhotoItem> items);
	public void onTakePhotoCoverSpot();
}
