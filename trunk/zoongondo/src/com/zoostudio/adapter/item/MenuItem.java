package com.zoostudio.adapter.item;

public class MenuItem {
	private String id;
	private String name;
	private String price;
	private String likeCount;
	private String spot_id;
	private String urlImage;

	public MenuItem(String name, String price, String likeCount) {
		this.name = name;
		this.price = price;
		this.likeCount = likeCount;
	}

	public MenuItem() {
	}

	public String getId() {
		return id;
	}

	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice() {
		return price;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public String getLikeCount() {
		return likeCount;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpotId() {
		return spot_id;
	}

	public void setSpotId(String spot_id) {
		this.spot_id = spot_id;
	}
}
