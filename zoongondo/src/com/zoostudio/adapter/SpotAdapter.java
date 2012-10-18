package com.zoostudio.adapter;

import java.util.List;

import org.bookmark.helper.FormatterCore;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.ngon.R;

public class SpotAdapter extends ArrayAdapter<SpotItem> {

	private LayoutInflater mInflater;
	private double mLongtitude;
	private double mLatitude;
	private IOnRequestMoreListener listener;

	public SpotAdapter(Context context, List<SpotItem> data,
			Location currentLocation) {
		super(context, -1, data);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (null != currentLocation)
			setCurrentLocation(currentLocation);
	}

	public void setCurrentLocation(Location currentLocation) {
		mLongtitude = currentLocation.getLongitude();
		mLatitude = currentLocation.getLatitude();
	}

	public void setRequestMoreListener(IOnRequestMoreListener listener) {
		this.listener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SpotItem item = getItem(position);
		ViewHolder viewHolder;
		if (null == convertView) {
			Log.e("SpotAdapter","Create new View = " + position);
			convertView = mInflater.inflate(R.layout.item_spot, null);
			viewHolder = new ViewHolder();
			viewHolder.tvName = (TextView) convertView
					.findViewById(R.id.spot_name);
			viewHolder.tvAddress = (TextView) convertView
					.findViewById(R.id.spot_address);
			viewHolder.tvDistance = (TextView) convertView
					.findViewById(R.id.spot_distance);
			viewHolder.imageSpot = (SmartImageView) convertView
					.findViewById(R.id.imageSpot);

			convertView.setTag(viewHolder);
		} else {
			Log.e("SpotAdapter","Recycle View ="+ position);
			viewHolder = (ViewHolder) convertView.getTag();
			String txt = viewHolder.tvName.getText().toString();
			Log.e("SpotAdapter",txt);
			viewHolder.imageSpot.setImageBitmap(null);
		}

		viewHolder.tvName.setText(item.getName());
		viewHolder.tvAddress.setText(item.getAddress());

		try {
			double distance = Math.round(item.getLocation().distanceTo(
					mLongtitude, mLatitude) * 100) / 100;
			viewHolder.tvDistance.setText(FormatterCore.numberFormat(distance));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((position == getCount() - 1) && listener != null) {
			listener.request();
		}
		Log.e("SpotAdapter","Load url = " + item.getUrlImageSpot());
		viewHolder.imageSpot
				.setImageUrl(item.getUrlImageSpot());
		
		return convertView;
	}

	private static class ViewHolder {
		private TextView tvName, tvAddress, tvDistance, tvDistanceUnit;
		private SmartImageView imageSpot;
	}

	public interface IOnRequestMoreListener {
		public void request();
	}
}
