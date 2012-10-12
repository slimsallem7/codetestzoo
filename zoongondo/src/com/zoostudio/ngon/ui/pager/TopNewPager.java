package com.zoostudio.ngon.ui.pager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.zoostudio.adapter.SpotAdapter;
import com.zoostudio.adapter.event.OnSpotitemClick;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.task.GetTopNewSpotTask;
import com.zoostudio.ngon.ui.SearchActivity;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;

public class TopNewPager extends NgonHomePager implements OnPreExecuteDelegate{
	private ListView lvSpot;
	private boolean mFirstDisplay = true;

	public TopNewPager(Integer indexPager) {
		super(indexPager);
	}

	@Override
	public void onAttach(Activity activity) {
		Tag = "TopNewPager";
		super.onAttach(activity);
	}

	public void onTabSelected(int position) {
		super.onTabSelected(position);
		if (mFirstDisplay) {
			GetTopNewSpotTask task = new GetTopNewSpotTask(getActivity(), 20);
			task.setOnSpotItemReceiver(this);
			task.setOnDataErrorDelegate(this);
			task.setOnPreExecuteDelegate(this);
			task.execute();
			setUiLoading();
			mFirstDisplay = false;
		}
	}

	@Override
	public void initControls() {
		super.initControls();
		if (null == mAdapter) {
			mAdapter = new SpotAdapter(getActivity(), new ArrayList<SpotItem>(),
					null);
//			mMessage.setText(getString(R.string.lang_vi_spotlist_loading_message));
			
		}
		lvSpot = (ListView) findViewById(R.id.spotlist);
		View header = mInflater.inflate(R.layout.item_home_search, null);
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), SearchActivity.class));
			}
		});
		
		lvSpot.addHeaderView(header,null,false);
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
