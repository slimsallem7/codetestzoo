package com.zoostudio.ngon.task;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.facebook.android.Utility;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.exception.ZooException;
import com.zoostudio.ngon.task.callback.OnAddDishListener;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class CreateDishTask extends RestClientTask {
	private String mDishName;
	private String mSpotId;
	private String mDishId;
	private Context mContext;
	private MediaItem mMediaItem;
	private OnAddDishListener dishListener;
	private String dishImageUrl = "";

	public CreateDishTask(Activity activity, String dish_name, String spot_id,
			MediaItem mediaItem) {
		super(activity);
		mContext = activity.getApplicationContext();
		mDishName = dish_name;
		mSpotId = spot_id;
		mMediaItem = mediaItem;
	}

	@Override
	public void doExecute() {
		restClient.addParam("name", mDishName);
		restClient.addParam("spot_id", mSpotId);
		restClient.put("/dish");
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		try {
			boolean status = jsonObject.getBoolean("status");
			if (status) {
				mDishId = result.getString("dish_id");
				publishProgress();
				if (null == mMediaItem)
					return RestClientNotification.OK;
				Uri photoUri = Uri
						.fromFile(new File(mMediaItem.getPathMedia()));
				Log.e("SupportCheckIn", " doInBackground Context =" + mContext);
				byte[] photoData = Utility.scaleImageDish(mContext, photoUri,
						mMediaItem);
				restClient.addParam("dish_id", mDishId);
				restClient.postMultiPart("/photo", "photo", NgonTaskUtil
						.convertByteToByteArrayBody(mDishId,
								mMediaItem.getMineType(), photoData));
				JSONObject result = new JSONObject(restClient.getResponse());
				if (result.getBoolean("status")) {
					dishImageUrl = result.getString("image_url");
					return RestClientNotification.OK;
				} else {
					mErrorCode = result.getInt("error_code");
					return RestClientNotification.ERROR;
				}
			} else {
				mErrorCode = result.getInt("error_code");
				return RestClientNotification.ERROR;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mErrorCode = ZooException.JSON.JSON_PARSE_ERROR;
			return RestClientNotification.ERROR_DATA;
		} catch (IOException e) {
			e.printStackTrace();
			return RestClientNotification.ERROR_DATA;
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
		dishListener.onAddDishListenerSuccess();

	}

	@Override
	protected void onPostExecute(Integer status) {
		if (null != onDataErrorDelegate) {
			onDataErrorDelegate.onActionDataError(this, mErrorCode);
		}
	}

	public void setOnAddDishListener(OnAddDishListener listener) {
		dishListener = listener;
	}
}
