package com.zoostudio.ngon.task;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class CreateDishTask extends RestClientTask {
    private String mDishName;
    private String mSpotId;

    public CreateDishTask(Activity activity, String dish_name, String spot_id) {
        super(activity);
        mDishName = dish_name;
        mSpotId = spot_id;
    }

    @Override
    public void doExecute() {
        restClient.addParam("name", mDishName);
        restClient.addParam("spot_id", mSpotId);
        
        restClient.put("/dish");
    }

}
