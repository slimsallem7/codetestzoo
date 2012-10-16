package com.zoostudio.ngon.task;

import org.apache.http.entity.mime.content.ByteArrayBody;

public class NgonTaskUtil {
	public static ByteArrayBody convertByteToByteArrayBody(String spot_id,String mineType,
			byte[] data) {
		ByteArrayBody bab = new ByteArrayBody(data,mineType, "photo_spot_"
				+ spot_id + ".jpg");
		return bab;
	}
}
