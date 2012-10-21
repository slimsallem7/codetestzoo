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
import android.util.Log;
import android.widget.RemoteViews;

import com.facebook.android.Utility;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.adapter.item.MenuItem;
import com.zoostudio.adapter.item.ShareItem;
import com.zoostudio.ngon.R;
import com.zoostudio.restclient.NgonRestClient;

public class SupportCheckInUploadPhoto extends AsyncTask<Void, ItemUpload, Integer> {
	private ArrayList<MediaItem> medias;
	private String mSpotId;
	private ArrayList<ShareItem> shareItem;
	private String comment;
	private volatile boolean mUpLoadStatus;
	private int imageIndex;
	private String checkinId;
	private Activity mActivity;
	private Context mContext;
	private NgonRestClient restClient;
	
	public SupportCheckInUploadPhoto(Activity activity,
			ArrayList<MediaItem> medias, String spotId,
			String checkinId) {
		mActivity = activity;
		mContext = activity.getApplicationContext();
		this.medias = medias;
		this.mSpotId = spotId;
		shareItem = new ArrayList<ShareItem>();
		imageIndex = 0;
		this.checkinId = checkinId;
		SharedPreferences pref = activity.getSharedPreferences("account",
				Context.MODE_PRIVATE);
		String tokenKey = pref.getString("token_key", "");
		String tokenSecret = pref.getString("token_secret", "");
		restClient = new NgonRestClient(tokenKey, tokenSecret, true);
	}

	@Override
	protected Integer doInBackground(Void... params) {
		Log.e("UploadPhoto", "Upload image");
		ArrayList<MenuItem> dishItems;
		StringBuffer buffer = new StringBuffer(1024);
		for (MediaItem media : medias) {
			ItemUpload upload = new ItemUpload();
			ShareItem shareItem = new ShareItem();
			shareItem.setTitle("Title ZooStudio");
			try {
				Uri photoUri = Uri.fromFile(new File(media.getPathMedia()));
				Log.e("SupportCheckIn"," doInBackground Context =" +  mContext);
				byte[] photoData = Utility.scaleImage(mContext, photoUri, media);
				restClient.addParam("spot_id", mSpotId);
				dishItems = media.getDishTagged();

				if (!dishItems.isEmpty()) {
					String dishes = "";
					buffer.delete(0, buffer.length());
					for (MenuItem dish : dishItems) {
						buffer.append(dish.getDishId()).append(",");
					}
					dishes = buffer.toString();
					dishes = dishes.substring(0, dishes.length() - 1);

					restClient.addParam("dishes", dishes);
				}
				restClient.postMultiPart("/photo", "photo",
						NgonTaskUtil.convertByteToByteArrayBody(mSpotId, media.getMineType(),photoData));
				try {
					JSONObject result = new JSONObject(restClient.getResponse());
					if (mUpLoadStatus = result.getBoolean("status")) {
						// String linkURl = result.getString("image_url");
						// shareItem.setLink(linkURl);
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
		return 0;
	}

	@Override
	protected void onProgressUpdate(ItemUpload... values) {
		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(
						Context.NOTIFICATION_SERVICE);
		CharSequence tickerText = mActivity.getResources().getString(R.string.title_notification);
		long when = System.currentTimeMillis();
		CharSequence contentTitle;
		RemoteViews remoteViews = new RemoteViews(mActivity
				.getPackageName(), R.layout.layout_notification);
		int icon;
		if (values[0].status) {
			contentTitle = mActivity.getResources().getString(R.string.content_notification_ok);
			icon = R.drawable.icon_notifi_ok;
			remoteViews.setImageViewResource(R.id.img_notification,
					R.drawable.icon_notifi_ok);
		} else {
			contentTitle = mActivity.getResources().getString(R.string.content_notification_fail);
			remoteViews.setImageViewResource(R.id.img_notification,
					R.drawable.icon_notifi_fail);
			icon = R.drawable.icon_notifi_fail;
		}
		Notification updateComplete = new Notification(icon,tickerText,when);
		remoteViews.setTextViewText(R.id.txtContentNotification, contentTitle);
		updateComplete.contentView = remoteViews;
		updateComplete.contentIntent = PendingIntent.getActivity(mActivity.getApplicationContext(), 1, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
		updateComplete.flags = Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(values[0].id, updateComplete);
	}
	

}
