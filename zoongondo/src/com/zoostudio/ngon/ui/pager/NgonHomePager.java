package com.zoostudio.ngon.ui.pager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoostudio.adapter.SpotAdapter;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.task.callback.OnSpotItemListener;
import com.zoostudio.ngon.ui.base.BaseFragmentScreen;
import com.zoostudio.ngon.views.NgonProgressView;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnDataErrorDelegate;
import com.zoostudio.service.impl.NgonLocationListener;
import com.zoostudio.service.impl.NgonLocationManager;

public abstract class NgonHomePager extends BaseFragmentScreen implements
		NgonLocationListener, OnSpotItemListener, OnDataErrorDelegate {
	protected final static int LOADING_STATE = 0;
	protected final static int EMPTY_SATE = 1;
	protected final static int ERROR_SATE = 2;
	protected final static int NORMAL_SATE = -1;
	private int mState = NORMAL_SATE;
	protected SpotAdapter mAdapter;
	protected NgonProgressView mProgressBar;
	protected Button mRetry;
	protected TextView mMessage;
	protected RelativeLayout mFooterView;

	protected void initControls() {
		mProgressBar = (NgonProgressView) findViewById(R.id.progressbar);
		mMessage = (TextView) findViewById(R.id.message);
		mRetry = (Button) findViewById(R.id.retry);
		mFooterView = (RelativeLayout) getLayoutInflater(null).inflate(
				R.layout.item_loading_more, null);
		mFooterView.setVisibility(View.GONE);

	}

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
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.i("Pager", "onHiddenChanged =" + hidden);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.i("Pager", "onDestroyView");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i("Pager", "onStart");
		mParent.setCurrentPager(getPagerIndex());
	}

	/**
	 * ID của Fragment trong XML (Important)
	 * 
	 * @return
	 */
	protected abstract int getPagerIndex();
	
	@Override
	protected void initActions() {
	}

	public void onTabSelected(int position) {
		Log.i(""+this.getClass().getName(),"State = " +mState);
		if (mState == ERROR_SATE) {
			setUiLoadError();
		} else if (mState == EMPTY_SATE) {
			setUiLoadEmpty();
		} else if (mState == LOADING_STATE) {
			setUiLoading();
		}
		mParent.setCurrentPager(getPagerIndex());
		if (!hasRequestLocation) {
			hasRequestLocation = true;
			NgonLocationManager.getInstance(mParent).requestLocation(this);
		}
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

	protected void setUiLoading() {
		mState = LOADING_STATE;
		mRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	protected void setUiLoadError() {
		mState = ERROR_SATE;
		mMessage.setText(getString(R.string.lang_vi_spotlist_error_message));
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	protected void setUiLoadEmpty() {
		mState = EMPTY_SATE;
		mMessage.setText(getString(R.string.lang_vi_spotlist_nearby_empty_message));
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
	}

	protected void setUiLoadDone() {
		mState = NORMAL_SATE;
		mMessage.setText("");
		mMessage.setVisibility(View.GONE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
		if (mFooterView.getVisibility() == View.GONE) {
			mFooterView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onSpotItemListener(ArrayList<SpotItem> data) {
		if (data.isEmpty()) {
			setUiLoadEmpty();
			return;
		}
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
