package com.zoostudio.adapter.event;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.ui.SpotDetailsActivity;

public class OnSpotitemClick implements OnItemClickListener {

	private Activity mActivity;

	public OnSpotitemClick(Activity activity) {
		mActivity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int pos,
			long id) {
		SpotItem item = (SpotItem) adapterView.getItemAtPosition(pos);

		Intent intent = new Intent(mActivity, SpotDetailsActivity.class);
		intent.putExtra(SpotDetailsActivity.EXTRA_SPOT, item);
		
		mActivity.startActivity(intent);
	}
}
