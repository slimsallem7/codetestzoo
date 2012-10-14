package com.zoostudio.ngon.views;

import com.zoostudio.ngon.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class NgonProgressView extends ImageView {
	AnimationDrawable anim;
	private boolean isAutoShow = true;
	
	public NgonProgressView(Context context) {
		super(context);
		init();
	}

	public NgonProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public NgonProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setImageResource(R.anim.loading);
		anim = (AnimationDrawable) getDrawable();
	}
	
	public void setAutoShow(boolean auto){
		isAutoShow = auto;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if (anim != null && isAutoShow) {
			anim.setVisible(true, true);
			anim.start();
		}
	}

	public void startAnim() {
		if (!isAutoShow) {
			anim.setVisible(true, true);
			anim.start();
		}
	}

	public void stopAnim() {
		if (null != anim && anim.isRunning())
			anim.stop();
	}
}
