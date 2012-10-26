package com.zoostudio.ngon.ui;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zoostudio.adapter.SpotPhotoAdapter;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.adapter.item.PhotoItem;
import com.zoostudio.adapter.item.ReviewItem;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.RequestCode;
import com.zoostudio.ngon.dialog.NgonDialog;
import com.zoostudio.ngon.dialog.NgonErrorDialog;
import com.zoostudio.ngon.dialog.NgonProgressDialog;
import com.zoostudio.ngon.dialog.SelectImageSoucreDialog;
import com.zoostudio.ngon.dialog.WaitingDialog;
import com.zoostudio.ngon.task.CreateReviewTask;
import com.zoostudio.ngon.task.GetSpotPhotoTask;
import com.zoostudio.ngon.task.GetSpotReviewTask;
import com.zoostudio.ngon.task.GetSpotTask;
import com.zoostudio.ngon.task.LikeTask;
import com.zoostudio.ngon.task.UploadPhotoTask;
import com.zoostudio.ngon.task.callback.OnCreateReviewTaskListener;
import com.zoostudio.ngon.task.callback.OnLikeTaskListener;
import com.zoostudio.ngon.task.callback.OnSpotPhotoTaskListener;
import com.zoostudio.ngon.task.callback.OnSpotReviewTaskListener;
import com.zoostudio.ngon.task.callback.OnSpotTaskListener;
import com.zoostudio.ngon.task.callback.OnUploadPhotoTask;
import com.zoostudio.ngon.utils.NotificationUtil;
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
		OnSpotPhotoTaskListener, OnUploadPhotoTask, OnCreateReviewTaskListener {
	public static final String EXTRA_SPOT = "com.ngon.do.spotdetailactivity.SPOT";

	protected static final int DIALOG_TAKE_PHOTO = 1000;

	private ImageButton btnCheckin;
	private ImageButton btnSpotMenu;
	private GetSpotPhotoTask getPhotoSpotTask;
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
	private ImageButton mMapSpot;

	private View mAddReView;
	private ImageButton mShareButton;
	private volatile boolean hasLoadReview;
	private ArrayList<String> dataTest;

	private int mCurrentTypeUpload;

	@Override
	protected int setLayoutView() {
		return R.layout.activity_spot_details_new;
	}

	@Override
	protected void initControls() {
		mSlideImageView = (ZooSlideView) this
				.findViewById(R.id.spot_details_slideImageDish);
		mLikerView = (ZooLikerView) findViewById(R.id.zooLikerView);
		mShareButton = (ImageButton) findViewById(R.id.share);
		btnCheckin = (ImageButton) findViewById(R.id.checkin);
		mListCommentView = (ListCommentView) findViewById(R.id.reView_ListCommentView);
		mMenu = (TextView) findViewById(R.id.menu);
		mAddReView = this.findViewById(R.id.addreview);
		tvSpotName = (TextView) findViewById(R.id.spot_name);
		tvSpotAddress = (TextView) findViewById(R.id.spot_address);
		mMapSpot = (ImageButton) findViewById(R.id.spot_map);
		mMenu.setOnClickListener(this);
		mAddReView.setOnClickListener(this);
		mShareButton.setOnClickListener(this);
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
		mCurrentTypeUpload = UploadPhotoTask.THUMB;
		Bundle bundle = this.getIntent().getExtras();
		mSpot = (SpotItem) bundle.getSerializable(EXTRA_SPOT);
	}

	@Override
	protected void onStop() {
		super.onStop();
		SmartImageView.cancelAllTasks();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("SpotDetailActivity", "onDestroy");
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
			showDialogAddReview();
			break;
		case R.id.share:
			shareSpot();
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

	/**
	 * Hien thi dialog de gui di nhan xet ve Spot
	 */
	private void showDialogAddReview() {
		NgonDialog.Builder builder = new NgonDialog.Builder(this);
		final EditText content = new EditText(getApplicationContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		content.setLines(5);
		content.setLayoutParams(layoutParams);
		content.setBackgroundDrawable(null);
		content.setPadding(5, 5, 5, 5);
		content.setGravity(Gravity.TOP | Gravity.LEFT);
		builder.setCancelable(true);
		builder.setTitle(getString(R.string.title_dialog_add_review));
		builder.setNegativeButton(R.string.dialog_close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.setPositiveButton(R.string.dialog_send,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CreateReviewTask reviewTask = new CreateReviewTask(
								SpotDetailsActivity.this, mSpot.getId(),
								content.getText().toString());
						reviewTask
								.setOnDataErrorDelegate(SpotDetailsActivity.this);
						reviewTask
								.setOnCreateReviewTaskListener(SpotDetailsActivity.this);
						reviewTask.execute();
						dialog.dismiss();
					}
				});
		builder.setInnerCustomView(content);
		builder.show();
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

		if (requestCode == RequestCode.REQUEST_IMAGE_FROM_CAMERA) {
			if (resultCode == RESULT_OK) {
				MediaItem photo = (MediaItem) data.getExtras().get(
						ZooCameraCommonActivity.MEDIA_CAPTURED);
				uploadPhoto(photo,mCurrentTypeUpload);
			}

		} else if (requestCode == RequestCode.REQUEST_IMAGE_FROM_GALLERY) {
			if (resultCode == RESULT_OK) {
				MediaItem photo = (MediaItem) data.getExtras().get(
						ChooseCommonMediaActivity.MEDIA_PICKED);

				uploadPhoto(photo,mCurrentTypeUpload);
			}
		}
	}

	private void uploadPhoto(MediaItem photo,int type) {
		UploadPhotoTask photoTask = new UploadPhotoTask(
				SpotDetailsActivity.this, mSpot.getId(), photo,type);
		photoTask.setOnPreExecuteDelegate(this);
		photoTask.setOnDataErrorDelegate(this);
		photoTask.setOnUploadPhotoTaskListener(this);
		photoTask.execute();
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
				mCurrentTypeUpload = UploadPhotoTask.THUMB;
				showDialog(DIALOG_TAKE_PHOTO);
			}

			@Override
			public void onSelectFullImage() {
			}

			@Override
			public void onListThumbClicked() {
			}

			@Override
			public void onTakePhotoCoverSpot() {
				mCurrentTypeUpload = UploadPhotoTask.COVER;
			}
		});

		mMapSpot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				double lat = mSpot.getLocation().getLatitude();
				double lon = mSpot.getLocation().getLongtitude();
				String uri = "geo:" + lat + "," + lon + "?z=17";
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Không thể mở Map",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// Load Spot Info
		infoTask = new GetSpotTask(this, mSpot.getId());
		infoTask.setOnSpotTaskListener(this);
		infoTask.setOnDataErrorDelegate(this);
		infoTask.setOnPreExecuteDelegate(this);
		infoTask.execute();

		// load photo
		getPhotoSpot();
	}

	private void getPhotoSpot() {
		if (null != getPhotoSpotTask && getPhotoSpotTask.isLoading())
			getPhotoSpotTask.cancel(true);
		getPhotoSpotTask = new GetSpotPhotoTask(this, mSpot.getId());
		getPhotoSpotTask.setOnSpotPhotoTaskListener(this);
		getPhotoSpotTask.execute();
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
				Intent intent = new Intent(getApplicationContext(),
						ChooseCommonMediaActivity.class);
				intent.putExtra(ChooseCommonMediaActivity.RETURN_WITH_RESULT,
						true);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivityForResult(intent,
						RequestCode.REQUEST_IMAGE_FROM_GALLERY);
				dialog.dismiss();
			}
		});

		mCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ZooCameraCommonActivity.class);
				intent.putExtra(ZooCameraCommonActivity.RETURN_WITH_RESULT,
						true);
				startActivityForResult(intent,
						RequestCode.REQUEST_IMAGE_FROM_CAMERA);
				dialog.dismiss();
			}
		});

		return dialog;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Load Spot Review
		if (!hasLoadReview) {
			reviewTask = new GetSpotReviewTask(this, mSpot.getId());
			reviewTask.setOnSpotReviewTaskListener(this);
			reviewTask.execute();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != reviewTask && reviewTask.isLoading())
			reviewTask.cancel(true);
	}

	@Override
	public synchronized void onActionDataError(RestClientTask task,
			int errorCode) {
		if (null != mWaitingDialog && mWaitingDialog.isShowing()) {
			mWaitingDialog.dismiss();
		}
		if (task instanceof UploadPhotoTask) {
			NotificationUtil.notificationUploadImage(this,
					NotificationUtil.ID_IMAGE_UPLOAD_SPOT_FAIL, false);
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
		hasLoadReview = true;
		ArrayList<ReviewItem> reviews = new ArrayList<ReviewItem>();
		reviews.add(new ReviewItem(1));
		reviews.add(new ReviewItem(2));
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
		Log.e("SpotDetailActivity", "data =" + data);
		mSlideImageView.setDatas(data);
	}

	@Override
	public void onUploadPhotoTaskListener(PhotoItem photoItem) {
		if (mWaitingDialog != null) {
			mWaitingDialog.dismiss();
			mWaitingDialog = null;
			long id = System.currentTimeMillis();
			NotificationUtil.notificationUploadImage(this, (int) id, true);
			getPhotoSpot();
		}
	}

	@Override
	public void onCreateReviewTaskListener() {
		Toast.makeText(getApplicationContext(),
				R.string.notifi_create_review_done, Toast.LENGTH_SHORT).show();

	}

	private void shareSpot() {
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		String appName = getResources().getString(R.string.app_name);
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, appName);
		String shareContent = getResources().getString(R.string.share_content);
		String shareMessage = shareContent + mSpot.getName() + " - "
				+ mSpot.getAddress();
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
		String titleShare = getResources().getString(R.string.share_spot);
		startActivity(Intent.createChooser(shareIntent, titleShare));
	}

	@Override
	public void onUploadCoverPhotoTaskListener(PhotoItem photoItem) {
		mSlideImageView.setImageMainSpot(photoItem.getPath());
	}
}
