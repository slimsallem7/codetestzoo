package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class SuggestDishTask extends RestClientTask {

	private String mText;

	public SuggestDishTask(Activity activity, String text) {
		super(activity);

		mText = text;
	}

	@Override
	protected void doExecute() {
		restClient.addParam("text", mText);
		restClient.get("/dish/suggest");
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		return RestClientNotification.OK;
		
	}

}
