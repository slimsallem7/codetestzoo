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
	private boolean hasImage;

	public CreateDishTask(Activity activity, String dish_name, String spot_id,
			MediaItem mediaItem) {
		super(activity);
		mContext = activity.getApplicationContext();
		mDishName = dish_name;
		mSpotId = spot_id;
		mMediaItem = mediaItem;
		hasImage = null == mMediaItem ? false : !mMediaItem.getPathMedia()
				.equals("");
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
			mErrorCode = ZooException.JSON.ADD_DISH.ERROR_ADD_DISH;
			if (status) {
				mDishId = result.getString("dish_id");
				publishProgress();
				if (!hasImage)
					return RestClientNotification.OK;
				Uri photoUri = Uri
						.fromFile(new File(mMediaItem.getPathMedia()));
				byte[] photoData = Utility.scaleImageDish(mContext, photoUri,
						mMediaItem);
				restClient.addParam("dish_id", mDishId);
				restClient.postMultiPart("/photo", "photo", NgonTaskUtil
						.convertByteToByteArrayBody(mDishId,
								mMediaItem.getMineType(), photoData));

				mErrorCode = ZooException.JSON.ERROR_UPLOAD_IMAGE;
				JSONObject result = new JSONObject(restClient.getResponse());
				if (result.getBoolean("status")) {
					return RestClientNotification.OK;
				} else {
					mErrorCode = result.getInt("error");
					return RestClientNotification.ERROR;
				}
			} else {
				mErrorCode = result.getInt("error");
				return RestClientNotification.ERROR;
			}
		} catch (JSONException e) {
			e.printStackTrace();
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
		if (null != onDataErrorDelegate && status != RestClientNotification.OK) {
			onDataErrorDelegate.onActionDataError(this, mErrorCode);
		}
	}

	public void setOnAddDishListener(OnAddDishListener listener) {
		dishListener = listener;
	}
}
