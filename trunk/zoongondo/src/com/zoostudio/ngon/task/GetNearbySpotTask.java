package com.zoostudio.ngon.task;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.ngon.task.callback.OnSpotItemListener;
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class GetNearbySpotTask extends RestClientTask {

    private int mLimit;
    private int mAccLvl;
    private double mLong;
    private double mLat;
    
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
	
	private OnSpotItemListener mListener;
	private ArrayList<SpotItem> mData;
    
    public GetNearbySpotTask(Activity activity, double latitude, double longitude) {
        this(activity, latitude, longitude, 0);
    }
    
    @Override
    protected void onPreExecute() {
    	
    }
    
    public GetNearbySpotTask(Activity activity, double latitude, double longitude, int limit) {
        this(activity, latitude, longitude, limit, 2);
    }

    public GetNearbySpotTask(Activity activity, double latitude, double longitude, int limit, int accuracy_level) {
        super(activity);
        mLimit = limit;
        mAccLvl = accuracy_level;
        mLong = longitude;
        mLat = latitude;
    }
    
    @Override
    public void doExecute() {
        restClient.addParam("long", mLong);
        restClient.addParam("lat", mLat);

        if (mLimit > 0) {
            restClient.addParam("limit", mLimit);
        }

        if (mAccLvl > 0) {
            restClient.addParam("max_distance", mAccLvl);
        }

        restClient.get("/spot/nearby");
    }
    @Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		JSONArray spotData;
		mData = new ArrayList<SpotItem>();
		try {
			spotData = jsonObject.getJSONArray("data");
			int size = spotData.length();
			if (size == 0) {
				return RestClientNotification.OK;
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
		return RestClientNotification.OK;
    }
    @Override
	protected void onPostExecute(Integer status) {

		if (mWaitingStatus && mWaitingDialog != null) {
			mWaitingDialog.dismiss();
		}

		if (null != mListener
				&& status == RestClientNotification.OK) {
			mListener.onSpotItemListener(mData);

		} else if (status == RestClientNotification.ERROR
				&& null != onDataErrorDelegate) {
			onDataErrorDelegate.actionDataError(this,mErrorCode);
		}
	}

    @Override
    public String getDumpData() {
        return "{\"data\":[{\"name\":\"Test 1\",\"distance\":\"43.030\"},{\"name\":\"Test 2\",\"distance\":\"43.030\"},{\"name\":\"Test 3\",\"distance\":\"43.030\"},{\"name\":\"Test 4\",\"distance\":\"43.030\"},{\"name\":\"Test 5\",\"distance\":\"43.030\"},{\"name\":\"Test 6\",\"distance\":\"43.030\"},{\"name\":\"Test 7\",\"distance\":\"43.030\"},{\"name\":\"Test 8\",\"distance\":\"43.030\"},{\"name\":\"Test 9\",\"distance\":\"43.030\"},{\"name\":\"Test 10\",\"distance\":\"43.030\"}]}"; 
    }
    
    public void setOnSpotItemReceiver(OnSpotItemListener listener){
    	this.mListener = listener;
    }
   
}
