package com.zoostudio.ngon.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class ZooImageView extends ImageView {
	private int fromDegree;
	private int toDegree;
	private Handler handler;

	public ZooImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAnimation();
	}

	public ZooImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAnimation();
	}

	public ZooImageView(Context context) {
		super(context);
		initAnimation();
	}

	private void initAnimation() {
		handler = new Handler();
		fromDegree = 0;
		toDegree = 0;
	}

	public void startAnimation(int degree) {
		if (fromDegree == degree)
			return;
		toDegree = degree;
		handler.post(new Runnable() {
			@Override
			public void run() {
				RotateAnimation rotateAnimation = new RotateAnimation(
						fromDegree, toDegree, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				rotateAnimation.setDuration(220);
				rotateAnimation.setFillAfter(true);
				ZooImageView.this.startAnimation(rotateAnimation);
				fromDegree = toDegree;
			}
		});
	}

}
