package com.zoostudio.ngon.ui;

import java.util.ArrayList;

import android.support.v4.view.ViewPager;

import com.zoostudio.adapter.SpotDetailImageAdapter;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.adapter.item.PhotoItem;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;

public class ActivityImageDetailSpot extends NgonActivity {
	private ViewPager viewPager;
	private SpotDetailImageAdapter adapter;
	private ArrayList<PhotoItem> data;
	public final static String IMAGE_THUMB = "com.zoostudio.ngon.ui.ActivityImageDetailSpot.IMAGE_THUMB";
	@Override
	protected int setLayoutView() {
		return R.layout.activity_detail_image_spot;
	}

	@Override
	protected void initControls() {
		viewPager = (ViewPager) findViewById(R.id.imagePagerSpot);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initVariables() {
		data =  (ArrayList<PhotoItem>) getIntent().getExtras().getSerializable(IMAGE_THUMB);
		adapter = new SpotDetailImageAdapter(getApplicationContext(), data);
	}

	@Override
	protected void initActions() {
		viewPager.setAdapter(adapter);
	}

}
