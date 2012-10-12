package com.zoostudio.ngon.task;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.adapter.item.ReviewItem;
import com.zoostudio.ngon.task.callback.OnSpotReviewTaskListener;
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class GetSpotReviewTask extends RestClientTask {

	private String mSpotId;
	private int mLimit;
	private int mOffset;
	private ArrayList<ReviewItem> data;
	private OnSpotReviewTaskListener mListener;

	public GetSpotReviewTask(Activity activity, String spot_id) {
		this(activity, spot_id, 0);
	}

	public GetSpotReviewTask(Activity activity, String spot_id, int limit) {
		this(activity, spot_id, limit, 0);
	}

	public GetSpotReviewTask(Activity activity, String spot_id, int limit,
			int offset) {
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

	@Override
	protected void onPostExecute(Integer status) {
		if (mWaitingStatus && mWaitingDialog != null) {
			mWaitingDialog.dismiss();
		}
		if (status == RestClientNotification.OK) {
			mListener.onSpotReviewTaskListener(data);

		} else if (status == RestClientNotification.ERROR
				&& null != onDataErrorDelegate) {
			onDataErrorDelegate.actionDataError(this, mErrorCode);
		}
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		data = new ArrayList<ReviewItem>();
		try {
			JSONArray array = jsonObject.getJSONArray("data");
			for (int i = 0, size = array.length(); i < size; i++) {
				JSONObject row = array.getJSONObject(i);
				ReviewItem item = ParserUtils.parseReview(row);
				data.add(item);
			}
			return RestClientNotification.OK;
		} catch (JSONException e) {
			e.printStackTrace();
			return RestClientNotification.ERROR_DATA;
		}

	}

	public void setOnSpotReviewTaskListener(OnSpotReviewTaskListener listener) {
		mListener = listener;
	}

}
