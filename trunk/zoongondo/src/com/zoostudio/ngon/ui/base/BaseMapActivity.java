package com.zoostudio.ngon.ui.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.zoostudio.adapter.AutoCompleteAdapter;
import com.zoostudio.adapter.item.InfoAddress;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonErrorDialog;
import com.zoostudio.ngon.utils.ConfigSize;
import com.zoostudio.ngon.utils.LocationUtil;
import com.zoostudio.ngon.utils.NgonTextWatcher;
import com.zoostudio.ngon.utils.OnAddressChanged;
import com.zoostudio.ngon.utils.OnAddressSuggestListener;
import com.zoostudio.ngon.utils.OnLocationRecevier;

public abstract class BaseMapActivity extends MapActivity implements
		OnLocationRecevier, OnAddressChanged, OnAddressSuggestListener,
		OnItemClickListener, OnClickListener {

	protected static final int DIALOG_ERROR = 0;

	private static final String BUNDLE_ERROR_CODE = null;

	public static final String EXTRA_CURRENT_ADDRESS = "com.zoostudio.ngon.ui.base.BaseMapActivity.EXTRA_CURRENT_ADDRESS";
	public static final String EXTRA_CURRENT_LONG = "com.zoostudio.ngon.ui.base.BaseMapActivity.EXTRA_CURRENT_LONG";
	public static final String EXTRA_CURRENT_LAT = "com.zoostudio.ngon.ui.base.BaseMapActivity.EXTRA_CURRENT_LAT";
	
	protected MapView mvSelectLocation;
	protected MapController mapControl;
	protected GeoPoint mCurrentGeoPoint;
	protected GeoPoint mMeGeoPoint;
	protected MapOverlay mapOverlay;
	protected Handler mHandler;
	protected AutoCompleteTextView etAddress;
	protected String mLastAddress;
	protected String mCurrentAddress;
	protected double mCurrentLat;
	protected double mCurrentLong;
	protected boolean mIsFirstTime;
	protected AutoCompleteAdapter mAutoCompleteAdapter;
	protected InputMethodManager imm;
	protected ImageButton btnGetLoc;
	private int mBtnGetLocationId;
	private int mEdtAddressId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		initControls();
		initVariables();
		initActions();
	}

	protected void initControls() {
		mEdtAddressId = getEditAddressId();
		etAddress = (AutoCompleteTextView) findViewById(mEdtAddressId);
		mBtnGetLocationId = getButtonGetLocationId();
		btnGetLoc = (ImageButton) findViewById(mBtnGetLocationId);
		mvSelectLocation = (MapView) findViewById(getMapViewId());
		mapControl = mvSelectLocation.getController();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != mMeGeoPoint) {
			mapControl.setCenter(mMeGeoPoint);
			mapControl.setZoom(16);
			mapControl.animateTo(mMeGeoPoint);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	protected void initVariables() {
		mapOverlay = new MapOverlay();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mHandler = new Handler();
		mAutoCompleteAdapter = new AutoCompleteAdapter(getBaseContext(),
				R.id.txtSuggestAddress, new ArrayList<InfoAddress>());
		mIsFirstTime = true;

		mCurrentAddress = this.getIntent().getExtras()
				.getString(EXTRA_CURRENT_ADDRESS);
		mCurrentLat = this.getIntent().getExtras().getDouble(EXTRA_CURRENT_LAT);
		mCurrentLong = this.getIntent().getExtras().getDouble(EXTRA_CURRENT_LONG);
		
		mCurrentLat = mCurrentLat * 1E6;
		mCurrentLong = mCurrentLong * 1E6;
		if (mCurrentLat != -1 && mCurrentLong != -1) {
			mMeGeoPoint = new GeoPoint((int) (mCurrentLat),
					(int) (mCurrentLong));
		}
		
		if (null != etAddress) {
			etAddress.setText(mCurrentAddress);
			etAddress.setAdapter(mAutoCompleteAdapter);
			etAddress.setOnItemClickListener(this);
			etAddress.setOnClickListener(this);
		}
		
		List<Overlay> listOfOverlays = mvSelectLocation.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);
		mvSelectLocation.invalidate();

		watcherAddress();
	}

	protected void initActions() {
		if(null != btnGetLoc){
			btnGetLoc.setOnClickListener(this);
		}
	}

	protected void watcherAddress() {
		etAddress.addTextChangedListener(new NgonTextWatcher() {
			@Override
			public void onTextRelease() {
				super.onTextRelease();
				new Thread(new Runnable() {
					@Override
					public void run() {
						LocationUtil locationUtil = new LocationUtil();
						locationUtil
								.setOnAddressSuggListener(BaseMapActivity.this);
						locationUtil.getSuggestAddress(getBaseContext(),
								etAddress.getText().toString());
					}
				}).start();
			}
		});
	}

	@Override
	public void onLocationRecevier(GeoPoint point) {
		try {
			mCurrentGeoPoint = point;
			mapControl.setCenter(point);
			mapControl.setZoom(16);
			mapControl.animateTo(mCurrentGeoPoint);
			mvSelectLocation.invalidate();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	class MapOverlay extends com.google.android.maps.Overlay {
		private int mMode;
		public static final int DRAG = 1;
		public static final int ZOOM = 2;
		private static final int NONE = 0;
		private boolean mCancelTabPoint = false;

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);
			if (null != mMeGeoPoint) {
				// ---translate the GeoPoint to screen pixels---
				Point screenPts = new Point();
				mapView.getProjection().toPixels(mMeGeoPoint, screenPts);

				// ---add the marker---
				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.icon_picker_location);

				canvas.drawBitmap(bmp, screenPts.x + ConfigSize.SIZE_PICKER,
						screenPts.y - ConfigSize.SIZE_PICKER, null);
			}

			if (null != mCurrentGeoPoint) {

				// ---translate the GeoPoint to screen pixels---
				Point screenPts = new Point();
				mapView.getProjection().toPixels(mCurrentGeoPoint, screenPts);

				// ---add the marker---
				// TODO: @DUYNT Sua anh Marker cho map
				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.pushpin);

				canvas.drawBitmap(bmp, screenPts.x - 7, screenPts.y - 26, null);
			}
			return true;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// Neu action la down mode = Drag (Scroll)
			case MotionEvent.ACTION_DOWN:
				mCancelTabPoint = false;
				mMode = DRAG;
				break;
			// Neu ngon tay thu 2 cham xuong. Va di chuyen. kiem tra xem khoang
			// cach giua 2 ngon tay > 10f thi la hanh dong Zoom
			case MotionEvent.ACTION_POINTER_DOWN:
				mMode = ZOOM;
				break;
			// Neu tha ngon tay ra hanh dong dag lam Zoom. thi ket thuc hanh
			// dong
			case MotionEvent.ACTION_UP:
				if (!mCancelTabPoint) {

					GeoPoint p = mapView.getProjection().fromPixels(
							(int) event.getX(), (int) event.getY());
					mCurrentGeoPoint = p;
					LocationUtil locationUtil = new LocationUtil();
					locationUtil
							.setOnAddressChangedListener(BaseMapActivity.this);
					Location location = new Location(
							LocationManager.GPS_PROVIDER);
					location.setLongitude(mCurrentGeoPoint.getLongitudeE6() / 1E6);
					location.setLatitude(mCurrentGeoPoint.getLatitudeE6() / 1E6);

					locationUtil.getAddress(getBaseContext(),
							mCurrentGeoPoint.getLatitudeE6() / 1E6,
							mCurrentGeoPoint.getLongitudeE6() / 1E6);

					return true;
				}
				break;
			// Neu ngon tay thu 2 tha ra thi dua mode ve mac dinh
			case MotionEvent.ACTION_POINTER_UP:
				mMode = NONE;
				break;
			// Neu ngon tay di chuyen. Ma hanh dong dang la Drag thi Scroll man
			// hinh. Nguoc lai la zoom
			case MotionEvent.ACTION_MOVE:
				if (mMode == DRAG || mMode == ZOOM) {
					mCancelTabPoint = true;
				}
				break;
			}
			return false;
		}
	}

	@Override
	public void onAddressChange(final String newAddress) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				String address = LocationUtil.parseAddress(newAddress);
				etAddress.setText(address);
			}
		});
	}

	// Call back khi go
	@Override
	public void onAddressSuggestRecever(final ArrayList<InfoAddress> address) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Log.i("SelectLocationManual", " add = " + address.toString());
				mAutoCompleteAdapter.clear();
				for (InfoAddress item : address) {
					mAutoCompleteAdapter.add(item, AutoCompleteAdapter.ADD_NEW);
				}
				mAutoCompleteAdapter.notifyDataSetInvalidated();
				mAutoCompleteAdapter.getFilter().filter(
						etAddress.getText().toString());
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		imm.hideSoftInputFromWindow(etAddress.getWindowToken(), 0);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == mBtnGetLocationId) {
			LocationUtil locationUtil = new LocationUtil();
			locationUtil.setOnLocationRecevier(this);
			mLastAddress = etAddress.getText().toString() + ", Viet nam";
			locationUtil.getLongLat(getBaseContext(), mLastAddress);
			return;

		} else if (v.getId() == mEdtAddressId) {
			if (mIsFirstTime && !etAddress.getText().equals("")) {
				etAddress.setText("");
				mIsFirstTime = false;
				return;
			}
		}
	}

	/*
	 * Layout id cua activity
	 */
	protected abstract int getLayoutId();

	protected abstract int getEditAddressId();

	protected abstract int getButtonGetLocationId();

	protected abstract int getMapViewId();

	protected void showDialogError(int errorCode) {
		Bundle bundle = new Bundle();
		bundle.putInt(BUNDLE_ERROR_CODE, errorCode);
		showDialog(DIALOG_ERROR, bundle);
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case DIALOG_ERROR:
			return createDialogError(args);

		default:
			break;
		}
		return super.onCreateDialog(id, args);
	}

	private Dialog createDialogError(Bundle args) {
		NgonErrorDialog builder = new NgonErrorDialog(this);
		builder.setErrorCode(args.getInt(BUNDLE_ERROR_CODE));
		return builder.create();
	}
}
