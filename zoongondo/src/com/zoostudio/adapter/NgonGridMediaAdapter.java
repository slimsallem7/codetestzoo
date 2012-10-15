package com.zoostudio.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.android.image.ZooAjustImageView;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.views.PhotoToggleButton;

public class NgonGridMediaAdapter extends ArrayAdapter<MediaItem> {
	private LayoutInflater inflater;
	private int SIZE;
	protected RelativeLayout.LayoutParams layoutParams;
	private OnItemSelectListener listener;

	public NgonGridMediaAdapter(Context context, int textViewResourceId,
			ArrayList<MediaItem> objects) {
		super(context, textViewResourceId, objects);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		SIZE = manager.getDefaultDisplay().getWidth() / 3;
		layoutParams = new RelativeLayout.LayoutParams(SIZE, SIZE);
	}

	public void setOnItemSelectListener(OnItemSelectListener listener) {
		this.listener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MediaItem item = this.getItem(position);
		ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_picker_media, null);
			holder.imageView1 = (ZooAjustImageView) convertView
					.findViewById(R.id.zooAjustImageView1);
			holder.chkSelectImage = (PhotoToggleButton) convertView
					.findViewById(R.id.check_icon);
			holder.imageView1.setScaleType(ScaleType.CENTER_CROP);
			holder.imageView1.setLayoutParams(layoutParams);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.imageView1.setImageId(0, R.drawable.picker_checkmark_hint);
			holder.chkSelectImage.setOnCheckedChangeListener(null);
		}

		holder.chkSelectImage.setChecked(item.isSelected());
		holder.chkSelectImage.setOnCheckedChangeListener(new ZooMediaCheck(
				position, item.getIdMedia()));

		if (item.getIdMedia() != -1) {
			holder.imageView1.setImageId(item.getIdMedia());
		}
		return convertView;
	}

	private class ZooMediaCheck implements OnCheckedChangeListener {
		int position;
		long idMedia;

		public ZooMediaCheck(int position, long idMedia) {
			this.position = position;
			this.idMedia = idMedia;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			MediaItem item = NgonGridMediaAdapter.this.getItem(position);
			item.setSelected(isChecked);
			listener.onMediaChanged(isChecked,idMedia,item);
		}

	}

	class ViewHolder {
		ZooAjustImageView imageView1;
		PhotoToggleButton chkSelectImage;
	}

	public interface OnItemSelectListener {
		public void onMediaChanged(boolean isChecked, long idMedia,MediaItem item);
	}
}
