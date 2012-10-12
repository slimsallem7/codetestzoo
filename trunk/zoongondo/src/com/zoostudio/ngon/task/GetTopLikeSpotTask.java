package com.zoostudio.ngon.task;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.location.Location;

import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.task.callback.OnSpotItemReceiver;
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class GetTopLikeSpotTask extends RestClientTask {

	private int mLimit;
	private int mOffset;
	private Location mLocation;
	private ArrayList<SpotItem> mData;
	private String[] imageDumps = {
			"http://i-cdn.apartmenttherapy.com/uimages/kitchen/2012_10_09-ProsciuttoArugulaPizza01.jpg",
			"http://i136.photobucket.com/albums/q197/bermuda2810/main_dish_image-1.jpg",
			"http://i-cdn.apartmenttherapy.com/uimages/kitchen/2012-10-09-ThinCrustPizza-2.jpg",
			"http://i-cdn.apartmenttherapy.com/uimages/kitchen/brownie%20pie.jpeg",
			"http://i-cdn.apartmenttherapy.com/uimages/kitchen/2012_09_26-bakewell4.jpg",
			"http://i-cdn.apartmenttherapy.com/uimages/kitchen/2012_06_04-Berries.jpeg",
			"http://www.thekitchn.com/dessert-recipe-baked-nutella-cream-cheese-sandwich-recipes-from-the-kitchn-177279",
			"http://img.foodnetwork.com/FOOD/2010/03/25/FNM_050110-Weeknight-Dinners-032_s4x3_lg.jpg",
			"http://img.foodnetwork.com/FOOD/2011/11/14/FNM_120111-WN-Dinners-009_s4x3_lg.jpg",
			"http://img.foodnetwork.com/FOOD/2012/05/04/FNM_060112-50-Things-to-Grill-in-Foil-Jerk-Chicken_s4x3_lg.jpg" };
	private OnSpotItemReceiver mListener;

	public GetTopLikeSpotTask(Activity activity, Location loc, int limit,
			int offset) {
		super(activity);

		mLimit = limit;
		mOffset = offset;
		mLocation = loc;
	}
	
	@Override
	protected void onPreExecute() {
		
	}
	
	public GetTopLikeSpotTask(Activity activity) {
		this(activity, 0);
	}

	public GetTopLikeSpotTask(Activity activity, int limit) {
		this(activity, limit, 0);
	}

	public GetTopLikeSpotTask(Activity activity, int limit, int offset) {
		this(activity, null, limit, offset);
	}

	public GetTopLikeSpotTask(Activity activity, Location loc, int limit) {
		this(activity, loc, limit, 0);
	}

	public GetTopLikeSpotTask(Activity activity, Location loc) {
		this(activity, loc, 0);
	}

	@Override
	public void doExecute() {
		if (mLimit > 0) {
			restClient.addParam("limit", mLimit);
			restClient.addParam("offset", mOffset);
		}

		if (null != mLocation) {
			restClient.addParam("lon", mLocation.getLongitude());
			restClient.addParam("lat", mLocation.getLatitude());
		}

		restClient.get("/spot/toplike");
	}

	@Override
	protected void parseJSONToObject(JSONObject jsonObject) {
		JSONArray spotData;
		mData = new ArrayList<SpotItem>();
		try {
			spotData = jsonObject.getJSONArray("data");
			int size = spotData.length();
			if (size == 0) {
				return;
			}
			for (int i = 0; i < size; i++) {
				JSONObject item = spotData.getJSONObject(i);
				SpotItem spotItem = ParserUtils.parseSpot(item);
				spotItem.setUrlImageSpot(imageDumps[i]);
				mData.add(spotItem);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostExecute(Integer status) {
		if (mWaitingStatus && mWaitingDialog != null) {
			mWaitingDialog.dismiss();
		}
		if (null != mListener && status == RestClientNotification.OK) {
			mListener.onDataReceiver(mData);
		} else if (status == RestClientNotification.ERROR && null != mListener) {
			mListener.onError(mErrorCode);
		}
	}

	public void setOnSpotItemReceiver(OnSpotItemReceiver listener) {
		this.mListener = listener;
	}

}
