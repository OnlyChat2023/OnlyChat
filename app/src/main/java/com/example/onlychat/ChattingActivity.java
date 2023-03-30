package com.example.onlychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;

public class ChattingActivity extends AppCompatActivity {
    Button btnBack, btnSetting, btnFile, btnImage, btnIcon, btnSend;
    ImageView imgAvatar;
    TextView txtName, txtOnline;
    EditText chatMessage;
    ListView chatContent;

    String[] messages = {
           "Sorry to bother you. I have a question for you",
            "I've been having a problem with my computer. I know you're an engineer so I thought you might be able to help me.",
            "I see",
            "What problem?",
            "I have a file that I can't open for some reason.",
            "I have a file that I can't open for some reason.",
            "Yes, I was working on it last night and everything was fine, but this morning."
    };

    String[] types = {
            "RECEIVE",
            "SEND",
            "RECEIVE",
            "RECEIVE",
            "RECEIVE",
            "SEND",
            "RECEIVE"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_chatting);

        btnBack = (Button) findViewById(R.id.chat_header_back);
        btnSetting = (Button) findViewById(R.id.chat_header_setting);
        imgAvatar = (ImageView) findViewById(R.id.chat_header_avatar);
        txtName = (TextView) findViewById(R.id.chat_header_name);
        txtOnline = (TextView) findViewById(R.id.chat_header_online);

        chatContent = (ListView) findViewById(R.id.message_content);

        btnFile = (Button) findViewById(R.id.chat_type_file);
        btnImage = (Button) findViewById(R.id.chat_type_image);
        btnIcon = (Button) findViewById(R.id.chat_type_icon);
        btnSend = (Button) findViewById(R.id.chat_type_send);
        chatMessage = (EditText) findViewById(R.id.chat_type_message);

        MessageReceive adapter = new MessageReceive(this, R.drawable.p4, messages, types);
        chatContent.setAdapter(adapter);
        chatContent.setSelection(0);
        chatContent.smoothScrollToPosition(0);
        chatContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                ChangeColorSelectedItem(v);
//                main.onMsgFromFragToMain("LEFT-FRAG", Integer.toString(position));
//                idTextView.setText("Mã số: " + ids[position]);
            }});
    }
}