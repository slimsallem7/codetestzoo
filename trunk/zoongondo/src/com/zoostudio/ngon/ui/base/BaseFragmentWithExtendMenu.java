package com.zoostudio.ngon.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.zoostudio.custom.view.ZooHorizontalScrollView;
import com.zoostudio.custom.view.ZooHorizontalScrollView.SizeCallback;
import com.zoostudio.ngon.R;

public abstract class BaseFragmentWithExtendMenu extends BaseFragmentScreen
		implements IOnBackPressed {

	private ZooHorizontalScrollView scrollView;
	protected View mMenuContent;
	private ImageView mBtnSlide;
	protected boolean mMenuOut;
	protected ClickListenerForScrolling mScrollListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scrollView = (ZooHorizontalScrollView) inflater.inflate(
				R.layout.activity_horz_scroll_with_list_menu, null);
		mView = createMainLayout(inflater);
		mMenuContent = createMenuLayout(inflater);
		return scrollView;
	}
	@Override
	protected void initVariables() {
		// TODO Auto-generated method stub
		super.initVariables();
		mMenuOut = false;
	}
	
	
	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's
	 * width.
	 */
	private class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View menu;

		/**
		 * Menu must NOT be out/shown to start with.
		 */

		public ClickListenerForScrolling(HorizontalScrollView scrollView,
				View menu) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
		}

		@Override
		public void onClick(View v) {
			int menuWidth = menu.getMeasuredWidth();
			menu.setVisibility(View.VISIBLE);
			if (!mMenuOut) {
				int left = 0;
				scrollView.smoothScrollTo(left, 0);
			} else {
				// Scroll to menuWidth so menu isn't on screen.
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
			}
			mMenuOut = !mMenuOut;
		}
	}

	/**
	 * Helper that remembers the width of the 'slide' button, so that the
	 * 'slide' button remains in view, even when the menu is showing.
	 */
	private static class SizeCallbackForMenu implements SizeCallback {
		int btnWidth;
		View btnSlide;

		public SizeCallbackForMenu(View btnSlide) {
			super();
			this.btnSlide = btnSlide;
		}

		@Override
		public void onGlobalLayout() {
			btnWidth = btnSlide.getMeasuredWidth();
		}

		@Override
		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIdx = 0;
			if (idx == menuIdx) {
				dims[0] = w - btnWidth;
			}
		}
	}

	protected abstract View createMainLayout(LayoutInflater inflater);

	protected abstract View createMenuLayout(LayoutInflater inflater);

	protected abstract ImageView initButtonSlide();

	@Override
	public boolean onBackPressed() {
		if (mMenuOut) {
			mScrollListener.onClick(null);
			return true;
		}
		return false;
	}
	
	@Override
	protected void initActions() {
		mBtnSlide = initButtonSlide();
		mScrollListener = new ClickListenerForScrolling(scrollView,
				mMenuContent);
		mBtnSlide.setOnClickListener(mScrollListener);
		final View[] children = new View[] { mMenuContent, mView };
		int scrollToViewIdx = 1;
//		scrollView.initViews(children, scrollToViewIdx,
//				new SizeCallbackForMenu(mBtnSlide));	
	}
}
