package com.zoostudio.ngon.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zoostudio.ngon.R;

public class TextViewExtended extends TextView {

	public TextViewExtended(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public TextViewExtended(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public TextViewExtended(Context context) {
		super(context);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NgonDo_Views, 0, 0);
        
		int resId = a.getResourceId(R.styleable.NgonDo_Views_icon, -1);
		
        if (resId != 0) {
            String position = a.getString(R.styleable.NgonDo_Views_iconPosition);
        	setIcon(resId, position);
        }
	}

	public void setIcon(int resId, String position) {
		if ("top".equalsIgnoreCase(position)) {
			setCompoundDrawablesWithIntrinsicBounds(
					null, getResources().getDrawable(resId), null, null);
		} else if ("right".equalsIgnoreCase(position)) {
			setCompoundDrawablesWithIntrinsicBounds(
					null, null, getResources().getDrawable(resId), null);
		} else if ("bottom".equalsIgnoreCase(position)) {
			setCompoundDrawablesWithIntrinsicBounds(
					null, null, null, getResources().getDrawable(resId));
		} else {
			setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(resId), null, null, null);
		}
	}
}
