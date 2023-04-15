package com.example.onlychat.GroupChat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlychat.Friends.AllFriends.CustomFriendItem;
import com.example.onlychat.GroupChat.CustomFriendCheckbox.CustomFriendCheckBox;
import com.example.onlychat.GroupChat.ListMessage.ListMessage;
import com.example.onlychat.R;

public class AddMember extends AppCompatActivity {
    ListView listFriends;

    TextView addBtn;
    ImageView backButton;

    Boolean validateAddBtn = false;
    String names[] = {
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
    };
    Boolean isSelected[] = {
            false, false, false,
            false, false, false,
            false, false, false,
            false, false, false
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
        backButton = (ImageView) findViewById(R.id.backButton);

        listFriends = (ListView) findViewById(R.id.listFriends);
        CustomFriendCheckBox customFriendCheckBox = new CustomFriendCheckBox(this,avatars,names,phoneNumbers);
        listFriends.setAdapter(customFriendCheckBox);

        listFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Logic of selected item ---> if the item is selected => it's background is purple
                TextView name = (TextView) view.findViewById(R.id.name);
                if (!isSelected[i]) {
                    name.setTextColor(Color.parseColor("#ffffff"));
                    view.setBackgroundColor(Color.parseColor("#352159"));
                }
                else {
                    name.setTextColor(Color.parseColor("#ffffff"));
                    view.setBackgroundColor(Color.parseColor("#181828"));
                }
                isSelected[i] = !isSelected[i];

                // Logic of add button
                boolean areAllFalse = true;
                for (boolean e : isSelected) {
                    if (e)
                        areAllFalse = false;
                }
                if (areAllFalse) {
                    addBtn.setTextColor(Color.parseColor("#352159"));
                    validateAddBtn = false;
                }
                else {
                    addBtn.setTextColor(Color.parseColor("#ffffff"));
                    validateAddBtn = true;
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateAddBtn) {
                    finish();
                    overridePendingTransition(R.anim.fixed,R.anim.left_to_right);
                }
                else {
                    Toast.makeText(addBtn.getContext(), "You must select at least 1 member", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fixed,R.anim.left_to_right);
            }
        });
    }
}