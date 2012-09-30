package com.zoostudio.ngon.ui;

import org.bookmark.helper.DeviceCore;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonDialog;
import com.zoostudio.ngon.utils.ConfigSize;

public class SplashScreen extends NgonActivity {

	@Override
	protected int setLayoutView() {
		// TODO Auto-generated method stub
		return R.layout.activity_splash_screen;
	}

	@Override
	protected void initControls() {
		if (DeviceCore.checkInternetConnect(this)) {
			ConfigSize configSize = new ConfigSize(getResources());
			configSize.loadResources();
			Intent i = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(i);
			finish();
		} else {
			createDialogNoConnection().show();
		}
	}

	private Dialog createDialogNoConnection() {
		NgonDialog.Builder builder = new NgonDialog.Builder(this);
		builder.setMessage(getString(R.string.dialog_title_attention));
		builder.setMessage(getString(R.string.dialog_no_connection));
		builder.setPositiveButton(getString(R.string.general_close),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
		builder.setCancelable(false);
		return builder.create();
	}

	@Override
	protected void initVariables() {
	}

	@Override
	protected void initActions() {

	}

}
