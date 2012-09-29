package com.zoostudio.ngon.utils;

import java.text.DecimalFormat;

import com.zoostudio.adapter.item.DistanceItem;

public class DistanceUtils {
	private final static DecimalFormat numberFormatter = new DecimalFormat(
			"#########.##");

	public static DistanceItem getDistanceDisplay(double metters) {
		DistanceItem item = new DistanceItem();

		if (metters < 100) {
			item.setUnit("m");
		} else if (metters < 1000) {
			item.setUnit("m");
		} else {
			metters /= 1000;
			item.setUnit("km");
		}

		item.setDistance(numberFormatter.format(metters));
		return item;
	}
}
