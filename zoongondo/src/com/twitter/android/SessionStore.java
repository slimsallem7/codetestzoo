package com.twitter.android;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionStore {

	private static final String ACCESS_TOKEN = "access_token";
	private static final String ACCESS_TOKEN_SECRET = "access_token_secret";
	private static final String KEY = "twitter-session";

	public static boolean save(AccessToken a, Context context) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(ACCESS_TOKEN, a.getToken());
		editor.putString(ACCESS_TOKEN_SECRET, a.getTokenSecret());
		return editor.commit();
	}

	public static AccessToken restore(Twitter session, Context context) {
		SharedPreferences settings = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		String token = settings.getString(ACCESS_TOKEN, "");
		String tokenSecret = settings.getString(ACCESS_TOKEN_SECRET, "");
		if (token != null && tokenSecret != null && !"".equals(tokenSecret)
				&& !"".equals(token)) {
			return new AccessToken(token, tokenSecret);
		}
		return null;
	}

	public static void clear(Context context) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.clear();
		editor.commit();
	}

}
