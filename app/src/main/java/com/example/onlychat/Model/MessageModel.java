package com.example.onlychat.Model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MessageModel implements Serializable {
    String _id,user_id,avatar;
    String name,nickname,message;
    ArrayList<Bitmap> bitimages;
    ArrayList<String> images;
    Date time;
    ArrayList<String> seenUser=new ArrayList<>();
    Bitmap bitmapAvt;

    public MessageModel(){}

    public MessageModel(String id, String userId, String avatar, String name, String nickName, String message, Date time, ArrayList<String> seenUser) {
        this._id = id;
        this.user_id = userId;
        this.avatar = avatar;
        this.name = name;
        this.nickname = nickName;
        this.message = message;
        this.time = time;
        this.seenUser = seenUser;
    }

    public MessageModel(String id, String userId, String avatar, String name, String nickName, ArrayList<Bitmap> images, Date time, ArrayList<String> seenUser) {
        this._id = id;
        this.user_id = userId;
        this.avatar = avatar;
        this.name = name;
        this.nickname = nickName;
        this.bitimages = images;
        this.time = time;
        this.seenUser = seenUser;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ArrayList<String> getSeenUser() {
        return seenUser;
    }

    public void setSeenUser(ArrayList<String> seenUser) {
        this.seenUser = seenUser;
    }
    public boolean hasImages() {
        return bitimages != null && !bitimages.isEmpty();
    }
    public ArrayList<Bitmap> getImages() {
        return bitimages;
    }

    public int getViewType(UserModel myInfo) {
        return (this.user_id.equals(myInfo.getId())) ? 0 : 1;
    }
    public ArrayList<String> getImagesStr() {
        return images;
    }
    public void setImages(ArrayList<Bitmap> images) {
        if (bitimages == null)
            bitimages = new ArrayList<Bitmap>();
        else
            bitimages.clear();
        bitimages.addAll(images);
    }
    public void clearImages() {
        bitimages.clear();
    }
    public boolean hasImagesStr() {
        return images != null && !images.isEmpty();
    }
    public Bitmap getBitmapAvatar() {
        return bitmapAvt;
    }
    public void setBitmapAvatar(Bitmap bitmap) {
        bitmapAvt = bitmap;
    }

    public boolean hasBitmapAvatar() {
        return bitmapAvt != null;
    }
    public boolean hasAvatar() {
        return avatar != null;
    }
}
