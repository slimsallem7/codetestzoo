package com.zoostudio.app.userinfo;

public class UserInfo {
    private String mUsername;
    private String mUserId;

    private static UserInfo _userInfo;

    public UserInfo() {
        mUsername = "";
        mUserId = "";
    }

    public static UserInfo getInstance() {
        if (_userInfo == null) _userInfo = new UserInfo();
        return _userInfo;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }
}
