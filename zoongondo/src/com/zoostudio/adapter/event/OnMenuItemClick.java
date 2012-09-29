package com.zoostudio.adapter.event;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnMenuItemClick implements OnItemClickListener {

    private Activity mActivity;
    private Class _class;
    public OnMenuItemClick(Activity activity,Class _class) {
        mActivity = activity;
        this._class = _class;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
//        MenuItem item = (MenuItem) adapterView.getItemAtPosition(pos);
        
        Intent intent = new Intent(mActivity, this._class);
        mActivity.startActivity(intent);
    }

}
