package com.zoostudio.android.image;

import java.io.InputStream;

import com.zoostudio.ngon.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

public class ZooAjustImageView extends SmartImageView {

	public ZooAjustImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public ZooAjustImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ZooAjustImageView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
//		setScaleType(ScaleType.CENTER_CROP);
//		setLayoutParams(new RelativeLayout.LayoutParams(NgonGridMediaAdapter.SIZE, NgonGridMediaAdapter.SIZE));
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
////		int mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
////		Log.i("Zoo", "Width  = " + mWidth);
////		setMeasuredDimension(resolveSize((int) mWidth, widthMeasureSpec),
////				resolveSize(mWidth, heightMeasureSpec));
//
//	}

	// Helpers to set image by URL
	public void setImageId(long idMedia) {
		setImage(new LocalImage(idMedia));
	}
	
	public void setImageId(long idMedia, int fallbackResId) {
		setImage(new LocalImage(idMedia), fallbackResId);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			InputStream is, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, options);
		// BitmapFactory.decodeFile(pathName, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, 1, options);
	}
}
