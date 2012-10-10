package com.zoostudio.adapter.item;

import com.zoostudio.ngon.utils.LocationItem;

import android.os.Parcel;
import android.os.Parcelable;

public class SpotItem implements Parcelable {
    private String name;
    private String address;
    private String id;
    private String urlImageSpot;
    private LocationItem location;
    
    public SpotItem(Parcel in) {
    	readFromParcel(in);
    }

	public SpotItem(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.address = builder.address;
		this.location = builder.location;
		this.urlImageSpot = builder.urlImageSpot;
	}

	private void readFromParcel(Parcel in) {
		name = in.readString();
		address = in.readString();
		id = in.readString();
		location = in.readParcelable(LocationItem.class.getClassLoader());
		urlImageSpot = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(address);
		dest.writeString(id);
		dest.writeParcelable(location, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(urlImageSpot);
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

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<SpotItem> CREATOR =
			new Parcelable.Creator<SpotItem>() {
		public SpotItem createFromParcel(Parcel in) {
			return new SpotItem(in);
		}

		public SpotItem[] newArray(int size) {
			return new SpotItem[size];
		}
	};
	
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
