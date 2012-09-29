package com.zoostudio.ngon.task;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class GetUserTask extends RestClientTask {

	private int mUserId;

	public GetUserTask(Activity activity, int user_id) {
		super(activity);
		mUserId = user_id;
	}

	@Override
	public void doExecute() {
		restClient.addParam("user_id", mUserId);
		restClient.get("/user");
	}

}
