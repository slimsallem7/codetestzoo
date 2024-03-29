package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class EditReviewTask extends RestClientTask {

	private int mReviewId;
	private String mContent;

	public EditReviewTask(Activity activity, int review_id, String content) {
		super(activity);
		mReviewId = review_id;
		mContent = content.trim();
	}

	@Override
	public void doExecute() {
		restClient.addParam("review_id", mReviewId);
		restClient.addParam("content", mContent);
		restClient.post("/review");
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		return RestClientNotification.OK;
	}

}
