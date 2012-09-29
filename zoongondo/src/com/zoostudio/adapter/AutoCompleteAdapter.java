package com.zoostudio.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.zoostudio.adapter.item.InfoAddress;
import com.zoostudio.ngon.R;

public class AutoCompleteAdapter extends ArrayAdapter<InfoAddress> implements
		Filterable {
	public static final int ADD_NEW = 0;
	protected static final int FILTER = 1;
	/**
	 * 
	 */
	private ArrayList<InfoAddress> mOriginalData;
	private LayoutInflater mInflater;

	public AutoCompleteAdapter(Context context, int textViewResourceId,
			ArrayList<InfoAddress> objects) {
		super(context, textViewResourceId, objects);
		mInflater = (LayoutInflater) this.getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		mOriginalData = new ArrayList<InfoAddress>();
		mOriginalData.addAll(objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InfoAddress item = this.getItem(position);
		ViewHolder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_sug_address, null);
			holder = new ViewHolder();
			holder.mSuggAddress = (TextView) convertView
					.findViewById(R.id.txtSuggestAddress);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mSuggAddress.setText(item.getAddress());

		return convertView;
	}

	class ViewHolder {
		TextView mSuggAddress;
	}

	public void add(InfoAddress object, int type) {
		super.add(object);
		if (type == ADD_NEW) {
			mOriginalData.add(object);
		}
	}

	@Override
	public Filter getFilter() {
		Filter myFilter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				ArrayList<InfoAddress> filt = new ArrayList<InfoAddress>();

				if (constraint != null && constraint.toString().length() > 0) {
					for (int i = 0, l = mOriginalData.size(); i < l; i++) {
						InfoAddress m = mOriginalData.get(i);
							filt.add(m);
					}
					filterResults.count = filt.size();
					filterResults.values = filt;
				} else {
					filterResults.values = filt;
					filterResults.count = filt.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence contraint,
					FilterResults results) {
				@SuppressWarnings("unchecked")
				ArrayList<InfoAddress> filteredList = (ArrayList<InfoAddress>) results.values;
				
				if (results != null && results.count > 0) {
					clear();
					for (InfoAddress c : filteredList) {
						add(c, FILTER);
					}
					notifyDataSetChanged();
				}
			}
		};
		return myFilter;
	}

	@Override
	public void clear() {
		super.clear();
		mOriginalData.clear();
	}

}
