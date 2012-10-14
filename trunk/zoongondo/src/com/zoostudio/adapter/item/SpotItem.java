package com.zoostudio.adapter.item;

import java.io.Serializable;

import com.zoostudio.ngon.utils.LocationItem;

public class SpotItem implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
    private String address;
    private String id;
    private String urlImageSpot;
    private LocationItem location;
    
	public SpotItem(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.address = builder.address;
		this.location = builder.location;
		this.urlImageSpot = builder.urlImageSpot;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public LocationItem getLocation() {
        return location;
    }

    public void setLocation(LocationItem location) {
        this.location = location;
    }

	public static class Builder {
		private String name;
	    private String address;
	    private String id;
	    private LocationItem location;
	    private String urlImageSpot;
	    
	    public Builder() {
	    	
	    }
	    
	    public Builder setName(String name) {
	    	this.name = name;
	    	return this;
	    }
	    
	    public Builder setAddress(String address) {
	    	this.address = address;
	    	return this;
	    }
	    
	    public Builder setId(String id) {
	    	this.id = id;
	    	return this;
	    }
	    
	    public Builder setLocation(LocationItem loc) {
	    	location = loc;
	    	return this;
	    }
	    
	    public Builder setUrlImage(String urlImage){
	    	this.urlImageSpot = urlImage;
	    	return this;
	    }
	    public SpotItem create() {
	    	return new SpotItem(this);
	    }
	}
	
	public void setUrlImageSpot(String urlImageSpot) {
		this.urlImageSpot = urlImageSpot;
	}
	public String getUrlImageSpot() {
		return urlImageSpot;
	}
}
