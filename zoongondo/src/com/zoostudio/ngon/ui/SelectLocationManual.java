package com.zoostudio.ngon.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.ImageButton;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.ui.base.BaseMapActivity;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;
import com.zoostudio.service.impl.NgonLocationManager;

public class SelectLocationManual extends BaseMapActivity implements
		OnPostExecuteDelegate {
	private ImageButton mSwitchAuto;
	private ButtonUp mUp;

	@Override
	public void actionPost(RestClientTask task, JSONObject result) {

	}

	public void cancelCreateSpot(View view) {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}

	private void didCreateSpot() {
		if (null == mCurrentGeoPoint) {
			this.setResult(RESULT_CANCELED);
			this.finish();
		}
		NgonLocationManager.getInstance(null).changeLocationToManual();

		Location location = new Location(
				android.location.LocationManager.GPS_PROVIDER);
		location.setLatitude(mCurrentGeoPoint.getLatitudeE6() / 1E6);
		location.setLongitude(mCurrentGeoPoint.getLongitudeE6() / 1E6);
		NgonLocationManager.getInstance(null).setCurrentLocationByManual(
				location);

		Intent result = new Intent();
		result.putExtra("NEW_ADDRESS", etAddress.getText().toString().trim());
		this.setResult(TabHome.RESULT_CHANGE_TO_MANUAL, result);
		this.finish();
	}

	@Override
	public void finish() {
		overridePendingTransition(R.anim.activity_slide_out_to_bottom, 0);
		super.finish();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_select_location_manual;
	}

	@Override
	protected int getEditAddressId() {
		return R.id.txtSuggestAddress;
	}

	@Override
	protected int getButtonGetLocationId() {
		return R.id.btn_get_location;
	}

	@Override
	protected int getMapViewId() {
		return R.id.mapView;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void initControls() {
		super.initControls();

//		mSwitchAuto = (ImageButton) findViewById(R.id.auto);
		mUp = (ButtonUp) findViewById(R.id.btn_up);
	}

	@Override
	protected void initActions() {
		super.initActions();
//		if (!NgonLocationManager.getInstance(null).isAuto()) {
//			mSwitchAuto.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					NgonLocationManager.getInstance(null)
//							.changeLocationToAuto();
//					SelectLocationManual.this
//							.setResult(MainScreen.RESULT_CHANGE_TO_AUTO);
//					SelectLocationManual.this.finish();
//				}
//			});
//		}else{
//			mSwitchAuto.setEnabled(false);
//			mSwitchAuto.setVisibility(View.INVISIBLE);
//		}
//		
		mUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		this.setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
}
