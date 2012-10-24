package com.zoostudio.ngon.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.bookmark.helper.DeviceCore;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.cropimage.CropImageActivity;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.views.ZooCameraView;
import com.zoostudio.ngon.views.ZooCheckBoxButtonView;
import com.zoostudio.ngon.views.ZooImageButtonView;
import com.zoostudio.ngon.views.ZooImageView;

@TargetApi(9)
public class ZooCameraCommonActivity extends NgonActivity implements
		OnCheckedChangeListener, ZooCameraView.OnFocusListener {
	public static final String IMAGE_IDS = "IMAGE ID CAPTURED";
	public static final String RETURN_WITH_RESULT = "com.zoostudio.ngon.ui.ZooCameraCommonActivity.RETURN_WITH_RESULT";
	public static final String MEDIA_CAPTURED = "com.zoostudio.ngon.ui.ZooCameraCommonActivity.MEDIA_CAPTURED";
	private ZooImageButtonView btnCamera;
	private Camera mCamera;
	private ZooCheckBoxButtonView checkBox;
	private ZooCameraView mCameraPreview;
	private CheckBox mChkFocus;
	private int degree;
	private MyOrientationEventListener myOrientationEventListener;
	private ZooImageView mBtnChangeCamera;
	private volatile boolean isCaputring;
	private PowerManager.WakeLock wl;
	private boolean needReturn;

	@Override
	protected int setLayoutView() {
		return R.layout.activity_camera_for_square;
	}

	@Override
	protected void initControls() {
		btnCamera = (ZooImageButtonView) this.findViewById(R.id.btnCapture);
		checkBox = (ZooCheckBoxButtonView) this.findViewById(R.id.flash);
		mChkFocus = (CheckBox) this.findViewById(R.id.chkFocusCamera);

		mBtnChangeCamera = (ZooImageView) this
				.findViewById(R.id.btnChangeCamera);
		mBtnChangeCamera.setVisibility(View.INVISIBLE);
		checkBox.setOnCheckedChangeListener(this);
		mCamera = getCameraInstance();

		myOrientationEventListener = new MyOrientationEventListener(this,
				SensorManager.SENSOR_DELAY_NORMAL);

		if (myOrientationEventListener.canDetectOrientation()) {
			myOrientationEventListener.enable();
		} else {
			Toast.makeText(getApplicationContext(), "Khong the detect",
					Toast.LENGTH_SHORT).show();
		}

		if (null == mCamera) {
			Toast.makeText(getApplicationContext(), "Cant open camera",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mCameraPreview = new ZooCameraView(this, mCamera, this);
		RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);
		preview.addView(mCameraPreview, 0);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

		Toast.makeText(getApplicationContext(), "Touch to forcus",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		wl.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		wl.acquire();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		myOrientationEventListener.disable();
	}

	@Override
	protected void initVariables() {
		isCaputring = false;
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			needReturn = bundle.getBoolean(RETURN_WITH_RESULT, false);
		}
	}

	@Override
	protected void initActions() {
		btnCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isCaputring)
					return;
				boolean rs = mCameraPreview.getMaxSize(degree);
				if (rs) {
					mCameraPreview.preTakeCamera();
					mCamera.takePicture(null, null, mPicture);
				}
			}
		});

	}

	private Camera getCameraInstance() {
		Camera camera;
		if (DeviceCore.getAndroidVersionCode() >= 9) {
			camera = Camera.open(0);
		} else {
			camera = Camera.open();
		}
		return camera;
	}

	PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			camera.stopPreview();
			isCaputring = true;
			long timeStamp = System.currentTimeMillis();
			String title = "IMG_" + timeStamp + ".jpg";
			ContentValues values = new ContentValues();
			values.put(Images.Media.TITLE, title);
			values.put(Images.Media.DESCRIPTION, "ZooStudio");
			values.put(Images.Media.MIME_TYPE, "image/jpg");
			values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, timeStamp);
			values.put(MediaStore.Images.ImageColumns.ORIENTATION, degree);

			Uri url = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

			try {
				OutputStream imageOut = getContentResolver().openOutputStream(
						url);
				try {
					imageOut.write(data);
					imageOut.flush();
					imageOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				long mediaId = ContentUris.parseId(url);
				String mediaPath = getRealPathFromURI(url);
				MediaItem item = new MediaItem();
				item.setValue(mediaPath, mediaId, degree, "image/jpg");
				
				if (needReturn) {
					Intent intent = new Intent();
					intent.putExtra(MEDIA_CAPTURED, item);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Intent intent = new Intent(getApplicationContext(),
							CropImageActivity.class);
					intent.putExtra(CropImageActivity.MEDIA_ITEM, item);
					intent.putExtra("SOURCE", CropImageActivity.FROM_CAMERA);
					startActivity(intent);
				}

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	};

	private String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			mCameraPreview.turnOnFlash();
		} else {
			mCameraPreview.turnOffFlash();
		}
	}

	class MyOrientationEventListener extends OrientationEventListener {

		public MyOrientationEventListener(Context context, int rate) {
			super(context, rate);
		}

		@Override
		public void onOrientationChanged(int arg0) {
			if (arg0 >= 0 && arg0 <= 45) {
				degree = 90;
				btnCamera.startAnimation(-90);
				checkBox.startAnimation(-90);
				mBtnChangeCamera.startAnimation(-90);
			} else if (arg0 >= 45 && arg0 <= 130) {
				degree = 180;
				btnCamera.startAnimation(-degree);
				checkBox.startAnimation(-degree);
				mBtnChangeCamera.startAnimation(-degree);
			} else if (arg0 >= 240 && arg0 <= 300) {
				degree = 0;
				btnCamera.startAnimation(degree);
				checkBox.startAnimation(degree);
				mBtnChangeCamera.startAnimation(degree);
			} else if (arg0 >= 140 && arg0 <= 220) {
				degree = 270;
				btnCamera.startAnimation(90);
				checkBox.startAnimation(90);
				mBtnChangeCamera.startAnimation(90);
			} else if (arg0 >= 300) {
				degree = 90;
				btnCamera.startAnimation(-90);
				checkBox.startAnimation(-90);
				mBtnChangeCamera.startAnimation(-90);
			}
		}
	}

	@Override
	public void onFocus() {
		mChkFocus.setChecked(true);
	}

	@Override
	public void onNormal() {
		mChkFocus.setChecked(false);
	}
}
