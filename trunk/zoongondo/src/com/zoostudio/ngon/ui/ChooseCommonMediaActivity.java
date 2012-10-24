package com.zoostudio.ngon.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;

import com.zoostudio.adapter.NgonGridDishMediaAdapter;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.cropimage.CropImageActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.utils.OnScanMediaListener;
import com.zoostudio.ngon.utils.Scanner;
/**
 * Can chu y class nay : 
 * Neu khong truyen vao Bundle RETURN_WITH_RESULT
 * Thi sau khi pick anh se tu dong sang Activity Crop anh
 * Muc dich de phuc vu cho viec add Dish (IMPORTANT):
 * -NEN FIX LAI CO CHE SAU NAY. 
 * HIEN TAI DANG BI FIX CUNG CHO Activity Add Dish
 * @author VIETBQ
 *
 */
public class ChooseCommonMediaActivity extends Activity implements
		OnScanMediaListener, OnItemClickListener, OnClickListener {
	private GridView mGridMedia;
	private Scanner mScanner;
	private NgonGridDishMediaAdapter mAdapter;
	private ImageButton mCamera;
	private boolean mNeedReturn;
	public static final String RETURN_WITH_RESULT = "com.zoostudio.ngon.ui.ChooseDishMediaActivity.RETURN_WITH_RESULT";
	public static final String MEDIA_PICKED = "com.zoostudio.ngon.ui.ChooseDishMediaActivity.MEDIA_PICKED";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_dish_media);
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			mNeedReturn = bundle.getBoolean(RETURN_WITH_RESULT, false);
		}

		mGridMedia = (GridView) findViewById(R.id.listImage);

		mCamera = (ImageButton) findViewById(R.id.btn_take_camera);
		if (mNeedReturn)
			mCamera.setVisibility(View.INVISIBLE);

		mScanner = new Scanner(this);
		mScanner.setOnScanMediaListener(this);

		mAdapter = new NgonGridDishMediaAdapter(this.getApplicationContext(),
				0, new ArrayList<MediaItem>());
		mGridMedia.setAdapter(mAdapter);
		mGridMedia.setOnItemClickListener(this);
		mCamera.setOnClickListener(this);
		mScanner.loadMedia();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		MediaItem item = (MediaItem) intent.getExtras().getSerializable(
				CropImageActivity.MEDIA_CALL_BACK);
		mAdapter.insert(item, 0);
	}

	@Override
	public void onScanFinished(ArrayList<MediaItem> media) {
		for (MediaItem item : media) {
			mAdapter.add(item);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		MediaItem item = mAdapter.getItem(position);
		if (mNeedReturn) {
			Intent intent = new Intent();
			intent.putExtra(MEDIA_PICKED, item);
			setResult(RESULT_OK, intent);
			finish();
		} else {
			Intent intent = new Intent(getApplicationContext(),
					CropImageActivity.class);
			intent.putExtra(CropImageActivity.MEDIA_ITEM, item);
			intent.putExtra("SOURCE", CropImageActivity.FROM_GALLERY);
			startActivity(intent);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		SmartImageView.cancelAllTasks();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getApplicationContext(),
				ZooCameraCommonActivity.class);
		startActivity(intent);
	}
}
