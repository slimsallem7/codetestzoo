package com.zoostudio.android.image;

import com.zoostudio.adapter.item.DishItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.utils.ConfigSize;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.View;

public class ZooImageDishBorder extends SmartImageView {
	public final static int TYPE_NULL = 0;
	public final static int TYPE_NO_IMAGE = 1;
	public final static int TYPE_IMAGE = 1;

	private int type;
	private String mImageLargeUrl;
	private String mTitleDish;
	
	private OnUnSelectedListener listener;
	private DishItem disItem;
	private static Bitmap bg;
	
	public ZooImageDishBorder(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		type = TYPE_NULL;
		mImageLargeUrl = "";
		mTitleDish = "";
	}

	public ZooImageDishBorder(Context context, AttributeSet attrs) {
		super(context, attrs);
		type = TYPE_NULL;
		mImageLargeUrl = "";
		mTitleDish = "";
	}

	public ZooImageDishBorder(Context context) {
		super(context);
		type = TYPE_NULL;
		mImageLargeUrl = "";
		mTitleDish = "";
	}
	
	public void setImageUrl(String url,int type,DishItem dishItem) {
		this.type = type;
		this.mImageLargeUrl = dishItem.getUrlImageThumb();
		this.mTitleDish = dishItem.getTitle();
		this.disItem = dishItem;
		setImage(new ZooAvatarWebImage(url, ConfigSize.SIZE_THUMB));
	}
	
	public void setImageBitmap(Bitmap source, int type,DishItem dishItem) {
		this.type = type;
		this.mImageLargeUrl = dishItem.getUrlImageThumb();
		this.mTitleDish = dishItem.getTitle();
		this.disItem = dishItem;
		super.setImageBitmap(source);
	}
	
	@Override
	public void setImageBitmap(Bitmap source) {
		if(null == source){
			super.setImageBitmap(null);
			return;
		}
		if(null == bg){
			bg = BitmapFactory.decodeStream(getContext().getResources()
				.openRawResource(R.drawable.icon_bg_black_dish));
		}
		Bitmap result = Bitmap.createBitmap(bg.getWidth(), bg.getHeight(),
				Config.ARGB_8888);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(bg, 0, 0, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		paint.setXfermode(null);
		super.setImageBitmap(result);
	}
	public int getType() {
		return type;
	}

	public String getmImageLargeUrl() {
		return mImageLargeUrl;
	}

	public String getNameDish() {
		return mTitleDish;
	}
	
	public void remove() {
		this.setImageBitmap(null);
		this.type = ZooImageDishBorder.TYPE_NULL;
		if(null!=listener){
			this.listener.onUnSelectListener(this,disItem);
		}
	}

	public interface OnUnSelectedListener {
		public void onUnSelectListener(View view,DishItem disItem);
	}


	public void setOnUnSelectedDish(OnUnSelectedListener listener) {
		this.listener = listener;
	}
}
