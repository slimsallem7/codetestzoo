package com.zoostudio.ngon.task;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.exception.ZooException;
import com.zoostudio.ngon.task.callback.OnCreateReviewTaskListener;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class CreateReviewTask extends RestClientTask {
	private String mContent;
	private String mSpotId;
	private OnCreateReviewTaskListener listener;

	public CreateReviewTask(Activity activity, String spot_id, String content) {
		super(activity);
		mContent = content;
		mSpotId = spot_id;
	}

	@Override
	public void doExecute() {
		restClient.addParam("content", mContent);
		restClient.addParam("spot_id", mSpotId);
		restClient.put("/review");
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		try {
			if (jsonObject.getBoolean("status")) {
				return RestClientNotification.OK;
			} else {
				mErrorCode = ZooException.NETWORK.CANT_GET_DATA;
				return RestClientNotification.ERROR;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mErrorCode = ZooException.JSON.JSON_PARSE_ERROR;
		}
		return RestClientNotification.ERROR;
	}

	@Override
	protected void onPostExecute(Integer status) {
		if (null != listener && status == RestClientNotification.OK) {
			listener.onCreateReviewTaskListener();
		} else if (status == RestClientNotification.ERROR
				&& null != onDataErrorDelegate) {
			onDataErrorDelegate.onActionDataError(this, mErrorCode);
		}
	}

	public void setOnCreateReviewTaskListener(
			OnCreateReviewTaskListener listener) {
		this.listener = listener;
	}
}
