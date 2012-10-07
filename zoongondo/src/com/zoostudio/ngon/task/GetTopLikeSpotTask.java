package com.zoostudio.ngon.task;

import android.app.Activity;
import android.location.Location;

import com.zoostudio.restclient.RestClientTask;

public class GetTopLikeSpotTask extends RestClientTask {

    private int mLimit;
    private int mOffset;
    private Location mLocation;

    public GetTopLikeSpotTask(Activity activity, Location loc, int limit, int offset) {
        super(activity);

        mLimit = limit;
        mOffset = offset;
        mLocation = loc;
    }

    public GetTopLikeSpotTask(Activity activity) {
        this(activity, 0);
    }

    public GetTopLikeSpotTask(Activity activity, int limit) {
        this(activity, limit, 0);
    }

    public GetTopLikeSpotTask(Activity activity, int limit, int offset) {
        this(activity, null, limit, offset);
    }

    public GetTopLikeSpotTask(Activity activity, Location loc, int limit) {
        this(activity, loc, limit, 0);
    }

    public GetTopLikeSpotTask(Activity activity, Location loc) {
        this(activity, loc, 0);
    }

    @Override
    public void doExecute() {
        if (mLimit > 0) {
            restClient.addParam("limit", mLimit);
            restClient.addParam("offset", mOffset);
        }

        if (null != mLocation) {
            restClient.addParam("lon", mLocation.getLongitude());
            restClient.addParam("lat", mLocation.getLatitude());
        }

        restClient.get("/spot/toplike");
    }

}