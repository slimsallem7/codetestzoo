package com.zoostudio.ngon.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.zoostudio.ngon.R;

public class ButtonListitemAdd extends TextView {
	public ButtonListitemAdd(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initControls();
	}

	public ButtonListitemAdd(Context context, AttributeSet attrs) {
		super(context, attrs);
		initControls();
	}

	public ButtonListitemAdd(Context context) {
		super(context);
		initControls();
	}
	
	private void initControls() {
		setBackgroundResource(R.drawable.btn_listitemadd);
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		setTextColor(Color.WHITE);
		setGravity(Gravity.CENTER);
	}
}
