package com.zoostudio.ngon.ui;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.zoostudio.adapter.item.DishItem;
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
import com.zoostudio.ngon.utils.ArrayUtils;
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.ngon.utils.PipedDeepCopy;
import com.zoostudio.ngon.views.ButtonListitemAdd;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;

public class ChooseDish extends NgonActivity implements OnPreExecuteDelegate,
		OnPostExecuteDelegate, OnClickListener, OnItemClickListener,
		ChooseDishAdapter.OnDishChoice {

	public static final String EXTRA_SPOT = "com.ngon.do.choosedish.SPOT";

	private ListView lvMenu;
	private SpotItem mSpot;
	private GetMenuTask mMenuTask;
	private WaitingDialog mWaitingDialog;
	private ChooseDishAdapter mMenuAdapter;
	private ButtonListitemAdd btnAddDish;
	private TextView mDishChoiced;
	private String strDish;
	private ArrayList<DishItem> listSelected;
	private ArrayList<DishItem> mTempDish;
	private ArrayList<DishItem> originalList;
	private ArrayList<DishItem> mTempOriginalList;
	private Button btnDone;
	private View mDishSelected;
	private int mCount;
	private View incPopUp;
	private ButtonUp btnUp;
	private View footer;
	private final String URL1 = "http://i366.photobucket.com/albums/oo105/AvonB7/Food/Rovellones.jpg";
	private final String URL2 = "http://i284.photobucket.com/albums/ll26/msbowjangles16/Chinese-food.jpg";
	private final String URL3 = "http://i870.photobucket.com/albums/ab270/moonbeambouvier/Food/20121008Dessert.jpg";
	private final String URL4 = "http://i1245.photobucket.com/albums/gg587/PB_Loves/Scratch%20n%20Sniff/f2d9c76b.jpg";
	private final String URL5 = "http://i1245.photobucket.com/albums/gg587/PB_Loves/Scratch%20n%20Sniff/0664ca87.jpg";
	private final String URL6 = "http://i255.photobucket.com/albums/hh149/MargieHaire/Animals/food_02.jpg";
	private final String URL7 = "http://i1092.photobucket.com/albums/i414/herrysusanto1/food.jpg";

	@Override
	protected int setLayoutView() {
		return R.layout.activity_choose_dish;
	}

	@Override
	protected void initControls() {
		strDish = this.getResources().getString(R.string.dish);
		// mSpot = getSpot();
		lvMenu = (ListView) findViewById(R.id.list_menu);
		mDishChoiced = (TextView) findViewById(R.id.txtDishchoices);
		btnDone = (Button) findViewById(R.id.checkin);
		// mMenuTask = new GetMenuTask(this, mSpot.getId());
		mDishSelected = findViewById(R.id.layoutDishSelected);
		lvMenu.setOnItemClickListener(this);
		btnDone.setOnClickListener(this);
		mDishSelected.setOnClickListener(this);
		LayoutInflater inflate = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = inflate.inflate(R.layout.inc_footer_add_dish, null);
		lvMenu.addFooterView(footer);
		btnUp = (ButtonUp) findViewById(R.id.btn_up);
	}

	private void copyToOriginal(ArrayList<DishItem> arrayList) {
		for (int i = 0, n = arrayList.size(); i < n; i++) {
			originalList.add((DishItem) PipedDeepCopy.copy(arrayList.get(i)));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initVariables() {
		// mMenuTask.setOnPreExecuteDelegate(this);
		// mMenuTask.setOnPostExecuteDelegate(this);
		// mMenuTask.execute();
		mTempDish = new ArrayList<DishItem>();
		mTempOriginalList = new ArrayList<DishItem>();

		originalList = (ArrayList<DishItem>) this.getIntent().getExtras()
				.getSerializable("LIST_DISH");

		listSelected = (ArrayList<DishItem>) this.getIntent().getExtras()
				.getSerializable("LIST_SELECTED");
		mCount = 0;
		if (listSelected.isEmpty()) {
			ArrayList<DishItem> arrayList = new ArrayList<DishItem>();
			arrayList.add(new DishItem("0", "Lau thap cam", URL1, URL1));
			arrayList.add(new DishItem("1", "Lau ga", URL2, URL2));
			arrayList.add(new DishItem("2", "Lau nam", URL3, URL3));
			arrayList.add(new DishItem("3", "Suon xao chua ngot", URL4, URL4));
			arrayList.add(new DishItem("4", "Ga quay ngu vi", URL5, URL5));
			arrayList.add(new DishItem("5", "Vi xao xa ot", URL6, URL6));
			arrayList.add(new DishItem("6", "Lau mang", URL7, URL7));
			mMenuAdapter = new ChooseDishAdapter(this, 0, arrayList, this);
			copyToOriginal(arrayList);
		} else {
			mDishSelected.setVisibility(View.VISIBLE);
			mCount = listSelected.size();
			mDishChoiced.setText("" + mCount + " " + strDish + "    ");
			ArrayList<DishItem> listUnSelect = loadDishItem();
			mMenuAdapter = new ChooseDishAdapter(this, 0, listUnSelect, this);
		}

		mTempDish = (ArrayList<DishItem>) ArrayUtils.copyArray(listSelected);
		mTempOriginalList = (ArrayList<DishItem>) ArrayUtils
				.copyArray(originalList);
		lvMenu.setAdapter(mMenuAdapter);

		// lvMenu.setOnItemClickListener(new
		// OnMenuItemClick(this,MenuDetail.class));
		// lvMenu.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// btnAddDish.setOnClickListener(this);
	}

	private ArrayList<DishItem> loadDishItem() {
		ArrayList<DishItem> array;
		array = new ArrayList<DishItem>();
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
			return extras.getParcelable(EXTRA_SPOT);
		} else {
			finish();
			return null;
		}
	}

	@Override
	public void actionPre(RestClientTask task) {
		mWaitingDialog = new WaitingDialog(this);
		mWaitingDialog.show();
	}

	@Override
	public void actionPost(RestClientTask task, JSONObject result) {
		if (task == mMenuTask) {
			mWaitingDialog.dismiss();

			try {
				boolean status = result.getBoolean("status");
				if (status) {
					JSONArray menuData = result.getJSONArray("data");

					doLoadMenu(menuData);
				} else {
					int errorCode = result.getInt("error_code");
					doLoadMenuError(errorCode);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				finish();
			}
		}
	}

	private void doLoadMenu(JSONArray menuData) throws JSONException {
		for (int i = 0, size = menuData.length(); i < size; i++) {
			JSONObject row = menuData.getJSONObject(i);

			MenuItem item = ParserUtils.parseMenu(row, mSpot.getId());

			// mMenuAdapter.add(item);
		}
	}

	private void doLoadMenuError(int errorCode) {
		switch (errorCode) {
		case ErrorCode.SPOT_NOT_EXIST:

			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnAddDish) {
			Intent intent = new Intent(this, AddDishActivity.class);
			intent.putExtra("spot_id", mSpot);
			startActivityForResult(intent, RequestCode.ADD_DISH);
		} else if (v == btnDone) {
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
		btnUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

	}

	@Override
	public void onDishChoiceListener(int count, DishItem dishItem) {
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
		for (Iterator<DishItem> it = mTempDish.iterator(); it.hasNext();) {
			DishItem item = it.next();
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
}
