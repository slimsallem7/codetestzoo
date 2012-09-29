package com.zoostudio.ngon.task;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class GetSpotReviewTask extends RestClientTask {

	private String mSpotId;
	private int mLimit;
	private int mOffset;

	public GetSpotReviewTask(Activity activity, String spot_id) {
		this(activity, spot_id, 0);
	}

	public GetSpotReviewTask(Activity activity, String spot_id, int limit) {
		this(activity, spot_id, limit, 0);
	}

	public GetSpotReviewTask(Activity activity, String spot_id, int limit, int offset) {
		super(activity);
		mSpotId = spot_id;
		mLimit = limit;
		mOffset = offset;
	}

	public void setOffset(int offset) {
		mOffset = offset;
	}

	public void setLimit(int limit) {
		mLimit = limit;
	}

	@Override
	public void doExecute() {
		restClient.addParam("spot_id", mSpotId);
		if (mLimit > 0)
			restClient.addParam("limit", mLimit);
		if (mOffset > 0) {
			restClient.addParam("offset", mOffset);
		}

		restClient.get("/spot/review");
	}

}
