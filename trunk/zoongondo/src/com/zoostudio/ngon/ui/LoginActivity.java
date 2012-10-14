package com.zoostudio.ngon.ui;

import org.bookmark.helper.ValidCore;
import org.bookmark.helper.ViewCore;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.zoostudio.app.userinfo.UserInfo;
import com.zoostudio.exception.ZooException;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.RequestCode;
import com.zoostudio.ngon.dialog.NgonDialog;
import com.zoostudio.ngon.dialog.NgonProgressDialog;
import com.zoostudio.ngon.task.LoginTask;
import com.zoostudio.ngon.task.RequestTokenTask;
import com.zoostudio.ngon.utils.Utils;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnDataErrorDelegate;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;

public class LoginActivity extends NgonActivity implements OnClickListener,
		OnPostExecuteDelegate, OnDataErrorDelegate {

	private EditText etUsername;
	private EditText etPassword;
	private CheckBox cbShowPass;
	private Button btnLogin;
	private Button btnForgot;
	private Button btnRegister;
	private NgonProgressDialog progressDialog;
	private SharedPreferences pref;
	private ImageView mLogo;
	private RelativeLayout mForm;
	
	private Animation animMoveLogo;
	private Animation animShowLoginForm;
	
	private String password;
	private String username;

	@Override
	protected int setLayoutView() {
		return R.layout.activity_login;
	}

	@Override
	protected void initControls() {
		if (Utils.checkAuthTokenExist(this) == false) {
			RequestTokenTask requestToken = new RequestTokenTask(this);
			requestToken.execute();
		}
		
		mForm = (RelativeLayout) findViewById(R.id.form);
		mForm.setVisibility(View.GONE);
		etUsername = (EditText) findViewById(R.id.txt_username);
		etPassword = (EditText) findViewById(R.id.txt_password);
		cbShowPass = (CheckBox) findViewById(R.id.btn_show_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnForgot = (Button) findViewById(R.id.btn_forgot_password);
		btnRegister = (Button) findViewById(R.id.btn_register);
		mLogo = (ImageView) findViewById(R.id.logo);

		etUsername.setText("duynt");
		etPassword.setText("papahotel");
	}

	@Override
	protected void initVariables() {
		pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		Display d = getWindowManager().getDefaultDisplay();
		final int logoWidth = getResources().getDimensionPixelOffset(R.dimen.login_logo_width);
		final int logoHeight = getResources().getDimensionPixelOffset(R.dimen.login_logo_height);
		final int screenWidth = d.getWidth();
		final int screenHeight = d.getHeight();
		final int logoLeft = (screenWidth - logoWidth) / 2;
		final int logoTopBeforeMove = (screenHeight - logoHeight) / 2;
		
		LayoutParams params = new LayoutParams(logoWidth, logoHeight);
		params.setMargins(logoLeft, logoTopBeforeMove, 0, 0);
		mLogo.setLayoutParams(params);
		
		animMoveLogo = new TranslateAnimation(0, 0, 0, 0 - logoTopBeforeMove);
		animMoveLogo.setDuration(500);
		animMoveLogo.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) { }
			
			@Override
			public void onAnimationRepeat(Animation animation) { }
			
			@Override
			public void onAnimationEnd(Animation animation) {
				LayoutParams params = new LayoutParams(logoWidth, logoHeight);
				params.setMargins(logoLeft, 0, 0, 0);
				mLogo.setLayoutParams(params);
				
				mForm.startAnimation(animShowLoginForm);
			}
		});
		
		animShowLoginForm = new AlphaAnimation(0, 1);
		animShowLoginForm.setDuration(500);
		animShowLoginForm.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				mForm.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) { }
			
			@Override
			public void onAnimationEnd(Animation animation) { }
		});
		
		btnLogin.setOnClickListener(this);
		btnForgot.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		progressDialog = new NgonProgressDialog(this);

		ViewCore.setShowHidePassword(etPassword, cbShowPass);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			username = etUsername.getText().toString().trim().toLowerCase();
			password = etPassword.getText().toString().trim();
			
			if (username.equals("") && password.equals(""))
				return;

			String validateResult = validateLoginParams();
			if (validateResult == null) {
				executeLogin();
			} else {
				showDialog(validateResult);
			}
			break;

		case R.id.btn_forgot_password:
			startActivity(new Intent(getApplicationContext(),
					ForgotPassword.class));
			break;

		case R.id.btn_register:
			startActivityForResult(new Intent(getApplicationContext(),
					Register.class), RequestCode.REGISTER);
			break;
		}
	}

	private String validateLoginParams() {
		if (username.equals(""))
			return getString(R.string.login_type_username);

		if (!ValidCore.alpha_dash(username))
			return getString(R.string.login_username_not_valid);
		if (!ValidCore.length(username,
				getResources().getInteger(R.integer.defaultval_username_min),
				getResources().getInteger(R.integer.defaultval_username_max))) {
			return getString(R.string.login_username_not_valid);
		}

		if (password.equals(""))
			return "";

		if (!ValidCore.length(password,
				getResources().getInteger(R.integer.defaultval_password_min),
				getResources().getInteger(R.integer.defaultval_password_max))) {
			return getString(R.string.login_password_not_valid);
		}
		return null;
	}

	// TODO : BI leak screen o day. Trong truong hop (Login qua lau bi timeout )
	private void executeLogin() {
		LoginTask loginTask = new LoginTask(this, username, password);
		loginTask.setOnPostExecuteDelegate(this);
		loginTask.setOnDataErrorDelegate(this);

		loginTask.execute();

		progressDialog.setMessage(R.string.login_loggingin);
		progressDialog.show();
	}

	@Override
	public void actionPost(RestClientTask task, JSONObject result) {
		try {
			if (progressDialog != null)
				progressDialog.dismiss();

			if (result.getBoolean("status")) {
				UserInfo.getInstance()
						.setUsername(result.getString("username"));
				UserInfo.getInstance().setUserId(result.getString("user_id"));
				saveLoginInfo();
				
				startActivity(new Intent(getApplicationContext(),
						ActivityHostScreen.class));
				finish();
			} else {
				String message = "";
				switch (result.getInt("error_code")) {
				case 0:
					break;
				default:
					break;
				}

				showDialog(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void saveLoginInfo() {
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(getString(R.string.pref_savedinfo_username_key), username);
		editor.putString(getString(R.string.pref_savedinfo_password_key), password);
		editor.commit();
	}

	private void showDialog(String message) {
		NgonDialog.Builder builder = new NgonDialog.Builder(this);
		builder.setMessage(message);
		builder.setNegativeButton(getString(R.string.string_close),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RequestCode.REGISTER:
			switch (resultCode) {
			case RESULT_OK:
				Bundle bundle = data.getExtras();
				etUsername
						.setText(bundle.getString(Register.REGISTER_USERNAME));
				etPassword
						.setText(bundle.getString(Register.REGISTER_PASSWORD));
				executeLogin();
				break;
			}
			break;
		}
	}

	@Override
	protected void initActions() {
		new CountDownTimer(1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) { }
			
			@Override
			public void onFinish() {
				if (checkSavedLoginInfo()) {
					gotoHomeScreen();
				} else {
					showLoginForm();
				}
			}
		}.start();
	}

	@Override
	public void onActionDataError(RestClientTask task, int errorCode) {
		showDialog(ZooException.NETWORK.NETWORK_ERROR_DESC);
	}
	
	private boolean checkSavedLoginInfo() {
		if (pref.getString(getString(R.string.pref_savedinfo_username_key), "").equals("") ||
				pref.getString(getString(R.string.pref_savedinfo_password_key), "").equals("")) 
			return false;
		else return true;
	}
	
	private void showLoginForm() {
		mLogo.startAnimation(animMoveLogo);
	}
	
	private void gotoHomeScreen() {
		startActivity(new Intent(getApplicationContext(),
				ActivityHostScreen.class));
		finish();
	}
}
