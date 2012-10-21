package com.zoostudio.ngon.ui;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zoostudio.adapter.SpotPhotoAdapter;
import com.zoostudio.adapter.item.PhotoItem;
import com.zoostudio.adapter.item.ReviewItem;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.cropimage.CropImageActivity;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.RequestCode;
import com.zoostudio.ngon.dialog.NgonDialog;
import com.zoostudio.ngon.dialog.NgonErrorDialog;
import com.zoostudio.ngon.dialog.NgonProgressDialog;
import com.zoostudio.ngon.dialog.SelectImageSoucreDialog;
import com.zoostudio.ngon.dialog.WaitingDialog;
import com.zoostudio.ngon.task.GetSpotPhotoTask;
import com.zoostudio.ngon.task.GetSpotReviewTask;
import com.zoostudio.ngon.task.GetSpotTask;
import com.zoostudio.ngon.task.LikeTask;
import com.zoostudio.ngon.task.UploadPhotoTask;
import com.zoostudio.ngon.task.callback.OnLikeTaskListener;
import com.zoostudio.ngon.task.callback.OnSpotPhotoTaskListener;
import com.zoostudio.ngon.task.callback.OnSpotReviewTaskListener;
import com.zoostudio.ngon.task.callback.OnSpotTaskListener;
import com.zoostudio.ngon.task.callback.OnUploadPhotoTask;
import com.zoostudio.ngon.views.ButtonCaptionedIcon;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.ngon.views.ListCommentView;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnDataErrorDelegate;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;
import com.zoostudio.zooslideshow.LikerItem;
import com.zoostudio.zooslideshow.OnSlideShowListener;
import com.zoostudio.zooslideshow.ZooLikerView;
import com.zoostudio.zooslideshow.ZooSlideView;

public class SpotDetailsActivity extends NgonActivity implements
		OnClickListener, OnPreExecuteDelegate, OnDataErrorDelegate,
		OnSpotTaskListener, OnSpotReviewTaskListener, OnLikeTaskListener,
		OnSpotPhotoTaskListener, OnUploadPhotoTask {
	public static final String EXTRA_SPOT = "com.ngon.do.spotdetailactivity.SPOT";

	protected static final int DIALOG_TAKE_PHOTO = 1000;

	private ImageButton btnCheckin;
	private ImageButton btnSpotMenu;
	private GetSpotPhotoTask photoTask;
	private GetSpotReviewTask reviewTask;
	private GetSpotTask infoTask;
	private TextView tvSpotName;
	private TextView tvSpotAddress;
	private NgonProgressDialog mWaitingDialog;
	private LinearLayout lvReview;
	private LinearLayout mListReview;
	private ButtonCaptionedIcon mLike;
	private ImageView mImageFood;
	private SpotPhotoAdapter photoAdapter;
	private Dialog pickImageDialog;
	private Handler mHandler;
	private SelectImageSoucreDialog mSelectImageSoucreDialog;
	private ImageView ivSpotMap;
	private ButtonUp mUp;
	private RelativeLayout mReportSpot;
	private SpotItem mSpot;
	private ZooSlideView mSlideImageView;
	private ZooLikerView mLikerView;
	private ListCommentView mListCommentView;
	private TextView mMenu;

	private View mAddReView;

	private ArrayList<String> dataTest;

	@Override
	protected int setLayoutView() {
		return R.layout.activity_spot_details_new;
	}

	@Override
	protected void initControls() {
		mSlideImageView = (ZooSlideView) this
				.findViewById(R.id.spot_details_slideImageDish);
		mLikerView = (ZooLikerView) findViewById(R.id.zooLikerView);
		btnCheckin = (ImageButton) findViewById(R.id.checkin);
		mListCommentView = (ListCommentView) findViewById(R.id.reView_ListCommentView);
		mMenu = (TextView) findViewById(R.id.menu);
		mAddReView = this.findViewById(R.id.addreview);
		tvSpotName = (TextView) findViewById(R.id.spot_name);
		tvSpotAddress = (TextView) findViewById(R.id.spot_address);
		mMenu.setOnClickListener(this);
		mAddReView.setOnClickListener(this);

		ArrayList<LikerItem> likers = new ArrayList<LikerItem>();
		likers.add(new LikerItem("", "11"));
		likers.add(new LikerItem("", "12"));
		likers.add(new LikerItem("", "13"));
		likers.add(new LikerItem("", "14"));
		mLikerView.setDatas(likers, 15);

		mUp = (ButtonUp) findViewById(R.id.btn_up);
	}

	@Override
	protected void initVariables() {
		mHandler = new Handler();
		Bundle bundle = this.getIntent().getExtras();
		mSpot = (SpotItem) bundle.getSerializable(EXTRA_SPOT);
	}

	@Override
	protected void onStop() {
		super.onStop();
		SmartImageView.cancelAllTasks();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkin:
			Intent intent = new Intent(this, ActivityCheckin.class);
			intent.putExtra(EXTRA_SPOT, mSpot);
			startActivity(intent);
			break;
		case R.id.addreview:
			break;
		case R.id.share:
			break;
		case R.id.like:
			LikeTask likeTask = new LikeTask(this, mSpot.getId());
			likeTask.setOnLikeTaskListener(this);
			likeTask.execute();
			break;
		case R.id.menu:
			Intent i = new Intent(this, SpotMenu.class);
			startActivity(i);
			break;
		case R.id.addphoto:
			doUploadPhoto();
			break;
		case R.id.spot_map:
			doNavigator();
			break;
		}
	}

	private void doNavigator() {
		startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse("google.navigation:q=" + mSpot.getAddress())));
	}

	private void doUploadPhoto() {
		mSelectImageSoucreDialog = new SelectImageSoucreDialog(this);
		pickImageDialog = mSelectImageSoucreDialog.create();
		pickImageDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RequestCode.CROP_IMAGE) {
			if (resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
				this.getContentResolver().notifyChange(selectedImage, null);
				ContentResolver cr = this.getContentResolver();
				try {
					Bitmap bitmapOrgi = android.provider.MediaStore.Images.Media
							.getBitmap(cr, selectedImage);
					UploadPhotoTask uploadTask = new UploadPhotoTask(this,
							mSpot.getId(), bitmapOrgi);
					uploadTask.setOnPreExecuteDelegate(this);
					uploadTask.setOnUploadPhotoTaskListener(this);
					uploadTask.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (requestCode == RequestCode.REQUEST_PICK_IMAGE_FROM_CAMERA) {
			pickImageDialog.dismiss();
			if (resultCode == RESULT_OK) {
				startActivityCropBitmap(mSelectImageSoucreDialog.getImageUri(),
						CropImageActivity.FROM_CAMERA);
			}

		} else if (requestCode == RequestCode.REQUEST_PICK_IMAGE_FROM_GALLERY) {
			pickImageDialog.dismiss();
			if (resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();

				startActivityCropBitmap(selectedImage,
						CropImageActivity.FROM_GALLERY);
			}
		}
	}

	private void startActivityCropBitmap(final Uri yourURIWithImage,
			final int source) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(getApplicationContext(),
						CropImageActivity.class);
				intent.setData(yourURIWithImage);
				intent.putExtra("SOURCE", source);
				startActivityForResult(intent, RequestCode.CROP_IMAGE);
			}
		});
	}

	@Override
	public void onActionPre(RestClientTask task) {
		if (null == mWaitingDialog) {
			mWaitingDialog = new WaitingDialog(this);
			mWaitingDialog.show();
		}
	}

	@Override
	protected void initActions() {
		mSlideImageView.setImageMainSpot(mSpot.getUrlImageSpot());

		btnCheckin.setOnClickListener(this);
		mLikerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "Test",
						Toast.LENGTH_SHORT).show();
			}
		});

		mUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		mSlideImageView.setOnSildeShowListener(new OnSlideShowListener() {
			@Override
			public void onTakePhoto() {
				showDialog(DIALOG_TAKE_PHOTO);
			}

			@Override
			public void onSelectFullImage() {
			}

			@Override
			public void onListThumbClicked() {
			}
		});

		// Load Spot Info
		infoTask = new GetSpotTask(this, mSpot.getId());
		infoTask.setOnSpotTaskListener(this);
		infoTask.setOnDataErrorDelegate(this);
		infoTask.setOnPreExecuteDelegate(this);
		infoTask.execute();

		// Load Spot Review
		reviewTask = new GetSpotReviewTask(this, mSpot.getId());
		reviewTask.setOnSpotReviewTaskListener(this);
		reviewTask.execute();

		// load photo
		photoTask = new GetSpotPhotoTask(this, mSpot.getId());
		photoTask.setOnSpotPhotoTaskListener(this);
		photoTask.execute();
	}

	private void sendMailReport() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL, new String[] { "bqviet.vn@gmail.com" });
		i.putExtra(Intent.EXTRA_SUBJECT, "Phan hoi su dung NgonDo");
		i.putExtra(Intent.EXTRA_TEXT, "");
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There are no email clients installed.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_TAKE_PHOTO:
			return createDialogTakePhoto();

		default:
			break;
		}
		return super.onCreateDialog(id);
	}

	private Dialog createDialogTakePhoto() {
		NgonDialog.Builder builder = new NgonDialog.Builder(this);
		builder.setCancelable(true);
		builder.setTitle(getString(R.string.dialog_spotdetail_addphoto));
		builder.setNegativeButton(R.string.dialog_close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		View content = getLayoutInflater().inflate(R.layout.dialog_pick_image,
				null);
		content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		TextView mGallery = (TextView) content
				.findViewById(R.id.btnPickFromGallery);
		TextView mCamera = (TextView) content
				.findViewById(R.id.btnPickFromCamera);

		builder.setInnerCustomView(content);

		final NgonDialog dialog = builder.create();

		mGallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		mCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		return dialog;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != dataTest)
			mSlideImageView.setDatas(dataTest);
	}

	@Override
	public synchronized void onActionDataError(RestClientTask task,
			int errorCode) {
		if (null != mWaitingDialog && mWaitingDialog.isShowing()) {
			mWaitingDialog.dismiss();
		}
	}

	/*
	 * Du lieu duoc tra ve khi lay duoc thong tin cua spot (non-Javadoc)
	 * 
	 * @see
	 * com.zoostudio.ngon.task.callback.OnSpotTaskListener#onSpotTaskListener
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void onSpotTaskListener(String name, String address) {
		if (mWaitingDialog.isShowing())
			mWaitingDialog.dismiss();
		mSpot.setName(name);
		mSpot.setAddress(address);
		tvSpotName.setText(mSpot.getName());
		tvSpotAddress.setText(mSpot.getAddress());
	}

	@Override
	public void onSpotReviewTaskListener(ArrayList<ReviewItem> data) {
		ArrayList<ReviewItem> reviews = new ArrayList<ReviewItem>();
		reviews.add(new ReviewItem(1));
		reviews.add(new ReviewItem(2));
		reviews.add(new ReviewItem(3));
		reviews.add(new ReviewItem(4));
		reviews.add(new ReviewItem(0));
		mListCommentView.setDatas(reviews);
	}

	@Override
	public void onSpotAlreadyLike(int messId) {
		NgonErrorDialog errorDialog = new NgonErrorDialog(this);
		errorDialog.setMessage(messId);
		errorDialog.show();
	}

	@Override
	public void onSpotPhotoTaskListener(ArrayList<PhotoItem> data) {
		dataTest = new ArrayList<String>();
		dataTest.add("");
		dataTest.add("");
		dataTest.add("");
		dataTest.add("");
		dataTest.add("");
		mSlideImageView.setDatas(dataTest);
	}

	@Override
	public void onUploadPhotoTaskListener(PhotoItem photoItem) {
		if (mWaitingDialog != null) {
			mWaitingDialog.dismiss();
			mWaitingDialog = null;
		}
	}

}
