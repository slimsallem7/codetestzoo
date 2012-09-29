package com.zoostudio.adapter;

import java.util.List;

import com.zoostudio.adapter.item.CheckInItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.ngon.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NgonListCheckInAdapter extends ArrayAdapter<CheckInItem> {
	private LayoutInflater inflater;

	public NgonListCheckInAdapter(Context context, int textViewResourceId,
			List<CheckInItem> objects) {
		super(context, textViewResourceId, objects);
		inflater = (LayoutInflater) context.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_user_pager, null);
			holder.imgAvatar = (SmartImageView) convertView
					.findViewById(R.id.imgAvatar);
			holder.useractivity = (TextView) convertView
					.findViewById(R.id.useractivity);
			holder.spotAddress = (TextView) convertView
					.findViewById(R.id.spot_address);
			holder.status = (TextView) convertView.findViewById(R.id.txtStatus);
			holder.timestamp = (TextView) convertView
					.findViewById(R.id.timestamp);
			holder.imageSpot = (SmartImageView) convertView
					.findViewById(R.id.imgSpot);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imgAvatar.setImageResource(R.drawable.ic_avatar_default);
		holder.imageSpot.setImageResource(R.drawable.sample);
		holder.useractivity.setText(new StringBuilder()
				.append("VietBQ")
				.append(getContext().getString(R.string.user_pager_at))
				.append("Zoo Studio").toString());
		holder.spotAddress.setText("83B, Ly Thuong Kiet");
		holder.status
				.setText(this.getItem(position).getStatus());
		holder.timestamp.setText("5phut truoc");

		return convertView;
	}

	class ViewHolder {
		private SmartImageView imgAvatar;
		private TextView useractivity;
		private TextView spotAddress;
		private TextView status;
		private TextView timestamp;
		private SmartImageView imageSpot;

	}
}
