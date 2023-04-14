package com.example.onlychat.Model;

import com.example.onlychat.Interfaces.RoomOptions;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomModel implements Serializable {
    String id,avatar;
    String name;
    ArrayList<MessageModel> messages;
    RoomOptions options;

    public RoomModel(String id, String avatar, String name, ArrayList<MessageModel> messages, RoomOptions options) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.messages = messages;
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<MessageModel> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageModel> messages) {
        this.messages = messages;
    }

    public RoomOptions getOptions() {
        return options;
    }

    public void setOptions(RoomOptions options) {
        this.options = options;
    }
}