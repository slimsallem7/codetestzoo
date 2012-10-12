package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class GetSearchSpotTask extends RestClientTask {

	private String mName;
	private int mLimit;
	private double mLong;
	private double mLat;

	public GetSearchSpotTask(Activity activity, double latitude, double longitude) {
		this(activity, "", latitude, longitude, 0);
	}

	public GetSearchSpotTask(Activity activity, String name, double latitude,
			double longitude) {
		this(activity, name, latitude, longitude, 0);
	}

	public GetSearchSpotTask(Activity activity, String name, double latitude,
			double longitude, int limit) {
		super(activity);
		mName = name;
		mLimit = limit;
		mLong = longitude;
		mLat = latitude;
	}

	public void setSearchName(String name) {
		mName = name;
	}

	@Override
	public void doExecute() {
		if (mName.length() == 0) {
			return;
		}

		restClient.addParam("long", mLong);
		restClient.addParam("lat", mLat);
		restClient.addParam("name", mName);
		if (mLimit > 0) {
			restClient.addParam("limit", mLimit);
		}

		restClient.get("/spot/search");
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		return RestClientNotification.OK;
	}

}
