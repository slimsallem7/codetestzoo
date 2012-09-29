package com.zoostudio.adapter.item;

import java.io.Serializable;

public class MediaItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2966177523387393551L;
	private long idMedia;
	private String pathMedia;

	private boolean isSelected;

	public MediaItem() {
		idMedia = -1;
		isSelected = false;
	}

	public long getIdMedia() {
		return idMedia;
	}

	public void setValue(String path, long mediaId) {
		this.pathMedia = path;
		this.idMedia = mediaId;
	}

	public void setValue(String path, long mediaId, boolean selected) {
		this.pathMedia = path;
		this.idMedia = mediaId;
		this.isSelected = selected;
	}

	public String getPath1() {
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

}
