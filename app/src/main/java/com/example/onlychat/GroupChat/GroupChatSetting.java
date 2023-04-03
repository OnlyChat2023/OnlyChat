package com.example.onlychat.GroupChat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.onlychat.GlobalChat.ListMessage.Options.CustomMemberItem;
import com.example.onlychat.MainActivity;
import com.example.onlychat.R;

public class GroupChatSetting extends AppCompatActivity {

    Button addBtn;
    RelativeLayout share;
    RelativeLayout members;
    ListView listMembers;

    Integer avatars[] = {
            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,R.drawable.global_chat_avatar,
            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,R.drawable.global_chat_avatar,
            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,R.drawable.global_chat_avatar

    };
    String names[] = {
            "Adam M.Mathew","Albert Willson","Andrew McLeod", "Brittany Smith",
            "Adam M.Mathew","Albert Willson","Andrew McLeod", "Brittany Smith",
            "Adam M.Mathew","Albert Willson","Andrew McLeod", "Brittany Smith"
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_setting);

        share = (RelativeLayout) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                // overlay
                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
//                boolean focusable = true; // lets taps outside the popup also dismiss it
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                // Popup
                View popupView = inflater.inflate(R.layout.layout_share_box, null);
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,900,1070,focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        overlayWindow.dismiss();
                    }
                });
            }
        });

        members = (RelativeLayout) findViewById(R.id.members);
        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                // overlay
                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
//                boolean focusable = true; // lets taps outside the popup also dismiss it
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                // Popup
                View popupView = inflater.inflate(R.layout.global_chat_popup_members, null);

                // set list members
                listMembers = (ListView)  popupView.findViewById(R.id.listMembers);
                CustomMemberItem customMemberItem=new CustomMemberItem(popupView.getContext(),avatars,names);
                listMembers.setAdapter(customMemberItem);
                listMembers.setSelection(0);
                listMembers.smoothScrollToPosition(0);
                listMembers.setDivider(null);
                listMembers.setDividerHeight(0);


                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,900,1250,focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        overlayWindow.dismiss();
                    }
                });
            }
        });

        addBtn = (Button) findViewById(R.id.add_member_btn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity_add_member = new Intent(GroupChatSetting.this, AddMember.class);
                startActivity(activity_add_member);
            }
        });

    }
}