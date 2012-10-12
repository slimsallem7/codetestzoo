package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class GetFeedByUseIdTask extends RestClientTask {
    public static final int ACTION_TYPE_CHECKIN = 1;
    public static final int ACTION_TYPE_LIKE = 2;

    private String mUserId;	
    private int mLimit;
    private int mOffset;

    public GetFeedByUseIdTask(Activity activity) {
        this(activity, "");
    }

    public GetFeedByUseIdTask(Activity activity, String user_id) {
        this(activity, user_id, 0);
    }

    public GetFeedByUseIdTask(Activity activity, String user_id, int limit) {
        this(activity, user_id, limit, 0);
    }

    public GetFeedByUseIdTask(Activity activity, String user_id, int limit, int offset) {
        super(activity);

        mUserId = user_id;
        mLimit = limit;
        mOffset = offset;
    }

    @Override
    protected void doExecute() {
        if (mUserId.length() > 0) {
            restClient.addParam("user_id", mUserId);
        }

        if (mLimit > 0) {
            restClient.addParam("limit", mLimit);
            restClient.addParam("offset", mOffset);
        }

        restClient.get("/user/feed");
    }

    @Override
    public String getDumpData() {
        return "{\"status\":true,\"num_feed\":2,\"feeds\":[{\"type\":\"1\",\"timestamp\":\"1331886363\",\"spot\":{\"id\":\"101\",\"name\":\"Test 97\",\"address\":\"pacific\"},\"dishes\":[{\"id\":\"1\",\"name\":\"kem\"},{\"id\":\"2\",\"name\":\"bun\"},{\"id\":\"3\",\"name\":\"mien\"}]},{\"type\":\"2\",\"timestamp\":\"1331886363\",\"spot\":{\"id\":\"100\",\"name\":\"Test 96\",\"address\":\"pacific\"}}]}";
    }

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		return RestClientNotification.OK;
	}

}
