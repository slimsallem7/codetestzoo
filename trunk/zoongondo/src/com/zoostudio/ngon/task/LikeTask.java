package com.zoostudio.ngon.task;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.ngon.ErrorCode;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.task.callback.OnLikeTaskListener;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class LikeTask extends RestClientTask {

	private String mSpotId;
	private OnLikeTaskListener mListener;
	private int msgId = -1;

	public LikeTask(Activity activity, String spot_id) {
		super(activity);
		mSpotId = spot_id;
	}

	@Override
	public void doExecute() {
		restClient.addParam("spot_id", mSpotId);
		restClient.put("/spot/like");
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		boolean status;
		try {
			status = jsonObject.getBoolean("status");
			if (!status) {
				int errorCode = jsonObject.getInt("error_code");
				msgId = 0;
				if (errorCode == ErrorCode.SPOT_ALREADY_LIKE) {
					msgId = R.string.string_error_spot_already_like;
				}
				return RestClientNotification.OK;
			}
			return RestClientNotification.NO_DATA;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return RestClientNotification.ERROR_DATA;
	}

	public void setOnLikeTaskListener(OnLikeTaskListener listener) {
		mListener = listener;
	}
	
	@Override
	protected void onPostExecute(Integer status) {
		if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
			mWaitingDialog.dismiss();
		}
		if (status == RestClientNotification.OK) {
			mListener.onSpotAlreadyLike(msgId);

		} else if (status == RestClientNotification.ERROR
				&& null != onDataErrorDelegate) {
			onDataErrorDelegate.onActionDataError(this, mErrorCode);
		}
	}

}
