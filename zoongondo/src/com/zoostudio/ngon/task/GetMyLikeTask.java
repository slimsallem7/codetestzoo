package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class GetMyLikeTask extends RestClientTask {
    private int mLimit;

    public GetMyLikeTask(Activity activity) {
        this(activity, 0);
    }

    public GetMyLikeTask(Activity activity, int limit) {
        super(activity);
        mLimit = limit;
    }

    @Override
    public void doExecute() {
        if (mLimit > 0) {
            restClient.addParam("limit", mLimit);
        }

        restClient.get("/my/like");
    }

	@Override
	protected void parseJSONToObject(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		
	}

}
