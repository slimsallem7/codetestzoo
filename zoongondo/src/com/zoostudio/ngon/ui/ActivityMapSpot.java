package com.zoostudio.ngon.ui;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.ui.base.BaseMapActivity;

/**
 * Dùng để hiển thị các địa điểm trên bản đồ. Mỗi địa điểm có một ảnh minh họa.
 * Khi ấn vào mỗi điểm thì vào trang chi tiết của địa điểm đó.
 * Danh sách địa điểm giống trang xem dạng list, cần có thêm nút để chọn giữa 3 danh sách: 
 * nearby, top rated và top new.
 * @author ThaiDuy
 *
 */
public class ActivityMapSpot extends BaseMapActivity {
	public static ArrayList<SpotItem> spotList = new ArrayList<SpotItem>();
	private Bitmap bmSpotImage;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_map;
	}

	@Override
	protected int getEditAddressId() {
		return 0;
	}
	
	@Override
	protected void initVariables() {
		super.initVariables();
	}

	@Override
	protected int getButtonGetLocationId() {
		return R.id.auto;
	}

	@Override
	protected int getMapViewId() {
		return R.id.mapView;
	}

	@Override
	protected void drawSpotLocation(MapView mapView, Canvas canvas) {
		Iterator<SpotItem> iterator = spotList.iterator();
		while (iterator.hasNext()) {
			SpotItem spot = iterator.next();
			
			double mSpotLat = spot.getLocation().getLatitude();
			double mSpotLong = spot.getLocation().getLongtitude();
			mSpotLat = mSpotLat * 1E6;
			mSpotLong = mSpotLong * 1E6;
			if (mSpotLat != -1 && mSpotLong != -1) {
				GeoPoint spotGeoPoint = new GeoPoint((int) (mSpotLat),
						(int) (mSpotLong));
				
				Point screenPts = new Point();
				mapView.getProjection().toPixels(spotGeoPoint, screenPts);
				bmSpotImage = BitmapFactory.decodeResource(getResources(),
						R.drawable.pushpin);
				
				canvas.drawBitmap(bmSpotImage, screenPts.x - 7, screenPts.y - 26, null);
			}
		}
	}
}
