package com.zoostudio.ngon.task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;

import com.facebook.android.Utility;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.adapter.item.PhotoItem;
import com.zoostudio.exception.ZooException;
import com.zoostudio.ngon.task.callback.OnUploadPhotoTask;
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class UploadPhotoTask extends RestClientTask {

	private String mSpotId;
	private int[] mDishesId;
	private ContentBody mPhoto;
	private OnUploadPhotoTask mListener;
	private PhotoItem photoItem;
	private MediaItem mMediaItem;
	private Context mContext;
	private int mType;
	public final static int COVER = 0;
	public final static int THUMB = 1;

	public UploadPhotoTask(Activity activity, String spot_id, MediaItem item,
			int type) {
		this(activity, spot_id, item, new int[] {}, type);
	}

	public UploadPhotoTask(Activity activity, String spot_id, MediaItem item,
			int[] dishesId, int type) {
		super(activity);
		mContext = activity.getApplicationContext();
		mDishesId = dishesId;
		mSpotId = spot_id;
		mMediaItem = item;
		mType = type;
	}

	@Override
	public void doExecute() {
		restClient.addParam("spot_id", mSpotId);
		if (mDishesId.length > 0) {
			String dishes = "";
			for (int dish : mDishesId) {
				dishes += dish + ",";
			}
			dishes = dishes.substring(0, dishes.length() - 1);
			restClient.addParam("dishes", dishes);
		}
		Uri photoUri = Uri.fromFile(new File(mMediaItem.getPathMedia()));
		try {
			byte[] photoData = Utility.scaleImage(mContext, photoUri,
					mMediaItem);
			mPhoto = NgonTaskUtil.convertByteToByteArrayBody(mSpotId,
					mMediaItem.getMineType(), photoData);
			restClient.postMultiPart("/photo", "photo", mPhoto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		boolean status;
		try {
			status = jsonObject.getBoolean("status");
			if (status) {
				photoItem = ParserUtils.parsePhoto(jsonObject);
				return RestClientNotification.OK;
			} else {
				mErrorCode = ZooException.NETWORK.CANT_GET_DATA;
				return RestClientNotification.ERROR;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mErrorCode = ZooException.JSON.JSON_PARSE_ERROR;
		}
		return RestClientNotification.ERROR;
	}

	@Override
	protected void onPostExecute(Integer status) {
		if (mWaitingStatus && mWaitingDialog != null) {
			mWaitingDialog.dismiss();
		}
		if (null != mListener && status == RestClientNotification.OK
				&& photoItem != null) {
			if (mType == THUMB) {
				mListener.onUploadPhotoTaskListener(photoItem);
			} else {
				mListener.onUploadCoverPhotoTaskListener(photoItem);
			}
		} else if (status == RestClientNotification.ERROR
				&& null != onDataErrorDelegate) {
			onDataErrorDelegate.onActionDataError(this, mErrorCode);
		}
	}

	public void setOnUploadPhotoTaskListener(OnUploadPhotoTask listener) {
		mListener = listener;
	}
}
