package com.example.onlychat.Interfaces;

import java.util.ArrayList;

public interface SeenMessageListener {
    public void onSeen(String RoomID, ArrayList<String> SeenUsers);
}
