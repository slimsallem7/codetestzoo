package com.zoostudio.ngon.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.ui.base.BaseMapActivity;
import com.zoostudio.service.impl.NgonLocationManager;

public class ActivityHostScreen extends TabActivity {
	protected static final String TAG = "ActivityHostScreen";
	private TabHost tabHost;
	private TabWidget tabWidget;
	private ImageButton mMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_host);
		mMap = (ImageButton) findViewById(R.id.map);
		
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		
		TabSpec homeSpec = tabHost.newTabSpec("Home").setIndicator("Home");
		Intent homeIntent = new Intent(getApplicationContext(), TabHome.class);
		homeSpec.setContent(homeIntent);
		createIndicator(homeSpec, R.drawable.ic_ab_home_on);

		TabSpec userProfileSpec = tabHost.newTabSpec("Profile").setIndicator("Profile");
		Intent userIntent = new Intent(getApplicationContext(), TabProfile.class);
		userProfileSpec.setContent(userIntent);
		createIndicator(userProfileSpec, R.drawable.ic_ab_profile_on);

		TabSpec commingSpec = tabHost.newTabSpec("Setting").setIndicator("Setting");
		Intent commingIntent = new Intent(getApplicationContext(), TabSetting.class);
		commingSpec.setContent(commingIntent);
		createIndicator(commingSpec, R.drawable.ic_ab_notification_on);
		
		mMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ActivitySelectLocationManual.class);
				intent.putExtra(ActivitySelectLocationManual.EXTRA_CURRENT_ADDRESS, "");
				
				double currentLat = -1, currentLong = -1;
				try {
					currentLat = NgonLocationManager.getInstance(null)
							.getCurrentLocation().getLatitude();
					currentLong = NgonLocationManager.getInstance(null)
							.getCurrentLocation().getLongitude();
					intent.putExtra(BaseMapActivity.EXTRA_CURRENT_LAT, currentLat);
					intent.putExtra(BaseMapActivity.EXTRA_CURRENT_LONG, currentLong);
					startActivity(intent);
				} catch (Exception e) {
					Log.e(TAG, "Error:" + e.getMessage());
				}
			}
		});
	}

	private void createIndicator(TabSpec tabSpec, int drawableId) {
		View tabIndicator = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.tab_indicator, tabWidget, false);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		tabSpec.setIndicator(tabIndicator);
		tabHost.addTab(tabSpec);
	}
}
