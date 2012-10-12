package com.zoostudio.ngon.ui.pager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoostudio.adapter.SpotAdapter;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.task.callback.OnSpotItemListener;
import com.zoostudio.ngon.ui.base.BaseFragmentScreen;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnDataErrorDelegate;
import com.zoostudio.service.impl.NgonLocationListener;
import com.zoostudio.service.impl.NgonLocationManager;

public abstract class NgonHomePager extends BaseFragmentScreen implements
		NgonLocationListener, OnSpotItemListener, OnDataErrorDelegate {
	protected final static int NORMAL_STATE = 0;
	protected final static int EMPTY_SATE = 1;
	protected final static int ERROR_SATE = 2;
	protected int mState;
	protected SpotAdapter mAdapter;

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

	@Override
	protected void initActions() {
		if (mState == ERROR_SATE) {
			setUiLoadError();
		} else if (mState == EMPTY_SATE) {
			setUiLoadEmpty();
		}
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

	protected abstract void setUiLoadEmpty();

	protected abstract void setUiLoadError();

	@Override
	public void onSpotItemListener(ArrayList<SpotItem> data) {
		if (data.isEmpty()) {
			mState = EMPTY_SATE;
			setUiLoadEmpty();
			return;
		}

		mState = NORMAL_STATE;
		mAdapter.clear();
		for (SpotItem spotItem : data) {
			mAdapter.add(spotItem);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public synchronized void actionDataError(RestClientTask task, int errorCode) {
		mState = ERROR_SATE;
	}
}
