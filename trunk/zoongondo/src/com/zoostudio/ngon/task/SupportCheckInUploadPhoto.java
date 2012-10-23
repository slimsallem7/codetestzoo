package com.zoostudio.ngon.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.facebook.android.BaseRequestListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Utility;
import com.twitter.android.TwitterSupport;
import com.zoostudio.adapter.PagerUtils;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.adapter.item.PhotoItem;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.Config;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.restclient.NgonRestClient;

public class SupportCheckInUploadPhoto extends
		AsyncTask<Void, ItemUpload, Integer> {
	private ArrayList<MediaItem> medias;
	private String mSpotId;
	private volatile boolean mUpLoadStatus;
	private int imageIndex;
	private Activity mActivity;
	private Context mContext;
	private NgonRestClient restClient;
	private ArrayList<PhotoItem> mUrlImage;
	private String mCheckinId;
	private boolean shareTwitter;
	private String message;
	private TwitterSupport twitterSupport;
	private String address;
	private boolean shareFacebook;
	private SpotItem spotItem;

	public SupportCheckInUploadPhoto(Activity activity,
			ArrayList<MediaItem> medias, SpotItem spotItem, String checkinId) {
		mActivity = activity;
		mContext = activity.getApplicationContext();
		this.medias = medias;
		mUrlImage = new ArrayList<PhotoItem>();
		imageIndex = 0;
		this.spotItem = spotItem;
		mSpotId = this.spotItem.getId();
		mCheckinId = checkinId;
		SharedPreferences pref = activity.getSharedPreferences("account",
				Context.MODE_PRIVATE);
		String tokenKey = pref.getString("token_key", "");
		String tokenSecret = pref.getString("token_secret", "");
		restClient = new NgonRestClient(tokenKey, tokenSecret, true);
	}

	@Override
	protected Integer doInBackground(Void... params) {
		Log.e("UploadPhoto", "Upload image");
		for (MediaItem media : medias) {
			ItemUpload upload = new ItemUpload();
			try {
				Uri photoUri = Uri.fromFile(new File(media.getPathMedia()));
				Log.e("SupportCheckIn", " doInBackground Context =" + mContext);
				byte[] photoData = Utility
						.scaleImage(mContext, photoUri, media);
				restClient.addParam("checkin_id", mCheckinId);
				restClient.addParam("spot_id", mSpotId);
				restClient.postMultiPart("/photo", "photo", NgonTaskUtil
						.convertByteToByteArrayBody(mCheckinId,
								media.getMineType(), photoData));
				try {
					JSONObject result = new JSONObject(restClient.getResponse());
					if (mUpLoadStatus = result.getBoolean("status")) {
						PhotoItem linkURl = ParserUtils.parsePhoto(result);
						mUrlImage.add(linkURl);
					}
					upload.id = imageIndex;
					upload.status = mUpLoadStatus;
					publishProgress(upload);
					imageIndex++;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (shareFacebook)
			postFacebook();
		return 0;
	}

	@Override
	protected void onPostExecute(Integer result) {
		if (shareTwitter)
			postTwitter();
	}

	@Override
	protected void onProgressUpdate(ItemUpload... values) {
		NotificationManager notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence tickerText = mActivity.getResources().getString(
				R.string.title_notification);
		long when = System.currentTimeMillis();
		CharSequence contentTitle;
		RemoteViews remoteViews = new RemoteViews(mActivity.getPackageName(),
				R.layout.layout_notification);
		int icon;
		if (values[0].status) {
			contentTitle = mActivity.getResources().getString(
					R.string.content_notification_ok);
			icon = R.drawable.icon_notifi_ok;
			remoteViews.setImageViewResource(R.id.img_notification,
					R.drawable.icon_notifi_ok);
		} else {
			contentTitle = mActivity.getResources().getString(
					R.string.content_notification_fail);
			remoteViews.setImageViewResource(R.id.img_notification,
					R.drawable.icon_notifi_fail);
			icon = R.drawable.icon_notifi_fail;
		}
		Notification updateComplete = new Notification(icon, tickerText, when);
		remoteViews.setTextViewText(R.id.txtContentNotification, contentTitle);
		updateComplete.contentView = remoteViews;
		updateComplete.contentIntent = PendingIntent.getActivity(
				mActivity.getApplicationContext(), 1, new Intent(),
				PendingIntent.FLAG_CANCEL_CURRENT);
		updateComplete.flags = Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(values[0].id, updateComplete);
	}

	public void setFacebookShare(String mess, String address) {
		this.message = mess;
		this.address = address;
		this.shareFacebook = true;
	}

	private void postFacebook() {
		Bundle params = new Bundle();
		params.putString("name", address);
		params.putString("message", message);
		params.putString("link",
				"https://play.google.com/store/apps/details?id=com.bookmark.money&hl=en");
		String picture;
		
		if(!mUrlImage.isEmpty()){
			picture = mUrlImage.get(0).getPath();
		}else{
			picture = Config.LINK_DEFAULT;
		}
		params.putString("picture", picture);
		params.putString("description", spotItem.getAddress());
		Utility.mAsyncRunner.request("me/feed", params, "POST",
				new PhotoUploadListener(), null);
	}

	public void setTwitterShare(String mess, TwitterSupport twitterSupport) {
		this.message = mess;
		this.twitterSupport = twitterSupport;
		shareTwitter = true;
	}

	private void postTwitter() {
		twitterSupport.postStatus(message, null);
	}

	/*
	 * callback for the photo upload
	 */
	public class PhotoUploadListener extends BaseRequestListener {

		@Override
		public void onComplete(final String response, final Object state) {
		}

		public void onFacebookError(FacebookError error) {
		}
	}
}
