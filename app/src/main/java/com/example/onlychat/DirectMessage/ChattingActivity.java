package com.example.onlychat.DirectMessage;

import static java.lang.System.err;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;

import com.example.onlychat.DiaLog.ChangeNickNameDialog;
import com.example.onlychat.DiaLog.DMBottomDialog;
import com.example.onlychat.DirectMessage.Option.OptionActivity;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.R;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity {
    ImageView imgAvatar, btnFile, btnImage, btnIcon, btnSend;
    ImageView enclose;
    ImageView image;
    ImageView icon;
    View gap;
    Button btnBack, btnSetting;
    TextView txtName, txtOnline;
    EditText chatMessage;
    ListView chatContent;
    RelativeLayout chatLayout;
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
        btnBack = (Button) findViewById(R.id.backButton);
        btnSetting = (Button) findViewById(R.id.optionButton);
//        imgAvatar = (ImageView) findViewById(R.id.imageView4);
        txtName = (TextView) findViewById(R.id.textName);
        txtOnline = (TextView) findViewById(R.id.textSubName);

        chatContent = (ListView) findViewById(R.id.listMessages);

        btnFile = (ImageView) findViewById(R.id.encloseIcon);
        btnImage = (ImageView) findViewById(R.id.imageIcon);
        btnIcon = (ImageView) findViewById(R.id.iconIcon);
        gap = (View) findViewById(R.id.gap);
        btnSend = (ImageView) findViewById(R.id.sendIcon);
        chatMessage = (EditText) findViewById(R.id.chatText);
        chatLayout = (RelativeLayout) findViewById(R.id.chatLayout);

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
        chatContent.smoothScrollToPosition(adapter.getCount() - 1);
        chatContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView timeMsg = (TextView) v.findViewById(R.id.timeMessage);
                if (timeMsg.getVisibility() == View.GONE)
                    timeMsg.setVisibility(View.VISIBLE);
                else
                    timeMsg.setVisibility(View.GONE);
            }});

        chatContent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DMBottomDialog dialog = new DMBottomDialog().newInstance(i);
                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());

                return true;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ChattingActivity.this, ChatBot.class);
//                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fixed,R.anim.left_to_right);
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
                overridePendingTransition(R.anim.right_to_left, R.anim.fixed);
            }
        });

        final boolean[] state = {true};


        chatLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                chatLayout.getWindowVisibleDisplayFrame(r);

                int heightDiff = chatLayout.getRootView().getHeight() - r.height();
                if (state[0] && heightDiff > 0.25*chatLayout.getRootView().getHeight()) {
                    chatContent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatContent.setSelection(adapter.getCount() - 1);
                        }
                    },150);

                    chatContent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnFile.animate().translationX(-220).setDuration(120);
                            btnImage.animate().translationX(-220).setDuration(120);
                            btnIcon.animate().translationX(-220).setDuration(120);
                            gap.animate().translationX(-220).setDuration(120);
                            chatMessage.animate().translationX(-220).setDuration(120);
                            chatMessage.setPadding(0,0,0,0);
                        }
                    }, 160);
                    state[0] = false;
                }
                else if (!state[0] && heightDiff <= 0.25*chatLayout.getRootView().getHeight()) {
                    state[0] = true;
                    chatContent.post(new Runnable() {
                        @Override
                        public void run() {
                            btnFile.animate().translationX(-0).setDuration(100);
                            btnImage.animate().translationX(-0).setDuration(100);
                            btnIcon.animate().translationX(-0).setDuration(100);
                            gap.animate().translationX(-0).setDuration(100);
                            chatMessage.animate().translationX(-0).setDuration(100);
                            chatMessage.setPadding(0,0,230,0);
                        }
                    });
                }
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