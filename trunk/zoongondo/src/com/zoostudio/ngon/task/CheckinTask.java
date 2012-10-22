package com.zoostudio.ngon.task;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.exception.ZooException;
import com.zoostudio.ngon.task.callback.OnCheckinTaskListener;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class CheckinTask extends RestClientTask {

	private String mSpotId;
	private ArrayList<String> mDishesId;
	private String mComment;
	private OnCheckinTaskListener mListener;
	private String mCheckinId;

	public CheckinTask(Activity activity, String spot_id) {
		this(activity, spot_id, "", new ArrayList<String>());
	}

	public CheckinTask(Activity activity, String spot_id, String comment) {
		this(activity, spot_id, comment, new ArrayList<String>());
	}

	public CheckinTask(Activity activity, String spot_id, String comment,
			ArrayList<String> dishes_id) {
		super(activity);
		mSpotId = spot_id;
		mDishesId = dishes_id;
		mComment = comment;
	}

	@Override
	public void doExecute() {
		if (isCancelled())
			return;
		restClient.addParam("spot_id", mSpotId);
		if (mDishesId.size() > 0) {
			StringBuffer buffer = new StringBuffer(1024);
			String dishes = "";
			for (String dish : mDishesId) {
				buffer.append(dish).append(",");
			}
			dishes = buffer.toString();
			dishes = dishes.substring(0, dishes.length() - 1);
			restClient.addParam("dishes", dishes);
		}
		if (!mComment.equals("")) {
			restClient.addParam("comment", mComment);
		}
		restClient.put("/spot/checkin");
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		boolean status;
		try {
			status = jsonObject.getBoolean("status");
			if (status) {
//				mCheckinId = jsonObject.getString("checkin_id");
				mCheckinId = "101010101";
				return RestClientNotification.OK;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mErrorCode = ZooException.JSON.JSON_PARSE_ERROR;
			return RestClientNotification.ERROR;
		}
		mErrorCode = ZooException.NETWORK.CANT_GET_DATA;
		return RestClientNotification.ERROR;

	}

	@Override
	protected void onPostExecute(Integer status) {
		isLoading = false;
		if (mWaitingStatus && mWaitingDialog != null) {
			mWaitingDialog.dismiss();
		}
		if (null != mListener && status == RestClientNotification.OK) {
			mListener.onCheckinTaskListener(mCheckinId);
		} else if (status == RestClientNotification.ERROR
				&& null != onDataErrorDelegate) {
			onDataErrorDelegate.onActionDataError(this, mErrorCode);
		}
	}

	public void setOnCheckinTaskListener(OnCheckinTaskListener listener) {
		mListener = listener;
	}
}
