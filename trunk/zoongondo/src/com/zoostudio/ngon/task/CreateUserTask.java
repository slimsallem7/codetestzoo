package com.zoostudio.ngon.task;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class CreateUserTask extends RestClientTask {

    private String mUsername;
    private String mPassword;
    private String mPhone;
    private String mEmail;

    public CreateUserTask(Activity activity, String username, String password) {
        this(activity, username, password, "");
    }

    public CreateUserTask(Activity activity, String username, String password, String email) {
        super(activity);
        mUsername = username;
        mPassword = password;
        mEmail = email;
    }

    @Override
    protected void doExecute() {
        restClient.addParam("username", mUsername);
        restClient.addParam("password", mPassword);
        restClient.addParam("phone", mPhone);
        if (false == mEmail.equals("")) {
            restClient.addParam("email", mEmail);
        }

        restClient.put("/create_user");
    }

}
