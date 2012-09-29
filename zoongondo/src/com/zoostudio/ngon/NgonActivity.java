package com.zoostudio.ngon;

import org.json.JSONObject;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.zoostudio.ngon.task.GetAddressFromGeoTask;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;
import com.zoostudio.service.impl.NgonLocationListener;

public abstract class NgonActivity extends Activity implements NgonLocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutView());
        initControls();
        initVariables();
        initActions();
    }

    protected abstract int setLayoutView();

    protected abstract void initControls();

    protected abstract void initVariables();
    
    protected abstract void initActions();
    
    @Override
    public void onLocationReceiver(Location location) {
    }
    
    @Override
    public void onFailGetLocation() {
    }
    
    @Override
    public void onGettingLocation() {
    }
    
    protected void setLocationBar() {
        GetAddressFromGeoTask locationTask = new GetAddressFromGeoTask(this, 21.02498547062812, 105.84438800811768);
        locationTask.setOnPostExecuteDelegate(new OnPostExecuteDelegate() {
            @Override
            public void actionPost(RestClientTask task, JSONObject result) {
                TextView tvAddress = (TextView) findViewById(R.id.location_address);
                tvAddress.setText(result.toString());
            }
        });
        locationTask.execute();
    }
}
