package com.zoostudio.ngon.task;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.adapter.item.MenuItem;
import com.zoostudio.ngon.task.callback.OnMenuTaskListener;
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class GetMenuTask extends RestClientTask {

	private String mSpotId;
	private ArrayList<MenuItem> data;
	private OnMenuTaskListener mListener;

	public GetMenuTask(Activity activity, String spot_id) {
		super(activity);
		mSpotId = spot_id;
	}

	@Override
	public void doExecute() {
		restClient.addParam("spot_id", mSpotId);
		restClient.get("/spot/menu");
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		try {
			boolean status = jsonObject.getBoolean("status");
			if (status) {
				data = new ArrayList<MenuItem>();
				JSONArray menuData = jsonObject.getJSONArray("data");
				for (int i = 0, size = menuData.length(); i < size; i++) {
					JSONObject row = menuData.getJSONObject(i);
					MenuItem item = ParserUtils.parseMenu(row, mSpotId);
					data.add(item);
				}
				return RestClientNotification.OK;
			} else {
				mErrorCode = jsonObject.getInt("error_code");
				return RestClientNotification.ERROR;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return RestClientNotification.ERROR_DATA;
		}
	}

	@Override
	protected void onPostExecute(Integer status) {
		if (mWaitingStatus && mWaitingDialog != null) {
			mWaitingDialog.dismiss();
		}
		if (status == RestClientNotification.OK) {
			mListener.onMenuTaskListener(data);
		} else if (status == RestClientNotification.ERROR
				&& null != onDataErrorDelegate) {
			onDataErrorDelegate.onActionDataError(this, mErrorCode);
		}
	}

	public void setOnMenuTaskListener(OnMenuTaskListener listener) {
		mListener = listener;
	}

}
