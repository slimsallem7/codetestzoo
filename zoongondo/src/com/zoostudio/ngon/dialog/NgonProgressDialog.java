package com.zoostudio.ngon.dialog;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.views.NgonProgressView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.widget.TextView;

public class NgonProgressDialog extends NgonDialog {
	private TextView mMessage;
	private Context mCtx;
	private NgonProgressView progressView;

	public NgonProgressDialog(Context context) {
		super(context, R.style.NgonDialog);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		initLayout();
		mCtx = context;
	}

	private void initLayout() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(inflater.inflate(R.layout.dialog_progress, null));
		progressView = (NgonProgressView) this.findViewById(R.id.progress);
		mMessage = (TextView) findViewById(R.id.message);
	}

	public void setMessage(String message) {
		if (mMessage == null)
			return;
		if (message == null)
			return;
		mMessage.setText(message);
	}

	public void setMessage(int message) {
		setMessage(mCtx.getString(message));
	}

	@Override
	public void dismiss() {
		if (null != progressView) {
			progressView.stopAnim();
		}
		super.dismiss();
	}
}
