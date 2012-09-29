package com.zoostudio.ngon.task;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class GetUserLikeTask extends RestClientTask {

	private int mUserId;
	private int mLimit;

	public GetUserLikeTask(Activity activity, int user_id) {
		this(activity, user_id, 0);
	}

	public GetUserLikeTask(Activity activity, int user_id, int limit) {
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

		restClient.get("/user/like");
	}

}
