package com.zoostudio.adapter.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DishItem implements Serializable {
	private String dishId;
	private String urlImageThumb;
	private String title;
	private String cost;
	private String urlImageLarge;
	
	private boolean selected;
	private boolean deleted;
	
	public DishItem() {
		selected = false;
		deleted = false;
	}
	
	public DishItem(String dishId, String imageThumbUrl, String title, String cost,String imageLargeUrl) {
		super();
		this.dishId = dishId;
		this.urlImageThumb = imageThumbUrl;
		this.title = title;
		this.cost = cost;
		this.urlImageLarge = imageLargeUrl;
	}
	

	

	public void setDishId(String dishId) {
		this.dishId = dishId;
	}

	public String getDishId() {
		return dishId;
	}

	public DishItem(String id, String title,String urlImageThumb,String urlImageLarge) {
		this.title = title;
		this.dishId = id;
		this.urlImageThumb = urlImageThumb;
		this.urlImageLarge = urlImageLarge;
		selected = false;
	}

	public String getUrlImageThumb() {
		return urlImageThumb;
	}

	public String getTitle() {
		return title;
	}

	public String getCost() {
		return cost;
	}

	public void setUrlImageThumb(String imageUrl) {
		this.urlImageThumb = imageUrl;
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
}
