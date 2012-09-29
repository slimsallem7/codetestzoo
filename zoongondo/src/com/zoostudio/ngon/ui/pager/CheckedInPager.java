package com.zoostudio.ngon.ui.pager;

import java.util.ArrayList;

import android.widget.ListView;

import com.zoostudio.adapter.NgonListCheckInAdapter;
import com.zoostudio.adapter.item.CheckInItem;
import com.zoostudio.ngon.R;

public class CheckedInPager extends NgonUserPager {
	private ListView mListCheckIn;
	private NgonListCheckInAdapter adapter;

	@Override
	protected int getLayoutId() {
		return R.layout.pager_checked_in;
	}

	@Override
	public void onTabSelected(int position) {

	}

	@Override
	public void initVariables() {
		adapter = new NgonListCheckInAdapter(this.getActivity(), 0, new ArrayList<CheckInItem>());
	}

	@Override
	public void initViews() {
		mListCheckIn = (ListView) mView.findViewById(R.id.listCheckedIn);
	}

	@Override
	public void initActions() {
		mHandler.post(loadTempData);
		mListCheckIn.setAdapter(adapter);
	}
	Runnable loadTempData = new Runnable() {
		
		@Override
		public void run() {
			adapter.add(new CheckInItem("Lorem ipsum dolor sit amet"));
			adapter.add(new CheckInItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vel est vel eros aliquet iaculis ut sed neque"));
			adapter.add(new CheckInItem("Lorem ipsum dolor sit amet"));
			adapter.add(new CheckInItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vel est vel eros aliquet iaculis ut sed neque"));
			adapter.add(new CheckInItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit"));
			adapter.add(new CheckInItem("Lorem ipsum dolor sit amet"));
			adapter.add(new CheckInItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vel est vel eros aliquet iaculis ut sed neque"));
			adapter.add(new CheckInItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit"));
			adapter.add(new CheckInItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit"));
			adapter.notifyDataSetChanged();
		}
	};
}
