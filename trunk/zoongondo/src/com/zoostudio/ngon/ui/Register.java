package com.zoostudio.ngon.ui;

import org.bookmark.helper.ValidCore;
import org.bookmark.helper.ViewCore;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.zoostudio.ngon.ErrorCode;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonDialog;
import com.zoostudio.ngon.dialog.NgonDialog.Builder;
import com.zoostudio.ngon.dialog.NgonProgressDialog;
import com.zoostudio.ngon.task.CreateUserTask;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;

public class Register extends NgonActivity implements OnClickListener, OnPostExecuteDelegate {

    public static final String REGISTER_USERNAME = "ngondo.register.username";
    public static final String REGISTER_PASSWORD = "ngondo.register.password";

    private EditText etUsername;
    private EditText etPassword;
    private CheckBox cbShowPass;
    private Button btnRegister;
    private EditText etEmail;

    private String username;
    private String password;

    private NgonProgressDialog progressDialog;

    @Override
    protected int setLayoutView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initControls() {
        etUsername = (EditText) findViewById(R.id.txt_username);
        etPassword = (EditText) findViewById(R.id.txt_password);
        etEmail = (EditText) findViewById(R.id.txt_email);
        cbShowPass = (CheckBox) findViewById(R.id.btn_show_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
    }

    @Override
    protected void initVariables() {
        btnRegister.setOnClickListener(this);
        ViewCore.setShowHidePassword(etPassword, cbShowPass);
        progressDialog = new NgonProgressDialog(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:
                String validate = validateRegisterParams();
                if (validate == null) executeRegister();
                else showDialog(validate);

                break;
        }
    }

    private void executeRegister() {
        username = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString();
        CreateUserTask registerTask = new CreateUserTask(this, username, password, email);
        registerTask.setOnPostExecuteDelegate(this);
        registerTask.execute();

        progressDialog.setMessage(getString(R.string.register_registering));
        progressDialog.show();
    }

    @Override
    public void actionPost(RestClientTask task, JSONObject result) {
        try {
            if (progressDialog != null) progressDialog.dismiss();

            if (result.getBoolean("status")) {
                Intent intent = new Intent();
                intent.putExtra(REGISTER_USERNAME, username);
                intent.putExtra(REGISTER_PASSWORD, password);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Builder dialog = new NgonDialog.Builder(this);
                dialog.setTitle(R.string.string_error);
                dialog.setPositiveButton(R.string.string_close, null);
                int msg = 0;

                switch (result.getInt("error_code")) {
                    case ErrorCode.MISSING_PARAM:

                        break;
                    case ErrorCode.USERNAME_EXIST:
                        msg = R.string.string_error_username_exist;
                        break;
                    case ErrorCode.USERNAME_WRONG_FORMAT:
                        msg = R.string.login_username_not_valid;
                        break;
                    case ErrorCode.PASSWORD_SHORT_OR_LONG:
                        msg = R.string.login_password_not_valid;
                        break;
                    case ErrorCode.PHONE_NUMBER_EXIST:
                        msg = R.string.phone_already_exist;
                        break;
                    case ErrorCode.PHONE_NUMBER_WRONG_FORMAT:
                        msg = R.string.phone_wrong_format;
                        break;
                    case ErrorCode.EMAIL_EXIST:
                        msg = R.string.email_already_exist;
                        break;
                    case ErrorCode.EMAIL_WRONG_FORMAT:
                        msg = R.string.email_wrong_format;
                        break;
                }

                dialog.setMessage(msg);
                dialog.setNegativeButton(getString(R.string.string_close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        } catch (JSONException e) {}
    }

    private String validateRegisterParams() {
        String username = etUsername.getText().toString().trim();
        if (username.equals("")) return getString(R.string.login_username_not_valid);

        if (!ValidCore.alpha_dash(username)) return getString(R.string.login_username_not_valid);
        if (!ValidCore.length(username, getResources().getInteger(R.integer.defaultval_username_min), getResources()
                .getInteger(R.integer.defaultval_username_max))) return getString(R.string.login_username_not_valid);

        String password = etPassword.getText().toString().trim();
        if (password.equals("")) return getString(R.string.login_password_not_valid);

        if (!ValidCore.length(password, getResources().getInteger(R.integer.defaultval_password_min), getResources()
                .getInteger(R.integer.defaultval_password_max))) return getString(R.string.login_password_not_valid);

        String email = etEmail.getText().toString().trim();
        if (email.equals("") || !ValidCore.email(email)) return getString(R.string.email_wrong_format);

        return null;
    }

    private void showDialog(String message) {
        NgonDialog.Builder builder = new NgonDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.string_close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

	@Override
	protected void initActions() {
		// TODO Auto-generated method stub
		
	}
}
