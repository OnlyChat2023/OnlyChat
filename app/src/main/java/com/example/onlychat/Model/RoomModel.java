package com.example.onlychat.Model;

import com.example.onlychat.Interfaces.RoomOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class RoomModel implements Serializable {
<<<<<<< HEAD
    String id;
    String avatar;
    String name;
    String qr_code;
=======
    String _id,avatar,name;
>>>>>>> d7b83eb9bd5dc7e10d896aa80cd61f02b79707c9
    ArrayList<MessageModel> messages;
    RoomOptions options;
    Date update_time;

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
}