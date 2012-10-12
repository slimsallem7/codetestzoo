package com.zoostudio.ngon.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

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
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.views.ZooCameraView;
import com.zoostudio.ngon.views.ZooCheckBoxButtonView;
import com.zoostudio.ngon.views.ZooImageButtonView;
import com.zoostudio.ngon.views.ZooImageView;
import com.zoostudio.ngon.views.ZooTextView;

@TargetApi(9)
public class CameraActivity extends NgonActivity implements
		OnCheckedChangeListener, ZooCameraView.OnFocusListener {
	public static final String IMAGE_IDS = "IMAGE ID CAPTURED";
	private ZooImageButtonView btnCamera;
	private Camera mCamera;
	private ZooCheckBoxButtonView checkBox;
	private ZooCameraView mCameraPreview;
	private CheckBox mChkFocus;
	private volatile boolean hasCapture;
	private Handler handler;
	private int degree;
	private MyOrientationEventListener myOrientationEventListener;
	private ZooTextView countImage;
	private ZooImageView mBtnChangeCamera;
	private int numbersImageCaptured;
	private volatile boolean isCaputring;
	private PowerManager.WakeLock wl;
	private ArrayList<MediaItem> mediasCaptured;
	private ImageButton btnBackToGallery;

	@Override
	protected int setLayoutView() {
		return R.layout.activity_camera;
	}

	@Override
	protected void initControls() {
		handler = new Handler();
		hasCapture = false;
		btnCamera = (ZooImageButtonView) this.findViewById(R.id.btnCapture);
		checkBox = (ZooCheckBoxButtonView) this.findViewById(R.id.flash);
		countImage = (ZooTextView) this.findViewById(R.id.countImageCaptured);
		mChkFocus = (CheckBox) this.findViewById(R.id.chkFocusCamera);
		btnBackToGallery = (ImageButton) findViewById(R.id.btnBackToGallery);
		countImage.setVisibility(View.INVISIBLE);
		mBtnChangeCamera = (ZooImageView) this
				.findViewById(R.id.btnChangeCamera);
		mBtnChangeCamera.setVisibility(View.INVISIBLE);
		checkBox.setOnCheckedChangeListener(this);
		numbersImageCaptured = 0;
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
		mediasCaptured = new ArrayList<MediaItem>();
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
		btnBackToGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finishCamera();
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
		private MediaItem item;

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
					imageOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				item = new MediaItem();
				long mediaId = ContentUris.parseId(url);
				Images.Thumbnails.getThumbnail(getContentResolver(), mediaId,
						Images.Thumbnails.MINI_KIND, null);
				item.setValue(getRealPathFromURI(url), mediaId, true);
				mediasCaptured.add(item);

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			hasCapture = true;
			try {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (!countImage.isShown()) {
							countImage.setVisibility(View.VISIBLE);
						}
						numbersImageCaptured++;
						countImage.setText("" + numbersImageCaptured);
						isCaputring = false;
					}
				});
			} catch (Exception e) {
			}
			mCameraPreview.onTakeCameraDone();
			camera.startPreview();
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

	@Override
	public void onBackPressed() {
		finishCamera();
	}

	private void finishCamera() {
		if (hasCapture) {
			Intent data = new Intent();
			data.putExtra(IMAGE_IDS, mediasCaptured);
			this.setResult(RESULT_OK, data);
		} else {
			this.setResult(RESULT_CANCELED);
		}
		this.finish();
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
				if (countImage.isShown()) {
					countImage.startAnimation(-90);
				}
				mBtnChangeCamera.startAnimation(-90);
			} else if (arg0 >= 45 && arg0 <= 130) {
				degree = 180;
				btnCamera.startAnimation(-degree);
				checkBox.startAnimation(-degree);
				if (countImage.isShown())
					countImage.startAnimation(-degree);
				mBtnChangeCamera.startAnimation(-degree);
			} else if (arg0 >= 240 && arg0 <= 300) {
				degree = 0;
				btnCamera.startAnimation(degree);
				checkBox.startAnimation(degree);
				if (countImage.isShown())
					countImage.startAnimation(degree);
				mBtnChangeCamera.startAnimation(degree);
			} else if (arg0 >= 140 && arg0 <= 220) {
				degree = 270;
				btnCamera.startAnimation(90);
				checkBox.startAnimation(90);
				if (countImage.isShown())
					countImage.startAnimation(90);
				mBtnChangeCamera.startAnimation(90);
			} else if (arg0 >= 300) {
				degree = 90;
				btnCamera.startAnimation(-90);
				checkBox.startAnimation(-90);
				if (countImage.isShown())
					countImage.startAnimation(-90);
				mBtnChangeCamera.startAnimation(-90);
			}
		}
	}

	// private void onFocusChange() {
	// mChkFocus.setChecked(false);
	// mCameraPreview.focusChange();
	// }

	@Override
	public void onFocus() {
		mChkFocus.setChecked(true);
	}

	@Override
	public void onNormal() {
		mChkFocus.setChecked(false);
	}
}
