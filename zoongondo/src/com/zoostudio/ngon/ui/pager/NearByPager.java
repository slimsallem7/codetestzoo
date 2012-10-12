package com.zoostudio.ngon.ui.pager;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.zoostudio.adapter.SpotAdapter;
import com.zoostudio.adapter.event.OnSpotitemClick;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonProgressDialog;
import com.zoostudio.ngon.task.GetNearbySpotTask;
import com.zoostudio.ngon.ui.SearchActivity;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;
import com.zoostudio.service.impl.NgonLocationManager;

public class NearByPager extends NgonHomePager implements OnClickListener,
		OnPreExecuteDelegate {
	private ListView lvSpot;
	private NgonProgressDialog mProgressLocation;
	/**
	 * Dùng để lưu trạng thái đã request more hay chưa. Được đặt true khi bắt
	 * đầu request more; Đặt false khi có dữ liệu trả về. Khi có thêm request
	 * lấy more mà trước đó đã có request more và chưa có dữ liệu trả về (tức
	 * đang là true) thì không làm gì cả, ngược lại thì request more;
	 */
	boolean firedRequestMore = false;

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

		lvSpot = (ListView) mView.findViewById(R.id.spotlist);
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
		Log.i("Pager", "getSpotData");
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		mAdapter.clear();
		mAdapter.notifyDataSetChanged();
		GetNearbySpotTask spotTask = new GetNearbySpotTask(getActivity(),
				latitude, longitude, 0,
				mParent.getCurrentPositionDistance() + 1);
		spotTask.setOnSpotItemReceiver(this);
		spotTask.setOnDataErrorDelegate(this);
		spotTask.setOnPreExecuteDelegate(this);
		spotTask.execute();
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
	public void onSpotItemListener(ArrayList<SpotItem> data) {
		super.onSpotItemListener(data);
		setUiLoadDone();
	}

	@Override
	public void actionDataError(RestClientTask task, int errorCode) {
		setUiLoadError();
	}

	@Override
	public void actionPre(RestClientTask task) {
		mState = LOADING_STATE;
		setUiLoading();
	}
}
