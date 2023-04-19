package com.example.onlychat.Model;

import android.graphics.Bitmap;

import com.example.onlychat.Interfaces.RoomOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class RoomModel implements Serializable {
    String _id;
    String avatar;
    String name;
    String qr_code;
    ArrayList<MessageModel> messages;
    RoomOptions options;
    Date update_time;
    Bitmap bitmapAvatar;

    public RoomModel() {}

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
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

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public ArrayList<com.example.onlychat.Model.MessageModel> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<com.example.onlychat.Model.MessageModel> messages) {
        this.messages = messages;
    }
    public void pushMessage(MessageModel message) {
        this.messages.add(message);
    }

    public RoomOptions getOptions() {
        return options;
    }

    public void setOptions(RoomOptions options) {
        this.options = options;
    }

    public boolean hasBitmapAvatar() {
        return bitmapAvatar != null;
    }

    public void setBitmapAvatar(Bitmap bitmap) {
        this.bitmapAvatar = bitmap;
    }

    public Bitmap getBitmapAvatar() {
        return this.bitmapAvatar;
    }

    public boolean hasAvatar() {
        return this.avatar != null;
    }
}