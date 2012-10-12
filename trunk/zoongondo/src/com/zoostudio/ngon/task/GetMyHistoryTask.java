package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class GetMyHistoryTask extends RestClientTask {

    private int mLimit;

    public GetMyHistoryTask(Activity activity) {
        this(activity, 0);
    }

    public GetMyHistoryTask(Activity activity, int limit) {
        super(activity);
        mLimit = limit;
    }

    @Override
    public void doExecute() {
        if (mLimit > 0) {
            restClient.addParam("limit", mLimit);
        }

        restClient.get("/my/history");
    }

	@Override
	protected void parseJSONToObject(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		
	}
}
