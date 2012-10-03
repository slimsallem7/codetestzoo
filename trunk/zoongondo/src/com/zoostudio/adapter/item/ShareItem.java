package com.zoostudio.adapter.item;

public class ShareItem {
	private String title;
	private String link;
	private String desc;
	private byte[] image;

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTitle() {
		return title;
	}

	public String getDesc() {
		return desc;
	}

	public byte[] getImage() {
		return image;
	}

	public String getLink() {
		return link;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
