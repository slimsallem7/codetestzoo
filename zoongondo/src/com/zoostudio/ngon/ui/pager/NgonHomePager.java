package com.zoostudio.ngon.ui.pager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoostudio.adapter.SpotAdapter;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.RequestCode;
import com.zoostudio.ngon.task.callback.OnSpotItemListener;
import com.zoostudio.ngon.ui.ActivityMapSpot;
import com.zoostudio.ngon.ui.AddSpotActivity;
import com.zoostudio.ngon.ui.base.BaseFragmentScreen;
import com.zoostudio.ngon.utils.Logger;
import com.zoostudio.ngon.views.NgonProgressView;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnDataErrorDelegate;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;
import com.zoostudio.service.impl.NgonLocationListener;
import com.zoostudio.service.impl.NgonLocationManager;

public abstract class NgonHomePager extends BaseFragmentScreen implements
		NgonLocationListener, OnSpotItemListener, OnDataErrorDelegate,
		OnPreExecuteDelegate {
	protected final static int LOADING_STATE = 0;
	protected final static int EMPTY_SATE = 1;
	protected final static int ERROR_SATE = 2;
	protected final static int ERROR_LOAD_MORE_SATE = 3;
	protected final static int LOADING_MORE_STATE = 4;
	protected final static int NORMAL_SATE = -1;

	private final static String KEY_STATE = "KEY STATE";
	private final static String KEY_ARRAY_SPOT = "KEY ARRAY SPOT";

	private int mState = NORMAL_SATE;
	protected SpotAdapter mAdapter;
	protected NgonProgressView mProgressBar;
	protected Button mRetry;
	protected TextView mMessage;
	protected RelativeLayout mFooterView;
	private NgonProgressView mProgressLoadMore;
	protected ArrayList<SpotItem> mDataBackup;
	private boolean mNeedShow;
	private View mLayoutAddSpot;
	protected ListView lvSpot;
	protected int mIndexPager;
	protected boolean mHasLoadData;
	protected LayoutInflater mInflater;
	protected boolean hasRequestLocation;
	private String mStringError;
	private String mStringEmpty;
	
	protected void initControls() {
		lvSpot = (ListView) findViewById(R.id.spotlist);
		mProgressBar = (NgonProgressView) findViewById(R.id.progressbar);
		mProgressBar.setAutoShow(false);
		mMessage = (TextView) findViewById(R.id.message);
		mRetry = (Button) findViewById(R.id.retry);
		mFooterView = (RelativeLayout) getLayoutInflater(null).inflate(
				R.layout.item_loading_more, null);
		mLayoutAddSpot = getLayoutInflater(null).inflate(
				R.layout.inc_footer_add_spot, null);
		mProgressLoadMore = (NgonProgressView) mFooterView
				.findViewById(R.id.ngonProgressLoadMore);
		mProgressLoadMore.setAutoShow(false);
		// lvSpot.addFooterView(mFooterView);
		lvSpot.addFooterView(mLayoutAddSpot);
		mFooterView.setVisibility(mNeedShow ? View.VISIBLE : View.GONE);
	}

	public void initVariables() {
		mDataBackup = new ArrayList<SpotItem>();
		mAdapter = new SpotAdapter(getActivity(), new ArrayList<SpotItem>(),
				null);
	}

	public NgonHomePager() {
		hasRequestLocation = false;
	}

	public NgonHomePager(Integer indexPager) {
		hasRequestLocation = false;
		this.mIndexPager = indexPager;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_STATE, mState);
		outState.putSerializable(KEY_ARRAY_SPOT, mDataBackup);
		Logger.e(this.getClass().getName(), "onSaveInstanceState");
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mInflater = inflater;
		mView = mInflater.inflate(R.layout.fragment_home_page, null);
		if (null != savedInstanceState) {
			mState = savedInstanceState.getInt(KEY_STATE);
			mDataBackup = (ArrayList<SpotItem>) savedInstanceState
					.getSerializable(KEY_ARRAY_SPOT);
		}
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
		mProgressLoadMore.stopAnim();
		for (int i = 0, n = mAdapter.getCount(); i < n; i++) {
			SpotItem item = mAdapter.getItem(i);
			mDataBackup.add(item);
		}
		mAdapter.clear();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i("" + this.getClass().getName(), "onStart");
	}

	/**
	 * ID của Fragment trong XML (Important)
	 * 
	 * @return
	 */
	protected abstract int getPagerIndex();

	@Override
	protected void initActions() {
		mRetry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshSpotItem();
			}
		});

		mLayoutAddSpot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mParent, AddSpotActivity.class);
				startActivityForResult(intent, RequestCode.ADD_SPOT);
			}
		});
	}

	public void onTabSelected() {
		mParent.setCurrentPager(getPagerIndex());
		if (mState == ERROR_SATE) {
			setUiLoadError();
		} else if (mState == EMPTY_SATE) {
			setUiLoadEmpty();
		} else if (mState == LOADING_STATE) {
			setUiLoading();
		}
		if (mNeedShow)
			mProgressLoadMore.startAnim();
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mStringError = activity
				.getString(R.string.lang_vi_spotlist_error_message);
		mStringEmpty = activity
				.getString(R.string.lang_vi_spotlist_nearby_empty_message);
	}

	protected void setUiLoading() {
		mRetry.setVisibility(View.GONE);
		mMessage.setText("");
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.startAnim();
	}

	protected void setUiLoadError() {
		mMessage.setText(mStringError);
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.VISIBLE);
		mProgressBar.stopAnim();
		mProgressBar.setVisibility(View.GONE);
	}

	protected void setUiLoadEmpty() {
		mMessage.setText(mStringEmpty);
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.stopAnim();
		mProgressBar.setVisibility(View.GONE);
	}

	protected void setUiLoadDone() {
		mNeedShow = true;
		mMessage.setText("");
		mMessage.setVisibility(View.GONE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.stopAnim();
		mProgressBar.setVisibility(View.GONE);
		if (mFooterView.getVisibility() == View.GONE) {
			mFooterView.setVisibility(View.VISIBLE);
		}
		mProgressLoadMore.startAnim();
	}

	@Override
	public void onSpotItemListener(ArrayList<SpotItem> data) {
		if (data.isEmpty()) {
			if (mState == LOADING_STATE) {
				setUiLoadEmpty();
				mState = EMPTY_SATE;
			}
			mState = NORMAL_SATE;
			return;
		}
		// //////////////////////////////////////////////////////
		mAdapter.clear();
		for (SpotItem spotItem : data) {
			mAdapter.add(spotItem);
			// TEST ACTIVITYMAPSPOT
			ActivityMapSpot.spotList.add(spotItem);
		}
		mAdapter.notifyDataSetChanged();
		// //////////////////////////////////////////////////////
		if (mState == LOADING_STATE) {
			setUiLoadDone();
		}
		mState = NORMAL_SATE;
	}

	@Override
	public synchronized void onActionDataError(RestClientTask task,
			int errorCode) {
		if (mState == LOADING_STATE) {
			mState = ERROR_SATE;
			setUiLoadError();

		} else if (mState == LOADING_MORE_STATE) {
			mState = ERROR_LOAD_MORE_SATE;
		}
	}

	protected void loadMoreSpotItem() {
		mState = LOADING_MORE_STATE;
	}

	protected void refreshSpotItem() {
		mState = LOADING_STATE;
		mFooterView.setVisibility(View.GONE);
	}

	@Override
	public void onActionPre(RestClientTask task) {
		if (mState == LOADING_STATE) {
			setUiLoading();
		}
	}
}
