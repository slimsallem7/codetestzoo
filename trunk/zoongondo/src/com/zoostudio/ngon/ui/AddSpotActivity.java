package com.zoostudio.ngon.ui;

import org.bookmark.helper.ValidCore;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.exception.ZooLocationException;
import com.zoostudio.ngon.ErrorCode;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.WaitingDialog;
import com.zoostudio.ngon.task.CreateSpotTask;
import com.zoostudio.ngon.task.GetAddressFromGeoTask;
import com.zoostudio.ngon.ui.base.BaseMapActivity;
import com.zoostudio.ngon.utils.LocationUtil;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;
import com.zoostudio.service.impl.NgonLocationManager;

public class AddSpotActivity extends BaseMapActivity implements
OnPostExecuteDelegate {
	private EditText etSpotName;
	private EditText etSpotAddress;
	private Button btnCreateSpot;
	private WaitingDialog waitingDialog;
	private ButtonUp mUp;
	private String spot_name;
	private String spot_address;

	protected void initControls() {
		super.initControls();
		mUp = (ButtonUp) findViewById(R.id.btn_up);
		etSpotName = (EditText) this.findViewById(R.id.spot_name);
		etSpotAddress = (EditText) this.findViewById(R.id.spot_address);
		btnCreateSpot = (Button) this.findViewById(R.id.create_spot);

	}

	private void loadCurrentAddress(Location location) {
		LocationUtil locationAddress = new LocationUtil();
		locationAddress.setOnAddressChangedListener(this);
		locationAddress.getAddress(this, location);
	}
	
	@Override
	protected void loadLocation() {
		
	}
	protected void initVariables() {
		super.initVariables();
		try {
			loadCurrentAddress(NgonLocationManager.getInstance(null)
					.getCurrentLocation());
		} catch (ZooLocationException e) {
			e.printStackTrace();
		}
	}

	private void doCreateSpot() {
		spot_name = etSpotName.getText().toString().trim();
		spot_address = etSpotAddress.getText().toString().trim();

		if (!ValidCore.length(spot_name, 1)) {
			showDialogError(ErrorCode.SPOTNAME_SHORT_OR_LONG);
			return;
		}

		if (!ValidCore.length(spot_address, 1)) {
			showDialogError(ErrorCode.ADDRESS_SHORT_OR_LONG);
			return;
		}

		waitingDialog = new WaitingDialog(this);
		waitingDialog.show();
		CreateSpotTask createTask = null;
		if (spot_address.length() == 0) {
			createTask = new CreateSpotTask(this, spot_name, mCurrentLat,
					mCurrentLong);
		} else {
			createTask = new CreateSpotTask(this, spot_name, spot_address,
					mCurrentLat, mCurrentLong);
		}
		createTask.setOnPostExecuteDelegate(this);
		createTask.execute();
	}

	@Override
	public void actionPost(RestClientTask task, JSONObject result) {
		if (task instanceof CreateSpotTask) {
			waitingDialog.dismiss();
			try {
				boolean status = result.getBoolean("status");

				if (status) {
					String spot_id = result.getString("spot_id");
					SpotItem spotItem = new SpotItem.Builder()
							.setId(spot_id)
							.setName(spot_name)
							.setAddress(spot_address)
							.create();
					
					Intent intent = new Intent(this, SpotDetailsActivity.class);
					intent.putExtra(SpotDetailsActivity.EXTRA_SPOT, spotItem);
					startActivity(intent);
					finish();
				} else {
					int errorCode = 0;
					if (result.has("error_code"))
						errorCode = result.getInt("error_code");
					showDialogError(errorCode);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (task instanceof GetAddressFromGeoTask) {
			boolean status;
			try {
				status = result.getBoolean("status");
				if (!status)
					return;

				String address = result.getString("address");
				etSpotAddress.setText(address);
			} catch (JSONException e) {

			}
		}
	}

	protected void initActions() {
		mUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		btnCreateSpot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doCreateSpot();
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_create_spot;
	}

	@Override
	protected int getEditAddressId() {
		return R.id.spot_address;
	}

	@Override
	protected int getButtonGetLocationId() {
		return R.id.btn_get_location;
	}

	@Override
	protected int getMapViewId() {
		return R.id.mapView;
	}
}
