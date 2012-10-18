package com.zoostudio.adapter;

import java.util.Hashtable;

import com.zoostudio.ngon.ui.pager.NgonHomePager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class NgonPagerAdapter extends FragmentStatePagerAdapter  {
	protected Context mContext;

	private int mCount;
	private String[] titles;
	protected FragmentManager fm;
	private Hashtable<Integer, Fragment> mPageReferenceMap;
	public NgonPagerAdapter(Context context, FragmentManager fm, String[] titles) {
		super(fm);
		mCount = titles.length;
		mContext = context;
		this.fm = fm;
		mPageReferenceMap = new Hashtable<Integer, Fragment>();
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
		Fragment fragment=  PagerUtils.makeHomeTab(mContext,this.fm, position);
//		fragmentId.put(position, fragment.getTag());
		mPageReferenceMap.put(Integer.valueOf(position), fragment);
		return fragment;
	}

	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mPageReferenceMap.remove(Integer.valueOf(position));
	}
	
	public NgonHomePager getFragment(int position) {
		return (NgonHomePager) mPageReferenceMap.get(position);
	}
}
