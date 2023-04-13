package com.example.onlychat;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.EditProfile.EditProfile;
import com.example.onlychat.Friends.AllFriends.AllFriends;
import com.example.onlychat.Friends.Friends;
import com.example.onlychat.GlobalChat.GlobalChat;
import com.example.onlychat.GlobalChat.ListMessage.ListMessage;
import com.example.onlychat.GroupChat.AddMember;
import com.example.onlychat.GroupChat.GroupChatSetting;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Profile.Profile;

public class MainActivity extends AppCompatActivity {

    GlobalPreferenceManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new GlobalPreferenceManager(this);
        pref.ValidateRemember();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isLogin", pref.getIsLoggedIn());

        Intent intent = new Intent(MainActivity.this, MainScreen.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}