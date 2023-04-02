package com.example.onlychat.ChatBot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.R;

public class MainScreen extends AppCompatActivity {
    ListView listMessage;
    String names[] = {
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
    };
    Integer avatars[] = {
            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,
            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,
            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,
            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,
            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,
            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,
    };
    String messages[] = {
            "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...",
            "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...",
            "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...",
            "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...",
            "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...",
            "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...", "Sorry to bother you. I have a questi...",
    };
    String times[]={
            "2:00 PM","4:00 PM","6:00 PM",
            "2:00 PM","4:00 PM","6:00 PM",
            "2:00 PM","4:00 PM","6:00 PM",
            "2:00 PM","4:00 PM","6:00 PM",
            "2:00 PM","4:00 PM","6:00 PM",
            "2:00 PM","4:00 PM","6:00 PM",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.global_chat_list_chat);

        listMessage = (ListView) findViewById(R.id.listMessage);

        listMessage.setSelection(0);
        listMessage.smoothScrollToPosition(0);
        listMessage.setDivider(null);
        listMessage.setDividerHeight(0);

        CustomChatItem customChatItem=new CustomChatItem(this,avatars,names,messages,times);
        listMessage.setAdapter(customChatItem);

        listMessage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                // overlay
                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                final PopupWindow overlayWindow = new PopupWindow(overlayView, width, height, true);
                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                // Popup
                View popupView = inflater.inflate(R.layout.global_chat_popup_above, null);

                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT, 600, focusable);
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                popupView.setTranslationY(600);
                popupView.animate().translationY(0).setDuration(200);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        overlayWindow.dismiss();
                    }
                });

                return false;
            }
        });
    }
}