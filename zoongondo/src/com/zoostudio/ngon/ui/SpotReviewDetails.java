package com.zoostudio.ngon.ui;

import org.json.JSONObject;

import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.task.GetSpotReviewTask;
import com.zoostudio.restclient.RestClientTask;
import com.zoostudio.restclient.RestClientTask.OnPostExecuteDelegate;

public class SpotReviewDetails extends NgonActivity implements OnPostExecuteDelegate {

    private String mSpotId;

    @Override
    protected int setLayoutView() {
        return 0;
    }

    @Override
    protected void initControls() {
        GetSpotReviewTask reviewTask = new GetSpotReviewTask(this, mSpotId);
        reviewTask.setOnPostExecuteDelegate(this);
        reviewTask.execute();
    }

    @Override
    protected void initVariables() {

    }

    @Override
    public void actionPost(RestClientTask task, JSONObject result) {

    }

	@Override
	protected void initActions() {
		// TODO Auto-generated method stub
		
	}

}
