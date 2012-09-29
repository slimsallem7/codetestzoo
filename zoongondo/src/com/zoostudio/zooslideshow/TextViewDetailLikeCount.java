package com.zoostudio.zooslideshow;

import com.zoostudio.ngon.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewDetailLikeCount extends TextView {

	public TextViewDetailLikeCount(Context context, AttributeSet attrs) {
		super(context, attrs);
		setIcon();
	}

	public TextViewDetailLikeCount(Context context) {
		super(context);
		setIcon();
	}
	
	public void setIcon() {
		setCompoundDrawablesWithIntrinsicBounds(
				getResources().getDrawable(R.drawable.ic_likesmall), 
				null, null, null);
	}
}
