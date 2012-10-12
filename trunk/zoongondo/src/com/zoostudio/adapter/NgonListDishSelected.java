package com.zoostudio.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.zoostudio.adapter.item.MenuItem;
import com.zoostudio.ngon.R;

public class NgonListDishSelected extends ArrayAdapter<MenuItem> {
	private LayoutInflater inflater;

	public NgonListDishSelected(Context context, int textViewResourceId,
			List<MenuItem> objects) {
		super(context, textViewResourceId, objects);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		MenuItem item = this.getItem(position);
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_dish_dialog, null);
			holder.titleDish = (TextView) convertView
					.findViewById(R.id.txtTitleDish);
			holder.chkDish = (CheckBox) convertView.findViewById(R.id.chkDish);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.chkDish.setOnCheckedChangeListener(null);
		}
		holder.titleDish.setText(item.getTitle());
		holder.chkDish.setChecked(item.isSelected());
		holder.chkDish.setOnCheckedChangeListener(new OnChecked(item));
		return convertView;
	}

	private class ViewHolder {
		private TextView titleDish;
		private CheckBox chkDish;
	}

	private class OnChecked implements OnCheckedChangeListener {
		private MenuItem dishItem;

		public OnChecked(MenuItem dishItem) {
			this.dishItem = dishItem;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			this.dishItem.setDeleted(isChecked);
		}
	}
}
