package com.zoostudio.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NgonPagerAdapter extends FragmentPagerAdapter  {
	protected Context mContext;

	private int mCount;
	private String[] titles;
	protected FragmentManager fm;

	public NgonPagerAdapter(Context context, FragmentManager fm, String[] titles) {
		super(fm);
		mCount = titles.length;
		mContext = context;
		this.fm = fm;
		this.titles = titles;
	}
	
	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position].toUpperCase();
	}
	
	@Override
	public Fragment getItem(int position) {
		return PagerUtils.makeHomeTab(mContext,this.fm, position);
	}
}
