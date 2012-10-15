package com.zoostudio.ngon.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zoostudio.adapter.NgonGridDishMediaAdapter;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.android.image.LocalImage;
import com.zoostudio.cropimage.CropImageActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.RequestCode;
import com.zoostudio.ngon.utils.OnScanMediaListener;
import com.zoostudio.ngon.utils.Scanner;

public class ChooseDishMediaActivity extends Activity implements
		OnScanMediaListener, OnItemClickListener {
	private GridView mGridMedia;
	private ArrayList<MediaItem> mMedia;
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
		mScanner.loadMedia();
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
		String mediaPath = mAdapter.getItem(position).getPathMedia();
		Intent intent = new Intent(getApplicationContext(),
				CropImageActivity.class);
		intent.putExtra(CropImageActivity.MEDIA_PATH, mediaPath);
		intent.putExtra("SOURCE", CropImageActivity.FROM_GALLERY);
		startActivityForResult(intent, RequestCode.CROP_IMAGE);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Long[] keys = new Long[mAdapter.getCount()];
		for (int i = 0, n = mAdapter.getCount(); i < n; i++) {
			keys[i] = mAdapter.getItem(i).getIdMedia();
		}
		LocalImage.clearMemory(keys);
	}
}
