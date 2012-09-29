package com.zoostudio.ngon.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.zoostudio.ngon.R;

public class ButtonCaptionedIcon extends TextView {
	
	public ButtonCaptionedIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDefaultAttrs(attrs);
	}

	private void initDefaultAttrs(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NgonDo_Views, 0, 0);
        int resId = a.getResourceId(R.styleable.NgonDo_Views_icon, 0);
        if (resId != 0) {
        	setIcon(resId);
        }
    	setGravity(Gravity.CENTER);
	}

	public ButtonCaptionedIcon(Context context) {
		super(context);
	}
	
	public void setIcon(int resId) {
		setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(resId), null, null);
	}
}
