package com.twitter.android;

public interface OnTwitterListener {
	public void onAuthTwitterSuccess();

	public void onErrorTwitter();

	public void onLogoutTwitter();

	public void onLogoutTwitterError();
	
	public void onUpdateTwitterFinish();
	
	public void onUpdateTwitterError();
}
