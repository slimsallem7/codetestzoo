package com.zoostudio.ngon.ui;

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
	public static final String EXTRA_SPOTLIST = "com.zoostudio.ngon.ui.ActivityMapSpot.EXTRA_SPOTLIST";

	@Override
	protected int getLayoutId() {
		return R.layout.activity_map;
	}

	@Override
	protected int getEditAddressId() {
		return 0;
	}

	@Override
	protected int getButtonGetLocationId() {
		return R.id.auto;
	}

	@Override
	protected int getMapViewId() {
		return R.id.mapView;
	}

}
