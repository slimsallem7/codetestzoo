package com.zoostudio.ngon.task.callback;

import com.zoostudio.adapter.item.PhotoItem;

public interface OnUploadPhotoTask {
	public void onUploadPhotoTaskListener(PhotoItem photoItem);
	public void onUploadCoverPhotoTaskListener(PhotoItem photoItem);
}
