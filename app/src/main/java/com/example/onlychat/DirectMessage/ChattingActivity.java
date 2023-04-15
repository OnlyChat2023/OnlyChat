package com.example.onlychat.DirectMessage;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;

import com.example.onlychat.DiaLog.DMBottomDialog;
import com.example.onlychat.DirectMessage.Option.OptionActivity;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.R;
import com.vanniktech.emoji.EmojiPopup;

import java.util.ArrayList;
import java.util.Calendar;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.EasyPermissions;

public class ChattingActivity extends AppCompatActivity {
    ImageView imgAvatar, btnFile, btnImage, btnIcon, btnSend;
    String me_id = "6430c86d1b48c829004aa89b";
    View gap;
    Button btnBack, btnSetting;
    TextView txtName, txtOnline;
    EditText chatMessage;
    ListView chatContent;
    RelativeLayout chatLayout;
    RoomModel userInf;
    ArrayList<Uri> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        setContentView(R.layout.global_chat_list_message);
        btnBack = (Button) findViewById(R.id.backButton);
        btnSetting = (Button) findViewById(R.id.optionButton);
        imgAvatar = (ImageView) findViewById(R.id.avatar);
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
        userInf = (RoomModel) main_chat.getSerializableExtra("roomChat");
//        imgAvatar.setImageResource(userInf.getAvatar());
        txtName.setText(userInf.getName());
        txtOnline.setText("Online");
        txtOnline.setTextColor(getResources().getColor(R.color.online_green));

//        MessageReceive adapter = new MessageReceive(this, userInf.getAvatar(), me_id, userInf.getMessages());
//        chatContent.setAdapter(adapter);
//        chatContent.setSelection(adapter.getCount() - 1);
//        chatContent.smoothScrollToPosition(adapter.getCount() - 1);
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
                finish();
                overridePendingTransition(R.anim.fixed,R.anim.left_to_right);
            }
        });

        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                requestPermission();
                String[] strings = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                if(EasyPermissions.hasPermissions(ChattingActivity.this, strings)) {
                    imagePicker();
                }
                else {
                    EasyPermissions.requestPermissions(
                            ChattingActivity.this,
                            "App needs to access your camera & storage",
                            100,
                            strings);
                }
            }
        });

        // EMOJI
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(
                findViewById(R.id.root_view)
        ).build(chatMessage);

        btnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    emojiPopup.toggle();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chatMessage.getText().toString();
                if (msg.length() != 0) {
//                    adapter.AddMessage(new com.example.onlychat.Manager.Model.MessageModel("1234567890", me_id, null, "", "", msg, Calendar.getInstance().getTime(), null));
//                    adapter.AddMessage(new MessageModel("1234567890", me_id, null, "", "", msg, Calendar.getInstance().getTime(), null));
                    chatMessage.setText("");

                }
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChattingActivity.this, OptionActivity.class);
                for (Member mem : userInf.getOptions().getMembers()){
                    if (!mem.getId().equals(me_id)){
                        intent.putExtra("friend", mem);
                    }
                    else{
                        intent.putExtra("me", mem);
                    }
                }
                intent.putExtra("option", userInf.getOptions());
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
//                            chatContent.setSelection(adapter.getCount() - 1);
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

    @Override
    protected void onStart() {
        super.onStart();

//        for (Member mem : this.userInf.getOptions().getMembers()) {
//            if (!mem.getId().equals(me_id)) {
//                txtName.setText(mem.getNickname());
//                break;
//            }
//        }
    }

    public void setNickname(String frNN, String meNN) {
        for(Member mem : this.userInf.getOptions().getMembers()){
            if (mem.getId().equals(me_id)){
                mem.setNickname(meNN);
            } else{
                mem.setNickname(frNN);
            }
        }
    }

    private void imagePicker(){
        System.out.println("RUN HERE");
        FilePickerBuilder.getInstance()
                .setActivityTitle("Select Images")
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN, 3)
                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN, 4)
                .setMaxCount(4)
                .setSelectedFiles(arrayList)
                .pickPhoto(ChattingActivity.this);
        System.out.println();
//        .setActivityTheme(Integer.parseInt("FFFFFF"))
    }
}