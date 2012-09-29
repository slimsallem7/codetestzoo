package com.zoostudio.zooslideshow;

public class LikerItem {
	private String mUrlAvatar;
	private String mTimes;

	public LikerItem(String urlAvatar, String times) {
		this.mUrlAvatar = urlAvatar;
		this.mTimes = times;
	}

	public void setTimes(String times) {
		this.mTimes = times;
	}

	public void setmUrlAvatar(String urlAvatar) {
		this.mUrlAvatar = urlAvatar;
	}

	public String getUrlAvatar() {
		return mUrlAvatar;
	}

	public String getLikeCount() {
		return mTimes;
	}
}
