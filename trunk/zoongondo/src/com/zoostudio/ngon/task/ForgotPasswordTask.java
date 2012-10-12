package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class ForgotPasswordTask extends RestClientTask {

	public ForgotPasswordTask(Activity activity, String email) {
		super(activity);
	}

	@Override
	public void doExecute() {

	}

	@Override
	protected void parseJSONToObject(JSONObject jsonObject) {
		
	}

}
