package com.zoostudio.ngon.task;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class GetMeTask extends RestClientTask {

	public GetMeTask(Activity activity) {
		super(activity);
	}

	@Override
	protected void doExecute() {
		restClient.get("/user/me");
	}

}
