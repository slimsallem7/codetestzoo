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

public class ChooseDishMediaActivity extends Activity implements
		OnScanMediaListener, OnItemClickListener, OnClickListener {
	private GridView mGridMedia;
	private Scanner mScanner;
	private NgonGridDishMediaAdapter mAdapter;
	private ImageButton mCamera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_dish_media);
		mGridMedia = (GridView) findViewById(R.id.listImage);
		mCamera = (ImageButton) findViewById(R.id.btn_take_camera);
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
		Intent intent = new Intent(getApplicationContext(),
				CropImageActivity.class);
		intent.putExtra(CropImageActivity.MEDIA_ITEM,
				mAdapter.getItem(position));
		intent.putExtra("SOURCE", CropImageActivity.FROM_GALLERY);
		startActivity(intent);
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
				CameraForSquareActivity.class);
		startActivity(intent);
	}
}
