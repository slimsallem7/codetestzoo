package com.zoostudio.ngon.task;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class DeleteHistoryTask extends RestClientTask {

	private int mHistoryId;

	public DeleteHistoryTask(Activity activity, int history_id) {
		super(activity);
		mHistoryId = history_id;
	}

	@Override
	public void doExecute() {
		restClient.addParam("history_id", mHistoryId);
		restClient.delete("/history");
	}

}
