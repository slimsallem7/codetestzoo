package com.zoostudio.ngon.views;

import com.zoostudio.ngon.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewRounded extends ImageView {

	public ImageViewRounded(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NgonDo_Views, 0, 0);
        int resId = a.getResourceId(R.styleable.NgonDo_Views_icon, 0);
        if (resId != 0) {
        	Bitmap bm = BitmapFactory.decodeResource(getResources(), resId);
        	setImageBitmap(bm);
        }
	}

	public ImageViewRounded(Context context) {
		super(context);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(getRoundedCornerBitmap(bm));
	}

	private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = bitmap.getWidth();
		final float roundPy = bitmap.getHeight();

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
}
