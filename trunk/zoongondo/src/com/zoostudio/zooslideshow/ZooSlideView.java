package com.zoostudio.zooslideshow;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zoostudio.adapter.item.PhotoItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.android.image.ZooImageThumb;
import com.zoostudio.ngon.R;

public class ZooSlideView extends RelativeLayout implements OnClickListener {
	private LinearLayout mLayoutThumb;
	private SmartImageView mMainPhoto;
	private ImageButton mMaskImage;
	private ImageButton mTakePhoto;
	private ArrayList<PhotoItem> mUrlsImage;
	private OnSlideShowListener mListener;

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

	public void setDatas(ArrayList<PhotoItem> urlsImage) {
		this.mUrlsImage = urlsImage;
		setUpImages();
	}

	private void initViews(Context context) {
		mUrlsImage = new ArrayList<PhotoItem>();
		setUpDefaultImage();
		mTakePhoto = (ImageButton) findViewById(R.id.addphoto);
		mLayoutThumb = (LinearLayout) findViewById(R.id.thumbnail_list);
		mMainPhoto = (SmartImageView) findViewById(R.id.imageViewPhoto);
		mMaskImage = (ImageButton) findViewById(R.id.maskMainImage);
		mLayoutThumb.setVisibility(View.GONE);
		mTakePhoto.setVisibility(View.GONE);
	}

	public void setImageMainSpot(String urlImage) {
		if (null== urlImage || urlImage.equals(""))
			return;
		mLayoutThumb.setVisibility(View.VISIBLE);
		mTakePhoto.setVisibility(View.VISIBLE);
		mMaskImage.setVisibility(View.GONE);
		mMainPhoto.setImageUrl(urlImage);
	}

	private void setUpDefaultImage() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.default_slide_image, this);
	}

	public void setOnSildeShowListener(OnSlideShowListener listener) {
		this.mListener = listener;
	}

	private void setUpImages() {
		mLayoutThumb.removeAllViews();
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
			ZooImageThumb imageView = new ZooImageThumb(getContext());
			imageView.setShowBorder(false);
			imageView.setBackgroundResource(R.drawable.img_listlikeavatar);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setImageUrl(mUrlsImage.get(i).getMediumPath());
			mLayoutThumb.addView(imageView, params);
		}

		mTakePhoto.setOnClickListener(this);
		mLayoutThumb.setOnClickListener(this);
		mMaskImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onTakePhotoCoverSpot();
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (null != mListener) {
			if (v == mTakePhoto) {
				mListener.onTakePhoto();
			} else if (v == mLayoutThumb) {
				mListener.onListThumbClicked(mUrlsImage);
			} 
		}
	}

	public void releaseBitmap() {
		mMainPhoto.setImageBitmap(null);
		mMaskImage.setVisibility(View.VISIBLE);
	}
}
