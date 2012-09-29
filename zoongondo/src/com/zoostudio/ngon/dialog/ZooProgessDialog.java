package com.zoostudio.ngon.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoostudio.ngon.R;

public class ZooProgessDialog extends DialogFragment {
	private TextView mMessage;
	private String mText;
	
	public static ZooProgessDialog newInstance(String message) {
		ZooProgessDialog frag = new ZooProgessDialog();
		Bundle args = new Bundle();
		args.putString("message", message);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mText = getArguments().getString("message");
		setStyle(DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Translucent_NoTitleBar);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_progress, null);
		mMessage = (TextView) v.findViewById(R.id.message);
		mMessage.setText(mText);
		
		ImageView img = (ImageView) v.findViewById(R.id.progress);
		img.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.loading));
		
		return v;
	}
}
