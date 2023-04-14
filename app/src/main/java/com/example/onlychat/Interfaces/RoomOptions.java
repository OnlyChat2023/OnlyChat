package com.example.onlychat.Interfaces;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomOptions implements Serializable{
    String _id,user_id;
    Boolean notify, block;
    String qr_code;
    ArrayList<com.example.onlychat.Interfaces.Member> members;
    public RoomOptions(){}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Boolean getNotify() {
        return notify;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public Boolean getNotifications() {
        return notify;
    }

    public void setNotifications(Boolean notifications) {
        this.notify = notifications;
    }

    public Boolean getBlocking() {
        return block;
    }

    public void setBlocking(Boolean blocking) {
        this.block = blocking;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

//    public String getNickname() {
//        return nickname;
//    }
//
//    public void setNickname(String nickname) {
//        this.nickname = nickname;
//    }
}
