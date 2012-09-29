package com.zoostudio.adapter.item;

import java.util.ArrayList;

public class CheckInItem {
	private int mId;
	private long mTime;
	private SpotItem mSpotItem;
	private ArrayList<FoodItem> mFoodList;
	private String mStatus;
	
	public CheckInItem() {
		mFoodList = new ArrayList<FoodItem>();
	}
	public CheckInItem(String test) {
		mStatus = test;
		mFoodList = new ArrayList<FoodItem>();
	}
	public long getTime() {
		return mTime;
	}
	
	public SpotItem getSpotItem() {
		return mSpotItem;
	}
	
	public ArrayList<FoodItem> getFoodList() {
		return mFoodList;
	}
	
	public int getId() {
		return mId;
	}
	
	public void setId(int id) {
		mId = id;
	}
	
	public void setTime(long time) {
		mTime = time;
	}
	
	public void setSpotItem(SpotItem spot) {
		mSpotItem = spot;
	}
	
	public void setFoodList(ArrayList<FoodItem> mFoodItems) {
		mFoodList = mFoodItems;
	}
	
	public void addDish(FoodItem item) {
		mFoodList.add(item);
	}
	public String getStatus() {
		return mStatus;
	}
}
