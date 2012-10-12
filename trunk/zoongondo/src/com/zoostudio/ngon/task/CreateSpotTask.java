package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class CreateSpotTask extends RestClientTask {

    private String mName;
    private double mLat;
    private double mLong;
    private String mAddress;

    public CreateSpotTask(Activity activity, String name, double latitude, double longitude) {
        super(activity);
        mName = name;
        mLat = latitude;
        mLong = longitude;
    }

    public CreateSpotTask(Activity activity, String name, String address, double latitude, double longitude) {
        super(activity);
        mName = name;
        mLat = latitude;
        mLong = longitude;
        mAddress = address;
    }

    @Override
    public void doExecute() {
        restClient.addParam("name", mName);
        restClient.addParam("lat", mLat);
        restClient.addParam("long", mLong);
        if (mAddress != null) restClient.addParam("address", mAddress);

        restClient.put("/spot");
    }

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		return RestClientNotification.OK;
	}

}
