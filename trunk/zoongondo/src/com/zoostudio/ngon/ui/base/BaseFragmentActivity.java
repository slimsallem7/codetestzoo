package com.zoostudio.ngon.ui.base;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.TextView;

import com.zoostudio.custom.view.ClickListenerForScrolling;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.RequestCode;
import com.zoostudio.ngon.dialog.NgonDialog;
import com.zoostudio.ngon.dialog.NgonDialog.Builder;
import com.zoostudio.ngon.dialog.NgonProgressDialog;
import com.zoostudio.ngon.ui.pager.NgonHomePager;
import com.zoostudio.service.impl.NgonLocation;
import com.zoostudio.service.impl.NgonLocationManager;
import com.zoostudio.ui.manager.ScreenManager;

public abstract class BaseFragmentActivity extends FragmentActivity implements
		NgonLocationManager.OnNgonServiceListener, OnClickListener,OnPageChangeListener {
	public TextView mLocationAddress;
	protected Handler mHandler;
	protected String TAG = "BaseFragmentActivity";
	protected FragmentManager mFragmentManager;
	protected int mRequestCode;
	private NgonProgressDialog mProgressWaiting;
	protected String mLastScreen;
	protected ScreenManager mManagerScreen;
	protected IOnBackPressed mListener;
	protected int mCurrentService;
	protected ArrayList<Integer> mPagerIndex;
	private boolean isFirtTimeLoad;
	protected int mCurrentPager;
	protected ClickListenerForScrolling mListenerForScrolling;
	private StringBuilder mBuilderTab;
	protected int mCurrentPositionDistance;
	private String mContentError;
	protected int mCurrentRequestType;
	private boolean mIsFirstTime = true;
	protected boolean mRestartActivity = false;
	protected String mContentChooseDialogService;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(getLayoutId());
		mContentError = this.getResources().getString(
				R.string.error_message_start_location_service);
		mContentChooseDialogService = this.getResources().getString(
				R.string.string_content_dialog_choose_service);
		NgonLocationManager.getInstance(this);
		mHandler = new Handler();
		mProgressWaiting = new NgonProgressDialog(this);
		mBuilderTab = new StringBuilder(128);
		initVariable();
		initScreen();
	}

	protected abstract int getLayoutId();

	protected void initScreen() {
	}

	@Override
	protected void onPause() {
		super.onPause();
		NgonLocationManager.getInstance(null).unBind();
		NgonLocationManager.getInstance(null).resetAll();
	}

	protected void startService(int type) {
		mCurrentService = type;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mProgressWaiting.setMessage("Service starting..");
				mProgressWaiting.show();
			}
		});

		NgonLocationManager.getInstance(this).setOnServiceListener(this);
		NgonLocationManager.getInstance(this).bindNgonService(type);
	}
	
	@Override
	public void onPageSelected(int position) {
		
	}
	// Neu start service location thanh cong se call back vao method nay
	@Override
	public void onReadyService() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mProgressWaiting.dismiss();
				getFragmentPager(mCurrentPager).onTabSelected();
			}
		});
	}

	// Neu start service location bi loi se callback vao method nay
	@Override
	public void onErrorStartService() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mProgressWaiting.dismiss();
				showDialogChooseService(mContentError);
			}
		});
	}

	protected void showDialogChooseService(String content) {
		Builder dialog2 = new NgonDialog.Builder(this);
		dialog2.setCancelable(false);
		dialog2.setMessage(content);
		dialog2.setTitle("Service");
		dialog2.setPositiveButton("GPS", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCurrentRequestType = RequestCode.REQUEST_LOCATION_GPS_SERVICE;
				dialog.dismiss();
				checkGPS();
			}
		});

		dialog2.setNegativeButton("3G/Wifi", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCurrentRequestType = RequestCode.REQUEST_LOCATION_NETWORK_SERVICE;
				dialog.dismiss();
				checkNetworkProvider();
			}
		});
		dialog2.show();
	}

	@Override
	protected void onResume() {
		Log.i("Activity", "onResume");
		super.onResume();
		if (mIsFirstTime) {
			mIsFirstTime = false;
			LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			boolean mGpsEnabled = locManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if(mGpsEnabled){
				startService(NgonLocation.REQUEST_GPS);
			}else{
				showDialogChooseService(mContentChooseDialogService);
			}
		} else {
			NgonLocationManager.getInstance(null).resetActivity(this);
			checkStartService(mCurrentRequestType);
		}
	}

	private void checkStartService(int type) {
		if (NgonLocationManager.getInstance(null).isManual())
			return;

		LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean mNetwork = locManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		boolean mGpsEnabled = locManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (type == RequestCode.REQUEST_LOCATION_NETWORK_SERVICE && mNetwork) {
			startService(NgonLocation.REQUEST_PROVIDER);
		} else if (type == RequestCode.REQUEST_LOCATION_GPS_SERVICE
				&& mGpsEnabled) {
			startService(NgonLocation.REQUEST_GPS);
		} else if (mNetwork) {
			startService(NgonLocation.REQUEST_PROVIDER);
		} else if (mGpsEnabled) {
			startService(NgonLocation.REQUEST_GPS);
		} else {
			showDialogChooseService(mContentChooseDialogService);
		}
	}

	private void checkGPS() {
		LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean mGpsEnabled = locManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!mGpsEnabled) {
			this.startActivityForResult(new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
					RequestCode.REQUEST_LOCATION_GPS_SERVICE);
		} else {
			startService(NgonLocation.REQUEST_GPS);
		}
	}
	
	private void checkNetworkProvider() {
		LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean mNetwork = locManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (!mNetwork) {
			this.startActivityForResult(new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
					RequestCode.REQUEST_LOCATION_NETWORK_SERVICE);
		} else {
			startService(NgonLocation.REQUEST_PROVIDER);
		}
	}

	protected abstract void initVariable();

	public void setCurrentPager(int myPagerId) {
		if (!mPagerIndex.contains(myPagerId)) {
			mPagerIndex.add(myPagerId);
		}
		if (isFirtTimeLoad) {
			isFirtTimeLoad = false;
			this.mCurrentPager = myPagerId;
		}

	}

	protected NgonHomePager getFragmentPager(int position) {
		mBuilderTab.append("android:switcher:").append(R.id.content_pager)
				.append(":").append(position);
		NgonHomePager fragment = (NgonHomePager) 
				getSupportFragmentManager().findFragmentByTag(
						mBuilderTab.toString());
		mBuilderTab.delete(0, mBuilderTab.length());
		return fragment;
	}

//	@Override
//	public void onBackPressed() {
//		showQuitDialog();
//	}
//
//	private void showQuitDialog() {
//		Builder dialog = new NgonDialog.Builder(this);
//		dialog.setTitle(R.string.dialog_exit_title);
//		dialog.setCancelable(false);
//		dialog.setMessage(R.string.dialog_exit_msg);
//		dialog.setPositiveButton(R.string.dialog_exit_title, this);
//		dialog.setNegativeButton(R.string.string_cancel, this);
//		dialog.show();
//	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			dialog.dismiss();
			this.finish();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			dialog.dismiss();
			break;
		}
	}

	public int getCurrentPositionDistance() {
		return mCurrentPositionDistance;
	}
}
