package com.zoostudio.ngon.views;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zoostudio.ngon.R;

public class ImageRollIndicator extends LinearLayout {
	ArrayList<ImageView> pages = new ArrayList<ImageView>();
	private int selectedIndex = 0;

	public ImageRollIndicator(Context context) {
		super(context);
		init();
	}

	public ImageRollIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		setGravity(Gravity.CENTER);
	}

	public void setPageCount(int total) {
		int margin = getContext().getResources().getDimensionPixelOffset(R.dimen.dimen5);
		
		for (int i = 0; i < total; i++) {
			ImageView v = new ImageView(getContext());
			LayoutParams params = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(margin, 0, margin, 0);
			v.setLayoutParams(params);
			v.setImageResource(R.drawable.img_imageroll_unselected);

			pages.add(v);

			addView(v);
		}
	}
	
	public void setSelectedPage(int index) {
		pages.get(selectedIndex).setImageResource(R.drawable.img_imageroll_unselected);
		selectedIndex = index;
		pages.get(selectedIndex).setImageResource(R.drawable.img_imageroll_selected);
	}
	
	public int getSelectedPage() {
		return selectedIndex;
	}
}
