package com.zoostudio.ngon.views;

import com.zoostudio.ngon.R;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class PhotoToggleButton extends ToggleButton{
	
	public PhotoToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PhotoToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PhotoToggleButton(Context context) {
		super(context);
	}
	
	
	@Override
	protected void onAnimationEnd() {
		super.onAnimationEnd();
	}
	
	@Override
	protected void onAnimationStart() {
		super.onAnimationStart();
	}
	
	public void startAnimation(){
		this.getResources().getAnimation(R.anim.fade_out);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
	}
}
