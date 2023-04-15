package com.example.onlychat.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MessageModel implements Serializable {
    String id,user_id,avatar;
    String name,nickname ,message;
    Date time;
    ArrayList<String> seenUser=new ArrayList<>();
    public MessageModel(){}
    public MessageModel(String id, String userId, String avatar, String name, String nickName, String message, Date time, ArrayList<String> seenUser) {
        this.id = id;
        this.user_id = userId;
        this.avatar = avatar;
        this.name = name;
        this.nickname = nickName;
        this.message = message;
        this.time = time;
        this.seenUser = seenUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
