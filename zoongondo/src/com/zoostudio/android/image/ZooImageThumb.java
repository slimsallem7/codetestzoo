package com.zoostudio.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.zoostudio.ngon.utils.ConfigSize;

public class ZooImageThumb extends ZooAvatarImageView {
	private static Rect rect;
	private Paint paintBorderWhite;
	private static int width;
	private static int height;
	private static float ratio;
	private static int y;
	private static int x;

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
		paint.setColor(Color.parseColor("#CEB786"));
		paint.setStrokeWidth(ConfigSize.SIZE_BORDER);

		paintBorderWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintBorderWhite.setStyle(Style.STROKE);
		paintBorderWhite.setColor(Color.parseColor("#FFFFFF"));
		paintBorderWhite.setStrokeWidth(ConfigSize.SIZE_BORDER_WHITE);
	}

	@Override
	public void setImageBitmap(Bitmap source) {
		width = source.getWidth();
		height = source.getHeight();
		if(width <=0 || height <=0) return;

		ratio = calculateRatio(width, height, ConfigSize.SIZE_THUMB,
				ConfigSize.SIZE_THUMB);

		Matrix matrix = new Matrix();
		matrix.postScale(ratio, ratio);
		source = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);

		width = source.getWidth();
		height = source.getHeight();

		x = (width - ConfigSize.SIZE_THUMB) / 2;
		y = (height - ConfigSize.SIZE_THUMB) / 2;

		source = Bitmap.createBitmap(source, x, y, ConfigSize.SIZE_THUMB,
				ConfigSize.SIZE_THUMB);

		super.setImageBitmap(source);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null == rect) {
			rect = new Rect(0, 0, this.getWidth(), this.getHeight());
		}
		canvas.drawRect(rect, paintBorderWhite);
		canvas.drawRect(rect, paint);
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

	private float calculateRatio(int width, int height, int reqWidth,
			int reqHeight) {
		float inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = (float) reqHeight / (float) height;
			} else {
				inSampleSize = (float) reqWidth / (float) width;
			}
		}
		return inSampleSize;
	}

}
