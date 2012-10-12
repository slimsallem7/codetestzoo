package com.zoostudio.ngon.task;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.ngon.task.callback.OnSpotTaskListener;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class GetSpotTask extends RestClientTask {

	private String mSpotId;
	private OnSpotTaskListener mListener;
	private String mNameSpot;
	private String mAddressSpot;

	public GetSpotTask(Activity activity, String spot_id) {
		super(activity);
		mSpotId = spot_id;
	}

	@Override
	public void doExecute() {
		restClient.addParam("spot_id", mSpotId);
		restClient.get("/spot");
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		try {
			mNameSpot = jsonObject.getString("name");
			mAddressSpot = jsonObject.getString("address");
			return RestClientNotification.OK;
		} catch (JSONException e) {
			e.printStackTrace();
			return RestClientNotification.ERROR_DATA;
		}
	}

	@Override
	protected void onPostExecute(Integer status) {
		if (mWaitingStatus && mWaitingDialog != null
				&& mWaitingDialog.isShowing()) {
			mWaitingDialog.dismiss();
		}

		if (null != onPostExecuteDelegate
				&& status == RestClientNotification.OK) {
			mListener.onSpotTaskListener(mNameSpot, mAddressSpot);

		} else if (status == RestClientNotification.ERROR
				&& null != onDataErrorDelegate) {
			onDataErrorDelegate.actionDataError(this, mErrorCode);
		}
	}

	public void setOnSpotTaskListener(OnSpotTaskListener listener) {
		mListener = listener;
	}

}
