package com.example.onlychat.Model;

import com.example.onlychat.Model.RoomModel;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

public class UserModel implements Serializable {
     String id,name,username,email,phone,facebook,instagram,university,description,avatar;
    String token;
     ArrayList<RoomModel> directChat = new ArrayList<RoomModel>();
     ArrayList<RoomModel> groupChat = new ArrayList<RoomModel>();
     ArrayList<RoomModel> globalChat = new ArrayList<RoomModel>();
     ArrayList<RoomModel> botChat = new ArrayList<RoomModel>();

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public ArrayList<RoomModel> getDirectChat() {
        return directChat;
    }

    public void setDirectChat(ArrayList<RoomModel> directChat) {
        this.directChat = directChat;
    }

    public ArrayList<RoomModel> getGroupChat() {
        return groupChat;
    }

    public void setGroupChat(ArrayList<RoomModel> groupChat) {
        this.groupChat = groupChat;
    }

    public ArrayList<RoomModel> getGlobalChat() {
        return globalChat;
    }

    public void setGlobalChat(ArrayList<RoomModel> globalChat) {
        this.globalChat = globalChat;
    }

    public ArrayList<RoomModel> getBotChat() {
        return botChat;
    }

    public void setBotChat(ArrayList<RoomModel> botChat) {
        this.botChat = botChat;
    }

    public UserModel(){}


    public String getId() {
        return id;
    }

    public  void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
