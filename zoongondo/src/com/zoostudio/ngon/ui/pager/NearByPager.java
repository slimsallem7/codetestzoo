package com.zoostudio.ngon.ui.pager;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.zoostudio.adapter.SpotAdapter;
import com.zoostudio.adapter.event.OnSpotitemClick;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonProgressDialog;
import com.zoostudio.ngon.task.GetNearbySpotTask;
import com.zoostudio.ngon.ui.SearchActivity;

public class NearByPager extends NgonHomePager implements OnClickListener
		 {
	private NgonProgressDialog mProgressLocation;
	/**
	 * Dùng để lưu trạng thái đã request more hay chưa. Được đặt true khi bắt
	 * đầu request more; Đặt false khi có dữ liệu trả về. Khi có thêm request
	 * lấy more mà trước đó đã có request more và chưa có dữ liệu trả về (tức
	 * đang là true) thì không làm gì cả, ngược lại thì request more;
	 */
	boolean firedRequestMore = false;
	private double mLongitude;
	private double mLatitude;
	private GetNearbySpotTask mLoadMoreSpotTask;
	private GetNearbySpotTask mLoadNewSpotTask;

	public NearByPager(Integer indexPager) {
		super(indexPager);
	}

	public NearByPager() {
		super();
	}

	@Override
	public void initControls() {
		super.initControls();
		mProgressLocation = new NgonProgressDialog(this.getActivity());
		mProgressLocation.setCancelable(true);

		mProgressLocation.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				NearByPager.this.getActivity().finish();
			}
		});

		if (null == mAdapter) {
			mAdapter = new SpotAdapter(getActivity(),
					new ArrayList<SpotItem>(), null);
		}
		mAdapter.setRequestMoreListener(new SpotAdapter.IOnRequestMoreListener() {
			@Override
			public void request() {
				if (firedRequestMore)
					return;
			}
		});

		
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
		lvSpot.addFooterView(mFooterView);

		lvSpot.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		lvSpot.setAdapter(mAdapter);
	}

	@Override
	public void initVariables() {
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onFragmentResult(Bundle bundle, int requestCode, int resultCode) {
		super.onFragmentResult(bundle, requestCode, resultCode);
		String result = bundle.getString("RESULT");
		result = result + " result Code = " + resultCode;
	}

	@Override
	protected int getPagerIndex() {
		return mIndexPager;
	}

	private void getSpotData(Location location) {
		mLongitude = location.getLongitude();
		mLatitude = location.getLatitude();
		refreshSpotItem();
	}

	@Override
	public void onLocationChanged(Location location) {
		if (null != mProgressLocation && mProgressLocation.isShowing())
			mProgressLocation.dismiss();
		mAdapter.setCurrentLocation(location);
		getSpotData(location);
	}

	@Override
	public void onGettingLocation() {
		super.onGettingLocation();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mProgressLocation.setMessage(R.string.request_location);
				mProgressLocation.show();
			}
		});

	}


	@Override
	protected void loadMoreSpotItem() {
		if (mLoadMoreSpotTask != null && mLoadMoreSpotTask.isLoading())
			return;
		super.loadMoreSpotItem();
		mLoadMoreSpotTask = new GetNearbySpotTask(getActivity(), mLatitude,
				mLongitude, 0, mParent.getCurrentPositionDistance() + 1);
		mLoadMoreSpotTask.setOnSpotItemReceiver(this);
		mLoadMoreSpotTask.setOnDataErrorDelegate(this);
		mLoadMoreSpotTask.setOnPreExecuteDelegate(this);
		mLoadMoreSpotTask.execute();
	}

	@Override
	protected void refreshSpotItem() {
		super.refreshSpotItem();
		mLoadNewSpotTask = new GetNearbySpotTask(getActivity(), mLatitude,
				mLongitude, 0, mParent.getCurrentPositionDistance() + 1);
		mLoadNewSpotTask.setOnSpotItemReceiver(this);
		mLoadNewSpotTask.setOnDataErrorDelegate(this);
		mLoadNewSpotTask.setOnPreExecuteDelegate(this);
		mLoadNewSpotTask.execute();
	}

}
