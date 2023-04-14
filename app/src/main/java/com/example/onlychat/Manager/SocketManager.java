package com.example.onlychat.Manager;

import com.example.onlychat.Model.UserModel;
import com.google.gson.Gson;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {
    private static Socket socket;

    public synchronized static void getInstance() {
        if (socket == null) {
            try {
                socket = IO.socket("http://192.168.1.125:5000");
                socket.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void joinRoom(String roomName, UserModel user) {
        if (socket != null) {
            socket.emit("joinRoom", roomName, new Gson().toJson(user));
        }
    }
}
