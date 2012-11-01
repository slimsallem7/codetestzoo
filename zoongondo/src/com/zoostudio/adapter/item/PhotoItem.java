package com.zoostudio.adapter.item;

import java.io.Serializable;

public class PhotoItem implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6230190900341892772L;
	private String id;
    private String path;
    private String medium_path;
    private String small_path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMediumPath() {
        return medium_path;
    }

    public void setMediumPath(String medium_path) {
        this.medium_path = medium_path;
    }

    public String getSmallPath() {
        return small_path;
    }

    public void setSmallPath(String small_path) {
        this.small_path = small_path;
    }

}
