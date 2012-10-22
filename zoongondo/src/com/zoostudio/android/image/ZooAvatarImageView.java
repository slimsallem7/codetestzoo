package com.zoostudio.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.utils.ConfigSize;

public class ZooAvatarImageView extends SmartImageView {
	protected  Paint paint;
	protected WebImage border;
	private static Bitmap bitmapBorder;
	private static Bitmap bitmapAvatarDefault;
	private static Bitmap bitmapBgCircle;

	public ZooAvatarImageView(Context context) {
		super(context);
		initVariables();
	}

	public ZooAvatarImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVariables();
	}

	// Helpers to set image by URL
	public void setImageUrl(String url) {
		setImage(new ZooAvatarWebImage(url, ConfigSize.SIZE_AVATAR));
	}

	public ZooAvatarImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initVariables();
	}

	// Helpers to set image by URL
	public void setImageId(long idMedia) {
		setImage(new LocalImage(idMedia));
	}

	protected void initVariables() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		border = new WebImage("ZooBorderAvatar");
		createBitmapDefault();
	}

	protected void createBitmapDefault() {
		if (null == bitmapAvatarDefault) {
			bitmapAvatarDefault = BitmapFactory.decodeStream(getContext()
					.getResources().openRawResource(
							R.drawable.ic_avatar_default));
		}
		if (null == bitmapBgCircle) {
			bitmapBgCircle = BitmapFactory.decodeStream(getContext()
					.getResources().openRawResource(R.drawable.ic_bg_circle));
		}
		
		if(null == bitmapBorder){
			bitmapBorder = BitmapFactory.decodeStream(getContext()
					.getResources().openRawResource(R.drawable.ic_border_circle));
		}
		setImageBitmap(bitmapAvatarDefault);
	}

	@Override
	public void setImageBitmap(Bitmap source) {
		if (null == source || source.getWidth() <=0 || source.getHeight() <=0)
			return;
		Bitmap result = makeCircleImage(source);
		super.setImageBitmap(result);
	}

	protected Bitmap makeCircleImage(Bitmap source) {
		Bitmap bitmapCircle = Bitmap.createBitmap(ConfigSize.SIZE_AVATAR,
				ConfigSize.SIZE_AVATAR, Config.ARGB_8888);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Canvas canvas = new Canvas(bitmapCircle);
		canvas.drawBitmap(bitmapBgCircle, 0, 0, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		paint.setXfermode(null);
		return bitmapCircle;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawCircleBorder(canvas);
	}

	protected void drawCircleBorder(Canvas canvas) {
		canvas.drawBitmap(bitmapBorder, 0, 0, paint);
	}

}