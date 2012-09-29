package com.zoostudio.ngon.task;

import android.app.Activity;
import android.content.Context;

import com.zoostudio.restclient.RestClientTask;

public class LoginTask extends RestClientTask {

    private String mUsername;
    private String mPassword;
    private String mTokenKey;

    public LoginTask(Activity activity, String username, String password) {
        super(activity);
        
        mUsername = username.trim().toLowerCase();
        mPassword = password;
        mTokenKey = activity.getSharedPreferences("account", Context.MODE_PRIVATE).getString("token_key", "");
    }

    @Override
    public void doExecute() {
        restClient.addParam("username", mUsername);
        restClient.addParam("password", mPassword);
        restClient.addParam("token_key", mTokenKey);
        restClient.get("/login");
    }
    @Override
    public String getDumpData() {
        // TODO Auto-generated method stub
        mDumpData = "{\"status\":true,\"username\":\"vietbq\",\"userid\":\"6\"}";
        return mDumpData;
    }
}
