package com.zoostudio.ngon.dialog;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.RequestCode;
import com.zoostudio.ngon.dialog.NgonDialog.Builder;
import com.zoostudio.ngon.utils.DataUtil;

public class SelectImageSoucreDialog extends Builder {
	private Uri mImageUri;

	public SelectImageSoucreDialog(final Activity activity) {
		super(activity);
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mCustomView = inflater.inflate(R.layout.dialog_pick_image, null);
		// Watch for button clicks.
		Button button = (Button) mCustomView
				.findViewById(R.id.btnPickFromCamera);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				try {
					// place where to store camera taken picture
					File photo = DataUtil.createTemporaryFile("picture", ".jpg");
					photo.delete();
					mImageUri = Uri.fromFile(photo);
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);
					// start camera intent
					activity.startActivityForResult(cameraIntent,
							RequestCode.REQUEST_IMAGE_FROM_CAMERA);
				} catch (IOException e) {
					Log.v("TEMP", "Can't create file to take picture!");
					Toast.makeText(activity,
							"Please check SD card! Image shot is impossible!",
							10000);
				}
			}
		});

		Button buttonCamera = (Button) mCustomView
				.findViewById(R.id.btnPickFromGallery);
		buttonCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				activity.startActivityForResult(intent,
						RequestCode.REQUEST_IMAGE_FROM_GALLERY);
			}
		});
		setCustomView(mCustomView);
	}

	public Uri getImageUri() {
		return mImageUri;
	}
}
