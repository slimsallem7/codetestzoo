package com.zoostudio.ngon.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;

import com.zoostudio.ngon.R;

public class TabSetting extends FragmentActivity implements OnPageChangeListener {
	private Button mLogout;
	private SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_setting);
		
		initControls();
		initVariables();
		initActions();		
	}

	private void initControls() {
//		mLogout = (Button) findViewById(R.id.logout);
	}

	private void initVariables() {
		pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	}

	private void initActions() {
//		mLogout.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				logout();
//			}
//		});
	}

	private void logout() {
		
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(getString(R.string.pref_savedinfo_username_key), "");
		editor.putString(getString(R.string.pref_savedinfo_password_key), "");
		editor.commit();
		
		startActivity(new Intent(getApplicationContext(), LoginActivity.class));
		finish();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		
	}
	
}
