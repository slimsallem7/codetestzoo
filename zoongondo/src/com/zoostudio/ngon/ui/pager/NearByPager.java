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
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.ngon.views.NgonProgressView;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;

public class NearByPager extends NgonHomePager implements OnClickListener,
		OnPostExecuteDelegate {

	private static final String TAG = "NearByPager";
	private ListView lvSpot;
	private SpotAdapter adapter;
	private NgonProgressView mProgressBar;
	private Button mRetry;
	private TextView mMessage;
	private ProgressDialog progressDialog;

	private RelativeLayout mFooterView;
	private String[] imageDumps = {
			"http://i-cdn.apartmenttherapy.com/uimages/kitchen/2012_10_09-ProsciuttoArugulaPizza01.jpg",
			"http://i136.photobucket.com/albums/q197/bermuda2810/main_dish_image-1.jpg",
			"http://i-cdn.apartmenttherapy.com/uimages/kitchen/2012-10-09-ThinCrustPizza-2.jpg",
			"http://i-cdn.apartmenttherapy.com/uimages/kitchen/brownie%20pie.jpeg",
			"http://i-cdn.apartmenttherapy.com/uimages/kitchen/2012_09_26-bakewell4.jpg",
			"http://i-cdn.apartmenttherapy.com/uimages/kitchen/2012_06_04-Berries.jpeg",
			"http://www.thekitchn.com/dessert-recipe-baked-nutella-cream-cheese-sandwich-recipes-from-the-kitchn-177279",
			"http://img.foodnetwork.com/FOOD/2010/03/25/FNM_050110-Weeknight-Dinners-032_s4x3_lg.jpg",
			"http://img.foodnetwork.com/FOOD/2011/11/14/FNM_120111-WN-Dinners-009_s4x3_lg.jpg",
			"http://img.foodnetwork.com/FOOD/2012/05/04/FNM_060112-50-Things-to-Grill-in-Foil-Jerk-Chicken_s4x3_lg.jpg" };
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

			setUiLoadDone();

			try {
				JSONArray spotData = result.getJSONArray("data");
				int size = spotData.length();

				if (size == 0) {
					setUiLoadEmpty();
					return;
				}
				// if(null!=mFooterViewAddNew.getParent()){
				// mFooterView.addView(mFooterViewAddNew);
				// }
				adapter.clear();
				for (int i = 0; i < size; i++) {
					JSONObject item = spotData.getJSONObject(i);
					SpotItem spotItem = ParserUtils.parseSpot(item);
					spotItem.setUrlImageSpot(imageDumps[i]);
					adapter.add(spotItem);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				setUiLoadError();
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
		setUiLoading();
	}

	private void setUiLoading() {
		mMessage.setVisibility(View.GONE);
		mRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
		Log.e(TAG, "SetUiLoading");
	}

	private void setUiLoadError() {
		mMessage.setText(getString(R.string.lang_vi_spotlist_error_message));
		mMessage.setVisibility(View.VISIBLE);
		mRetry.setVisibility(View.VISIBLE);
	}

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
