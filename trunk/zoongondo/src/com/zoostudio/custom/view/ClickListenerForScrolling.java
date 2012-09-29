package com.zoostudio.custom.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;

public abstract class ClickListenerForScrolling implements OnClickListener,
		OnWidthOfButtonChanged {

	HorizontalScrollView scrollView;
	View menu;
	/**
	 * Menu must NOT be out/shown to start with.
	 */
	private boolean menuOut = false;
	private boolean mIsFullScreen = false;
	private int widthButton;

	public boolean isMenuOut() {
		return menuOut;
	}

	public ClickListenerForScrolling(HorizontalScrollView scrollView,
			View menu, int widthScreen) {
		super();
		this.scrollView = scrollView;
		this.menu = menu;
	}

	@Override
	public void onClick(View v) {
		int menuWidth = menu.getMeasuredWidth();
		// Ensure menu is visible
		menu.setVisibility(View.VISIBLE);
		if (!menuOut) {
			// Scroll to 0 to reveal menu
			int left = this.widthButton;
			scrollView.smoothScrollTo(left, 0);
		} else {
			// Scroll to menuWidth so menu isn't on screen.
			int left = menuWidth;
			scrollView.smoothScrollTo(left, 0);
		}
		menuOut = !menuOut;
		onClickToSlide();
	}

	protected abstract void onClickToSlide();

	@Override
	public void onWidthChange(int width) {
		this.widthButton = width;
	}

	public void onChangeScreenMenu() {
		if (!mIsFullScreen) {
			scrollView.smoothScrollBy(-this.widthButton, 0);
		} else {
			scrollView.smoothScrollTo(widthButton, 0);
		}
		mIsFullScreen = !mIsFullScreen;
	}
}
