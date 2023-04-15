package com.example.onlychat;

import android.app.Application;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

public class OnlyChatApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        EmojiManager.install(new GoogleEmojiProvider());
    }
}
