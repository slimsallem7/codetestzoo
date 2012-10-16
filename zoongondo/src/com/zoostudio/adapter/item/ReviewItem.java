package com.zoostudio.adapter.item;

public class ReviewItem {

    private String content;
    private String id;
    private long timestamp;
    private UserItem user;
    
    private String[] imageDumps = {
			"http://narutozona-en.com/uploads/fotos/1231169819_naruto_-_gaara_of_the_desert.png",
			"http://media.wths.net/heimstead/ayalai0727/Images/Naruto_-_Temari.png",
			"http://3.bp.blogspot.com/-gxGmjqTAC5E/TcQLb4v4duI/AAAAAAAAAEc/5ibdEpWHMuE/s1600/Hatake-Kakashi.png",
			"http://4.bp.blogspot.com/_Qf5kfbE8tiU/S60jQ6DRaEI/AAAAAAAAAEU/zfbenCvsj_c/s1600/Sasuke.png",
			"http://download-naruto-episodes.edogo.com/wp-content/uploads/2009/02/akimichi_choji.png"
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
