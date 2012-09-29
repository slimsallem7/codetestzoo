package com.zoostudio.adapter;

import java.util.ArrayList;

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
import com.zoostudio.ngon.R;

public class SpotAdapter extends ArrayAdapter<SpotItem> {

	private LayoutInflater mInflater;
	private double mLongtitude;
	private double mLatitude;
	private IOnRequestMoreListener listener;

	public SpotAdapter(Context context, ArrayList<SpotItem> data,
			Location currentLocation) {
		super(context, R.id.actionbar, data);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (null != currentLocation)
			setCurrentLocation(currentLocation);
	}

	public void setCurrentLocation(Location currentLocation) {
		mLongtitude = currentLocation.getLongitude();
		mLatitude = currentLocation.getLatitude();
		notifyDataSetChanged();
	}

	public void setRequestMoreListener(IOnRequestMoreListener listener) {
		this.listener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SpotItem item = getItem(position);
		ViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = this.mInflater.inflate(R.layout.item_spot, null);
			viewHolder.tvName = (TextView) convertView
					.findViewById(R.id.spot_name);
			viewHolder.tvAddress = (TextView) convertView
					.findViewById(R.id.spot_address);
			viewHolder.tvDistance = (TextView) convertView
					.findViewById(R.id.spot_distance);
//			viewHolder.tvDistanceUnit = (TextView) convertView
//					.findViewById(R.id.distance_unit);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvName.setText(item.getName());
		viewHolder.tvAddress.setText(item.getAddress());
		
		try {
			Log.i("SpotAdatper", "Position = " + position + "| " + item.getLocation());
			double distance = Math.round(item.getLocation().distanceTo(
					mLongtitude, mLatitude) * 100) / 100;
			viewHolder.tvDistance.setText(FormatterCore.numberFormat(distance));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if ((position == getCount() - 1) && listener != null) {
			listener.request();
		}

		return convertView;
	}

	private static class ViewHolder {
		private TextView tvName, tvAddress, tvDistance, tvDistanceUnit;
	}

	public interface IOnRequestMoreListener {
		public void request();
	}
}
