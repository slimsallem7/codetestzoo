package com.zoostudio.ngon.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
import com.facebook.android.SupportLoginFacebook;
import com.facebook.android.Utility;
import com.twitter.android.OnTwitterListener;
import com.twitter.android.TwitterSupport;
import com.zoostudio.adapter.item.DishItem;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.android.image.ZooImageDishBorder;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonDialog;
import com.zoostudio.ngon.dialog.NgonDialog.Builder;
import com.zoostudio.ngon.ui.base.BaseMapActivity;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.ngon.views.HorizontalPager;
import com.zoostudio.ngon.views.VerticalImageThumbView;

public class ActivityCheckin extends BaseMapActivity implements
		HorizontalPager.OnItemChangeListener,
		HorizontalPager.OnScreenSwitchListener,
		android.view.View.OnClickListener, OnTwitterListener {
	private HorizontalPager pagerDish;
	private VerticalImageThumbView mImageThumbViews;
	private static final int CHOOSE_DISH = 0;
	private static final int REQUEST_MEDIA = 1;
	private final static int AUTHORIZE_FACEBOOK = 2;

	public static final String APP_ID = "254841481305890";
	private static final String[] PERMISSIONS = new String[] {
			"publish_stream", "photo_upload", "offline_access",
			"publish_checkins" };
	private CheckBox mShareFacebook;
	private CheckBox mShareTwitter;
	private CheckBox mShareTumbler;

	private ButtonUp mUp;
	private ArrayList<DishItem> mDishseOriginal;
	private ArrayList<DishItem> mDishseSelected;
	private TextView lblDishSelected;
	private String lblDishCount;
	private TextView mAddressMap;
	private String lblDish;
	private StringBuilder builder;
	private View incView;
	private ImageButton btnTakePhoto;
	private TextView pickImageFromGallery;
	private TextView pickImageFromCamera;
	private ArrayList<MediaItem> mMediaSelected;
	private Button btnCheckIn;
	private EditText mEditWriteReview;
	private boolean checkFB;

	private SupportLoginFacebook supportLoginFacebook;
	private TwitterSupport twitterSupport;
	private boolean checkTW;
	private final static String CALL_BACK_URL = "zoostudio-ngon-do-checkin://callback";
	private final static String CALL_BACK_SCHEME = "zoostudio-ngon-do-checkin";

	@Override
	protected int getLayoutId() {
		return R.layout.activity_checkin;
	}

	protected void initControls() {
		super.initControls();
		mEditWriteReview = (EditText) findViewById(R.id.write_review);
		mAddressMap = (TextView) findViewById(R.id.maptitle);
		pagerDish = (HorizontalPager) findViewById(R.id.dishPager);
		btnTakePhoto = (ImageButton) findViewById(R.id.take_photo);
		mImageThumbViews = (VerticalImageThumbView) findViewById(R.id.taken_photos);
		mImageThumbViews.initViews();
		lblDish = this.getResources().getString(R.string.comment_label_dish);
		lblDishSelected = (TextView) this.findViewById(R.id.select_food);
		btnCheckIn = (Button) findViewById(R.id.checkin);
		mShareFacebook = (CheckBox) findViewById(R.id.share_facebook);
		mShareTwitter = (CheckBox) findViewById(R.id.share_twitter);
		mShareTumbler = (CheckBox) findViewById(R.id.share_tumblr);
		pagerDish.setOnItemClick(this);
		btnTakePhoto.setOnClickListener(this);
		pagerDish.setOnScreenSwitchListener(this);
		lblDishSelected.setOnClickListener(this);
		mUp = (ButtonUp) findViewById(R.id.btn_up);
	}

	private void initShare() {
		twitterSupport = new TwitterSupport(getApplicationContext(), this);
		// Create the Facebook Object using the app id.
		Utility.mFacebook = new Facebook(APP_ID);
		// Instantiate the asynrunner object for asynchronous api calls.
		Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
		// restore session if one exists
		SessionStore.restore(Utility.mFacebook, this);
		SessionEvents.addAuthListener(new FbAPIsAuthListener());
		SessionEvents.addLogoutListener(new FbAPIsLogoutListener());
		supportLoginFacebook = new SupportLoginFacebook();
		supportLoginFacebook.init(this, AUTHORIZE_FACEBOOK, Utility.mFacebook,
				PERMISSIONS);

		if (Utility.mFacebook.isSessionValid()) {
			checkFB = true;
			mShareFacebook.setChecked(true);
		}
		if (twitterSupport.validSession()) {
			mShareTwitter.setChecked(true);
			checkTW = true;
		}
	}

	protected void initVariables() {
		super.initVariables();
		mDishseSelected = new ArrayList<DishItem>();
		mDishseOriginal = new ArrayList<DishItem>();
		builder = new StringBuilder(1024);
		lblDishCount = this.getResources().getString(
				R.string.comment_dish_count);
		lblDish = this.getResources().getString(R.string.comment_label_dish);
		mAddressMap.setText(mCurrentAddress);

		initShare();
	}

	protected void initActions() {
		super.initActions();

		pagerDish.setOnItemClick(this);

		mUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		mImageThumbViews.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ViewPhotoActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				ActivityCheckin.this.startActivity(intent);
			}
		});
		btnCheckIn.setOnClickListener(this);
		mShareFacebook.setOnCheckedChangeListener(facebookCheckListener);
		mShareTwitter.setOnCheckedChangeListener(twitterCheckListener);
	}

	/*
	 * Khong duoc xoa(non-Javadoc)
	 * 
	 * @see com.zoostudio.ngon.ui.base.BaseMapActivity#watcherAddress()
	 */
	@Override
	protected void watcherAddress() {

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("Checkin", "OnResume");
	}

	@Override
	public void onNewIntent(Intent newIntent) {
		super.onNewIntent(newIntent);
		twitterSupport.authorizeCallBack(newIntent, CALL_BACK_SCHEME);
	}

	@Override
	public void onItemClick() {
		Intent intent = new Intent(this, ChooseDish.class);
		intent.putExtra("LIST_DISH", mDishseOriginal);
		intent.putExtra("LIST_SELECTED", mDishseSelected);
		startActivityForResult(intent, CHOOSE_DISH);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CHOOSE_DISH) {
			if (resultCode == RESULT_OK) {
				pagerDish.resetViews();
				Bundle extras = data.getExtras();
				mDishseSelected = (ArrayList<DishItem>) extras
						.getSerializable("LIST_SELECTED");
				lblDishSelected.setText(lblDishCount + " "
						+ mDishseSelected.size() + " " + lblDish);
				mDishseOriginal = (ArrayList<DishItem>) extras
						.getSerializable("LIST_DISH");
				pagerDish.setData(mDishseSelected);
			}
		} else if (requestCode == REQUEST_MEDIA) {
			if (resultCode == RESULT_CANCELED)
				return;
			Bundle bundle = data.getExtras();
			mMediaSelected = (ArrayList<MediaItem>) bundle
					.getSerializable(ChooseImageActivity.MEDIA_SELECTED);
			mImageThumbViews.clearData();
			mImageThumbViews.setData(mMediaSelected);
		} else {
			Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
		}

	}

	@Override
	public void onScreenSwitched(int screen) {
	}

	@Override
	public void onItemShowPopup(View view, String title, String urlImage) {
		incView = this.getLayoutInflater().inflate(
				R.layout.inc_dialog_delete_photo, null);
		TextView textView = (TextView) incView.findViewById(R.id.txtLabelDish);
		textView.setText(title);
		if (null != urlImage && !urlImage.equals("")) {
			SmartImageView imageView = (SmartImageView) incView
					.findViewById(R.id.imgLargeDish);
			// imageView.setImageUrl(urlImage);
			imageView.setImageResource(R.drawable.sampe_larg_image_dish);
		}
		Builder dialog = new NgonDialog.Builder(this);
		dialog.setCancelable(true);
		dialog.setInnerCustomView(incView);
		dialog.setMessage(R.string.dialog_exit_msg);
		dialog.setPositiveButton(R.string.dialog_delete_dish,
				new onUnSelectDish(view));
		dialog.setNegativeButton(R.string.string_close,
				new onUnSelectDish(view));
		dialog.show();
	}

	private class onUnSelectDish implements OnClickListener {
		private View view;

		public onUnSelectDish(View view) {
			this.view = view;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				dialog.dismiss();
				((ZooImageDishBorder) view).remove();
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnTakePhoto) {
			showDialogChooseImage();
		} else if (v == pickImageFromCamera) {

		} else if (v == pickImageFromGallery) {

		} else if (v == btnCheckIn) {
			if (checkFB) {
				postStatus();
			}
			if (checkTW) {
				postTwitter();
			}
		} else {
			Intent intent = new Intent(this, ChooseDish.class);
			intent.putExtra("LIST_DISH", mDishseOriginal);
			intent.putExtra("LIST_SELECTED", mDishseSelected);
			startActivityForResult(intent, CHOOSE_DISH);
		}
	}

	@Override
	public void onItemUnSelect(DishItem dishItem) {
		mDishseSelected.remove(dishItem);
		String dishId = dishItem.getDishId();
		for (int i = 0, n = mDishseOriginal.size(); i < n; i++) {
			if (mDishseOriginal.get(i).getDishId().equals(dishId)) {
				mDishseOriginal.get(i).setSelected(false);
			}
		}

		int n = mDishseSelected.size();
		if (n > 0) {
			builder.append(lblDishCount).append(" ")
					.append(mDishseSelected.size()).append(" ").append(lblDish);
			lblDishSelected.setText(builder.toString());
			builder.delete(0, builder.length());
		} else {
			lblDishSelected.setText(this.getResources().getString(
					R.string.comment_eat_dish));
		}
	}

	private void showDialogChooseImage() {
		Intent intent = new Intent(this, ChooseImageActivity.class);
		startActivityForResult(intent, REQUEST_MEDIA);
	}

	@Override
	protected int getEditAddressId() {
		return 0;
	}

	@Override
	protected int getButtonGetLocationId() {
		return 0;
	}

	@Override
	protected int getMapViewId() {
		return R.id.mapView;
	}

	private OnCheckedChangeListener facebookCheckListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked && Utility.mFacebook.isSessionValid()) {
				checkFB = true;
			} else if (isChecked) {
				supportLoginFacebook.logOnFacebook();
			} else {
				checkFB = false;
//				supportLoginFacebook.logOutFacebook();
			}
		}
	};

	private OnCheckedChangeListener twitterCheckListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked && twitterSupport.validSession()) {
				checkTW = true;
			} else if (isChecked) {
				twitterSupport.logonTwitter(CALL_BACK_URL, ActivityCheckin.this);
			} else {
				checkTW = false;
//				twitterSupport.logoutTwitter(Checkin.this.getApplicationContext());
			}
		}
	};

	/*
	 * The Callback for notifying the application when log out starts and
	 * finishes.
	 */
	public class FbAPIsLogoutListener implements LogoutListener {
		@Override
		public void onLogoutBegin() {

		}

		@Override
		public void onLogoutFinish() {
			mShareFacebook.setChecked(false);
		}
	}

	/*
	 * The Callback for notifying the application when authorization succeeds or
	 * fails.
	 */

	public class FbAPIsAuthListener implements AuthListener {
		@Override
		public void onAuthSucceed() {
			checkFB = true;
		}

		@Override
		public void onAuthFail(String error) {
			checkFB = false;
			mShareFacebook.setChecked(false);
		}
	}

	/*
	 * callback for the photo upload
	 */
	public class PhotoUploadListener extends BaseRequestListener {

		@Override
		public void onComplete(final String response, final Object state) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// new UploadPhotoResultDialog(Checkin.this,
					// "Upload Photo executed", response).show();
					Toast.makeText(getApplicationContext(), "Post Done !!!",
							Toast.LENGTH_SHORT).show();
				}
			});
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(getApplicationContext(),
					"Facebook Error: " + error.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	@SuppressWarnings("unused")
	private void postPhoto(MediaItem item) {
		try {
			Bundle params = new Bundle();
			Uri photoUri = Uri.fromFile(new File(item.getPathMedia()));
			params.putByteArray("photo",
					Utility.scaleImage(getApplicationContext(), photoUri, item));
			params.putString("caption", mEditWriteReview.getText().toString());
			params.putString("place", "252812541444808");
			// JSONObject coordinates = new JSONObject();
			// coordinates.put("latitude", mCurrentLat);
			// coordinates.put("longitude", mCurrentLong);
			// params.putString("coordinates",coordinates.toString());
			Utility.mAsyncRunner.request("me/photos", params, "POST",
					new PhotoUploadListener(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void postStatus() {
		String mess = mEditWriteReview.getText().toString();
		Bundle params = new Bundle();
		params.putString("name", "Chipa - Chipa");
		params.putString("message", mess);
		params.putString(
				"picture",
				"http://nr7.upanh.com/b3.s30.d2/8d82b2bf380bb202d65cbf224eeed4b3_49687847.untitled5.jpg");
		params.putString("link", "http://ngon.do/spot/1");
		String dish = "";
		for (DishItem item : mDishseSelected) {
			dish += item.getTitle();
			dish += " \r\n";
		}
		params.putString("description", dish);
		Utility.mAsyncRunner.request("me/feed", params, "POST",
				new PhotoUploadListener(), null);
	}

	@Override
	public void onAuthTwitterSuccess() {
		checkTW = true;
	}

	@Override
	public void onErrorTwitter() {
		mShareTwitter.setChecked(false);
	}

	@Override
	public void onLogoutTwitter() {
		mShareTwitter.setChecked(false);
	}

	@Override
	public void onLogoutTwitterError() {

	}

	@Override
	public void onUpdateTwitterFinish() {
		Toast.makeText(getApplicationContext(), "Post Twitter Done",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpdateTwitterError() {
		Toast.makeText(getApplicationContext(), "Post Twitter Error",
				Toast.LENGTH_SHORT).show();
	}

	private void postTwitter() {
		String mess = mEditWriteReview.getText().toString();
		twitterSupport.postStatus(mess, null);
	}
}
