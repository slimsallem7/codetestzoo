package com.zoostudio.ngon.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.RequestCode;

public class PickImageDialog extends NgonDialog implements
		android.view.View.OnClickListener {
	private LayoutInflater inflater;
	private TextView pickImageFromCamera;
	private TextView pickImageFromGallery;

	public PickImageDialog(Context context) {
		super(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInnerCustomView = inflater.inflate(R.layout.dialog_pick_image, null);
		pickImageFromCamera = (TextView) mInnerCustomView
				.findViewById(R.id.btnPickFromCamera);
		pickImageFromGallery = (TextView) mInnerCustomView
				.findViewById(R.id.btnPickFromGallery);

		pickImageFromCamera.setOnClickListener(this);
		pickImageFromGallery.setOnClickListener(this);
	}

	public void onClick(View view) {
		if (view == pickImageFromCamera) {
			Intent intent = new Intent(
					Intent.ACTION_CAMERA_BUTTON,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			getOwnerActivity().startActivityForResult(intent,
					RequestCode.REQUEST_IMAGE_FROM_CAMERA);
			dismiss();
		} else {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			getOwnerActivity().startActivityForResult(intent,
					RequestCode.REQUEST_IMAGE_FROM_GALLERY);
			dismiss();
		}
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View v = inflater.inflate(R.layout.dialog_pick_image, container, false);
	//
	// // Watch for button clicks.
	// Button button = (Button) v.findViewById(R.id.btnPickFromCamera);
	// button.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// Intent intent = new
	// Intent(Intent.ACTION_CAMERA_BUTTON,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	// getActivity().startActivityForResult(intent,
	// RequestCode.REQUEST_PICK_IMAGE_FROM_CAMERA);
	// dismiss();
	// }
	// });
	//
	// Button buttonCamera = (Button) v.findViewById(R.id.btnPickFromGallery);
	// buttonCamera.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// Intent intent = new Intent(
	// Intent.ACTION_PICK,
	// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	// getActivity().startActivityForResult(intent,
	// RequestCode.REQUEST_PICK_IMAGE_FROM_GALLERY);
	// dismiss();
	// }
	// });
	// return v;
	// }
}
