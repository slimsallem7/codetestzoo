package com.zoostudio.ngon.ui.pager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zoostudio.adapter.SpotAdapter;
import com.zoostudio.adapter.event.OnSpotitemClick;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.task.GetTopNewSpotTask;
import com.zoostudio.ngon.ui.SearchActivity;

public class TopNewPager extends NgonHomePager  {
	private ListView lvSpot;
	private ProgressBar mProgressBar;
	private TextView mMessage;
	private Button mRetry;
	private boolean mFirstDisplay = true;

	public TopNewPager(Integer indexPager) {
		super(indexPager);
	}

	@Override
	public void onAttach(Activity activity) {
		Tag = "TopNewPager";
		super.onAttach(activity);
	}

	@Override
	protected void onTabSelected(int position) {
		super.onTabSelected(position);
		if (mFirstDisplay) {
			GetTopNewSpotTask task = new GetTopNewSpotTask(getActivity(), 20);
			task.setOnSpotItemReceiver(this);
			task.setOnDataErrorDelegate(this);
			task.execute();
			mFirstDisplay = false;
		}
	}

	@Override
	public void initControls() {
//		mProgressBar = (ProgressBar) findViewById(R.id.spotlist_progress);
//		mMessage = (TextView) findViewById(R.id.spotlist_message);
//		mRetry = (Button) findViewById(R.id.spotlist_retry);
		
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
//		initUiLoading();
	}

	@Override
	public void initVariables() {
	}

	@Override
	protected int getPagerIndex() {
		return mIndexPager;
	}

	@Override
	protected void initActions() {
	}

	@SuppressWarnings("unused")
	private void initUiLoading() {
		
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}
	@SuppressWarnings("unused")
	private void initUiError() {
		mMessage.setText(getString(R.string.lang_vi_spotlist_error_message));
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}
	@SuppressWarnings("unused")
	private void initUiLoadEmpty() {
		mMessage.setText(getString(R.string.lang_vi_spotlist_nearby_empty_message));
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
	}
	@SuppressWarnings("unused")
	private void initUiLoadDone() {
		mMessage.setText("");
		mMessage.setVisibility(View.GONE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
	}

}
