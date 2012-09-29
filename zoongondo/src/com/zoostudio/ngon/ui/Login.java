package com.zoostudio.ngon.ui;

import org.bookmark.helper.ValidCore;
import org.bookmark.helper.ViewCore;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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

public class Login extends NgonActivity implements OnClickListener,
		OnPostExecuteDelegate, OnDataErrorDelegate {

	private EditText etUsername;
	private EditText etPassword;
	private CheckBox cbShowPass;
	private Button btnLogin;
	private Button btnForgot;
	private Button btnRegister;
	private NgonProgressDialog progressDialog;

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
		
		etUsername = (EditText) findViewById(R.id.txt_username);
		etPassword = (EditText) findViewById(R.id.txt_password);
		cbShowPass = (CheckBox) findViewById(R.id.btn_show_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnForgot = (Button) findViewById(R.id.btn_forgot_password);
		btnRegister = (Button) findViewById(R.id.btn_register);

		etUsername.setText("duynt");
		etPassword.setText("papahotel");
	}

	@Override
	protected void initVariables() {
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (etUsername.getText().toString().trim().equals("")
						&& etPassword.getText().toString().trim().equals(""))
					return;

				String validateResult = validateLoginParams();
				if (validateResult == null) {
					executeLogin();
				} else {
					showDialog(validateResult);
				}
			}
		});
		btnForgot.setOnClickListener(this);
		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getApplicationContext(),
						Register.class), RequestCode.REGISTER);
			}
		});
		progressDialog = new NgonProgressDialog(this);

		ViewCore.setShowHidePassword(etPassword, cbShowPass);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			if (etUsername.getText().toString().trim().equals("")
					&& etPassword.getText().toString().trim().equals(""))
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
		String username = etUsername.getText().toString().trim().toLowerCase();
		String password = etPassword.getText().toString().trim();

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
		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();

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
				// startActivity(new Intent(getApplicationContext(),
				// MainScreen.class));
				startActivity(new Intent(getApplicationContext(),
						HostScreen.class));
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

	}

	@Override
	public void actionDataError(RestClientTask task, int errorCode) {
		showDialog(ZooException.NETWORK.NETWORK_ERROR_DESC);
	}
}
