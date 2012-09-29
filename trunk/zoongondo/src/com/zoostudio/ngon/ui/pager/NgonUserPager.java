package com.zoostudio.ngon.ui.pager;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class NgonUserPager extends Fragment {
	protected View mView;
	private boolean didInit = false;
	protected Handler mHandler;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		if (!didInit) {
			mHandler = new Handler();
			initVariables();
			didInit = true;
		}
		initViews();
		initActions();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(getLayoutId(), null);
		return mView;
	}
	protected abstract int getLayoutId();
	
	public static <T extends NgonUserPager> T findOrCreateNgonPager(
			FragmentManager fm, String tag, Class<T> typeClass) {
		@SuppressWarnings("unchecked")
		T fragment = (T) fm.findFragmentByTag(tag);
		if (fragment == null) {
			try {
				fragment = typeClass.newInstance();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (java.lang.InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} 
		}
		return fragment;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("SAVE", "SAVE ALL");
	}

	public abstract void onTabSelected(int position);
	
	public abstract void initVariables();
	
	public abstract void initViews();
	
	public abstract void initActions();
}
