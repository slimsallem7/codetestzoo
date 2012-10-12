package com.zoostudio.adapter.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MenuItem implements Serializable {
	private String dishId;
	private String urlImageThumb;
	private String name;
	private String price;
	private String urlImageLarge;
	private String likeCount;
	private String spot_id;
	private boolean selected;
	private boolean deleted;
	
	public MenuItem() {
		selected = false;
		deleted = false;
	}
	
	public MenuItem(String dishId, String imageThumbUrl, String title, String cost,String imageLargeUrl) {
		super();
		this.dishId = dishId;
		this.urlImageThumb = imageThumbUrl;
		this.name = title;
		this.price = cost;
		this.urlImageLarge = imageLargeUrl;
	}
	
	public void setDishId(String dishId) {
		this.dishId = dishId;
	}

	public String getDishId() {
		return dishId;
	}

	public MenuItem(String id, String title,String urlImageThumb,String urlImageLarge) {
		this.name = title;
		this.dishId = id;
		this.urlImageThumb = urlImageThumb;
		this.urlImageLarge = urlImageLarge;
		selected = false;
	}

	public MenuItem(String name, String price, String likeCount) {
		this.name = name;
		this.price = price;
		this.likeCount = likeCount;
	}

	public String getUrlImageThumb() {
		return urlImageThumb;
	}

	public String getTitle() {
		return name;
	}

	public String getCost() {
		return price;
	}

	public void setUrlImageThumb(String imageUrl) {
		this.urlImageThumb = imageUrl;
	}

	public void setCost(String cost) {
		this.price = cost;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getLargeImageUrl() {
		return urlImageLarge;
	}
	
	public void setLargeImageUrl(String largeImageUrl) {
		this.urlImageLarge = largeImageUrl;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setSpotId(String spot_id) {
		this.spot_id = spot_id;
	}
	
	public String getSpotId() {
		return spot_id;
	}
}
