package com.zoostudio.ngon.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.adapter.item.MenuItem;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.cropimage.CropImageActivity;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.WaitingDialog;
import com.zoostudio.ngon.task.CreateDishTask;
import com.zoostudio.ngon.task.callback.OnAddDishListener;
import com.zoostudio.ngon.utils.ImageUtil;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnDataErrorDelegate;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;

public class AddDishActivity extends NgonActivity implements
		OnPreExecuteDelegate, OnAddDishListener, OnDataErrorDelegate {
	protected static final String EXTRA_SPOT = "com.ngon.do.adddishactivity.SPOT";
	private EditText etDishName;
	private Button btnAddDish;
	private Button btnAddAndMore;
	private ImageView mThumb;
	private SpotItem mSpot;
	private WaitingDialog mWaitingDialog;
	private ButtonUp mUp;
	private int sizeReq;
	private MediaItem mMediaItem;
	private String addDishOk;
	private MenuItem menuItem;

	@Override
	protected int setLayoutView() {
		return R.layout.activity_add_dish;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mMediaItem = (MediaItem) intent.getExtras().getSerializable(
				CropImageActivity.MEDIA_ITEM);
		int size = intent.getExtras().getInt(CropImageActivity.MEDIA_SIZE);
		Options options = new Options();
		options.outWidth = size;
		options.outHeight = options.outWidth;
		options.inSampleSize = ImageUtil.calculateInSampleSize(options,
				sizeReq, sizeReq);
		Log.e("AddDishActivity", "Path =" + mMediaItem.getPathMedia());
		menuItem.setImagePathLocal(mMediaItem.getPathMedia());
		Bitmap bitmap = BitmapFactory.decodeFile(mMediaItem.getPathMedia(),
				options);
		mThumb.setImageBitmap(bitmap);
	}

	@Override
	protected void initControls() {
		mSpot = getSpot();
		mUp = (ButtonUp) findViewById(R.id.btn_up);
		etDishName = (EditText) findViewById(R.id.dish_name);
		btnAddDish = (Button) findViewById(R.id.done);
		btnAddAndMore = (Button) findViewById(R.id.done_and_add_more);
		mThumb = (ImageView) findViewById(R.id.add_image);

		mUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
	}

	@Override
	protected void initVariables() {
		menuItem = new MenuItem();
		sizeReq = getResources().getDimensionPixelSize(R.dimen.dish_photo_size);
		addDishOk = getResources().getString(R.string.add_dish_success);
	}

	private SpotItem getSpot() {
		Bundle extras = getIntent().getExtras();
		if (extras.containsKey(AddDishActivity.EXTRA_SPOT)) {
			return (SpotItem) extras
					.getSerializable(AddDishActivity.EXTRA_SPOT);
		} else {
			finish();
			return null;
		}
	}

	@Override
	public void onActionPre(RestClientTask task) {
		mWaitingDialog = new WaitingDialog(this);
		mWaitingDialog.show();
	}

	@Override
	public void onBackPressed() {
		this.setResult(Activity.RESULT_CANCELED);
		super.onBackPressed();
	}

	@Override
	protected void initActions() {
		mUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		btnAddDish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String dish_name = etDishName.getText().toString().trim();
				menuItem.setName(dish_name);
				menuItem.setCost("3");
				if (dish_name.length() > 2) {
					CreateDishTask addDishTask = new CreateDishTask(
							AddDishActivity.this, dish_name, mSpot.getId(),
							mMediaItem);
					addDishTask.setOnPreExecuteDelegate(AddDishActivity.this);
					addDishTask.setOnAddDishListener(AddDishActivity.this);
					addDishTask.setOnDataErrorDelegate(AddDishActivity.this);
					addDishTask.execute();
				} else {
				}
			}
		});

		btnAddAndMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		mThumb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ChooseCommonMediaActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onAddDishListenerSuccess(String dishId) {
		mWaitingDialog.dismiss();
		Toast.makeText(getApplicationContext(), addDishOk, Toast.LENGTH_SHORT)
				.show();
		menuItem.setDishId(dishId);
		Intent intent = new Intent();
		intent.putExtra(ChooseDishActivity.EXTRA_MENU_ITEM, menuItem);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onActionDataError(RestClientTask task, int errorCode) {
		if (mWaitingDialog.isShowing())
			mWaitingDialog.dismiss();
		Toast.makeText(getApplicationContext(), "Thêm ảnh thất bại",
				Toast.LENGTH_SHORT).show();
	}
}
