package com.twitter.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zoostudio.ngon.R;

@SuppressLint("SetJavaScriptEnabled")
public class TwitterWebViewActivity extends Activity {
	private Intent mIntent;
	private String call_back;
	public final static String CALL_BACK = "CALL_BACK";
	public final static String URL = "URL";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_webview);
		mIntent = getIntent();
		call_back = (String) mIntent.getExtras().get(CALL_BACK);
		String url = (String) mIntent.getExtras().get(URL);
		WebView webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setSavePassword(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains(call_back)) {
					Uri uri = Uri.parse(url);
					String oauthVerifier = uri
							.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
					mIntent.putExtra(oauth.signpost.OAuth.OAUTH_VERIFIER,
							oauthVerifier);
					setResult(RESULT_OK, mIntent);
					finish();
					return true;
				} else {
					setResult(RESULT_CANCELED);
					finish();
				}
				return false;
			}
		});
		webView.loadUrl(url);
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}
}
