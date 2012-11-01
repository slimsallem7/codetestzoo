package com.zoostudio.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zoostudio.adapter.item.PhotoItem;
import com.zoostudio.android.image.SmartImageView;

public class SpotDetailImageAdapter extends PagerAdapter {
	private Context context;
	private ArrayList<PhotoItem> data;
	public SpotDetailImageAdapter(Context context, ArrayList<PhotoItem> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		SmartImageView imageView = new SmartImageView(context);
		imageView.setImageUrl(data.get(position).getPath());
		((ViewPager) collection).addView(imageView, 0);
		return imageView;
	}
	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((SmartImageView) view);
	}
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((SmartImageView) object);
	}

	@Override
	public int getCount() {
		return data.size();
	}
}
