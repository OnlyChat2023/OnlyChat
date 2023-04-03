package com.example.onlychat.DirectMessage;

import static java.lang.System.err;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;

import com.example.onlychat.DirectMessage.Option.OptionActivity;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.R;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity {
    ImageView imgAvatar, btnBack, btnSetting, btnFile, btnImage, btnIcon, btnSend;
    TextView txtName, txtOnline;
    EditText chatMessage;
    ListView chatContent;
    public Boolean isMute;

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

    String[] times = {
            "Sent at 12:57 04/03/2023",
            "Sent at 12:57 04/03/2023",
            "Sent at 12:57 04/03/2023",
            "Sent at 12:57 04/03/2023",
            "Sent at 12:57 04/03/2023",
            "Sent at 12:57 04/03/2023",
            "Sent at 12:57 04/03/2023",
            "Sent at 12:57 04/03/2023"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        setContentView(R.layout.global_chat_list_message);

        isMute = false;
        btnBack = (ImageView) findViewById(R.id.backButton);
        btnSetting = (ImageView) findViewById(R.id.optionButton);
        imgAvatar = (ImageView) findViewById(R.id.imageView4);
        txtName = (TextView) findViewById(R.id.textName);
        txtOnline = (TextView) findViewById(R.id.textSubName);

        chatContent = (ListView) findViewById(R.id.listMessages);

        btnFile = (ImageView) findViewById(R.id.encloseIcon);
        btnImage = (ImageView) findViewById(R.id.imageIcon);
        btnIcon = (ImageView) findViewById(R.id.iconIcon);
        btnSend = (ImageView) findViewById(R.id.sendIcon);
        chatMessage = (EditText) findViewById(R.id.chatText);

        Intent main_chat = getIntent();
        Bundle userInf = main_chat.getExtras();
        Bitmap bm_avatar = (Bitmap) main_chat.getParcelableExtra("Bitmap");
        imgAvatar.setImageBitmap(bm_avatar);
        txtName.setText(userInf.getString("name"));
        txtOnline.setText("Online");
        txtOnline.setTextColor(getResources().getColor(R.color.online_green));

        MessageReceive adapter = new MessageReceive(this, bm_avatar, ArrayString2ArrayList(messages), ArrayString2ArrayList(types), ArrayString2ArrayList(times));
        chatContent.setAdapter(adapter);
        chatContent.setSelection(adapter.getCount() - 1);
//        chatContent.smoothScrollToPosition(0);
        chatContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView timeMsg = (TextView) v.findViewById(R.id.timeMessage);
                if (timeMsg.getVisibility() == View.GONE)
                    timeMsg.setVisibility(View.VISIBLE);
                else
                    timeMsg.setVisibility(View.GONE);
            }});

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ChattingActivity.this, MainScreen.class);
//                startActivity(intent);
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chatMessage.getText().toString();
                if (msg.length() != 0) {
                    adapter.AddMessage(msg, "SEND", "Sent at 12:57 04/03/2023");
                    chatMessage.setText("");
                }
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChattingActivity.this, OptionActivity.class);
                Bundle userInf = new Bundle();

                userInf.putString("name", txtName.getText().toString());
                imgAvatar.setDrawingCacheEnabled(true);
                Bitmap b = imgAvatar.getDrawingCache();
                intent.putExtras(userInf);
                intent.putExtra("Bitmap", b);
                startActivity(intent);
            }
        });
    }

    ArrayList<String> ArrayString2ArrayList(String[] str){
        ArrayList<String> res = new ArrayList<String>();
        for (String val : str){
            res.add(val);
        }
        return res;
    }
}