package com.example.onlychat.GlobalChat.ListMessage.Options;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlychat.Interfaces.RoomOptions;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.R;

public class Options extends AppCompatActivity {

    RelativeLayout share;
    RelativeLayout members;
    ListView listMembers;
    ImageView backButton;

    TextView name;
    ImageView avatar;

    TextView memberQuantity;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_chat_chat_options);

        Intent intent = getIntent();
        RoomOptions options = (RoomOptions) intent.getSerializableExtra("Data");
        String names = (String) intent.getSerializableExtra("Name");
        String avatars = (String) intent.getSerializableExtra("Avatar");

        name = (TextView) findViewById(R.id.group_name);
        avatar = (ImageView) findViewById(R.id.avatar);
        memberQuantity =  (TextView) findViewById(R.id.memberQuantity);
        memberQuantity.setText("Members ("+options.getMembers().size()+")");
        name.setText(names);
        new HttpManager.GetImageFromServer(avatar).execute(avatars);


        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
            }
        });

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
                View popupView = inflater.inflate(R.layout.global_chat_popup_share, null);
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
                TextView quantity = (TextView) popupView.findViewById(R.id.quantity);
                quantity.setText("Members ("+options.getMembers().size()+")");
//                Log.i("Option", Integer.toString(options.getMembers().size()));
                CustomMemberItem customMemberItem=new CustomMemberItem(popupView.getContext(),options.getMembers());
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
    }
}