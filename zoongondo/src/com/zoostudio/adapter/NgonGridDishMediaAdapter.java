package com.zoostudio.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView.ScaleType;

import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.android.image.ZooAjustImageView;

public class NgonGridDishMediaAdapter extends ArrayAdapter<MediaItem> {
	private GridView.LayoutParams layoutParams;
	private int SIZE;

	public NgonGridDishMediaAdapter(Context context, int textViewResourceId,
			ArrayList<MediaItem> objects) {
		super(context, textViewResourceId, objects);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		SIZE = manager.getDefaultDisplay().getWidth() / 3;
		layoutParams = new GridView.LayoutParams(SIZE, SIZE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MediaItem item = this.getItem(position);
		if (null == convertView) {
			convertView = new ZooAjustImageView(getContext());
			((ZooAjustImageView) convertView)
					.setScaleType(ScaleType.CENTER_CROP);
			((ZooAjustImageView) convertView).setLayoutParams(layoutParams);
		} else {
			((ZooAjustImageView) convertView).setImageBitmap(null);
		}

		((ZooAjustImageView) convertView).setImagePath(item.getPathMedia());
		return convertView;
	}

}
