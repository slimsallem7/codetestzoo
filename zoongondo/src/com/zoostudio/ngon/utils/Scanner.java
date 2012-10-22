package com.zoostudio.ngon.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.util.Log;

import com.zoostudio.adapter.item.MediaItem;

public class Scanner {
	private static final int REQUEST_MEDIA = 0;
	private Activity activity;
	private OnScanMediaListener listener;
	private ArrayList<MediaItem> mediaIds;

	public Scanner(Activity activity) {
		this.activity = activity;
		mediaIds = new ArrayList<MediaItem>();
	}

	public void loadMedia() {
		String[] projection = { Images.Media._ID, Images.ImageColumns.DATA,
				Images.ImageColumns.ORIENTATION, Images.ImageColumns.MIME_TYPE };
		String selection = "";
		String[] selectionArgs = null;
		mediaIds.clear();

		CursorLoader loader = new CursorLoader(activity);
		loader.setProjection(projection);
		loader.setSelectionArgs(selectionArgs);
		loader.setSelection(selection);
		loader.setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		loader.setSortOrder(null);
		loader.registerListener(REQUEST_MEDIA,
				new OnLoadCompleteListener<Cursor>() {
					@Override
					public void onLoadComplete(Loader<Cursor> loader,
							Cursor cursor) {
						long mediaId;
						String pathMedia;
						int orient;
						String mineType;
						MediaItem item = null;
						if (cursor.getCount() > 0) {
							cursor.moveToFirst();
							while (!cursor.isAfterLast()) {
								mediaId = cursor.getLong(cursor
										.getColumnIndex(Images.Media._ID));
								pathMedia = cursor.getString(cursor
										.getColumnIndex(Images.ImageColumns.DATA));
								orient = cursor.getInt(cursor
										.getColumnIndex(Images.ImageColumns.ORIENTATION));
								mineType = cursor.getString(cursor
										.getColumnIndex(Images.Media.MIME_TYPE));
								cursor.moveToNext();
								item = new MediaItem();
								item.setValue(pathMedia, mediaId, orient,mineType);
								mediaIds.add(item);
							}
							Scanner.this.listener.onScanFinished(mediaIds);
						}
						cursor.close();
					}
				});
		loader.startLoading();

	}

	public void setOnScanMediaListener(OnScanMediaListener listener) {
		this.listener = listener;
	}
}
