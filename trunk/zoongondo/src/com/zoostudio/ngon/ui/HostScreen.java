package com.zoostudio.ngon.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.zoostudio.ngon.R;

public class HostScreen extends TabActivity {
	private TabHost tabHost;
	private TabWidget tabWidget;
	private ImageButton button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_host);
		button = (ImageButton) this.findViewById(R.id.checkin);
		
		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);
		tabHost.setup();

		tabWidget = (TabWidget) this.findViewById(android.R.id.tabs);
		
		TabSpec homeSpec = tabHost.newTabSpec("Home").setIndicator("Home");
		Intent homeIntent = new Intent(this, TabHome.class);
		homeSpec.setContent(homeIntent);
		createIndicator(homeSpec, R.drawable.ic_ab_home_on);

		TabSpec userProfileSpec = tabHost.newTabSpec("Profile").setIndicator("Profile");
		Intent userIntent = new Intent(this, TabProfile.class);
		userProfileSpec.setContent(userIntent);
		createIndicator(userProfileSpec, R.drawable.ic_ab_profile_on);

		TabSpec commingSpec = tabHost.newTabSpec("Setting").setIndicator("Setting");
		Intent commingIntent = new Intent(this, TabSetting.class);
		commingSpec.setContent(commingIntent);
		createIndicator(commingSpec, R.drawable.ic_ab_notification_on);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "TEST", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void createIndicator(TabSpec tabSpec, int drawableId) {
		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tabWidget, false);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		tabSpec.setIndicator(tabIndicator);
		tabHost.addTab(tabSpec);
	}
}
