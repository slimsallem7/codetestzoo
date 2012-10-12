package com.zoostudio.ngon.task;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.zoostudio.adapter.item.MenuItem;
import com.zoostudio.adapter.item.MenuItem;
import com.zoostudio.ngon.task.callback.OnMenuTaskListener;
import com.zoostudio.ngon.utils.ParserUtils;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class GetMenuTask extends RestClientTask {

	private String mSpotId;
	private ArrayList<MenuItem> data;
	private OnMenuTaskListener mListener;

	private String[] imageDump = {
			"http://i366.photobucket.com/albums/oo105/AvonB7/Food/Rovellones.jpg",
			"http://i284.photobucket.com/albums/ll26/msbowjangles16/Chinese-food.jpg",
			"http://i870.photobucket.com/albums/ab270/moonbeambouvier/Food/20121008Dessert.jpg",
			"http://i1245.photobucket.com/albums/gg587/PB_Loves/Scratch%20n%20Sniff/f2d9c76b.jpg",
			"http://i1245.photobucket.com/albums/gg587/PB_Loves/Scratch%20n%20Sniff/0664ca87.jpg",
			"http://i255.photobucket.com/albums/hh149/MargieHaire/Animals/food_02.jpg",
			"http://i1092.photobucket.com/albums/i414/herrysusanto1/food.jpg" };

	public GetMenuTask(Activity activity, String spot_id) {
		super(activity);
		mSpotId = spot_id;
	}

	@Override
	public void doExecute() {
		restClient.addParam("spot_id", mSpotId);
		restClient.get("/spot/menu");
	}

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		try {
			boolean status = jsonObject.getBoolean("status");
			if (status) {
				data = new ArrayList<MenuItem>();
				JSONArray menuData = jsonObject.getJSONArray("data");
				for (int i = 0, size = menuData.length(); i < size; i++) {
					JSONObject row = menuData.getJSONObject(i);
					MenuItem item = ParserUtils.parseMenu(row, mSpotId);
					item.setUrlImageThumb(imageDump[getImageDump()]);
					data.add(item);
				}
				return RestClientNotification.OK;
			} else {
				mErrorCode = jsonObject.getInt("error_code");
				return RestClientNotification.ERROR;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return RestClientNotification.ERROR_DATA;
		}
	}

	private int getImageDump() {
		Random random = new Random();
		return random.nextInt(7);
	}

	@Override
	protected void onPostExecute(Integer status) {
		if (mWaitingStatus && mWaitingDialog != null) {
			mWaitingDialog.dismiss();
		}
		if (status == RestClientNotification.OK) {
			mListener.onMenuTaskListener(data);
		} else if (status == RestClientNotification.ERROR
				&& null != onDataErrorDelegate) {
			onDataErrorDelegate.actionDataError(this, mErrorCode);
		}
	}

	public void setOnMenuTaskListener(OnMenuTaskListener listener) {
		mListener = listener;
	}

}
