package com.zoostudio.ngon.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zoostudio.adapter.SpotMenuAdapter;
import com.zoostudio.adapter.event.OnMenuItemClick;
import com.zoostudio.adapter.item.MenuItem;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.ErrorCode;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.RequestCode;
import com.zoostudio.ngon.task.GetMenuTask;
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.ngon.views.ButtonListitemAdd;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;
import com.zoostudio.restclient.RestClientTask.OnPreExecuteDelegate;

public class SpotMenu extends NgonActivity implements OnPreExecuteDelegate, OnPostExecuteDelegate {

    public static final String EXTRA_SPOT = "com.ngon.do.spotmenu.SPOT";
    
	private ListView lvMenu;
    private SpotItem mSpot;
    private GetMenuTask mMenuTask;
    private SpotMenuAdapter mMenuAdapter;

	private ButtonListitemAdd mFooterViewAddNew;
	private RelativeLayout mFooterView;
	private ButtonUp mUp;
	
	private Handler handler = new Handler();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null == data) return;
        Bundle extras = data.getExtras();

        if (requestCode == RequestCode.ADD_DISH) {
            if (resultCode == RESULT_OK) {
                MenuItem item = new MenuItem();
                item.setDishId(extras.getString("dish_id"));
                item.setName(extras.getString("dish_name"));
                item.setSpotId(mSpot.getId());

                mMenuAdapter.add(item);
            }
        }
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_spot_menu;
    }

    @Override
    protected void initControls() {
        lvMenu = (ListView) findViewById(R.id.list_menu);
        mFooterView = new RelativeLayout(getApplicationContext());
		mFooterView.setGravity(Gravity.CENTER);
		mUp = (ButtonUp) findViewById(R.id.btn_up);
		
		mFooterViewAddNew = new ButtonListitemAdd(getApplicationContext());
		mFooterViewAddNew.setText("Th�m m�n");
		mFooterViewAddNew.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mFooterViewAddNew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(getApplicationContext(), AddDishActivity.class);
//				intent.putExtra(AddDishActivity.EXTRA_SPOT, mSpot);
//				startActivityForResult(intent, RequestCode.ADD_DISH);
			}
		});
		mFooterView.addView(mFooterViewAddNew);
		lvMenu.addFooterView(mFooterView);
    }

    @Override
    protected void initVariables() {
//        mSpot = getSpot();
//        mMenuTask = new GetMenuTask(this, mSpot.getId());
//        
//        mMenuTask.setOnPreExecuteDelegate(this);
//        mMenuTask.setOnPostExecuteDelegate(this);
//        mMenuTask.execute();
    	ArrayList<MenuItem> items = new ArrayList<MenuItem>(); 
    	items.add(new MenuItem("Bun bo hue","10K","100"));
    	items.add(new MenuItem("Bun dau","20K","200"));
    	items.add(new MenuItem("Bun thit","30K","300"));
    	items.add(new MenuItem("Bun mong","40K","400"));
    	items.add(new MenuItem("Bun suon","50K","500"));
    	
        mMenuAdapter = new SpotMenuAdapter(this, R.id.actionbar,items);
        lvMenu.setAdapter(mMenuAdapter);
        lvMenu.setOnItemClickListener(new OnMenuItemClick(this,MenuDetail.class));
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
    }

    @Override
    public void actionPost(RestClientTask task, JSONObject result) {
        if (task == mMenuTask) {

            try {
                boolean status = result.getBoolean("status");
                if (status) {
                    JSONArray menuData = result.getJSONArray("data");
                    doLoadMenu(menuData);
                    mFooterView.addView(mFooterViewAddNew);
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
            mMenuAdapter.add(item);
        }
    }

    private void doLoadMenuError(int errorCode) {
        switch (errorCode) {
            case ErrorCode.SPOT_NOT_EXIST:

                break;
        }
    }

	@Override
	protected void initActions() {
		mUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}


}
