package com.zoostudio.ngon.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.zoostudio.ngon.R;

public class NotificationUtil {
	public static final int ID_IMAGE_UPLOAD_SPOT_OK = 1;
	public static final int ID_IMAGE_UPLOAD_SPOT_FAIL = 2;
	
	public static void notificationUploadImage(Activity mActivity,
			int idUpload, boolean status) {
		NotificationManager notificationManager = (NotificationManager) mActivity
				.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence tickerText = mActivity.getResources().getString(
				R.string.title_notification);
		long when = System.currentTimeMillis();
		CharSequence contentTitle;
		RemoteViews remoteViews = new RemoteViews(mActivity.getPackageName(),
				R.layout.layout_notification);
		int icon = R.drawable.icon_notifi;

		remoteViews.setImageViewResource(R.id.img_notification,
				R.drawable.icon_notifi);
		if (status) {
			contentTitle = mActivity.getResources().getString(
					R.string.content_notification_ok);
		} else {
			contentTitle = mActivity.getResources().getString(
					R.string.content_notification_fail);
		}

		Notification updateComplete = new Notification(icon, tickerText, when);
		remoteViews.setTextViewText(R.id.txtContentNotification, contentTitle);
		updateComplete.contentView = remoteViews;
		updateComplete.contentIntent = PendingIntent.getActivity(
				mActivity.getApplicationContext(), 1, new Intent(),
				PendingIntent.FLAG_CANCEL_CURRENT);
		updateComplete.flags = Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(idUpload, updateComplete);
	}
}
