package com.zoostudio.ngon.task;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class DeleteReviewTask extends RestClientTask {
	
	private int	mReviewId;
	
	public DeleteReviewTask(Activity activity,int review_id) {
		super(activity);
		mReviewId = review_id;
	}
	
	@Override public void doExecute() {
		restClient.addParam("review_id", mReviewId);
		restClient.delete("/review");
	}
	
}
