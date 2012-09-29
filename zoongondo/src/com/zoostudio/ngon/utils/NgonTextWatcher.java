package com.zoostudio.ngon.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public abstract class NgonTextWatcher implements TextWatcher {

	private CheckRelease checkRelease;
	private boolean cancelWatch;
	private boolean hasTyping = false;
	private int mLastLength;
	private int mChecking;
	
	@Override
	public void afterTextChanged(Editable s) {
		Log.i("TextWatcher", "afterTextChanged =  | count  = " + s.length());
		if(mChecking == 3){
			hasTyping = true;
		}else if(Math.abs(s.length() - mLastLength) == 1){
			mChecking ++;
		}else{
			mChecking = 0;
			hasTyping = false;
		}
		
		if (s.length() >= 3 && hasTyping) {
			checkRelease.mNeedCheck = true;
			hasTyping = false;
			mChecking--;
		}else{
			checkRelease.mNeedCheck = false;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		mLastLength = s.length();
		if (null == checkRelease && !cancelWatch) {
			checkRelease = new CheckRelease();
			new Thread(checkRelease).start();
		}
		checkRelease.reset();
		Log.i("TextWatcher", "beforeTextChanged = | count  = " + s.length());
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	public void onTextRelease(){
		checkRelease = null;
	}
	
	private class CheckRelease implements Runnable {
		private long start;
		private volatile boolean mBreak;
		private volatile boolean mNeedCheck;
		
		public CheckRelease() {
			start = System.currentTimeMillis();
		}
		
		@Override
		public void run() {
			while (!mBreak) {
				if (mNeedCheck) {
					if (System.currentTimeMillis() - start >= 1000) {
						onTextRelease();
						break;
					}
				}
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void reset() {
			start = System.currentTimeMillis();
		}
	}
	
	public void enableWatch(){
		cancelWatch = false;
	}
	
	public void disableWatch(){
		cancelWatch = true;
	}
}
