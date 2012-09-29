package com.zoostudio.ui.manager;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class ScreenManager {
	public static final String HOME_SCREEN = "Home Screen";
	public static final String SPOT_DETAILS = "Spot Details";

	private final FragmentActivity mActivity;
	private final int mContainerId;
	private final HashMap<String, ScreenInfo> mTabs = new HashMap<String, ScreenInfo>();
	ScreenInfo mLastScreen;

	public ScreenManager(FragmentActivity activity, int containerId) {
		mActivity = activity;
		mContainerId = containerId;
	}

	public void addScreen(Class<?> clss, Bundle args, String mScreenName) {
		ScreenInfo info = new ScreenInfo(mScreenName, clss, args);

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		info.mFragment = mActivity.getSupportFragmentManager()
				.findFragmentByTag(mScreenName);
		if (info.mFragment != null && !info.mFragment.isDetached()) {
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
			ft.detach(info.mFragment);
			ft.commit();
		}
		mTabs.put(mScreenName, info);
	}

	public void showScreen(String screenName) {
		ScreenInfo newTab = mTabs.get(screenName);
		if (mLastScreen != newTab) {
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();

			if (mLastScreen != null) {
				if (mLastScreen.mFragment != null) {
					ft.detach(mLastScreen.mFragment);
				}
			}

			if (newTab != null) {
				if (newTab.mFragment == null) {
					newTab.mFragment = Fragment.instantiate(mActivity,
							newTab.mClass.getName(), newTab.mBundle);
					ft.add(mContainerId, newTab.mFragment, newTab.mScreenName);
				} else {
					ft.attach(newTab.mFragment);
				}
			}
			mLastScreen = newTab;
			ft.commit();
			mActivity.getSupportFragmentManager().executePendingTransactions();
		}
	}

	public class ScreenInfo {
		private Fragment mFragment;
		private String[] mSubScreen;
		private boolean mIsSubShowing = false;
		private String mScreenName;
		private Bundle mBundle;
		private Class mClass;

		public ScreenInfo(String _ScreenName, Class<?> _class, Bundle _args,
				String... subScreen) {
			this.mScreenName = _ScreenName;
			this.mClass = _class;
			this.mBundle = _args;
			this.mSubScreen = subScreen;
		}
	}

}