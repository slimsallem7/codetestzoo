package com.zoostudio.zooslideshow;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.utils.ScreenUtil;

public class ZooSlideView extends RelativeLayout implements OnClickListener {
	private LinearLayout mLayoutThumb;
	private SmartImageView mMainPhoto;
	private ImageButton mTakePhoto;
	private ArrayList<String> mUrlsImage;
	private OnSlideShowListener mListener;
	private int height;
	private int width;

	public ZooSlideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public ZooSlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public ZooSlideView(Context context) {
		super(context);
		initViews(context);

	}

	public void setDatas(ArrayList<String> urlsImage) {
		this.mUrlsImage = urlsImage;
		setUpImages();
	}

	private void initViews(Context context) {
		mUrlsImage = new ArrayList<String>();
		setUpDefaultImage();
		mTakePhoto = (ImageButton) findViewById(R.id.addphoto);
		mLayoutThumb = (LinearLayout) findViewById(R.id.thumbnail_list);
		mMainPhoto = (SmartImageView) findViewById(R.id.imageViewPhoto);

	}

	public void setImageMainSpot(String urlImage) {
		mMainPhoto.setImageUrl(urlImage);
	}

	private void setUpDefaultImage() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.default_slide_image, this);
		height = this.getResources().getDimensionPixelSize(
				R.dimen.height_main_image_in_spotdetail_screen);
		width = ScreenUtil.getIntance(getContext()).getWidth();
	}

	public void setOnSildeShowListener(OnSlideShowListener listener) {
		this.mListener = listener;
	}

	private void setUpImages() {
		// mMainPhoto.setImageResource(R.drawable.main_dish_image);

		findViewById(R.id.addphotohint).setVisibility(View.GONE);
		int thumbSize = getResources().getDimensionPixelOffset(
				R.dimen.thumbSize);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				thumbSize, thumbSize);
		params.setMargins(0, 0,
				getResources().getDimensionPixelOffset(R.dimen.padding_medium),
				0);

		int size = mUrlsImage.size();
		if (size > 4)
			size = 4;

		for (int i = 0; i < size; i++) {
			SmartImageView imageView = new SmartImageView(getContext());
			imageView.setBackgroundResource(R.drawable.img_listlikeavatar);
			imageView.setImageResource(R.drawable.thumb_dish_image);
			mLayoutThumb.addView(imageView, params);
		}

		mTakePhoto.setOnClickListener(this);
		mLayoutThumb.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (null != mListener) {
			if (v == mTakePhoto) {
				mListener.onTakePhoto();
			} else if (v == mLayoutThumb) {
				mListener.onListThumbClicked();
			}
		}
	}

	public void releaseBitmap() {
		mMainPhoto.setImageBitmap(null);
	}
}
