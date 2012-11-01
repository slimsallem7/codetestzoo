package com.zoostudio.adapter.item;

public class DistanceItem {
	private String distance;
	private String unit;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer(1024);
		buffer.append(distance).append(" ").append(unit);
		return buffer.toString();
	}
}
