package com.zoostudio.ngon.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoostudio.ngon.R;

public class ZooAlertDialog extends DialogFragment {

	public static ZooAlertDialog newInstance(String title, String content) {
		ZooAlertDialog frag = new ZooAlertDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("content", content);
		frag.setArguments(args);
		return frag;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View v = inflater.inflate(R.layout.dialog_base, null);
		return new AlertDialog.Builder(getActivity())
				.setTitle("3G")
				.setView(v)
				.setCancelable(true)
				.setPositiveButton("Ok!",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// validation code
							}
						})
				.setNegativeButton("Aww, hell no!",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).create();

	}
}
