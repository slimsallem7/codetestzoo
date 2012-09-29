package com.zoostudio.ngon.ui.pager;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
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
import com.zoostudio.ngon.task.GetNearbySpotTask;
import com.zoostudio.ngon.ui.SearchActivity;
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;

public class NearByPager extends NgonHomePager implements OnClickListener,
		OnPostExecuteDelegate {

	private ListView lvSpot;
	private SpotAdapter adapter;
	private ProgressBar mProgressBar;
	private Button mRetry;
	private TextView mMessage;
	private ProgressDialog progressDialog;

	// private ButtonListitemAdd mFooterViewAddNew;
	// private ProgressBar mFooterViewLoading;
	//
	// private RelativeLayout mFooterView;

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

	@Override
	public void onAttach(Activity activity) {
		Tag = "NearByPager";
		super.onAttach(activity);
	}

	@Override
	public void initControls() {
		if (null == adapter) {
			adapter = new SpotAdapter(getActivity(), new ArrayList<SpotItem>(),
					null);
		}
		adapter.setRequestMoreListener(new SpotAdapter.IOnRequestMoreListener() {
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

		//
		// mProgressBar = (ProgressBar)
		// mView.findViewById(R.id.spotlist_progress);
		// mMessage = (TextView) mView.findViewById(R.id.spotlist_message);
		// mRetry = (Button) mView.findViewById(R.id.spotlist_retry);
		//
		// mFooterView = new RelativeLayout(getApplicationContext());
		// mFooterView.setGravity(Gravity.CENTER);
		//
		// mFooterViewLoading = new ProgressBar(getApplicationContext());
		// mFooterViewLoading.setLayoutParams(new LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//
		// mFooterViewAddNew = new ButtonListitemAdd(getApplicationContext());
		// mFooterViewAddNew.setText("Thêm địa điểm");
		// mFooterViewAddNew.setLayoutParams(new LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// mFooterViewAddNew.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (NgonLocationManager.getInstance(null).isLocationReady()) {
		// Intent intent = new Intent(mParent, AddSpot.class);
		// intent.putExtra("CURRENT_ADDRESS",
		// mParent.mLocationAddress.getText());
		// double currentLat = -1, currentLong = -1;
		// try {
		// currentLat = NgonLocationManager.getInstance(null)
		// .getCurrentLocation().getLatitude();
		// currentLong = NgonLocationManager.getInstance(null)
		// .getCurrentLocation().getLongitude();
		// } catch (ZooLocationException e) {
		// Logger.e(Tag,
		// "Error@onFooterViewClick:" + e.getMessage(), e);
		// }
		//
		// intent.putExtra("CURRENT_LAT", currentLat);
		// intent.putExtra("CURRENT_LONG", currentLong);
		// startActivity(intent);
		// } else {
		// Toast.makeText(mParent, "Location not ready",
		// Toast.LENGTH_SHORT);
		// }
		// }
		// });
		//
		// lvSpot.addFooterView(mFooterView);
		//
		// // initUiLoading();
		lvSpot.setAdapter(adapter);
	}

	// @Override
	// public void onLocationChanged(Location location) {
	// super.onLocationChanged(location);
	// adapter.setCurrentLocation(location);
	// getSpotData(location);
	// }

	// @Override
	// public void onLocationReceiver(Location location) {
	// super.onLocationReceiver(location);
	// if (null != progressDialog && progressDialog.isShowing())
	// progressDialog.dismiss();
	// adapter.setCurrentLocation(location);
	// getSpotData(location);
	// }

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

	@Override
	public void actionPost(RestClientTask task, JSONObject result) {
		if (task instanceof GetNearbySpotTask) {

			try {
				JSONArray spotData = result.getJSONArray("data");
				int size = spotData.length();

				if (size == 0) {
					// initUiLoadEmpty();
					return;
				}

				// initUiLoadDone();
				// if(null!=mFooterViewAddNew.getParent()){
				// mFooterView.addView(mFooterViewAddNew);
				// }
				adapter.clear();
				for (int i = 0; i < size; i++) {
					JSONObject item = spotData.getJSONObject(i);
					SpotItem spotItem = ParserUtils.parseSpot(item);
					adapter.add(spotItem);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				// TODO @duynt
				// Hiện tại chưa biết tình huống không lấy được dữ liệu từ phía
				// server
				// (có kết nối, tuy nhiên lỗi xảy ra phía server khiến không có
				// dữ liệu nào phù hợp trả về)
				// Khi biết sẽ đặt hàm initUiError() vào đó.
				// initUiError();
			} catch (Exception e) {
				e.printStackTrace();
			}
			adapter.notifyDataSetChanged();
		}
	}

	private void getSpotData(Location location) {
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		adapter.clear();
		adapter.notifyDataSetChanged();
		GetNearbySpotTask spotTask = new GetNearbySpotTask(getActivity(),
				latitude, longitude, 0,
				mParent.getCurrentPositionDistance() + 1);
		spotTask.setOnPostExecuteDelegate(this);
		spotTask.execute();
	}

	private void initUiLoading() {
		mMessage.setText(getString(R.string.lang_vi_spotlist_loading_message));
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	private void initUiError() {
		mMessage.setText(getString(R.string.lang_vi_spotlist_error_message));
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	private void initUiLoadEmpty() {
		mMessage.setText(getString(R.string.lang_vi_spotlist_nearby_empty_message));
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
	}

	private void initUiLoadDone() {
		mMessage.setText("");
		mMessage.setVisibility(View.GONE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
	}

	@Override
	protected void initActions() {
	}

	@Override
	public void onLocationChanged(Location location) {
		if (null != progressDialog && progressDialog.isShowing())
			progressDialog.dismiss();
		adapter.setCurrentLocation(location);
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
}
