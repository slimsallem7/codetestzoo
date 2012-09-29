package com.zoostudio.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

public class ZooImageThumb extends ZooAvatarImageView {


	public ZooImageThumb(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ZooImageThumb(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ZooImageThumb(Context context) {
		super(context);
	}

	@Override
	protected void initVariables() {
		super.initVariables();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
	}
	@Override
	public void setImageBitmap(Bitmap source) {
		super.setImageBitmap(source);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		if (null == rect) {
//			rect = new Rect(0, 0, this.getWidth(), this.getHeight());
//		}
//		canvas.drawRect(rect, paint);
	}

	/**
	 * Khong duoc xoa
	 */
	@Override
	protected void drawCircleBorder(Canvas canvas) {
	}

	/**
	 * Khong duoc xoa
	 */
	@Override
	protected void createBitmapDefault() {
	}

	/**
	 * Khong duoc xoa
	 */
	@Override
	protected Bitmap makeCircleImage(Bitmap source) {
		return source;
	}
}
