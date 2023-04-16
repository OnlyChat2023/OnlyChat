package com.example.onlychat.Interfaces;

import com.example.onlychat.Model.MessageModel;

public interface MessageListener {
    public void onMessage(MessageModel message, int position);
}