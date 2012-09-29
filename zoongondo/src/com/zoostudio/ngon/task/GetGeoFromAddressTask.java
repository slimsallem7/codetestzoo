package com.zoostudio.ngon.task;

import org.bookmark.helper.TextCore;

import android.app.Activity;

import com.zoostudio.restclient.RestClientTask;

public class GetGeoFromAddressTask extends RestClientTask {

	private String mAddress;

	public GetGeoFromAddressTask(Activity activity, String address) {
		super(activity);
		mAddress = TextCore.removeDuplicateWhitespace(address.trim());
	}

	@Override
	public void doExecute() {
		restClient.addParam("address", mAddress);
		restClient.get("/spot/geoencode");
	}

}
