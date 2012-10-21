package com.zoostudio.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.zoostudio.ngon.ui.pager.MyProfilePager;
import com.zoostudio.ngon.ui.pager.NearByPager;
import com.zoostudio.ngon.ui.pager.TopLikePager;
import com.zoostudio.ngon.ui.pager.TopNewPager;

public class PagerUtils {
	public final static int TOP_NEW = 0;
	public final static int NEAR_BY = 1;
	public final static int TOP_LIKE = 2;

	public final static int CHECKED_IN = 0;
	public final static int FRIENDS = 1;
	public final static int INDIVIDUAL = 2;

	public static Fragment makeHomeTab(Context context, FragmentManager fm,
			int position) {
		Fragment fragment = null;
//		StringBuilder builder = new StringBuilder();
//		builder.append("android:switcher:").append(R.id.indicator).append(":")
//				.append(position);
//		String tag = builder.toString();

		switch (position) {
		case TOP_NEW:
			fragment = new TopNewPager(TOP_NEW);
			break;
		case NEAR_BY:
			fragment = new NearByPager(NEAR_BY);
			break;
		case TOP_LIKE:
			fragment = new TopLikePager(TOP_LIKE);
			break;
		}
		return fragment;
	}

	public static Fragment makeUserTab(Context context, FragmentManager fm,
			int position) {
		Fragment fragment = null;
//		StringBuilder builder = new StringBuilder();
//		builder.append("android:switcher:").append(R.id.indicator).append(":")
//				.append(position);
//		String tag = builder.toString();

		switch (position) {
//		case CHECKED_IN:
//			fragment = new CheckedInPager();
//			break;
//		case FRIENDS:
//			fragment = new FriendPager();
//			break;
//		case INDIVIDUAL:
//			fragment = new MyProfilePager();
//			break;
		default:
			fragment = new MyProfilePager();
			break;
		}
		return fragment;
	}
}
