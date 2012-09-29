package com.zoostudio.ngon.ui;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.zoostudio.adapter.item.DishItem;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.android.image.ZooImageDishBorder;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonDialog;
import com.zoostudio.ngon.dialog.NgonDialog.Builder;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.ngon.views.HorizontalPager;
import com.zoostudio.ngon.views.VerticalImageThumbView;

public class Checkin extends MapActivity implements
		HorizontalPager.OnItemChangeListener,
		HorizontalPager.OnScreenSwitchListener,
		android.view.View.OnClickListener {
	private MapView mapView;
	private HorizontalPager pagerDish;
	private MapController mapControl;
	private GeoPoint mMeGeoPoint;
	private VerticalImageThumbView mImageThumbViews;
	private static final int CHOOSE_DISH = 0;
	private static final int REQUEST_MEDIA = 1;
	private CheckBox mShareFacebook;
	private CheckBox mShareTwitter;
	private CheckBox mShareTumbler;

	private ButtonUp mUp;
	private ArrayList<DishItem> mDishseOriginal;
	private ArrayList<DishItem> mDishseSelected;
	private TextView lblDishSelected;
	private String lblDishCount;
	private String lblDish;
	private StringBuilder builder;
	private View incView;
	private ImageButton btnTakePhoto;
	private TextView pickImageFromGallery;
	private TextView pickImageFromCamera;
	private ArrayList<MediaItem> mMediaSelected;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.activity_checkin);
		initControls();
		initVariables();
		initActions();
	}

	private void initControls() {
		pagerDish = (HorizontalPager) findViewById(R.id.dishPager);
		mapView = (MapView) findViewById(R.id.mapView);
		btnTakePhoto = (ImageButton) findViewById(R.id.take_photo);
		mImageThumbViews = (VerticalImageThumbView) findViewById(R.id.taken_photos);
		mImageThumbViews.initViews();
		mapControl = mapView.getController();
		lblDish = this.getResources().getString(R.string.comment_label_dish);
		lblDishSelected = (TextView) this.findViewById(R.id.select_food);
		mShareFacebook = (CheckBox) findViewById(R.id.share_facebook);
		mShareTwitter = (CheckBox) findViewById(R.id.share_twitter);
		mShareTumbler = (CheckBox) findViewById(R.id.share_tumblr);
		pagerDish.setOnItemClick(this);
		btnTakePhoto.setOnClickListener(this);
		pagerDish.setOnScreenSwitchListener(this);
		lblDishSelected.setOnClickListener(this);
		mUp = (ButtonUp) findViewById(R.id.btn_up);
	}

	private void initVariables() {
		mDishseSelected = new ArrayList<DishItem>();
		mDishseOriginal = new ArrayList<DishItem>();
		builder = new StringBuilder(1024);
		lblDishCount = this.getResources().getString(
				R.string.comment_dish_count);
		lblDish = this.getResources().getString(R.string.comment_label_dish);
	}

	private void initActions() {
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
				Checkin.this.startActivity(intent);
			}
		});

		if (null == mMeGeoPoint) {
			mMeGeoPoint = new GeoPoint((int) (21.025347 * 1E6),
					(int) (105.843755 * 1E6));
		}

		mapControl.setCenter(mMeGeoPoint);
		mapControl.setZoom(16);
		mapControl.animateTo(mMeGeoPoint);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();

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
}
