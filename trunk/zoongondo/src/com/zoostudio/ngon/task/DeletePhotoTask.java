package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class DeletePhotoTask extends RestClientTask {
	
	private int	mPhotoId;
	
	public DeletePhotoTask(Activity activity,int photo_id) {
		super(activity);
		mPhotoId = photo_id;
	}
	
	@Override public void doExecute() {
		restClient.addParam("photo_id", mPhotoId);
		restClient.delete("/photo");
	}

	@Override
	protected void parseJSONToObject(JSONObject jsonObject) {
		
	}
	
}
