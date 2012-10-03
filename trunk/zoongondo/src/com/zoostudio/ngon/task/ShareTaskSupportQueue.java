package com.zoostudio.ngon.task;

import android.os.AsyncTask;

import com.zoostudio.adapter.item.ShareItem;

public class ShareTaskSupportQueue extends AsyncTask<ShareItem, String, Void> {

	@Override
	protected Void doInBackground(ShareItem... params) {
		for(ShareItem item : params){
			
		}
		return null;
	}

}
