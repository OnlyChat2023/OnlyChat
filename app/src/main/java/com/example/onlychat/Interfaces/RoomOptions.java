package com.example.onlychat.Interfaces;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomOptions implements Serializable{
    Boolean notifications, blocking;
    String nickname;
    Integer qr_code;
    ArrayList<com.example.onlychat.Interfaces.Member> members;

    public RoomOptions(Boolean notifications, Boolean blocking, String nickname, Integer qr_code, ArrayList<com.example.onlychat.Interfaces.Member> members) {
        this.notifications = notifications;
        this.blocking = blocking;
        this.nickname = nickname;
        this.qr_code = qr_code;
        this.members = members;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    public Boolean getBlocking() {
        return blocking;
    }

    public void setBlocking(Boolean blocking) {
        this.blocking = blocking;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public Integer getQr_code() {
        return qr_code;
    }

    public void setQr_code(Integer qr_code) {
        this.qr_code = qr_code;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
