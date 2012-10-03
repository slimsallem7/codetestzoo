package com.zoostudio.adapter.item;

import java.io.Serializable;

public class MediaItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2966177523387393551L;
	private long idMedia;
	private String pathMedia;
	private int orient;

	private boolean isSelected;
	private String mineType;

	public MediaItem() {
		idMedia = -1;
		isSelected = false;
	}

	public long getIdMedia() {
		return idMedia;
	}

	public void setValue(String path, long mediaId, int orient, String mineType) {
		this.pathMedia = path;
		this.idMedia = mediaId;
		this.orient = orient;
		this.mineType = mineType;
	}

	public void setValue(String path, long mediaId, boolean selected) {
		this.pathMedia = path;
		this.idMedia = mediaId;
		this.isSelected = selected;
	}

	public String getPathMedia() {
		return pathMedia;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void onSelected() {
		this.isSelected = !isSelected;
	}

	public int getOrient() {
		return orient;
	}

	public String getMineType() {
		return mineType;
	}
}
