package com.zoostudio.adapter.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DishItem implements Serializable, Cloneable {
	private String dishId;
	private String imageUrl;
	private String title;
	private String cost;
	private String largeImageUrl;
	
	private boolean selected;
	private boolean deleted;
	
	public DishItem(String imageUrl, String title, String cost,String url) {
		super();
		this.imageUrl = imageUrl;
		this.title = title;
		this.cost = cost;
		this.imageUrl = url;
	}
	

	public DishItem() {
		selected = false;
		deleted = false;
	}

	public void setDishId(String dishId) {
		this.dishId = dishId;
	}

	public String getDishId() {
		return dishId;
	}

	public DishItem(String id, String title,String url) {
		this.title = title;
		this.dishId = id;
		this.imageUrl = url;
		selected = false;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public String getCost() {
		return cost;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getLargeImageUrl() {
		return largeImageUrl;
	}
	
	public void setLargeImageUrl(String largeImageUrl) {
		this.largeImageUrl = largeImageUrl;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
}
