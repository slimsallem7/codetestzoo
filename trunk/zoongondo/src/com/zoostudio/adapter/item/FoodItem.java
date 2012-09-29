package com.zoostudio.adapter.item;

import org.json.JSONException;
import org.json.JSONObject;

public class FoodItem {
	private String mName;
	private String mId;
	
	public FoodItem(JSONObject json) {
		try {
			mId = json.getString("id");
			mName = json.getString("name");
		} catch (JSONException e) { }
	}

	public String getName() {
		return mName;
	}
	
	public String getId() {
		return mId;
	}
	
	public void setName(String name) {
		this.mName = name;
	}
	
	public void setId(String id) {
		this.mId = id;
	}
}
