package com.zoostudio.restclient;

import org.bookmark.helper.DeviceCore;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

import com.zoostudio.exception.ZooException;
import com.zoostudio.ngon.dialog.WaitingDialog;

public abstract class RestClientTask extends AsyncTask<Void, Void, Integer> {

	private Activity mActivity;
	protected NgonRestClient restClient;
	protected OnPostExecuteDelegate onPostExecuteDelegate;
	protected OnPreExecuteDelegate onPreExecuteDelegate;
	private NoInternetDelegate noInternetDelegate;
	private OnUnauthorizedDelegate onUnauthorizedDelegate;
	private boolean mIsNeedAuth;
	protected String mDumpData;
	protected OnDataErrorDelegate onDataErrorDelegate;
	protected WaitingDialog mWaitingDialog;
	protected boolean mWaitingStatus;
	protected JSONObject result;
	protected int mErrorCode = -1;

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
		if (mWaitingStatus) {
			mWaitingDialog = new WaitingDialog(mActivity);
			mWaitingDialog.show();
		}

		if (null != onPreExecuteDelegate) {
			onPreExecuteDelegate.actionPre(this);
		}
	}

	@Override
	protected void onPostExecute(Integer status) {

		if (mWaitingStatus && mWaitingDialog != null) {
			mWaitingDialog.dismiss();
		}

		if (null != onPostExecuteDelegate
				&& status == RestClientNotification.OK) {
			onPostExecuteDelegate.actionPost(this, this.result);

		} else if (status == RestClientNotification.ERROR
				&& null != onDataErrorDelegate) {

			onDataErrorDelegate.actionDataError(this, mErrorCode);
		}
	}

	public Integer getResult() {
		if (null == restClient) {
			mErrorCode = ZooException.NETWORK.NETWORK_ERROR;
			return RestClientNotification.ERROR;
		}
		try {
			result = new JSONObject(restClient.getResponse());
			parseJSONToObject(result);
			return RestClientNotification.OK;
		} catch (JSONException e) {
			e.fillInStackTrace();
			mErrorCode = ZooException.JSON.JSON_PARSE_ERROR;
			return RestClientNotification.ERROR;
		} catch (NullPointerException e) {
			e.printStackTrace();
			mErrorCode = ZooException.NETWORK.NETWORK_ERROR;
			return RestClientNotification.ERROR;
		}
	}

	protected abstract void doExecute();

	@Override
	protected Integer doInBackground(Void... params) {
		if (DeviceCore.checkInternetConnect(mActivity)) {
			doExecute();
			if (restClient.getResponseCode() != 401 || mIsNeedAuth == false) {
				return getResult();
			} else if (null != onUnauthorizedDelegate) {
				mErrorCode = ZooException.NETWORK.UN_AUTH;
				return RestClientNotification.ERROR;
			} else {
				Editor editor = mActivity.getSharedPreferences("account",
						Context.MODE_PRIVATE).edit();
				editor.remove("token_key");
				editor.remove("token_key");
				editor.commit();
				// request re-login
				// Intent i = new Intent(mActivity, LoginActivity.class);
				// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// i.putExtra("relogin", true);
				// mActivity.startActivityForResult(i, RequestCode.RELOGIN);
				mErrorCode = ZooException.NETWORK.RELOGIN;
				return RestClientNotification.ERROR;
			}
		} else if (null != noInternetDelegate) {
			mErrorCode = ZooException.NETWORK.NO_INTERNET;
			return RestClientNotification.ERROR;
		}
		return RestClientNotification.ERROR;
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
	
	protected abstract int parseJSONToObject(JSONObject jsonObject);
	
}
