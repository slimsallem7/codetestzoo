package com.zoostudio.ngon.task;

import org.bookmark.helper.TextCore;
import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class GetSpotSuggest extends RestClientTask {

	private String mSearchCondition;
	private int mLimit;

	public GetSpotSuggest(Activity activity,String search_condition) {
		this(activity,search_condition, 0);
	}

	public GetSpotSuggest(Activity activity, String search_condition, int limit) {
		super(activity);
		setSearchCondition(search_condition);
		mLimit = limit;
	}

	public void setSearchCondition(String search_condition) {
		mSearchCondition = TextCore.removeDuplicateWhitespace(search_condition
				.trim());
	}

	@Override
	public void doExecute() {
		restClient.addParam("name", mSearchCondition);
		if (mLimit > 0) {
			restClient.addParam("limit", mLimit);
		}

		restClient.get("/spot/suggest");
	}

	@Override
	protected void parseJSONToObject(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		
	}

}
