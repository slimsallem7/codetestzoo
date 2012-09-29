package com.zoostudio.ngon.ui.pager;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.zoostudio.adapter.NgonListCheckInAdapter;
import com.zoostudio.adapter.item.CheckInItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonDialog;


public class MyProfilePager extends NgonUserPager {
	private ListView mListView;
	private LayoutInflater inflater;
	private SmartImageView mAvatar;
	private TextView userNameView;
	private TextView addressView;
	private NgonListCheckInAdapter infoUserAdapter;
	
	@Override
	protected int getLayoutId() {
		return R.layout.pager_user_profile;
	}

	@Override
	public void onTabSelected(int position) {
		
	}

	@Override
	public void initVariables() {
		inflater = this.getActivity().getLayoutInflater();
		infoUserAdapter = new NgonListCheckInAdapter(this.getActivity(), 0, new ArrayList<CheckInItem>());
	}

	@Override
	public void initViews() {
		mListView = (ListView) this.mView.findViewById(R.id.listProfile);
		View header = inflater.inflate(R.layout.inc_profile_header,null);
		mListView.addHeaderView(header);
		mListView.setAdapter(infoUserAdapter);
		mAvatar = (SmartImageView) header.findViewById(R.id.imageAvatarProfile);
		userNameView = (TextView) header.findViewById(R.id.txt_username);
		addressView = (TextView) header.findViewById(R.id.txtAdress);
	}

	@Override
	public void initActions() {
		loadTempData();
		
		mAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				createDialogTakePhoto();
			}
		});
	}
	
	private void loadTempData(){
		mAvatar.setImageUrl("");
		userNameView.setText("VietBQ");
		addressView.setText("83B, Ly thuong kiet");
		infoUserAdapter.add(new CheckInItem("Lorem ipsum dolor sit amet"));
		infoUserAdapter.add(new CheckInItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vel est vel eros aliquet iaculis ut sed neque"));
		infoUserAdapter.add(new CheckInItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit"));
	}
	
	private Dialog createDialogTakePhoto() {
		NgonDialog.Builder builder = new NgonDialog.Builder(this.getActivity());
		builder.setCancelable(true);
		builder.setTitle(getString(R.string.dialog_spotdetail_addphoto));
		builder.setPositiveButton(R.string.dialog_userprofile_removeavatar, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				deleteAvatar();
			}
		});
		builder.setNegativeButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		View content = getActivity().getLayoutInflater().inflate(R.layout.dialog_pick_image, null);
		content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		TextView mGallery = (TextView) content.findViewById(R.id.btnPickFromGallery);
		TextView mCamera = (TextView) content.findViewById(R.id.btnPickFromCamera);

		builder.setInnerCustomView(content);

		final NgonDialog dialog = builder.create();

		mGallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		mCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		return dialog;
	}

	private void deleteAvatar() {

	}
}
