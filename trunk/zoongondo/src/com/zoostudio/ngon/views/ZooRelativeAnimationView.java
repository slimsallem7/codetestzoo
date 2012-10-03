package com.zoostudio.ngon.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

import com.zoostudio.adapter.item.DishItem;

public class ZooRelativeAnimationView extends RelativeLayout {
	private OnAnimationEnd listener;
	private DishItem item;

	public ZooRelativeAnimationView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ZooRelativeAnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ZooRelativeAnimationView(Context context) {
		super(context);
	}

	public void startAnimation(DishItem item, OnAnimationEnd listener) {
		this.listener = listener;
		this.item = item;

		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0F, 0.0F);
		alphaAnimation.setDuration(300L);
		alphaAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				ZooRelativeAnimationView.this.listener.onStart();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ZooRelativeAnimationView.this.listener
						.onAnimationEnd(ZooRelativeAnimationView.this.item);
			}
		});
		this.setAnimation(alphaAnimation);
		// alphaAnimation.startNow();
	}

	public interface OnAnimationEnd {
		public void onAnimationEnd(DishItem item);

		public void onStart();
	}
}
