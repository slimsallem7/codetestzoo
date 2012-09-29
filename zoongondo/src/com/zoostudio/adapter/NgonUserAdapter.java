package com.zoostudio.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class NgonUserAdapter extends NgonPagerAdapter {

	public NgonUserAdapter(Context context, FragmentManager fm, String[] titles) {
		super(context, fm, titles);
	}
	@Override
	public Fragment getItem(int position) {
		return PagerUtils.makeUserTab(mContext,this.fm, position);
	}
}
