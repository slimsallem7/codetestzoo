package com.zoostudio.ngon.task;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class GetNearbySpotTask extends RestClientTask {

    private int mLimit;
    private int mAccLvl;
    private double mLong;
    private double mLat;

    public GetNearbySpotTask(Activity activity, double latitude, double longitude) {
        this(activity, latitude, longitude, 0);
    }

    public GetNearbySpotTask(Activity activity, double latitude, double longitude, int limit) {
        this(activity, latitude, longitude, limit, 2);
    }

    public GetNearbySpotTask(Activity activity, double latitude, double longitude, int limit, int accuracy_level) {
        super(activity);
        mLimit = limit;
        mAccLvl = accuracy_level;
        mLong = longitude;
        mLat = latitude;
    }
    
    @Override
    public void doExecute() {
        restClient.addParam("long", mLong);
        restClient.addParam("lat", mLat);

        if (mLimit > 0) {
            restClient.addParam("limit", mLimit);
        }

        if (mAccLvl > 0) {
            restClient.addParam("max_distance", mAccLvl);
        }

        restClient.get("/spot/nearby");
    }

    @Override
    public String getDumpData() {
        return "{\"data\":[{\"name\":\"Test 1\",\"distance\":\"43.030\"},{\"name\":\"Test 2\",\"distance\":\"43.030\"},{\"name\":\"Test 3\",\"distance\":\"43.030\"},{\"name\":\"Test 4\",\"distance\":\"43.030\"},{\"name\":\"Test 5\",\"distance\":\"43.030\"},{\"name\":\"Test 6\",\"distance\":\"43.030\"},{\"name\":\"Test 7\",\"distance\":\"43.030\"},{\"name\":\"Test 8\",\"distance\":\"43.030\"},{\"name\":\"Test 9\",\"distance\":\"43.030\"},{\"name\":\"Test 10\",\"distance\":\"43.030\"}]}"; 
    }
}
