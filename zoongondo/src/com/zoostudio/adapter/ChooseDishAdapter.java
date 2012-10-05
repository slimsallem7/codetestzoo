package com.zoostudio.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.zoostudio.adapter.item.DishItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.views.ZooRelativeAnimationView;

public class ChooseDishAdapter extends ArrayAdapter<DishItem> implements
		ZooRelativeAnimationView.OnAnimationEnd {
	private LayoutInflater mInflater;
	private volatile boolean isAnimating;
	private Handler handler;
	private OnDishChoice dishChoice;
	private int dishCount;

	public ChooseDishAdapter(Context context, int textViewResourceId,
			List<DishItem> objects, OnDishChoice listener) {
		super(context, textViewResourceId, objects);
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.handler = new Handler();
		this.isAnimating = false;
		this.dishChoice = listener;
		this.dishCount = 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DishItem item = getItem(position);
		ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_choose_dish, null);
			holder.menuName = (TextView) convertView
					.findViewById(R.id.txtTitleDish);
			holder.menuPrice = (TextView) convertView
					.findViewById(R.id.txtCost);
			holder.imgMenu = (SmartImageView) convertView
					.findViewById(R.id.imgDish);
			holder.mCheckBox = (CheckBox) convertView
					.findViewById(R.id.chkDishItem);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.mCheckBox.setOnCheckedChangeListener(null);
			holder.mCheckBox.setChecked(false);
		}

		holder.menuName.setText(item.getTitle());
		holder.mCheckBox.setChecked(item.isSelected());
		holder.imgMenu.setImageUrl("http://nr6.upanh.com/b6.s32.d1/2fb07778f2ab297f7821ededa6cf8016_49769616.monan.jpg");
		holder.mCheckBox.setOnCheckedChangeListener(new CheckDish(item,
				convertView));

		return convertView;
	}

	private class CheckDish implements OnCheckedChangeListener {
		private DishItem dishItem;
		private View view;

		public CheckDish(DishItem dishItem, View view) {
			this.dishItem = dishItem;
			this.view = view;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isAnimating || !isChecked) {
				buttonView.setOnCheckedChangeListener(null);
				buttonView.setChecked(!isChecked);
				return;
			}
//			int postion = ChooseDishAdapter.this.getPosition(dishItem);
//			ChooseDishAdapter.this.getItem(postion).setSelected(isChecked);
			if (isChecked) {
				((ZooRelativeAnimationView) view).startAnimation(
						dishItem, ChooseDishAdapter.this);
			} 
		}
	}

	private static class ViewHolder {
		private TextView menuName;
		private SmartImageView imgMenu;
		private TextView menuPrice;
		private CheckBox mCheckBox;
	}

	@Override
	public void onAnimationEnd(DishItem item) {
		Log.i("Zoo", "Remove item");
		handler.post(new RemoveItem(item));
	}

	private class RemoveItem implements Runnable {
		DishItem item;

		public RemoveItem(DishItem item) {
			this.item = item;
		}

		@Override
		public void run() {
			dishCount++;
			ChooseDishAdapter.this.dishChoice.onDishChoiceListener(dishCount,item);
			ChooseDishAdapter.this.remove(item);
			ChooseDishAdapter.this.notifyDataSetInvalidated();
			isAnimating = false;
		}
	}

	@Override
	public void onStart() {
		this.isAnimating = true;
	}

	public interface OnDishChoice {
		public void onDishChoiceListener(int count,DishItem item);
	}
}
