package com.zoostudio.ngon.ui.pager;

import java.lang.reflect.InvocationTargetException;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.ui.base.BaseFragmentScreen;
import com.zoostudio.service.impl.NgonLocationListener;
import com.zoostudio.service.impl.NgonLocationManager;

public abstract class NgonHomePager extends BaseFragmentScreen implements
		NgonLocationListener {

	public abstract void initControls();

	public abstract void initVariables();

	protected int mIndexPager;
	protected boolean mHasLoadData;
	protected LayoutInflater mInflater;
	protected boolean hasRequestLocation;

	public NgonHomePager() {
		hasRequestLocation = false;
	}

	public NgonHomePager(Integer indexPager) {
		hasRequestLocation = false;
		this.mIndexPager = indexPager;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mInflater = inflater;
		mView = mInflater.inflate(R.layout.fragment_home_page, null);
		return mView;
	}

	@Override
	public void onStart() {
		super.onStart();
		mParent.setCurrentPager(getPagerIndex());
	}

	/**
	 * ID của Fragment trong XML (Important)
	 * 
	 * @return
	 */
	protected abstract int getPagerIndex();

	public void doTabSelected(int position) {
		onTabSelected(position);
	}

	protected void onTabSelected(int position) {
		mParent.setCurrentPager(getPagerIndex());
		if (hasRequestLocation)
			return;
		hasRequestLocation = true;
		NgonLocationManager.getInstance(mParent).requestLocation(this);
	}

	@Override
	public void onFailGetLocation() {
	}

	@Override
	public void onGettingLocation() {
	}

	@Override
	public void onLocationReceiver(final Location location) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				onLocationChanged(location);
			}
		});
	}

	public static <T extends NgonHomePager> T findOrCreateNgonPager(
			FragmentManager fm, String tag, int index, Class<T> typeClass) {
		@SuppressWarnings("unchecked")
		T fragment = (T) fm.findFragmentByTag(tag);
		if (fragment == null) {
			try {
				fragment = typeClass.getConstructor(Integer.class).newInstance(
						index);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (java.lang.InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return fragment;
	}

}
