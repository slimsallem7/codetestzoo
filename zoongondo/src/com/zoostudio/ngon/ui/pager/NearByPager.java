package com.zoostudio.ngon.ui.pager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoostudio.adapter.SpotAdapter;
import com.zoostudio.adapter.event.OnSpotitemClick;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.task.GetNearbySpotTask;
import com.zoostudio.ngon.ui.SearchActivity;
import com.zoostudio.ngon.views.NgonProgressView;
import com.zoostudio.restclient.RestClientTask;

public class NearByPager extends NgonHomePager implements OnClickListener
		 {

	private static final String TAG = "NearByPager";
	private ListView lvSpot;
	private NgonProgressView mProgressBar;
	private Button mRetry;
	private TextView mMessage;
	private ProgressDialog progressDialog;

	private RelativeLayout mFooterView;
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
	public void onAttach(Activity activity) {
		Tag = "NearByPager";
		super.onAttach(activity);
	}

	@Override
	public void initControls() {
		if (null == mAdapter) {
			mAdapter = new SpotAdapter(getActivity(), new ArrayList<SpotItem>(),
					null);
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

		mProgressBar = (NgonProgressView) findViewById(R.id.progressbar);

		mMessage = (TextView) findViewById(R.id.message);
		mRetry = (Button) findViewById(R.id.retry);

		mFooterView = (RelativeLayout) getLayoutInflater(null).inflate(
				R.layout.item_loading_more, null);
		mFooterView.setVisibility(View.GONE);
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
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		mAdapter.clear();
		mAdapter.notifyDataSetChanged();
		GetNearbySpotTask spotTask = new GetNearbySpotTask(getActivity(),
				latitude, longitude, 0,
				mParent.getCurrentPositionDistance() + 1);
		spotTask.setOnSpotItemReceiver(this);
		spotTask.setOnDataErrorDelegate(this);
		spotTask.execute();
		setUiLoading();
	}

	private void setUiLoading() {
		mMessage.setVisibility(View.GONE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
		Log.e(TAG, "SetUiLoading");
	}
	@SuppressWarnings("unused")
	private void setUiLoadError() {
		mMessage.setText(getString(R.string.lang_vi_spotlist_error_message));
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.VISIBLE);
	}
	@SuppressWarnings("unused")
	private void setUiLoadEmpty() {
		mMessage.setText(getString(R.string.lang_vi_spotlist_nearby_empty_message));
		mMessage.setVisibility(View.VISIBLE);
	}
	
	private void setUiLoadDone() {
		if (mFooterView.getVisibility() == View.GONE) {
			mFooterView.setVisibility(View.VISIBLE);
		}
		mProgressBar.setVisibility(View.GONE);
	}

	@Override
	protected void initActions() {
	}

	@Override
	public void onLocationChanged(Location location) {
		if (null != progressDialog && progressDialog.isShowing())
			progressDialog.dismiss();
		mAdapter.setCurrentLocation(location);
		getSpotData(location);
	}

	@Override
	public void onGettingLocation() {
		super.onGettingLocation();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				progressDialog = ProgressDialog.show(mParent, "Service",
						"Dang lay location");
			}
		});

	}
	
	@Override
	public void onSpotItemListener(ArrayList<SpotItem> data) {
		super.onSpotItemListener(data);
		setUiLoadDone();
	}
	
	@Override
	public void actionDataError(RestClientTask task,int errorCode) {
		setUiLoadError();
	}
}
