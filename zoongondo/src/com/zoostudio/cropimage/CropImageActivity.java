package com.zoostudio.cropimage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.cropimage.imagelayer.LayerCrop;
import com.zoostudio.ngon.R;


public class CropImageActivity extends Activity implements
		OnReleaseTouchListener {

	/** Called when the activity is first created. */
	// private static final int DISK_CACHE_SIZE = 1024 * 1024 * 5; // 10MB
	// private static final String DISK_CACHE_SUBDIR = "thumbnails";
//	public final static String MEDIA_PATH = "com.zoostudio.cropimage.media";
	public final static String DESC_ERROR = "com.zoostudio.cropimage.error";
	public static final String CROP_IMAGE_ACTION = "com.zoostudio.action.CROP_IMAGE";
	public static final String IMAGE_CANCEL_CROP_FROM_CAMERA_ACTION = "com.zoostudio.action.CANCEL_CROP_FROM_CAMERA";
	private LayerCrop mLayerCrop;
	public static DisplayMetrics metrics;
	public static float DENSITY;
	public static int WIDTH_SCREEN;
	public static int HEIGHT_SCREEN;
	private ImageView mCropImage;

	private Button mBtnCrop;
	private Button mBtnDone;
	private Button mBtnCancel;
	private Button mBtnReCrop;
	private Bitmap mbitmapCroped;

	private ImageButton mBtnRotateLeft;
	private ImageButton mBtnRotateRight;

	private RelativeLayout mParent;
	private int minWidth;
	private int minHeight;
	private Bitmap bitmapOrgi;

	public final static int FROM_GALLERY = 0;
	public final static int FROM_CAMERA = 1;
	public static final int INVALID_DIMENSION = 2;
	private int mSource;
	private ImageInfo mInfoImage;
	public Uri mCurrentUri;
	private String mediaPath;

	private View layoutControl;
	private final static int OK = 1;
	private final static int FAIL = 2;
	public static final String MEDIA_CALL_BACK = "com.zoostudio.cropimage.mediacallback";
	public static final String MEDIA_SIZE = "com.zoostudio.cropimage.size";
	public static final String MEDIA_ITEM = "com.zoostudio.cropimage.mediaitem";
	
	private MediaItem mMediaItem;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent();
			if (msg.what == RESULT_OK) {
				intent.setAction(CROP_IMAGE_ACTION);
				intent.putExtra(MEDIA_ITEM, mMediaItem);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			} else {
				CropImageActivity.this.setResult(RESULT_CANCELED);
				CropImageActivity.this.finish();
			}
		}
	};
	private ProgressDialog progressDialog;
	private String mErrorInvalid;
	private LoadImageCropTask cropTask;
	public static int sWidthBitmap;
	public static int sHeightBitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_image);
		mSource = getIntent().getIntExtra("SOURCE", -1);
		minHeight = 240;
		minWidth = 240;

		if (mCurrentUri == null) {
			mCurrentUri = this.getIntent().getData();
		}
		layoutControl = findViewById(R.id.layout_control);
		mErrorInvalid = getString(R.string.mess_invalid_image);
		mMediaItem = (MediaItem) getIntent().getExtras().getSerializable(MEDIA_ITEM);
		mediaPath = mMediaItem.getPathMedia();
		mParent = (RelativeLayout) this.findViewById(R.id.parent);
		mCropImage = (ImageView) this.findViewById(R.id.resultCrop);
		mBtnDone = (Button) this.findViewById(R.id.btnDone);
		mBtnReCrop = (Button) this.findViewById(R.id.btnReCrop);
		mBtnCancel = (Button) this.findViewById(R.id.btnCancel);
		mBtnCrop = (Button) this.findViewById(R.id.btnCrop);

		mBtnRotateLeft = (ImageButton) this.findViewById(R.id.btnRotateLeft);
		mBtnRotateRight = (ImageButton) this.findViewById(R.id.btnRotateRight);
		mBtnRotateLeft.setVisibility(View.GONE);
		mBtnRotateRight.setVisibility(View.GONE);

		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		WIDTH_SCREEN = getWindowManager().getDefaultDisplay().getWidth();
		HEIGHT_SCREEN = getWindowManager().getDefaultDisplay().getHeight();

		cropTask = new LoadImageCropTask();
		cropTask.execute();
	}

	private class LoadImageCropTask extends AsyncTask<Void, Void, Integer> {
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(CropImageActivity.this);
			progressDialog.setMessage("Processing...");
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			getBitmapNeedCrop();
			boolean rs = validData();
			if (rs) {
				return OK;
			}
			return FAIL;
		}

		@Override
		protected void onPostExecute(Integer result) {
			progressDialog.dismiss();
			if (result == OK) {
				mLayerCrop = new LayerCrop(
						CropImageActivity.this.getBaseContext(), mediaPath,
						mInfoImage);
				ImageConfig config = new ImageConfig();
				config.minWidth = minWidth;
				config.maxWidth = 0;
				mLayerCrop.setConfig(config);
				mParent.addView(mLayerCrop, 1);
				mLayerCrop.setOnReleaseTouchListener(CropImageActivity.this);
			} else {
				Toast.makeText(getApplicationContext(), mErrorInvalid,
						Toast.LENGTH_SHORT).show();
				CropImageActivity.this.setResult(INVALID_DIMENSION);
				CropImageActivity.this.finish();
			}
		}

	}

	private void getBitmapNeedCrop() {
		if (bitmapOrgi == null) {
			if (mSource == FROM_CAMERA) {
				bitmapOrgi = getBitmapFromGallery(mCurrentUri);
			} else if (mSource == FROM_GALLERY) {
				bitmapOrgi = getBitmapFromGallery(mCurrentUri);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != mLayerCrop)
			mLayerCrop.releaseBitmap();
	}

	private boolean validData() {
		if (sWidthBitmap < minWidth || sHeightBitmap < minHeight) {
			return false;
		}
		return true;
	}

	private Bitmap getBitmapFromGallery(Uri selectedImage) {
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		String filePath = "";
		if (null != selectedImage) {
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			filePath = cursor.getString(columnIndex);
			cursor.close();
		}
		if (filePath.equals(""))
			filePath = mediaPath;
		String ext = getExtension(filePath);
		if (ext.equalsIgnoreCase("JPEG") || ext.equalsIgnoreCase("JPG")) {
			mInfoImage = new ImageInfo(filePath);
		} else {
			mInfoImage = new ImageInfo();
		}
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		sWidthBitmap = options.outWidth;
		sHeightBitmap = options.outHeight;

		return null;
	}

	private String getExtension(String file) {
		int dotposition = file.lastIndexOf(".");
		return file.substring(dotposition + 1, file.length());
	}

	public void doCrop(View view) {
		mbitmapCroped = mLayerCrop.cropImage();
		mBtnCrop.setVisibility(View.GONE);
		mBtnDone.setVisibility(View.VISIBLE);
		mBtnReCrop.setVisibility(View.VISIBLE);
		mBtnCancel.setVisibility(View.GONE);
		mLayerCrop.setVisibility(View.GONE);
		mCropImage.setVisibility(View.VISIBLE);
		mBtnRotateLeft.setVisibility(View.VISIBLE);
		mBtnRotateRight.setVisibility(View.VISIBLE);
		mCropImage.setImageBitmap(mbitmapCroped);
	}

	public void doCancel(View view) {
		if (mSource == FROM_CAMERA) {
			Intent intent = new Intent(IMAGE_CANCEL_CROP_FROM_CAMERA_ACTION);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(MEDIA_CALL_BACK, mMediaItem);
			startActivity(intent);

		} else {
			this.setResult(RESULT_CANCELED);
			this.finish();
		}
	}

	@Override
	public void onBackPressed() {
		doCancel(mParent);
	}

	public void doFinish(View view) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Processing...");
		progressDialog.show();
		saveCropToUri();
	}

	private void saveCropToUri() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					File photo = createTemporaryFile("ngondo_thumb", ".jpg");
					photo.delete();
					BufferedOutputStream outputStream = new BufferedOutputStream(
							new FileOutputStream(photo));
					mbitmapCroped.compress(Bitmap.CompressFormat.JPEG, 80,
							outputStream);
					mMediaItem = new MediaItem();
					mediaPath = photo.getPath();
					mMediaItem.setValue(mediaPath, -1, 0, "image/jpg");
					outputStream.flush();
					outputStream.close();
					handler.sendEmptyMessage(RESULT_OK);
				} catch (IOException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(RESULT_CANCELED);
				}
				progressDialog.dismiss();
			}
		}).start();
	}

	public void doReCrop(View view) {
		getBitmapNeedCrop();
		mParent.removeView(mLayerCrop);
		mLayerCrop = null;
		mBtnCancel.setVisibility(View.VISIBLE);
		mBtnReCrop.setVisibility(View.GONE);
		mBtnCrop.setVisibility(View.VISIBLE);
		mBtnDone.setVisibility(View.GONE);
		mCropImage.setVisibility(View.GONE);
		mbitmapCroped.recycle();
		mBtnRotateLeft.setVisibility(View.GONE);
		mBtnRotateRight.setVisibility(View.GONE);
		cropTask = new LoadImageCropTask();
		cropTask.execute();
	}

	public static File createTemporaryFile(String part, String ext)
			throws IOException {
		File tempDir = Environment.getExternalStorageDirectory();
		tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
		if (!tempDir.exists()) {
			tempDir.mkdir();
		}
		return File.createTempFile(part, ext, tempDir);
	}

	public void rotateLeft(View view) {
		Matrix transform = new Matrix();
		transform.setTranslate(mbitmapCroped.getWidth() / 2,
				mbitmapCroped.getHeight() / 2);
		transform.preRotate(-90, mbitmapCroped.getWidth() / 2,
				mbitmapCroped.getHeight() / 2);
		mbitmapCroped = Bitmap.createBitmap(mbitmapCroped, 0, 0,
				mbitmapCroped.getWidth(), mbitmapCroped.getHeight(), transform,
				true);
		mCropImage.setImageBitmap(mbitmapCroped);
		mCropImage.invalidate();
	}

	public void rotateRight(View view) {
		Matrix transform = new Matrix();
		transform.setTranslate(mbitmapCroped.getWidth() / 2,
				mbitmapCroped.getHeight() / 2);
		transform.preRotate(90, mbitmapCroped.getWidth() / 2,
				mbitmapCroped.getHeight() / 2);
		mbitmapCroped = Bitmap.createBitmap(mbitmapCroped, 0, 0,
				mbitmapCroped.getWidth(), mbitmapCroped.getHeight(), transform,
				true);
		mCropImage.setImageBitmap(mbitmapCroped);
		mCropImage.invalidate();
	}

	@Override
	public void onTouchReleaseListener(boolean release) {
		layoutControl.setVisibility(release ? View.VISIBLE : View.GONE);
	}
}