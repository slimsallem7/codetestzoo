package com.twitter.android;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.zoostudio.ngon.ZooNgonDo;

public class TwitterUtil {
	public static Twitter twitter;
	private final static String CONSUMER_KEY = "Lq1xD9RUpsbQwdinYMEYJA";
	private final static String CONSUMER_SECRET = "DMYTaaGgDEwgFwPtXHhS8Yegmp1gLdrOOCAA734Puw";
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
	private CommonsHttpOAuthConsumer consumer;
	private OAuthProvider provider;
	private String CALLBACK_URL = "";
	private Context context;

	public TwitterUtil(Context context) {
		this.context = context;
		twitter = TwitterFactory.getSingleton();
	}

	public void logonTwitter(String callback, Activity activity) {
		// check for saved log in details..
		if (validSession())
			return;
		this.CALLBACK_URL = callback;
		getConsumerProvider();

		authTwitter(activity);
	}

	public boolean validSession() {
		AccessToken token = SessionStore.restore(twitter, context);
		if (null == token) {
			return false;
		}
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(token);
		return true;
	}

	private void authTwitter(Activity activity) {
		try {
			consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY,
					CONSUMER_SECRET);
			provider = new CommonsHttpOAuthProvider(REQUEST_URL, ACCESS_URL,
					AUTHORIZE_URL);
			twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			LoginTwitter loginTwitter = new LoginTwitter(activity);
			loginTwitter.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setConsumerProvider() {
		if (provider != null) {
			((ZooNgonDo) context).setProvider(provider);
		}
		if (consumer != null) {
			((ZooNgonDo) context).setConsumer(consumer);
		}
	}

	private void getConsumerProvider() {
		OAuthProvider p = ((ZooNgonDo) context).getProvider();
		if (p != null) {
			provider = p;
		}
		CommonsHttpOAuthConsumer c = ((ZooNgonDo) context).getConsumer();
		if (c != null) {
			consumer = c;
		}
	}

	public void callbackUrl(Intent intent) {
		if (intent != null && intent.getData() != null) {
			Uri uri = intent.getData();
			if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
				String verifier = uri
						.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
				try {
					// this will populate token and token_secret in consumer
					provider.retrieveAccessToken(consumer, verifier);
					// Get Access Token and persist it
					AccessToken a = new AccessToken(consumer.getToken(),
							consumer.getTokenSecret());
					SessionStore.save(a, context);
					// initialize Twitter4J
					twitter = new TwitterFactory().getInstance();
					twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
					twitter.setOAuthAccessToken(a);
					((ZooNgonDo) context).setTwitter(twitter);
					// Log.e("Login", "Twitter Initialised");
				} catch (Exception e) {
					// Log.e(APP, e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	public void postStatus() {

	}

	public void logoutTwitter() {
		// TODO Auto-generated method stub

	}

	private class LoginTwitter extends AsyncTask<Void, Void, Void> {
		private Activity activity;

		public LoginTwitter(Activity activity) {
			this.activity = activity;
		}

		private RequestToken mRequestToken;

		@Override
		protected Void doInBackground(Void... params) {
			String authUrl;
			try {
				authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
				// mRequestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
				setConsumerProvider();
//				Intent intent = new Intent(this.activity,
//						TwitterWebViewActivity.class);
//				intent.putExtra("URL", authUrl);
				activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(authUrl)).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_NO_HISTORY
						| Intent.FLAG_FROM_BACKGROUND));
//				this.activity.startActivity(intent);
			} catch (OAuthMessageSignerException e) {
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
