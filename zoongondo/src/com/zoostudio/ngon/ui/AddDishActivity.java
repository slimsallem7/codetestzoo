package com.zoostudio.ngon.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.WaitingDialog;
import com.zoostudio.ngon.task.CreateDishTask;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;

public class AddDishActivity extends NgonActivity implements OnClickListener, OnPostExecuteDelegate, OnPreExecuteDelegate {
	protected static final String EXTRA_SPOT = "com.ngon.do.adddishactivity.SPOT";
	private EditText etDishName;
	private Button btnAddDish;
	private Button btnAddAndMore;
	private ImageView mThumb;
	private SpotItem mSpot;
	private WaitingDialog mWaitingDialog;
	private ButtonUp mUp;

	@Override
	protected int setLayoutView() {
		return R.layout.activity_add_dish;
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

	}

	private SpotItem getSpot() {
		Bundle extras = getIntent().getExtras();
		if (extras.containsKey(AddDishActivity.EXTRA_SPOT)) {
			return (SpotItem) extras.getSerializable(AddDishActivity.EXTRA_SPOT);
		} else {
			finish();
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.done:
			String dish_name = etDishName.getText().toString().trim();
			if (dish_name.length() > 2) {
				CreateDishTask addDishTask = new CreateDishTask(this, dish_name, mSpot.getId());
				addDishTask.setOnPreExecuteDelegate(this);
				addDishTask.setOnPostExecuteDelegate(this);
				addDishTask.execute();
			} else {}
			break;
		}
	}

	@Override
	public void actionPre(RestClientTask task) {
		mWaitingDialog = new WaitingDialog(this);
		mWaitingDialog.show();
	}

	@Override
	public void actionPost(RestClientTask task, JSONObject result) {
		if (task instanceof CreateDishTask) {
			mWaitingDialog.dismiss();

			try {
				boolean status = result.getBoolean("status");
				if (status) {
					String dishId = result.getString("dish_id");
					Intent intent = new Intent();
					intent.putExtra("dish_id", dishId);
					intent.putExtra("dish_name", etDishName.getText().toString().trim());
					setResult(RESULT_OK, intent);
					finish();
				} else {
					int errorCode = result.getInt("error_code");
					doCreateError(errorCode);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void doCreateError(int errorCode) {

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
				if (dish_name.length() > 2) {
					CreateDishTask addDishTask = new CreateDishTask(AddDishActivity.this, dish_name, mSpot.getId());
					addDishTask.setOnPreExecuteDelegate(AddDishActivity.this);
					addDishTask.setOnPostExecuteDelegate(AddDishActivity.this);
					addDishTask.execute();
				} else {}
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
				
			}
		});
	}

}
