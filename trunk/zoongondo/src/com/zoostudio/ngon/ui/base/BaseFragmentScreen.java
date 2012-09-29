package com.zoostudio.ngon.ui.base;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

public abstract class BaseFragmentScreen extends Fragment {

	protected String Tag = "BaseFragmentScreen";

	public static final String REQUEST_CODE = "Request Code";

	protected static final String PARENT_FRAGMENT = "Parent Fragment";

	protected BaseFragmentActivity mParent;

	protected int mRequestCode = -1;

	protected View mView;

	protected boolean mStartFragmentOnResult = false;

	protected Handler mHandler;

	private boolean didInit = false;
	
	public BaseFragmentScreen() {
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		if (!didInit) {
			mHandler = new Handler();
			initVariables();
			didInit = true;
		}
		initControls();
		initActions();
	}

	public View findViewById(int id) {
		return mView.findViewById(id);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("SAVE", "SAVE ALL");
	}

	protected abstract void initControls();

	protected void initVariables() {

	}

	protected abstract void initActions();

	/**
	 * Call back khi start fragment for Result
	 * 
	 * @param bundle
	 * @param requestCode
	 * @param resultCode
	 */
	public void onFragmentResult(Bundle bundle, int requestCode, int resultCode) {
	}

	/**
	 * Call back khi location thay doi o NgonLocation
	 * 
	 * @param location
	 */
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i(Tag, "onAttach");
		try {
			mParent = (BaseFragmentActivity) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					activity.toString()
							+ " must implement OnChangeFragment and must extend BaseFragmentActivity");
		}
	}

	protected Context getApplicationContext() {
		return getActivity().getApplicationContext();
	}

	protected void finish() {
		getActivity().finish();
	}
}
