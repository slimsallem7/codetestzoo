package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class GetUserHistoryTask extends RestClientTask {

	private int mUserId;
	private int mLimit;

	public GetUserHistoryTask(Activity activity, int user_id) {
		this(activity, user_id, 0);
	}

	public GetUserHistoryTask(Activity activity, int user_id, int limit) {
		super(activity);
		mUserId = user_id;
		mLimit = limit;
	}

	@Override
	public void doExecute() {
		restClient.addParam("user_id", mUserId);
		if (mLimit > 0) {
			restClient.addParam("limit", mLimit);
		}

		restClient.get("/user/history");
	}

	@Override
	protected void parseJSONToObject(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		
	}

}
