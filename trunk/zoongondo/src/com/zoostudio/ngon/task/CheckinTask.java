package com.zoostudio.ngon.task;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.adapter.item.CheckInItem;
import com.zoostudio.restclient.RestClientTask;

public class CheckinTask extends RestClientTask {

	private String mSpotId;
	private ArrayList<String> mDishesId;
	private String mComment;

	public CheckinTask(Activity activity, String spot_id) {
		this(activity, spot_id,"", new ArrayList<String>());
	}
	
	public CheckinTask(Activity activity, String spot_id,String comment) {
		this(activity, spot_id, comment,new ArrayList<String>());
	}

	public CheckinTask(Activity activity, String spot_id,String comment,
			ArrayList<String> dishes_id) {
		super(activity);
		mSpotId = spot_id;
		mDishesId = dishes_id;
		mComment = comment;
	}

	@Override
	public void doExecute() {
		restClient.addParam("spot_id", mSpotId);
		if (mDishesId.size() > 0) {
			StringBuffer buffer = new StringBuffer(1024);
			String dishes = "";
			for (String dish : mDishesId) {
				buffer.append(dish).append(",");
			}
			dishes = buffer.toString();
			dishes = dishes.substring(0, dishes.length() - 1);
			restClient.addParam("dishes", dishes);
		}
		if(!mComment.equals("")){
			restClient.addParam("comment", mComment);
		}
		restClient.put("/spot/checkin");
	}

	@Override
	protected void parseJSONToObject(JSONObject jsonObject) {
		
	}

}
