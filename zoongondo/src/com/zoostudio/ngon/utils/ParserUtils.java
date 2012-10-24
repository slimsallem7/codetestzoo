package com.zoostudio.ngon.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.zoostudio.adapter.item.MenuItem;
import com.zoostudio.adapter.item.PhotoItem;
import com.zoostudio.adapter.item.ReviewItem;
import com.zoostudio.adapter.item.SpotItem;
import com.zoostudio.adapter.item.UserItem;

public class ParserUtils {

	public static ReviewItem parseReview(JSONObject review)
			throws JSONException {
		ReviewItem item = new ReviewItem(1);
		item.setId(review.getString("id"));
		item.setContent(review.getString("content"));
		item.setTimestamp(review.getLong("time"));

		JSONObject userData = review.getJSONObject("user");
		item.setUser(parseUserForReview(userData));

		return item;
	}

	public static UserItem parseUserForReview(JSONObject data)
			throws JSONException {
		UserItem item = new UserItem();

		item.setId(data.getString("id"));
		item.setName(data.getString("name"));
		if (data.has("avatar")) {
			item.setAvatar(data.getString("avatar"));
		}
		return item;
	}

	public static SpotItem parseSpot(JSONObject spot) throws JSONException {
		LocationItem location = new LocationItem(spot.getDouble("lon"),
				spot.getDouble("lat"));

		SpotItem item = new SpotItem.Builder().setId(spot.getString("id"))
				.setName(spot.getString("name")).setLocation(location).create();

		item.setName(spot.getString("name"));
		item.setId(spot.getString("id"));

		item.setLocation(location);

		return item;
	}

	public static MenuItem parseMenu(JSONObject menu, String spot_id)
			throws JSONException {
		MenuItem item = new MenuItem();
		item.setDishId(menu.getString("id"));
		item.setSpotId(spot_id);
		item.setName(menu.getString("name"));
//		item.setLargeImageUrl(menu.getString("path"));
//		item.setThumbImageUrl(menu.getString("medium_path"));
		return item;
	}

	public static PhotoItem parsePhoto(JSONObject row) throws JSONException {
		PhotoItem item = new PhotoItem();
		item.setId(row.getString("photo_id"));
		item.setPath(ImageUtil.makeFullPathImage(row.getString("path")));
		item.setMediumPath(ImageUtil.makeFullPathImage(row
				.getString("medium_path")));
		item.setSmallPath(ImageUtil.makeFullPathImage(row
				.getString("small_path")));
		return item;
	}
}
