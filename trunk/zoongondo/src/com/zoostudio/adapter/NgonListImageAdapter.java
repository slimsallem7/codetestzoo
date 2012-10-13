package com.zoostudio.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.android.image.ZooImageThumb;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonDialog;
import com.zoostudio.ngon.dialog.NgonDialog.Builder;

public class NgonListImageAdapter extends ArrayAdapter<MediaItem> {
	private LayoutInflater inflater;
	private Activity parent;

	public NgonListImageAdapter(Context context, int textViewResourceId,
			ArrayList<MediaItem> objects, Activity pActivity) {
		super(context, textViewResourceId, objects);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.parent = pActivity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MediaItem item = this.getItem(position);
		ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_photo, null);
			holder.imageView = (ZooImageThumb) convertView
					.findViewById(R.id.imgDishCheckin);
			holder.button = (ImageButton) convertView
					.findViewById(R.id.imgBtnDeletePhoto);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.button.setOnClickListener(null);
		}
		holder.button.setOnClickListener(new OnDelete(item));
		return convertView;
	}

	private class ViewHolder {
		ZooImageThumb imageView;
		ImageButton button;
	}

	private class OnDelete implements OnClickListener {
		private MediaItem item;

		public OnDelete(MediaItem item) {
			this.item = item;
		}

		@Override
		public void onClick(View v) {
			showDialogConfirm(item);
		}
	}

	private void showDialogConfirm(final MediaItem item) {
		Builder dialog = new NgonDialog.Builder(parent);
		dialog.setCancelable(true);
		dialog.setTitle(R.string.string_notice);
		dialog.setMessage(R.string.dialog_delete_msg);
		dialog.setNegativeButton(R.string.string_cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.setPositiveButton(R.string.button_done,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						NgonListImageAdapter.this.remove(item);
						NgonListImageAdapter.this.notifyDataSetChanged();
						dialog.dismiss();
					}
				});
		dialog.show();
	}

}
