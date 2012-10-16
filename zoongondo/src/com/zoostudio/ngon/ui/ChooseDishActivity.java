package com.zoostudio.ngon.ui;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zoostudio.adapter.ChooseDishAdapter;
import com.zoostudio.adapter.NgonListDishSelected;
import com.zoostudio.adapter.item.MenuItem;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.ErrorCode;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.RequestCode;
import com.zoostudio.ngon.dialog.NgonDialog;
import com.zoostudio.ngon.dialog.NgonDialog.Builder;
import com.zoostudio.ngon.dialog.WaitingDialog;
import com.zoostudio.ngon.task.GetMenuTask;
import com.zoostudio.ngon.task.callback.OnMenuTaskListener;
import com.zoostudio.ngon.utils.ArrayUtils;
import com.zoostudio.ngon.utils.Logger;
import com.zoostudio.ngon.utils.PipedDeepCopy;
import com.zoostudio.ngon.views.ButtonListitemAdd;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnDataErrorDelegate;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;

public class ChooseDishActivity extends NgonActivity implements
		OnPreExecuteDelegate, OnClickListener, OnItemClickListener,
		OnDataErrorDelegate, ChooseDishAdapter.OnDishChoice, OnMenuTaskListener {

	public static final String EXTRA_SPOT = "com.ngon.do.choosedish.SPOT";
	public static final String EXTRA_MENU_ITEM = "com.ngon.do.choosedish.MENUITEM";
	protected static final String TAG = "ChooseDishActivity";

	private ListView lvMenu;
	private SpotItem mSpot;
	private GetMenuTask mMenuTask;
	private WaitingDialog mWaitingDialog;
	private ChooseDishAdapter mMenuAdapter;
	private ButtonListitemAdd btnAddDish;
	private TextView mDishChoiced;
	private String strDish;
	private ArrayList<MenuItem> listSelected;
	private ArrayList<MenuItem> mTempDish;
	private ArrayList<MenuItem> originalList;
	private ArrayList<MenuItem> mTempOriginalList;
	private Button btnDone;
	private View mDishSelected;
	private int mCount;
	private View incPopUp;
	private ButtonUp btnUp;
	private View footerView;
	private View mAddNew;
	
	@Override
	protected int setLayoutView() {
		return R.layout.activity_choose_dish;
	}

	@Override
	protected void initControls() {
		strDish = this.getResources().getString(R.string.dish);
		mSpot = getSpot();
		lvMenu = (ListView) findViewById(R.id.list_menu);
		mDishChoiced = (TextView) findViewById(R.id.txtDishchoices);
		btnDone = (Button) findViewById(R.id.checkin);
		mMenuTask = new GetMenuTask(this, mSpot.getId());
		mDishSelected = findViewById(R.id.layoutDishSelected);
		lvMenu.setOnItemClickListener(this);
		btnDone.setOnClickListener(this);
		mDishSelected.setOnClickListener(this);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerView = inflate.inflate(R.layout.inc_footer_add_dish, null);
		lvMenu.addFooterView(footerView, null, false);
		mAddNew = footerView.findViewById(R.id.footer);
		btnUp = (ButtonUp) findViewById(R.id.btn_up);
	}

	private void copyToOriginal(ArrayList<MenuItem> arrayList) {
		for (int i = 0, n = arrayList.size(); i < n; i++) {
			originalList.add((MenuItem) PipedDeepCopy.copy(arrayList.get(i)));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initVariables() {
		mTempDish = new ArrayList<MenuItem>();
		mTempOriginalList = new ArrayList<MenuItem>();

		getMenuDishFromExtras();

		mCount = 0;
		mMenuAdapter = new ChooseDishAdapter(this, 0,
				new ArrayList<MenuItem>(), this);

		if (null == listSelected || listSelected.isEmpty()) {
			listSelected = new ArrayList<MenuItem>();
			mMenuTask.setOnMenuTaskListener(this);
			mMenuTask.setOnPreExecuteDelegate(this);
			mMenuTask.setOnDataErrorDelegate(this);
			mMenuTask.execute();
		} else {
			mDishSelected.setVisibility(View.VISIBLE);
			mCount = listSelected.size();
			mDishChoiced.setText("" + mCount + " " + strDish + "    ");
			ArrayList<MenuItem> listUnSelect = loadDishItem();
			mMenuAdapter = new ChooseDishAdapter(this, 0, listUnSelect, this);
		}

		mTempDish = (ArrayList<MenuItem>) ArrayUtils.copyArray(listSelected);
		mTempOriginalList = (ArrayList<MenuItem>) ArrayUtils
				.copyArray(originalList);
		lvMenu.setAdapter(mMenuAdapter);
	}

	@SuppressWarnings("unchecked")
	private void getMenuDishFromExtras() {
		Bundle bundle = this.getIntent().getExtras();
		originalList = (ArrayList<MenuItem>) bundle
				.getSerializable("LIST_DISH");
		listSelected = (ArrayList<MenuItem>) bundle
				.getSerializable("LIST_SELECTED");
		if (null == originalList)
			originalList = new ArrayList<MenuItem>();
	}

	private ArrayList<MenuItem> loadDishItem() {
		ArrayList<MenuItem> array;
		array = new ArrayList<MenuItem>();
		for (int i = 0, n = originalList.size(); i < n; i++) {
			if (!originalList.get(i).isSelected()) {
				array.add(originalList.get(i));
			}
		}
		return array;
	}

	private SpotItem getSpot() {
		Bundle extras = getIntent().getExtras();
		if (extras.containsKey(EXTRA_SPOT)) {
			return (SpotItem) extras.getSerializable(EXTRA_SPOT);
		} else {
			finish();
			return null;
		}
	}

	@Override
	public void onActionPre(RestClientTask task) {
		mWaitingDialog = new WaitingDialog(this);
		mWaitingDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mMenuTask.cancel(true);
			}
		});
		mWaitingDialog.setCancelable(true);
		mWaitingDialog.show();
	}

	private void doLoadMenuError(int errorCode) {
		switch (errorCode) {
		case ErrorCode.SPOT_NOT_EXIST:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnDone) {
			if (!mTempDish.isEmpty()) {
				Intent intent = new Intent();
				intent.putExtra("LIST_DISH", mTempOriginalList);
				intent.putExtra("LIST_SELECTED", mTempDish);
				this.setResult(RESULT_OK, intent);
			} else {
				this.setResult(RESULT_CANCELED);
			}
			this.finish();
		} else if (v == mDishSelected) {
			if (!mTempDish.isEmpty()) {
				showDialog();
			} else {
				Toast.makeText(getApplicationContext(),
						R.string.choosedish_emptylist, Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (!mTempDish.isEmpty()) {
			Intent intent = new Intent();
			intent.putExtra("LIST_DISH", mTempOriginalList);
			intent.putExtra("LIST_SELECTED", mTempDish);
			this.setResult(RESULT_OK, intent);
		} else {
			this.setResult(RESULT_CANCELED);
		}

		super.onBackPressed();
	}

	@Override
	protected void initActions() {
		mAddNew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoAddDish();
			}
		});
		
		btnUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
	}

	protected void gotoAddDish() {
		Intent intent = new Intent(getApplicationContext(),
				AddDishActivity.class);
		intent.putExtra(AddDishActivity.EXTRA_SPOT, mSpot);
		startActivityForResult(intent, RequestCode.ADD_DISH);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
	}

	@Override
	public void onDishChoiceListener(int count, MenuItem dishItem) {
		if (mCount == 0) {
			showLayoutCount();
		}
		mCount++;
		mDishChoiced.setText("" + mCount + " " + strDish + "    ");
		mTempDish.add(dishItem);
		for (int i = 0, n = mTempOriginalList.size(); i < n; i++) {
			if (mTempOriginalList.get(i).getDishId()
					.equals(dishItem.getDishId())) {
				mTempOriginalList.get(i).setSelected(true);
				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!listSelected.isEmpty() && !originalList.isEmpty()) {
				Intent intent = new Intent();
				intent.putExtra("LIST_DISH", originalList);
				intent.putExtra("LIST_SELECTED", listSelected);
				this.setResult(RESULT_OK, intent);
			} else {
				this.setResult(RESULT_CANCELED);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showDialog() {
		incPopUp = this.getLayoutInflater().inflate(R.layout.inc_dish_selected,
				null);
		ListView listView = (ListView) incPopUp
				.findViewById(R.id.listDishSelected);
		TextView view = (TextView) incPopUp
				.findViewById(R.id.titleQuantityDish);
		NgonListDishSelected listDishSelected = new NgonListDishSelected(
				getApplicationContext(), 0, mTempDish);
		listView.setAdapter(listDishSelected);
		view.setText("" + mTempDish.size());
		Builder dialog = new NgonDialog.Builder(this);
		dialog.setCancelable(true);
		dialog.setInnerCustomView(incPopUp);
		dialog.setMessage(R.string.dialog_exit_msg);
		dialog.setNegativeButton(R.string.dialog_close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.setPositiveButton(R.string.delete,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						checkDeleteDish();
					}
				});
		dialog.show();
	}

	private void checkDeleteDish() {
		boolean hasChange = false;
		for (Iterator<MenuItem> it = mTempDish.iterator(); it.hasNext();) {
			MenuItem item = it.next();
			if (item.isDeleted()) {
				it.remove();
				item.setDeleted(false);
				mTempOriginalList.add(item);
				mMenuAdapter.add(item);
				mCount--;
				hasChange = true;
			}
		}
		if (hasChange) {
			mMenuAdapter.notifyDataSetChanged();
			if (mCount == 0) {
				hideLayoutCount();
			}
			mDishChoiced.setText("" + mCount + " " + strDish + "    ");
		}
	}

	/**
	 * Hien thi so luong mon an
	 */
	private void showLayoutCount() {
		mDishSelected.setVisibility(View.VISIBLE);
		Animation animation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.slide_up);
		mDishSelected.startAnimation(animation);
	}

	/**
	 * Hide layout dem so luong mon an
	 */
	private void hideLayoutCount() {
		Animation animation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.slide_down);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				mDishSelected.setVisibility(View.GONE);
			}
		});
		mDishSelected.startAnimation(animation);
	}

	@Override
	public void onMenuTaskListener(ArrayList<MenuItem> data) {
		if (mWaitingDialog.isShowing())
			mWaitingDialog.dismiss();
		copyToOriginal(data);
		for (MenuItem menuItem : data) {
			mMenuAdapter.add(menuItem);
		}
		mMenuAdapter.notifyDataSetChanged();
	}

	@Override
	public void onActionDataError(RestClientTask task, int errorCode) {
		if (errorCode == RestClientNotification.ERROR_DATA) {
			this.finish();
		} else {
			doLoadMenuError(errorCode);
		}
	}

//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		Logger.e(TAG, "onActivityResult");
//		if (resultCode == RESULT_OK) {
//			MenuItem item = (MenuItem) data.getExtras().getSerializable(
//					EXTRA_MENU_ITEM);
//			mMenuAdapter.add(item);
//			mTempOriginalList.add(item);
//			mMenuAdapter.notifyDataSetChanged();
//		}
//	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Logger.e(TAG, "onActivityResult");
		if (resultCode == RESULT_OK) {
			MenuItem item = (MenuItem) data.getExtras().getSerializable(
					EXTRA_MENU_ITEM);
			mMenuAdapter.add(item);
			mTempOriginalList.add(item);
			mMenuAdapter.notifyDataSetChanged();
		}
	}
}
