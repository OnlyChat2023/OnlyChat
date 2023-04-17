package com.example.onlychat.Interfaces;

import java.io.Serializable;

public class Member implements Serializable {
    String user_id, name, nickname,avatar,anonymous_avatar;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAnonymous_avatar() {
        return anonymous_avatar;
    }

    public void setAnonymous_avatar(String anonymous_avatar) {
        this.anonymous_avatar = anonymous_avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
