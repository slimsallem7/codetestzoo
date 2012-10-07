package com.twitter.android;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.GeoLocation;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.ZooNgonDo;
import com.zoostudio.ngon.dialog.NgonProgressDialog;

public class TwitterSupport {
	private final static String CONSUMER_KEY = "Lq1xD9RUpsbQwdinYMEYJA";
	private final static String CONSUMER_SECRET = "DMYTaaGgDEwgFwPtXHhS8Yegmp1gLdrOOCAA734Puw";
	private static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	private static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	private static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
	private CommonsHttpOAuthConsumer consumer;
	private OAuthProvider provider;
	private String CALLBACK_URL = "";
	private Context context;
	private OnTwitterListener listener;

	public TwitterSupport(Context context, OnTwitterListener listener) {
		this.context = context;
		this.listener = listener;
	}

	public void logonTwitter(String callback, Activity activity) {
		this.CALLBACK_URL = callback;
		LoginTwitter loginTwitter = new LoginTwitter(activity);
		loginTwitter.execute();
	}

	public boolean validSession() {
		AccessToken token = SessionStore.restore(context);
		if (null == token) {
			return false;
		}
		return true;
	}

	private void setConsumerProvider(CommonsHttpOAuthConsumer consumer,
			CommonsHttpOAuthProvider provider) {
		((ZooNgonDo) context).setProvider(provider);
		((ZooNgonDo) context).setConsumer(consumer);
	}

	private void getConsummerProvider() {
		consumer = ((ZooNgonDo) context).getConsumer();
		provider = ((ZooNgonDo) context).getProvider();
	}

	public void authorizeCallBack(Intent data, String callbackScheme) {
		final Uri uri = data.getData();
		if (uri != null && uri.getScheme().equals(callbackScheme)) {
			final String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
			RetrieveAccessToken retrieveAccessToken = new RetrieveAccessToken(
					verifier);
			retrieveAccessToken.execute();
		} else {
			listener.onErrorTwitter();
		}
	}

	public void postStatus(String status, GeoLocation location) {
		AsynTwitterPostStatus asynTwitterPostStatus = new AsynTwitterPostStatus(
				listener, status, location);
		asynTwitterPostStatus.start();
	}

	public void logoutTwitter(Context context) {
		SessionStore.clear(context);
	}

	private class LoginTwitter extends AsyncTask<Void, Void, Void> {
		private Activity activity;
		NgonProgressDialog progressDialog;

		public LoginTwitter(Activity activity) {
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new NgonProgressDialog(activity);
			progressDialog.setTitle(R.string.string_waiting);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String authUrl;
			try {
				CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
						CONSUMER_KEY, CONSUMER_SECRET);
				CommonsHttpOAuthProvider provider = new CommonsHttpOAuthProvider(
						REQUEST_URL, ACCESS_URL, AUTHORIZE_URL);
				setConsumerProvider(consumer, provider);
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
				((ZooNgonDo) context).setTwitter(twitter);
				authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
				onProgressUpdate();
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(authUrl))
						.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
								| Intent.FLAG_FROM_BACKGROUND);
				this.activity.startActivity(intent);
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

		@Override
		protected void onProgressUpdate(Void... values) {
			progressDialog.dismiss();
		}
	}

	/**
	 * Retrieve Access Token task.
	 */
	private class RetrieveAccessToken extends AsyncTask<Void, String, Boolean> {
		/**
		 * The Twitter OAuth verifier.
		 */
		private String oauth_verifier;

		/**
		 * Default constructor.
		 * 
		 * @param oauth_verifier
		 *            Twitter OAuth verifier
		 */
		public RetrieveAccessToken(String oauth_verifier) {
			this.oauth_verifier = oauth_verifier;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				getConsummerProvider();
				// retrieve the access token from the consumer and the OAuth
				// verifier returner by the Twitter Callback URL
				provider.retrieveAccessToken(consumer, this.oauth_verifier);
				return true;
			} catch (OAuthException oae) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				AccessToken accessToken = new AccessToken(consumer.getToken(),
						consumer.getTokenSecret());
				SessionStore.save(accessToken, context);
				Twitter twitter = ((ZooNgonDo) context).getTwitter();
				twitter.setOAuthAccessToken(accessToken);
				listener.onAuthTwitterSuccess();
			} else {
				listener.onErrorTwitter();
			}
		}

	}

	public class AsynTwitterPostStatus extends Thread {
		private String status;
		private GeoLocation location;
		private OnTwitterListener listener;
		private Handler handler;

		public AsynTwitterPostStatus(OnTwitterListener listener, String status,
				GeoLocation location) {
			this.status = status;
			this.location = location;
			this.listener = listener;
			if (null == handler)
				handler = new Handler();
		}

		@Override
		public void run() {
			StatusUpdate statusUpdate = new StatusUpdate(this.status);
			if (null != location) {
				statusUpdate.setLocation(location);
			}
			try {
				AccessToken a = SessionStore.restore(context);
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
				twitter.setOAuthAccessToken(a);
				twitter.updateStatus(status);

				handler.post(new Runnable() {
					@Override
					public void run() {
						listener.onUpdateTwitterFinish();
					}
				});
			} catch (TwitterException e) {
				e.printStackTrace();
				handler.post(new Runnable() {
					@Override
					public void run() {
						listener.onUpdateTwitterError();
					}
				});
			}
		}
	}
}
