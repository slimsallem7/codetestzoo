package com.zoostudio.ngon.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.task.ForgotPasswordTask;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;

public class ForgotPassword extends NgonActivity implements OnClickListener, OnPostExecuteDelegate {

    private EditText etEmail;
    private EditText etPhone;
    private Button btnForgot;

    @Override
    protected int setLayoutView() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void initControls() {
        etEmail = (EditText) findViewById(R.id.txt_email);
        etPhone = (EditText) findViewById(R.id.txt_phone);
        btnForgot = (Button) findViewById(R.id.btn_forgot_password);
    }

    @Override
    protected void initVariables() {
        btnForgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_forgot_password:
                String email = etEmail.getText().toString();

                if (email.equals("") == false) {
                    ForgotPasswordTask forgotTask = new ForgotPasswordTask(this, email);
                    forgotTask.setOnPostExecuteDelegate(this);
                    forgotTask.execute();
                }

                break;
        }
    }

    @Override
    public void actionPost(RestClientTask task, JSONObject result) {
        startActivity(new Intent(this, LoginActivity.class));
    }

	@Override
	protected void initActions() {
		// TODO Auto-generated method stub
		
	}

}
