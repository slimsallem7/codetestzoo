package com.zoostudio.ngon.ui.base;

import java.util.ArrayList;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class HostFragmentBase extends BaseFragmentScreen {

	protected FragmentManager mSubFragmentManager;
	protected int mCurrentSubFragment;
	protected ArrayList<Integer> mPagerIndex;
	private boolean isFirtTimeLoad = true;

	@Override
	protected void initControls() {

	}

	@Override
	protected void initVariables() {
		mPagerIndex = new ArrayList<Integer>();
	}

	public void setSubFragmentManager(FragmentManager fragmentManager,
			int currentSubFragment) {
		this.mSubFragmentManager = fragmentManager;
		if (!mPagerIndex.contains(currentSubFragment)) {
			mPagerIndex.add(currentSubFragment);
		}
		if (isFirtTimeLoad) {
			isFirtTimeLoad = false;
			this.mCurrentSubFragment = currentSubFragment;
		}
	}

	public void setCurrentSubFragment(int currentSubFragment) {
		this.mCurrentSubFragment = currentSubFragment;
	}

	@Override
	public void onFragmentResult(Bundle bundle, int requestCode, int resultCode) {
		super.onFragmentResult(bundle, requestCode, resultCode);
		BaseFragmentScreen fragment = (BaseFragmentScreen) mSubFragmentManager
				.findFragmentById(mCurrentSubFragment);
		fragment.onFragmentResult(bundle, requestCode, resultCode);
	}

	@Override
	public void onLocationChanged(final Location location) {
		super.onLocationChanged(location);
		final BaseFragmentScreen fragment = (BaseFragmentScreen) mSubFragmentManager
				.findFragmentById(mCurrentSubFragment);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				fragment.onLocationChanged(location);
			}
		});
	}

	public void showDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// DialogFragment newFragment = PickImageDialog.newInstance();
		// newFragment.show(ft, "dialog");
	}
}
