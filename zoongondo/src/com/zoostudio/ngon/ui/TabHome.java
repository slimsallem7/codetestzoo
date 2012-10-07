package com.zoostudio.ngon.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;
import com.zoostudio.adapter.NgonPagerAdapter;
import com.zoostudio.custom.view.ZooHorizontalScrollView;
import com.zoostudio.custom.view.ZooLinearLayout;
import com.zoostudio.exception.ZooLocationException;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.RequestCode;
import com.zoostudio.ngon.task.GetAddressFromGeoTask;
import com.zoostudio.ngon.task.GetSearchSpotTask;
import com.zoostudio.ngon.ui.base.BaseFragmentActivity;
import com.zoostudio.ngon.ui.base.BaseMapActivity;
import com.zoostudio.ngon.ui.pager.NgonHomePager;
import com.zoostudio.ngon.utils.OnAddressChanged;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;
import com.zoostudio.service.impl.NgonLocationManager;

public class TabHome extends BaseFragmentActivity implements
		OnPostExecuteDelegate, OnAddressChanged,
		android.view.View.OnClickListener {

	public static final int RESULT_CHANGE_TO_MANUAL = 99;
	public static final int RESULT_CHANGE_TO_AUTO = 91;
	private NgonPagerAdapter mHomeAdapter;
	private ViewPager mTabsPager;
	private TitlePageIndicator mIndicator;
	private ImageButton mBtnMenu;
	private EditText mTextSearch;
	private ProgressBar mProgressBarSearch;
	private GetSearchSpotTask mSearchTask;
	private ZooHorizontalScrollView mScrollView;
	private View mParentMenu;
	private View mIncMainHome;
	private LayoutInflater inflater;
	private ZooLinearLayout mMainPagerContent;
	private ImageButton mBtnSearch;
	private LinearLayout mSearchWrapper;
	private ImageButton mBtnCancelSearch;
	private Button mBtnChangeDistance;
	private LinearLayout mFooterHome;

	private View mBarTitle;

	protected int getLayoutId() {
		return R.layout.activity_pager;
	}

	protected void initVariable() {
		inflater = this.getLayoutInflater();
		mPagerIndex = new ArrayList<Integer>();
		mFragmentManager = this.getSupportFragmentManager();
		String[] titles = this.getResources().getStringArray(R.array.titles_home_screen);
		mHomeAdapter = new NgonPagerAdapter(this,
				this.getSupportFragmentManager(),titles);
	}

	protected void initScreen() {
//		Display display = this.getWindowManager().getDefaultDisplay();
//		mScrollView = (ZooHorizontalScrollView) this
//				.findViewById(R.id.scrollView);
//		mFooterHome = (LinearLayout) this.findViewById(R.id.footer_home);
//		MenuFragment fragment = (MenuFragment) mFragmentManager
//				.findFragmentById(R.id.menuHomeScreen);
//		mParentMenu = fragment.getParentMenu();
		mCurrentPositionDistance = 0;
//		mIncMainHome = inflater.inflate(R.layout.inc_main_home_screen, null);
//		mBarTitle = mIncMainHome.findViewById(R.id.actionbar);
//		mSearchWrapper = (LinearLayout) mIncMainHome
//				.findViewById(R.id.search_wrapper);
//		mBtnCancelSearch = (ImageButton) mIncMainHome
//				.findViewById(R.id.btn_cancel_search);
//		mMainPagerContent = (ZooLinearLayout) mIncMainHome
//				.findViewById(R.id.home_info);
//		mBtnMenu = (ImageButton) mIncMainHome.findViewById(R.id.btn_menu);
//		mBtnSearch = (ImageButton) mIncMainHome.findViewById(R.id.btn_search);
		mTabsPager = (ViewPager) this.findViewById(R.id.content_pager);
//		mBtnChangeDistance = (Button) mIncMainHome
//				.findViewById(R.id.btnChangeDistance);
//		mLocationAddress = (TextView) this.findViewById(R.id.location_address);
//		mTextSearch = (EditText) mIncMainHome
//				.findViewById(R.id.edt_home_search);
//		mProgressBarSearch = (ProgressBar) mIncMainHome
//				.findViewById(R.id.home_search_progress);
//
//		mBtnSearch.setOnClickListener(this);
//		mBtnCancelSearch.setOnClickListener(this);
//		mBtnChangeDistance.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dw = new ZooSeekBarPopUp(mBarTitle, mCurrentPositionDistance);
//				dw.setOnChangeListener(new ZooSeekBarPopUp.OnChangeListener() {
//					@Override
//					public void onChange(int value) {
//						mCurrentPositionDistance = value;
//						value++;
//						mBtnChangeDistance.setText("" + value + " km");
//					}
//				});
//				dw.showLikePopDownMenu(R.style.Animations_DialogChangeDistanceGrowFromTop);
//			}
//		});
//
//		mTextSearch.addTextChangedListener(new NgonTextWatcher() {
//			@Override
//			public void onTextRelease() {
//				super.onTextRelease();
//				mHandler.post(new Runnable() {
//					@Override
//					public void run() {
//						mSearchTask = new GetSearchSpotTask(MainScreen.this,
//								mTextSearch.getText().toString(), 0, 0);
//						mSearchTask
//								.setOnPostExecuteDelegate(new OnSearchResult());
//						mSearchTask.execute();
//						mProgressBarSearch.setVisibility(View.VISIBLE);
//					}
//				});
//			}
//		});
//		mFooterHome.setOnClickListener(new android.view.View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				locationEdit();
//			}
//		});
//		// Create a transparent view that pushes the other views in the HSV to
//		// the right.
//		// This transparent view allows the menu to be shown when the HSV is
//		// scrolled.
//		View transparent = new TextView(this);
//		transparent.setBackgroundColor(Color.parseColor("#00000000"));
//
//		final View[] children = new View[] { transparent, mIncMainHome };
//
//		// Scroll to app (view[1]) when layout finished.
//		int scrollToViewIdx = 1;
//
//		mListenerForScrolling = new ClickListenerForScrolling(mScrollView,
//				mParentMenu, display.getWidth()) {
//			@Override
//			protected void onClickToSlide() {
//				mMainPagerContent.changeTouchAble();
//				boolean enable = !mTextSearch.isEnabled();
//				mTextSearch.setEnabled(enable);
//			}
//		};
//		mScrollView.initViews(children, scrollToViewIdx,
//				new SizeCallbackForMenu(mBtnMenu, mListenerForScrolling),
//				mBtnMenu);
//
////		fragment.setClickListenerForScrolling(mListenerForScrolling);
//		mBtnMenu.setOnClickListener(mListenerForScrolling);
//
		mTabsPager.setAdapter(mHomeAdapter);
//		mIndicator = (TitlePageIndicator) this.findViewById(R.id.title_pager);
//		mIndicator.setOnPageChangeListener(this);
		mIndicator = (TitlePageIndicator) this.findViewById(R.id.indicator);
		mIndicator.setOnPageChangeListener(this);
		mIndicator.setViewPager(mTabsPager);
	}

	private class OnSearchResult implements OnPostExecuteDelegate {
		@Override
		public void actionPost(RestClientTask task, JSONObject result) {
			mProgressBarSearch.setVisibility(View.INVISIBLE);
			Toast.makeText(TabHome.this.getApplicationContext(), "Ngon....",
					Toast.LENGTH_SHORT).show();
		}
	}
	
//	@Override
//	public void onLocationChange(Location location) {
//		NgonHomePager pager = getFragmentPager(mCurrentPager);
//		pager.onLocationChanged(location);
//
//		LocationUtil locationAddress = new LocationUtil();
//		locationAddress.setOnAddressChangedListener(this);
//		locationAddress.getAddress(getBaseContext(), location);
//
//		Log.e(getClass().getName(), "onLocationChange:" + location.toString());
//	}

	public void locationEdit() {
		Intent intent = new Intent(getApplicationContext(), ActivitySelectLocationManual.class);
		intent.putExtra(BaseMapActivity.EXTRA_CURRENT_ADDRESS, mLocationAddress.getText());
		double currentLat = -1, currentLong = -1;
		try {
			currentLat = NgonLocationManager.getInstance(null)
					.getCurrentLocation().getLatitude();
			currentLong = NgonLocationManager.getInstance(null)
					.getCurrentLocation().getLongitude();
			intent.putExtra(BaseMapActivity.EXTRA_CURRENT_LAT, currentLat);
			intent.putExtra(BaseMapActivity.EXTRA_CURRENT_LONG, currentLong);

			overridePendingTransition(R.anim.fade_out,
					R.anim.activity_slide_in_from_bottom);
			startActivityForResult(intent, RequestCode.REQUEST_LOCATION_MANUAL);
			
		} catch (ZooLocationException e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent returnedIntent) {
		Log.i("Activity","Activity Result");
		super.onActivityResult(requestCode, resultCode, returnedIntent);
		switch (requestCode) {

		case RequestCode.REQUEST_LOCATION_GPS_SERVICE:
			mCurrentRequestType = RequestCode.REQUEST_LOCATION_GPS_SERVICE;
			mRestartActivity = true;
			break;
		case RequestCode.REQUEST_LOCATION_NETWORK_SERVICE:
			mCurrentRequestType = RequestCode.REQUEST_LOCATION_NETWORK_SERVICE;
			mRestartActivity = true;
			break;
		case RequestCode.REQUEST_LOCATION_MANUAL:
			if (resultCode == RESULT_CANCELED)
				break;
			else if (resultCode == RESULT_CHANGE_TO_MANUAL) {
				updateNewAddress(returnedIntent);
				NgonLocationManager.getInstance(null).notifiChangeLocation();
				break;
			} else if (resultCode == RESULT_CHANGE_TO_AUTO) {
				NgonLocationManager.getInstance(null).notifiChangeLocation();
				break;
			}
			break;
		}
	}

	private void updateNewAddress(Intent intent) {
		String address = intent.getExtras().getString(ActivitySelectLocationManual.RESULT_NEW_ADDRESS);
		this.mLocationAddress.setText(address);
	}

	@Override
	public void actionPost(RestClientTask task, JSONObject result) {
		if (task instanceof GetAddressFromGeoTask) {
			try {
				boolean status = result.getBoolean("status");
				if (!status)
					return;
			} catch (JSONException e) {
			}
		}
	}

	@Override
	public void onAddressChange(final String newAddress) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mLocationAddress.setText(newAddress);
			}
		});
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		NgonHomePager fragment = getFragmentPager(position);
		fragment.doTabSelected(position);
	}

	// TODO :@huy Lam animation cho nut search o man hinh MainScreen
	// XML Neu can thay doi inc_main_home_screen.xml (id = search_wrapper)
//	@Override
	public void onClick(View v) {
//		if (v.getId() == R.id.btn_search) {
//			mBtnMenu.setOnClickListener(null);
//			mSearchWrapper.setVisibility(View.VISIBLE);
//		} else if (v.getId() == R.id.btn_cancel_search) {
//			mBtnMenu.setOnClickListener(mListenerForScrolling);
//			mSearchWrapper.setVisibility(View.GONE);
//		}
	}
}