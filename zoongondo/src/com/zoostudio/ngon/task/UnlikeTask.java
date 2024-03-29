package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class UnlikeTask extends RestClientTask {

    private int mSpotId;

    public UnlikeTask(Activity activity, int spot_id) {
        super(activity);
        mSpotId = spot_id;
    }

    @Override
    public void doExecute() {
        restClient.addParam("spot_id", mSpotId);
        restClient.delete("/spot/like");
    }

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		return RestClientNotification.OK;
		
	}

}