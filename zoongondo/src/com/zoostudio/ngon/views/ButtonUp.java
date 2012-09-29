package com.zoostudio.ngon.views;

import com.zoostudio.ngon.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class ButtonUp extends LinearLayout {

    public ButtonUp(Context context) {
		super(context);
		initLayout();
        initControls();
	}

	public ButtonUp(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
        initControls();
        initDefaultValues(attrs);
    }

    private void initLayout() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.button_up, this);
    }

    private void initControls() {
        
    }

    private void initDefaultValues(AttributeSet attrs) {
        
    }

}
