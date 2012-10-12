package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.zoostudio.restclient.RestClientTask;

public class GetAddressFromGeoTask extends RestClientTask {

    private double mLong;
    private double mLat;

    public GetAddressFromGeoTask(Activity activity, Location location) {
        this(activity, location.getLatitude(), location.getLongitude());
    }

    public GetAddressFromGeoTask(Activity activity, double latitude, double longitude) {
        super(activity);
        mLong = longitude;
        mLat = latitude;
    }

    @Override
    public void doExecute() {
        restClient.addParam("long", mLong);
        restClient.addParam("lat", mLat);
        Log.i("GetAddressFromGeoTask","Long =" + mLong + " | Lat = " + mLat);
        restClient.get("/spot/geodecode");
    }

	@Override
	protected void parseJSONToObject(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		
	}

}
