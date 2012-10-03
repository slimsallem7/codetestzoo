/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;

public class SupportLoginFacebook {

	private Facebook mFb;
	private Handler mHandler;
	private SessionListener mSessionListener = new SessionListener();
	private String[] mPermissions;
	private Activity mActivity;
	private int mActivityCode;
	public Context mContext;

	public void init(final Activity activity, final int activityCode,
			final Facebook fb, final String[] permissions) {
		mActivity = activity;
		mContext = mActivity.getApplicationContext();
		mActivityCode = activityCode;
		mFb = fb;
		mPermissions = permissions;
		mHandler = new Handler();

		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);
	}

	public void logOnFacebook() {
		mFb.authorize(mActivity, mPermissions, mActivityCode,
				new LoginDialogListener());
	}

	public void logOutFacebook() {
		if (mFb.isSessionValid()) {
			SessionEvents.onLogoutBegin();
			AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
			asyncRunner.logout(mContext, new LogoutRequestListener());
		}
	}

	private final class LoginDialogListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {
			SessionEvents.onLoginSuccess();
		}

		@Override
		public void onFacebookError(FacebookError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onError(DialogError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onCancel() {
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private class LogoutRequestListener extends BaseRequestListener {
		@Override
		public void onComplete(String response, final Object state) {
			/*
			 * callback should be run in the original thread, not the background
			 * thread
			 */
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					SessionEvents.onLogoutFinish();
				}
			});
		}
	}

	private class SessionListener implements AuthListener, LogoutListener {

		@Override
		public void onAuthSucceed() {
			SessionStore.save(mFb, SupportLoginFacebook.this.mContext);
		}

		@Override
		public void onAuthFail(String error) {
		}

		@Override
		public void onLogoutBegin() {
		}

		@Override
		public void onLogoutFinish() {
			SessionStore.clear(SupportLoginFacebook.this.mContext);
		}
	}

}
