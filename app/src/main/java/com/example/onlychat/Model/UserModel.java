package com.example.onlychat.Model;

import java.io.Serializable;

public class UserModel implements Serializable {
    String id;
    String name,email,phone,facebook,instagram,description;
    Integer avatar;

    public UserModel(){}

    public UserModel(String name, String email, String phone, String facebook, String instagram, String description, Integer avatar) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.facebook = facebook;
        this.instagram = instagram;
        this.description = description;
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

    public Integer getAvatar() {
        return avatar;
    }

    public void setAvatar(Integer avatar) {
        this.avatar = avatar;
    }

//    public UserModel getUserFromDB(String id){
////        UserModel user = new UserModel();
////        return
//    }

    public void setUserToDB(){

    }

}
