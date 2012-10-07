package com.zoostudio.ngon.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.zoostudio.cropimage.CropImageActivity;
import com.zoostudio.ngon.ErrorCode;
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
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.ngon.views.ButtonCaptionedIcon;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.ngon.views.ListCommentView;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;
import com.zoostudio.zooslideshow.LikerItem;
import com.zoostudio.zooslideshow.OnSlideShowListener;
import com.zoostudio.zooslideshow.ZooLikerView;
import com.zoostudio.zooslideshow.ZooSlideView;

public class SpotDetailsActivity extends NgonActivity implements
		OnClickListener, OnPostExecuteDelegate, OnPreExecuteDelegate {
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

	@Override
	protected int setLayoutView() {
		return R.layout.activity_spot_details_new;
	}

	@Override
	protected void initControls() {
		mSlideImageView = (ZooSlideView) this
				.findViewById(R.id.spot_details_slideImageDish);

		mLikerView = (ZooLikerView) this.findViewById(R.id.zooLikerView);
		btnCheckin = (ImageButton) findViewById(R.id.checkin);
		mListCommentView = (ListCommentView) this
				.findViewById(R.id.reView_ListCommentView);
		mMenu = (TextView) this.findViewById(R.id.menu);
		mAddReView = this.findViewById(R.id.addreview);
		mMenu.setOnClickListener(this);
		mAddReView.setOnClickListener(this);
		ArrayList<String> datas = new ArrayList<String>();
		datas.add("");
		datas.add("");
		datas.add("");
		datas.add("");
		datas.add("");
		mSlideImageView.setDatas(datas);

		ArrayList<LikerItem> likers = new ArrayList<LikerItem>();
		likers.add(new LikerItem("", "11"));
		likers.add(new LikerItem("", "12"));
		likers.add(new LikerItem("", "13"));
		likers.add(new LikerItem("", "14"));
		mLikerView.setDatas(likers, 15);

		ArrayList<ReviewItem> reviews = new ArrayList<ReviewItem>();
		reviews.add(new ReviewItem());
		reviews.add(new ReviewItem());
		reviews.add(new ReviewItem());
		mListCommentView.setDatas(reviews);

		mUp = (ButtonUp) findViewById(R.id.btn_up);
	}

	@Override
	protected void initVariables() {
		mHandler = new Handler();
		Bundle bundle = this.getIntent().getExtras();
		mSpot = bundle.getParcelable(EXTRA_SPOT);
	}

	private void loadPhoto(JSONArray data) throws JSONException {
		for (int i = 0, size = data.length(); i < size; i++) {
			JSONObject row = data.getJSONObject(i);

			PhotoItem item = ParserUtils.parsePhoto(row);
			photoAdapter.add(item);
		}
	}

	private void loadReview(JSONArray data) throws JSONException {
		for (int i = 0, size = data.length(); i < size; i++) {
			JSONObject row = data.getJSONObject(i);
			ReviewItem item = ParserUtils.parseReview(row);
		}
	}

	private void loadSpotInfo(JSONObject result) throws JSONException {
		mSpot.setName(result.getString("name"));
		mSpot.setAddress(result.getString("address"));

		tvSpotName.setText(mSpot.getName());
		tvSpotAddress.setText(mSpot.getAddress());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkin:
			Intent intent = new Intent(this, ActivityCheckin.class);
			intent.putExtra("CURRENT_ADDRESS", "83B ly thuong kiet");
			intent.putExtra("CURRENT_LAT", 21.025347);
			intent.putExtra("CURRENT_LONG", 105.843755);
			startActivity(intent);
			break;

		case R.id.addreview:
			// Intent intentComment = new Intent(this, Checkin.class);
			// startActivity(intentComment);
			// intentComment.putExtra(Checkin.EXTRA_SPOT, mSpot);
			// startActivityForResult(intentComment, RequestCode.CHECKIN);
			break;

		case R.id.share:

			break;

		case R.id.like:
			LikeTask likeTask = new LikeTask(this, mSpot.getId());
			likeTask.setOnPostExecuteDelegate(this);
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
					uploadTask.setOnPostExecuteDelegate(this);
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
	public void actionPre(RestClientTask task) {
		if (null == mWaitingDialog) {
			mWaitingDialog = new WaitingDialog(this);
			mWaitingDialog.show();
		}
	}

	@Override
	public void actionPost(RestClientTask task, JSONObject result) {
		try {
			if (task instanceof GetSpotPhotoTask) {
				boolean status = result.getBoolean("status");

				if (status) {
					JSONArray data = result.getJSONArray("data");

					loadPhoto(data);
				}
			} else if (task instanceof GetSpotReviewTask) {
				boolean status = result.getBoolean("status");

				if (status) {
					JSONArray data = result.getJSONArray("data");

					loadReview(data);
				}
			} else if (task instanceof GetSpotTask) {
				mWaitingDialog.dismiss();
				mWaitingDialog = null;

				JSONObject spotInfo = result.getJSONObject("info");
				loadSpotInfo(spotInfo);
			} else if (task instanceof LikeTask) {
				boolean status = result.getBoolean("status");

				if (false == status) {
					int errorCode = result.getInt("error_code");
					NgonErrorDialog errorDialog = new NgonErrorDialog(this);
					int msg = 0;

					if (errorCode == ErrorCode.SPOT_ALREADY_LIKE) {
						msg = R.string.string_error_spot_already_like;
					}

					errorDialog.setMessage(msg);
					errorDialog.show();
				}
			} else if (task instanceof UploadPhotoTask) {

				if (mWaitingDialog != null) {
					mWaitingDialog.dismiss();
					mWaitingDialog = null;
				}

				boolean status = result.getBoolean("status");
				if (status) {
					JSONObject photoData = result.getJSONObject("data");
					PhotoItem photoItem = ParserUtils.parsePhoto(photoData);
					photoAdapter.add(photoItem);

				} else {

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initActions() {
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
}
