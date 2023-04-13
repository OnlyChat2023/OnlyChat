package com.example.onlychat.Interfaces;

import java.io.Serializable;
import java.lang.reflect.Member;
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
}
