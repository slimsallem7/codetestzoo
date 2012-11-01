package com.zoostudio.adapter;

import java.util.ArrayList;
import java.util.List;

import org.bookmark.helper.FormatterCore;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.zoostudio.adapter.item.DistanceItem;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.utils.DistanceUtils;

public class SpotAdapter extends ArrayAdapter<SpotItem> {

	private LayoutInflater mInflater;
	private double mLongtitude;
	private double mLatitude;
	public ArrayList<SpotItem> items;
	public ArrayList<SpotItem> filtered;
	private IOnRequestMoreListener listener;
	public final static int FILTER = 0;
	public final static int ADD_NEW = 1;
	private String[] sections = new String[0];
	private Filter filter;

	public SpotAdapter(Context context, List<SpotItem> data,
			Location currentLocation) {
		super(context, -1, data);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		filtered = new ArrayList<SpotItem>();
		items = new ArrayList<SpotItem>();
		filter = new SpotNameFilter();
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
			Log.e("SpotAdapter", "Create new View = " + position);
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
			Log.e("SpotAdapter", "Recycle View =" + position);
			viewHolder = (ViewHolder) convertView.getTag();
			String txt = viewHolder.tvName.getText().toString();
			Log.e("SpotAdapter", txt);
			viewHolder.imageSpot.setImageBitmap(null);
		}

		viewHolder.tvName.setText(item.getName());
		viewHolder.tvAddress.setText(item.getAddress());

		try {
			double distance = Math.round(item.getLocation().distanceTo(
					mLongtitude, mLatitude) * 100) / 100;
			DistanceItem distanceItem = DistanceUtils.getDistanceDisplay(distance);
//			viewHolder.tvDistance.setText(FormatterCore.numberFormat(distance));
			viewHolder.tvDistance.setText(distanceItem.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((position == getCount() - 1) && listener != null) {
			listener.request();
		}
		Log.e("SpotAdapter", "Load url = " + item.getUrlImageSpot());
		viewHolder.imageSpot.setImageUrl(item.getUrlImageSpot());

		return convertView;
	}

	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new SpotNameFilter();
		return filter;
	}

	private static class ViewHolder {
		private TextView tvName, tvAddress, tvDistance, tvDistanceUnit;
		private SmartImageView imageSpot;
	}

	public interface IOnRequestMoreListener {
		public void request();
	}

	private class SpotNameFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// NOTE: this function is *always* called from a background thread,
			// and
			// not the UI thread.
			constraint = constraint.toString().toLowerCase();
			FilterResults result = new FilterResults();

			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<SpotItem> filt = new ArrayList<SpotItem>();
				ArrayList<SpotItem> lItems = new ArrayList<SpotItem>();
				synchronized (this) {
					lItems.addAll(items);
				}
				for (int i = 0, l = lItems.size(); i < l; i++) {
					SpotItem m = lItems.get(i);
					if (m.getName().toLowerCase().contains(constraint))
						filt.add(m);
				}
				result.count = filt.size();
				result.values = filt;
			}
			/* Restore default list */
			else {
				synchronized (this) {
					result.values = items;
					result.count = items.size();
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// NOTE: this function is *always* called from the UI thread.
			filtered = (ArrayList<SpotItem>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filtered.size(); i < l; i++)
				add(filtered.get(i), FILTER);
			notifyDataSetInvalidated();
		}

	}

	public void addAll(ArrayList<SpotItem> list) {
		for (SpotItem item : list) {
			add(item);
		}
	}

	public void add(SpotItem object, int type) {
		super.add(object);
		if (type == ADD_NEW) {
			items.add(object);
			filtered.add(object);
		}
	}
}
