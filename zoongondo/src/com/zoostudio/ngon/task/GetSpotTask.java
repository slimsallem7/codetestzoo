package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class GetSpotTask extends RestClientTask {

	private String mSpotId;

	public GetSpotTask(Activity activity, String spot_id) {
		super(activity);
		mSpotId = spot_id;
	}

	@Override
	public void doExecute() {
		restClient.addParam("spot_id", mSpotId);
		restClient.get("/spot");
	}

	@Override
	protected void parseJSONToObject(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		
	}

}
