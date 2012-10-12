package com.zoostudio.ngon.task;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class UploadAvatarTask extends RestClientTask {

    private ContentBody mPhoto;

    public UploadAvatarTask(Activity activity, File photo) {
        this(activity, new FileBody(photo, "image/png"));
    }

    public UploadAvatarTask(Activity activity, Bitmap photo) {
        this(activity, convertBitmapToByteArray(photo));
    }

    public UploadAvatarTask(Activity activity, ContentBody photo) {
        super(activity);
        mPhoto = photo;
    }

    @Override
    protected void doExecute() {
        restClient.postMultiPart("/user/avatar", "photo", mPhoto);
    }

    private static ByteArrayBody convertBitmapToByteArray(Bitmap photo) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(CompressFormat.PNG, 90, bos);
        byte[] data = bos.toByteArray();
        ByteArrayBody bab = new ByteArrayBody(data, "image/png", "user_avatar.png");
        return bab;
    }

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		return RestClientNotification.OK;
	}

}
