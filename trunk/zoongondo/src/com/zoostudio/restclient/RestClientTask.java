package com.zoostudio.restclient;

import org.bookmark.helper.DeviceCore;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

import com.zoostudio.exception.ZooException;
import com.zoostudio.ngon.RequestCode;
import com.zoostudio.ngon.dialog.WaitingDialog;
import com.zoostudio.ngon.ui.LoginActivity;

public abstract class RestClientTask extends AsyncTask<Void, Void, JSONObject> {

	private Activity mActivity;
	protected NgonRestClient restClient;
	private OnPostExecuteDelegate onPostExecuteDelegate;
	private OnPreExecuteDelegate onPreExecuteDelegate;
	private NoInternetDelegate noInternetDelegate;
	private OnUnauthorizedDelegate onUnauthorizedDelegate;
	private boolean mIsNeedAuth;
	protected String mDumpData;
	private OnDataErrorDelegate onDataErrorDelegate;
	private WaitingDialog mWaitingDialog;
	private boolean mWaitingStatus;

	public RestClientTask(Activity activity) {
		this(activity, true);
	}

	public RestClientTask(Activity activity, boolean isNeedAuth) {

		mActivity = activity;
		mIsNeedAuth = isNeedAuth;
		SharedPreferences pref = activity.getSharedPreferences("account",
				Context.MODE_PRIVATE);
		String tokenKey = pref.getString("token_key", "");
		String tokenSecret = pref.getString("token_secret", "");

		restClient = new NgonRestClient(tokenKey, tokenSecret, isNeedAuth);
		if (getDumpData() != null) {
			restClient.setDumpData(getDumpData());
		}

	}

	public Activity getActivity() {
		return mActivity;
	}

	public void setEnableWaitingDialog(boolean status) {
		mWaitingStatus = status;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (mWaitingStatus) {
			mWaitingDialog = new WaitingDialog(mActivity);
			mWaitingDialog.show();
		}

		if (null != onPreExecuteDelegate) {
			onPreExecuteDelegate.actionPre(this);
		}
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);

		if (mWaitingStatus && mWaitingDialog != null) {
			mWaitingDialog.dismiss();
		}

		if (null != onPostExecuteDelegate) {
			onPostExecuteDelegate.actionPost(this, result);
		}
	}

	public JSONObject getResult() {
		if (null == restClient)
			return new JSONObject();
		try {
			Log.e("JSON " + getClass().getName(),
					"JSON data: " + restClient.getResponse());
			JSONObject data = new JSONObject(restClient.getResponse());

			return data;
		} catch (JSONException e) {
			e.fillInStackTrace();

			if (null != onDataErrorDelegate) {
				onDataErrorDelegate.actionDataError(this,
						ZooException.JSON.JSON_PARSE_ERROR);
			}
			return new JSONObject();
		} catch (NullPointerException e) {
			e.printStackTrace();
			if (null != onDataErrorDelegate) {
				onDataErrorDelegate.actionDataError(this,
						ZooException.NETWORK.NETWORK_ERROR);
			}
			return new JSONObject();
		}
	}

	protected abstract void doExecute();

	@Override
	protected JSONObject doInBackground(Void... params) {
		if (DeviceCore.checkInternetConnect(mActivity)) {
			doExecute();
			if (restClient.getResponseCode() != 401 || mIsNeedAuth == false) {
				return getResult();
			} else if (null != onUnauthorizedDelegate) {
				onUnauthorizedDelegate.actionUnauthorized(this);
			} else {
				Editor editor = mActivity.getSharedPreferences("account",
						Context.MODE_PRIVATE).edit();
				editor.remove("token_key");
				editor.remove("token_key");
				editor.commit();

				// request re-login
				Intent i = new Intent(mActivity, LoginActivity.class);
				i.putExtra("relogin", true);
				mActivity.startActivityForResult(i, RequestCode.RELOGIN);
			}
		} else if (null != noInternetDelegate) {
			noInternetDelegate.actionNoInternet(this);
		}
		return null;
	}

	public void setOnPostExecuteDelegate(OnPostExecuteDelegate delegate) {
		onPostExecuteDelegate = delegate;
	}

	public void setOnPreExecuteDelegate(OnPreExecuteDelegate delegate) {
		onPreExecuteDelegate = delegate;
	}

	public void setNoInternetDelegate(NoInternetDelegate delegate) {
		noInternetDelegate = delegate;
	}

	public void setOnUnauthorizedDelegate(OnUnauthorizedDelegate delegate) {
		onUnauthorizedDelegate = delegate;
	}

	public void setOnDataErrorDelegate(OnDataErrorDelegate delegate) {
		onDataErrorDelegate = delegate;
	}

	public String getDumpData() {
		return mDumpData;
	}

	public void setDumpData(String mDumpData) {
		this.mDumpData = mDumpData;
	}

	public interface OnPostExecuteDelegate {
		public void actionPost(RestClientTask task, JSONObject result);
	}

	public interface OnPreExecuteDelegate {
		public void actionPre(RestClientTask task);
	}

	public interface OnDataErrorDelegate {
		public void actionDataError(RestClientTask task, int errorCode);
	}

	public interface NoInternetDelegate {
		public void actionNoInternet(RestClientTask task);
	}

	public interface OnUnauthorizedDelegate {
		public void actionUnauthorized(RestClientTask task);
	}

}
