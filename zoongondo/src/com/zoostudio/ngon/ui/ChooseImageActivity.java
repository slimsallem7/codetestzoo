package com.zoostudio.ngon.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zoostudio.adapter.GalleryPagerAdapter;
import com.zoostudio.adapter.NgonGridMediaAdapter;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.utils.OnScanMediaListener;
import com.zoostudio.ngon.utils.Scanner;

public class ChooseImageActivity extends NgonActivity implements
		OnScanMediaListener, OnClickListener, OnItemClickListener,
		NgonGridMediaAdapter.OnItemSelectListener, OnCheckedChangeListener,
		OnPageChangeListener {
	public static final String MEDIA_SELECTED = "MEDIA SELECTED";
	private static final int CAPURE_IMAGE = 0;
	private GridView gridView;
	private NgonGridMediaAdapter adapter;
	private Scanner scanner;
	private ImageButton camera;
	private TextView mNumberMediaSelected;
	private int mNumberImage = 0;
	private ArrayList<MediaItem> allMedia;
	private ArrayList<MediaItem> seletedMedia;
	private RadioButton mRbtShowAllMedia;
	private RadioButton mRbtShowSeletedMedia;
	private ViewPager imagePager;
	private boolean isPagerShowing;
	private GalleryPagerAdapter galleryPagerAdapter;
	private ImageButton btnBackToGallery;
	private TextView mPagerIndex;
	private boolean isDataChange;
	private ImageButton btnCheckIn;

	@Override
	protected int setLayoutView() {
		return R.layout.activity_list_images;
	}

	@Override
	protected void initControls() {
		gridView = (GridView) findViewById(R.id.listImage);
		camera = (ImageButton) findViewById(R.id.btn_take_camera);
		btnCheckIn = (ImageButton) findViewById(R.id.btnSelected);
		mNumberMediaSelected = (TextView) findViewById(R.id.txtNumbersImage);
		mRbtShowAllMedia = (RadioButton) findViewById(R.id.showAllMedia);
		mRbtShowSeletedMedia = (RadioButton) findViewById(R.id.showSeletedMedia);
		imagePager = (ViewPager) findViewById(R.id.imagePager);
		mPagerIndex = (TextView) findViewById(R.id.pageIndex);
		int margin = getResources().getDimensionPixelSize(R.dimen.margin_pager);
		imagePager.setPageMargin(margin);
		btnBackToGallery = (ImageButton) findViewById(R.id.btnBackToGallery);
	}

	@Override
	protected void initVariables() {
		adapter = new NgonGridMediaAdapter(this.getApplicationContext(), 0,
				new ArrayList<MediaItem>());
		allMedia = new ArrayList<MediaItem>();
		seletedMedia = new ArrayList<MediaItem>();
		scanner = new Scanner(this);
		scanner.setOnBitmapListener(this);
		camera.setOnClickListener(this);
		imagePager.setOnPageChangeListener(this);
		btnCheckIn.setOnClickListener(this);
		mRbtShowAllMedia.setOnCheckedChangeListener(changeDataToAllMedia);
		mRbtShowSeletedMedia
				.setOnCheckedChangeListener(changeDataToSelectedMedia);
		btnBackToGallery.setOnClickListener(this);
		scanner.loadMedia();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void initActions() {
		adapter.setOnItemSelectListener(this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onScanFinished(final ArrayList<MediaItem> ids) {
		totalImage = ids.size();
		allMedia.clear();
		seletedMedia.clear();
		allMedia = ids;

		if (null == galleryPagerAdapter) {
			galleryPagerAdapter = new GalleryPagerAdapter(
					ChooseImageActivity.this, allMedia);
			imagePager.setAdapter(galleryPagerAdapter);
			galleryPagerAdapter.setOnItemSelectListener(this);
		} else {

		}
		if (!adapter.isEmpty()) {
			adapter.clear();
			adapter.notifyDataSetChanged();
		}
		for (MediaItem item : ids) {
			adapter.add(item);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		if (v == btnBackToGallery) {
			backToGridLayout();
		} else if (v == btnCheckIn) {
			Intent data = new Intent();
			data.putExtra(MEDIA_SELECTED, seletedMedia);
			this.setResult(RESULT_OK, data);
			this.finish();
		} else {
			Intent intent = new Intent(this, CameraActivity.class);
			startActivityForResult(intent, CAPURE_IMAGE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPURE_IMAGE && resultCode == RESULT_OK) {
			@SuppressWarnings("unchecked")
			ArrayList<MediaItem> mediaCaptured = (ArrayList<MediaItem>) data
					.getExtras().get(CameraActivity.IMAGE_IDS);

			mNumberImage += mediaCaptured.size();
			if (!mNumberMediaSelected.isShown())
				mNumberMediaSelected.setVisibility(View.VISIBLE);
			mNumberMediaSelected.setText("" + mNumberImage);

			for (MediaItem item : mediaCaptured) {
				adapter.insert(item, 0);
				allMedia.add(0, item);
				seletedMedia.add(item);
			}
			adapter.notifyDataSetChanged();
			galleryPagerAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		changeToPagerGallery();
		if (!imagePager.isShown()) {
			isPagerShowing = true;
			imagePager.setVisibility(View.VISIBLE);
		}
		imagePager.setCurrentItem(position);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(1000);
		imagePager.startAnimation(alphaAnimation);
	}

	/**
	 * Call back khi co 1 media duoc chon tu grid layout hoac tu gallery layout
	 */
	@Override
	public void onMediaChanged(boolean isChecked, long idMedia, MediaItem item) {
		if (isChecked) {
			mNumberImage++;
			seletedMedia.add(item);
		} else {
			mNumberImage--;
			removeFromSelected(idMedia);
		}
		if (mNumberImage > 0) {
			mNumberMediaSelected.setVisibility(View.VISIBLE);
			mNumberMediaSelected.setText("" + mNumberImage);
		} else {
			mNumberMediaSelected.setVisibility(View.INVISIBLE);
		}
		if (isPagerShowing) {
			isDataChange = true;
		}
	}

	/**
	 * Su dung de xoa mang cua cac media da chon Remove item from seleted media
	 * by idMedia
	 * 
	 * @param idMedia
	 */
	private void removeFromSelected(long idMedia) {
		MediaItem removeItem = null;
		for (MediaItem item : seletedMedia) {
			if (item.getIdMedia() == idMedia) {
				removeItem = item;
				break;
			}
		}
		if (null != removeItem)
			seletedMedia.remove(removeItem);
	}

	private OnCheckedChangeListener changeDataToAllMedia = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				buttonView.setTextColor(Color.parseColor("#FFFFFF"));
				adapter.clear();
				for (MediaItem item : allMedia) {
					adapter.add(item);
				}
				adapter.notifyDataSetChanged();
			} else {
				buttonView.setTextColor(Color.parseColor("#fc8884"));
			}
		}
	};

	private void changeToPagerGallery() {
		mRbtShowAllMedia.setVisibility(View.INVISIBLE);
		mRbtShowSeletedMedia.setVisibility(View.INVISIBLE);
		camera.setVisibility(View.INVISIBLE);
		mPagerIndex.setVisibility(View.VISIBLE);
		btnBackToGallery.setVisibility(View.VISIBLE);

	}

	private void changeToGridGallery() {
		mRbtShowAllMedia.setVisibility(View.VISIBLE);
		mRbtShowSeletedMedia.setVisibility(View.VISIBLE);
		mPagerIndex.setVisibility(View.GONE);
		camera.setVisibility(View.VISIBLE);
		btnBackToGallery.setVisibility(View.GONE);
	}

	private OnCheckedChangeListener changeDataToSelectedMedia = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				buttonView.setTextColor(Color.parseColor("#FFFFFF"));
				adapter.clear();
				for (MediaItem item : seletedMedia) {
					adapter.add(item);
				}
				adapter.notifyDataSetChanged();
			} else {
				buttonView.setTextColor(Color.parseColor("#fc8884"));
			}
		}
	};

	private int mCurrentPosition;
	private int totalImage;

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

	}

	@Override
	public void onBackPressed() {
		if (isPagerShowing) {
			backToGridLayout();
		} else {
			this.setResult(RESULT_CANCELED);
			this.finish();
		}
	}

	private void backToGridLayout() {
		if (isDataChange) {
			adapter.notifyDataSetChanged();
		}
		isDataChange = false;
		isPagerShowing = false;
		imagePager.setVisibility(View.GONE);
		gridView.setSelection(mCurrentPosition);
		changeToGridGallery();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		mCurrentPosition = position;
		mPagerIndex.setText("" + position + "/" + totalImage);
	}
}
