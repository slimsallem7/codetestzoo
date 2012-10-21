package com.zoostudio.adapter.item;

public class ReviewItem {

    private String content;
    private String id;
    private long timestamp;
    private UserItem user;
    
    private String[] imageDumps = {
			"http://wallpaperscraft.com/image/adele_girl_hair_face_look_9921_128x128.jpg",
			"http://wallpaperscraft.com/image/girl_eyes_face_makeup_manicure_blue-eyed_44388_128x128.jpg",
			"http://a.wattpad.net/useravatar/ImOnlyGinger.128.637778.jpg"
	};
    
    public ReviewItem(int index) {
    	user = new UserItem();
    	user.setAvatar(imageDumps[index]);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserItem getUser() {
        return user;
    }

    public void setUser(UserItem user) {
        this.user = user;
    }

}
