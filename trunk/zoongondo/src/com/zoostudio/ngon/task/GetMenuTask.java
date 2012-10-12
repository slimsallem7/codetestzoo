package com.zoostudio.ngon.task;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class GetMenuTask extends RestClientTask {

    private String mSpotId;

    public GetMenuTask(Activity activity, String spot_id) {
        super(activity);
        mSpotId = spot_id;
    }

    @Override
    public void doExecute() {
        restClient.addParam("spot_id", mSpotId);
        restClient.get("/spot/menu");
    }

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		return RestClientNotification.OK;
	}

}
