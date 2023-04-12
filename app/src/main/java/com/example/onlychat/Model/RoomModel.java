package com.example.onlychat.Model;

import com.example.onlychat.Interfaces.RoomOptions;

import java.util.ArrayList;

public class RoomModel {
    String id;
    Integer avatar;
    String name;
    ArrayList<MessageModel> messages;
    RoomOptions options;
}