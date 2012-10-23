package com.zoostudio.ngon.views;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.android.image.ZooImageThumb;
import com.zoostudio.ngon.R;

public class VerticalImageThumbView extends RelativeLayout {
	private ArrayList<MediaItem> datas;
	private ZooImageThumb thumb1;
	private ZooImageThumb thumb2;
	private ZooImageThumb thumb3;

	public VerticalImageThumbView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public VerticalImageThumbView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VerticalImageThumbView(Context context) {
		super(context);
	}

	public void setData(ArrayList<MediaItem> datas) {
		this.datas = datas;
		int n = this.datas.size();

		MediaItem item;
		if (this.datas.isEmpty())
			return;

		item = this.datas.get(0);
		thumb1.setImagePath(item.getPathMedia());

		if (n == 1)
			return;

		item = this.datas.get(1);
		thumb2.setImagePath(item.getPathMedia());

		if (n == 2)
			return;

		item = this.datas.get(2);
		thumb3.setImagePath(item.getPathMedia());
	}

	public void initViews() {
		thumb1 = (ZooImageThumb) this.findViewById(R.id.imgThumb1);
		thumb2 = (ZooImageThumb) this.findViewById(R.id.imgThumb2);
		thumb3 = (ZooImageThumb) this.findViewById(R.id.imgThumb3);
	}

	public void clearData() {
		thumb1.setImageResource(R.drawable.default_photo_dish_checkin);
		thumb2.setImageResource(R.drawable.default_photo_dish_checkin);
		thumb3.setImageResource(R.drawable.default_photo_dish_checkin);
	}
}
