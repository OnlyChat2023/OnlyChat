package com.example.onlychat.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MessageModel implements Serializable {
    String id,userId;
    Integer avatar;
    String name,nickName ,message;
    Date time;
    ArrayList<String> seenUser;

    public MessageModel(String id, String userId, Integer avatar, String name, String nickName, String message, Date time, ArrayList<String> seenUser) {
        this.id = id;
        this.userId = userId;
        this.avatar = avatar;
        this.name = name;
        this.nickName = nickName;
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
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAvatar() {
        return avatar;
    }

    public void setAvatar(Integer avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
