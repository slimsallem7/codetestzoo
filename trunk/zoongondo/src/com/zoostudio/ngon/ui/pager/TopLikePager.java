package com.zoostudio.ngon.ui.pager;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.zoostudio.adapter.SpotAdapter;
import com.zoostudio.adapter.event.OnSpotitemClick;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.task.GetTopLikeSpotTask;
import com.zoostudio.ngon.ui.SearchActivity;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;

public class TopLikePager extends NgonHomePager implements OnPreExecuteDelegate {
	private ListView lvSpot;
	private boolean mFirstDisplay = true;

	public TopLikePager(Integer indexPager) {
		super(indexPager);
	}

	public void onTabSelected(int position) {
		super.onTabSelected(position);
		if (mFirstDisplay) {
			mFirstDisplay = false;
			GetTopLikeSpotTask task = new GetTopLikeSpotTask(
					this.getActivity(), 20);
			task.setOnSpotItemReceiver(this);
			task.setOnDataErrorDelegate(this);
			task.setOnPreExecuteDelegate(this);
			task.execute();
		}
	}

	@Override
	public void initControls() {
		super.initControls();
		if (null == mAdapter) {
			mAdapter = new SpotAdapter(getActivity(),
					new ArrayList<SpotItem>(), null);
		}

		lvSpot = (ListView) findViewById(R.id.spotlist);
		View header = mInflater.inflate(R.layout.item_home_search, null);
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						SearchActivity.class));
			}
		});

		lvSpot.addHeaderView(header, null, false);
		lvSpot.addFooterView(mFooterView);
		lvSpot.setOnItemClickListener(new OnSpotitemClick(getActivity()));
		lvSpot.setAdapter(mAdapter);
	}

	@Override
	public void initVariables() {
	}

	@Override
	protected int getPagerIndex() {
		return mIndexPager;
	}


	@Override
	public void actionPre(RestClientTask task) {
		setUiLoading();
	}
	
	@Override
	public void onSpotItemListener(ArrayList<SpotItem> data) {
		super.onSpotItemListener(data);
		setUiLoadDone();
	}

	@Override
	public void actionDataError(RestClientTask task, int errorCode) {
		setUiLoadError();
	}

}
