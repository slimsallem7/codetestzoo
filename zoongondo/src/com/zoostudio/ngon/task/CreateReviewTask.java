package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class CreateReviewTask extends RestClientTask {
	private String	mContent;
	private String	   mSpotId;
	
	public CreateReviewTask(Activity activity,String spot_id, String content) {
		super(activity);
		mContent = content;
		mSpotId = spot_id;
	}
	
	@Override public void doExecute() {
		restClient.addParam("content", mContent);
		restClient.addParam("spot_id", mSpotId);
		
		restClient.put("/review");
	}

	@Override
	protected void parseJSONToObject(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		
	}
}
