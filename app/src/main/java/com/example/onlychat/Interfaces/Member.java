package com.example.onlychat.Interfaces;

import java.io.Serializable;

public class Member implements Serializable {
    String id, name, nickname;
    Integer avatar;

    public Member(String id, String name, String nickname, Integer avatar) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getAvatar() {
        return avatar;
    }

    public void setAvatar(Integer avatar) {
        this.avatar = avatar;
    }
}
