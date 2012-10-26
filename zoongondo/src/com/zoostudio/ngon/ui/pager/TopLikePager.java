package com.zoostudio.ngon.ui.pager;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.zoostudio.adapter.SpotAdapter;
import com.zoostudio.adapter.event.OnSpotitemClick;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.task.GetTopLikeSpotTask;
import com.zoostudio.ngon.ui.SearchActivity;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;

public class TopLikePager extends NgonHomePager implements OnPreExecuteDelegate {
	private boolean mFirstDisplay = true;
	private GetTopLikeSpotTask mLoadNewSpotTask;
	private GetTopLikeSpotTask mLoadMoreSpotTask;
	public TopLikePager(Integer indexPager) {
		super(indexPager);
	}

	public void onTabSelected() {
		super.onTabSelected();
		if (mFirstDisplay) {
			mFirstDisplay = false;
			refreshSpotItem();
		}
	}
	

	@Override
	public void initControls() {
		super.initControls();
		if (null == mAdapter) {
			mAdapter = new SpotAdapter(getActivity(),
					new ArrayList<SpotItem>(), null);
		}

		View header = mInflater.inflate(R.layout.item_home_search, null);
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						SearchActivity.class));
			}
		});

		lvSpot.addHeaderView(header, null, false);
		lvSpot.setOnItemClickListener(new OnSpotitemClick(getActivity()));
		lvSpot.setAdapter(mAdapter);
	}

	@Override
	public void initVariables() {
		super.initVariables();
	}

	@Override
	protected int getPagerIndex() {
		return mIndexPager;
	}

	@Override
	protected void loadMoreSpotItem() {
		if (mLoadMoreSpotTask != null && mLoadMoreSpotTask.isLoading())
			return;
		super.loadMoreSpotItem();
		mLoadMoreSpotTask = new GetTopLikeSpotTask(
				this.getActivity(), 20);
		mLoadMoreSpotTask.setOnSpotItemReceiver(this);
		mLoadMoreSpotTask.setOnDataErrorDelegate(this);
		mLoadMoreSpotTask.setOnPreExecuteDelegate(this);
		mLoadMoreSpotTask.execute();
	}
	
	@Override
	protected void refreshSpotItem() {
		super.refreshSpotItem();
		mLoadNewSpotTask = new GetTopLikeSpotTask(
				this.getActivity(), 20);
		mLoadNewSpotTask.setOnSpotItemReceiver(this);
		mLoadNewSpotTask.setOnDataErrorDelegate(this);
		mLoadNewSpotTask.setOnPreExecuteDelegate(this);
		mLoadNewSpotTask.execute();
	}
}
