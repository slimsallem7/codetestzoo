package com.zoostudio.ngon.task;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.zoostudio.restclient.RestClientTask;

public class UploadPhotoTask extends RestClientTask {

	private String mSpotId;
	private int[] mDishesId;
	private ContentBody mPhoto;

	public UploadPhotoTask(Activity activity, String spot_id, File photo) {
		this(activity, spot_id, photo, new int[] {});
	}

	public UploadPhotoTask(Activity activity, String spot_id, File photo, int[] dishes) {
		this(activity, spot_id, new FileBody(photo, "image/png"), dishes);
	}

	public UploadPhotoTask(Activity activity, String spot_id, Bitmap photo) {
		this(activity, spot_id, photo, new int[] {});
	}

	public UploadPhotoTask(Activity activity, String spot_id, Bitmap photo, int[] dishes) {
		this(activity, spot_id, convertBitmapToByteArray(spot_id, photo), dishes);
	}

	public UploadPhotoTask(Activity activity, String spot_id, ContentBody photo,
			int[] dishes) {
		super(activity);
		mSpotId = spot_id;
		mPhoto = photo;
		mDishesId = dishes;
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

		restClient.postMultiPart("/photo", "photo", mPhoto);
	}

	private static ByteArrayBody convertBitmapToByteArray(String spot_id,
			Bitmap photo) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		photo.compress(CompressFormat.PNG, 90, bos);
		byte[] data = bos.toByteArray();
		ByteArrayBody bab = new ByteArrayBody(data, "image/png", "photo_spot_"
				+ spot_id + ".png");
		return bab;
	}

}
