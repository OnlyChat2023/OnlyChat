package com.example.onlychat.GroupChat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.onlychat.Friends.AllFriends.CustomFriendItem;
import com.example.onlychat.GroupChat.CustomFriendCheckbox.CustomeFriendCheckBox;
import com.example.onlychat.R;

public class AddMember extends AppCompatActivity {
    ListView listFriends;

    TextView addBtn;
    String names[] = {
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
    };
    Integer avatars[]={
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar,R.drawable.global_chat_avatar2,
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar,R.drawable.global_chat_avatar2,
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar,R.drawable.global_chat_avatar2,
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar,R.drawable.global_chat_avatar2
    };
    String phoneNumbers[] = {
            "0973667324",
            "0937687267",
            "0776272828",
            "0973667324",
            "0937687267",
            "0776272828",
            "0973667324",
            "0937687267",
            "0776272828",
            "0973667324",
            "0937687267",
            "0776272828",
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        addBtn = (TextView) findViewById(R.id.addBtn);
        listFriends = (ListView) findViewById(R.id.listFriends);
        CustomeFriendCheckBox customeFriendCheckBox = new CustomeFriendCheckBox(this,avatars,names,phoneNumbers);
        listFriends.setAdapter(customeFriendCheckBox);

        listFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("POSITION: ");
                addBtn.setTextColor(Integer.parseInt("#ffffff"));
            }
        });
    }
}