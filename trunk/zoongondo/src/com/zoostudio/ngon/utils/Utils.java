package com.zoostudio.ngon.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class Utils {

    public static boolean checkAuthTokenExist(Context ctx) {
        boolean status = false;
        SharedPreferences pref = ctx.getSharedPreferences("account", Context.MODE_PRIVATE);
        String tokenKey = pref.getString("token_key", "");
        String tokenSecret = pref.getString("token_secret", "");

        if (tokenKey.length() > 0 && tokenSecret.length() > 0) {
            status = true;
        }

        return status;
    }
}
