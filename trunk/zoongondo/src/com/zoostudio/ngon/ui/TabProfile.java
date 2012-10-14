package com.zoostudio.ngon.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.viewpagerindicator.TitlePageIndicator;
import com.zoostudio.adapter.NgonUserAdapter;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.ui.pager.NgonUserPager;

public class TabProfile extends FragmentActivity implements OnPageChangeListener {
	private TitlePageIndicator mTitlePageIndicator;
	private ViewPager mTabsPager;
	private NgonUserAdapter mUserAdapter;
	private String titlesTabUserProfile[];
	private StringBuilder mBuilderTab;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_pager);
		mBuilderTab = new StringBuilder(256);
		titlesTabUserProfile = getResources().getStringArray(
				R.array.titles_tab_user_profile_screen);
		mUserAdapter = new NgonUserAdapter(getParent(),
				getSupportFragmentManager(), titlesTabUserProfile);
		mTitlePageIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		mTabsPager = (ViewPager) findViewById(R.id.content_pager);
		mTabsPager.setAdapter(mUserAdapter);
		mTitlePageIndicator.setViewPager(mTabsPager);
		mTitlePageIndicator.setOnPageChangeListener(this);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	
	@Override
	public void onPageSelected(int position) {
		NgonUserPager fragment = getFragmentPager(position);
		fragment.onTabSelected(position);
	}
	
	private NgonUserPager getFragmentPager(int position) {
		mBuilderTab.append("android:switcher:").append(R.id.content_pager)
				.append(":").append(position);
		NgonUserPager fragment = (NgonUserPager) getSupportFragmentManager()
				.findFragmentByTag(mBuilderTab.toString());
		mBuilderTab.delete(0, mBuilderTab.length());
		return fragment;
	}
}
