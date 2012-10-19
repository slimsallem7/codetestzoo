package com.zoostudio.ngon.ui.pager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.zoostudio.adapter.SpotAdapter;
import com.zoostudio.adapter.event.OnSpotitemClick;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.task.GetTopNewSpotTask;
import com.zoostudio.ngon.ui.SearchActivity;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;

public class TopNewPager extends NgonHomePager implements OnPreExecuteDelegate{
	private boolean mFirstDisplay = true;
	private GetTopNewSpotTask mLoadMoreSpotTask;
	private GetTopNewSpotTask mLoadNewSpotTask;

	public TopNewPager(Integer indexPager) {
		super(indexPager);
	}

	@Override
	public void onAttach(Activity activity) {
		Tag = "TopNewPager";
		super.onAttach(activity);
	}

	public void onTabSelected() {
		super.onTabSelected();
		if (mFirstDisplay) {
			refreshSpotItem();
			mFirstDisplay = false;
		}
	}

	@Override
	public void initControls() {
		super.initControls();
		if (null == mAdapter) {
			mAdapter = new SpotAdapter(getActivity(), new ArrayList<SpotItem>(),
					null);
		}
		View header = mInflater.inflate(R.layout.item_home_search, null);
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), SearchActivity.class));
			}
		});
		
		lvSpot.addHeaderView(header,null,false);
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
	protected void refreshSpotItem() {
		super.refreshSpotItem();
		mLoadNewSpotTask = new GetTopNewSpotTask(getActivity(), 20);
		mLoadNewSpotTask.setOnSpotItemReceiver(this);
		mLoadNewSpotTask.setOnDataErrorDelegate(this);
		mLoadNewSpotTask.setOnPreExecuteDelegate(this);
		mLoadNewSpotTask.execute();
	}
	@Override
	protected void loadMoreSpotItem() {
		if (mLoadMoreSpotTask != null && mLoadMoreSpotTask.isLoading())
			return;
		super.loadMoreSpotItem();
		mLoadMoreSpotTask = new GetTopNewSpotTask(getActivity(), 20);
		mLoadMoreSpotTask.setOnSpotItemReceiver(this);
		mLoadMoreSpotTask.setOnDataErrorDelegate(this);
		mLoadMoreSpotTask.setOnPreExecuteDelegate(this);
		mLoadMoreSpotTask.execute();
	}
}
