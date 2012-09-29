package com.zoostudio.adapter.item;

public class LikeItem {
	private long mTime;
	private SpotItem mSpotItem;
	private FoodItem mFoodItem;
	
	public void setTime(long time) {
		mTime = time;
	}
	
	public void setSpot(SpotItem spot) {
		mSpotItem = spot;
	}
	
	public void setFood(FoodItem food) {
		mFoodItem = food;
	}

	public long getTime() {
		return mTime;
	}

	public FoodItem getFoodItem() {
		return mFoodItem;
	}

	public SpotItem getSpotItem() {
		return mSpotItem;
	}
}
