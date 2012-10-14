package com.zoostudio.ngon.task;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.adapter.item.PhotoItem;
import com.zoostudio.ngon.task.callback.OnSpotPhotoTaskListener;
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class GetSpotPhotoTask extends RestClientTask {

	private String mSpotId;
	private int mLimit;
	private int mOffset;
	private OnSpotPhotoTaskListener mListener;
	private ArrayList<PhotoItem> data;

	public GetSpotPhotoTask(Activity activity, String spot_id) {
		this(activity, spot_id, 0);
	}

	public GetSpotPhotoTask(Activity activity, String spot_id, int limit) {
		this(activity, spot_id, limit, 0);
	}

	public GetSpotPhotoTask(Activity activity, String spot_id, int limit,
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

		restClient.get("/spot/photo");
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		try {
			boolean status;
			status = jsonObject.getBoolean("status");
			if (status) {
				JSONArray arr = result.getJSONArray("data");
				for (int i = 0, size = arr.length(); i < size; i++) {
					JSONObject row = arr.getJSONObject(i);
					PhotoItem item = ParserUtils.parsePhoto(row);
					data.add(item);
				}
				return RestClientNotification.OK;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return RestClientNotification.OK;
	}

	@Override
	protected void onPostExecute(Integer status) {
		if (mWaitingStatus && mWaitingDialog != null) {
			mWaitingDialog.dismiss();
		}

		if (status == RestClientNotification.OK) {
			mListener.onSpotPhotoTaskListener(data);
		} else if (status == RestClientNotification.ERROR
				&& null != onDataErrorDelegate) {
			onDataErrorDelegate.onActionDataError(this, mErrorCode);
		}
	}

	public void setOnSpotPhotoTaskListener(OnSpotPhotoTaskListener listener) {
		mListener = listener;
	}
}
