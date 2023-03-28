package com.example.onlychat.GlobalChat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlychat.R;

public class ListChat extends AppCompatActivity {
    ListView listMessage;
    ImageView profileImage;
    String names[] = {
            "Anonymous","Anonymous Private","Anonymous Publish"
    };
    Integer avatars[]={
            R.drawable.global_chat_message_avatar,R.drawable.global_chat_message_avatar,R.drawable.global_chat_message_avatar
    };
    String messages[] = {
            "Sorry to bother you. I have a questi...",
            "Sorry to bother you. I have a questi...",
            "Sorry to bother you. I have a questi...",
    };
    String times[]={"2:00 PM","4:00 PM","6:00 PM"};

    Integer avatarsImage[] = {
//            R.drawable.a1,
//            R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5,
//            R.drawable.a6, R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10,
//            R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15,
//            R.drawable.a16,R.drawable.a17, R.drawable.a18, R.drawable.a19, R.drawable.a20,
//            R.drawable.a21, R.drawable.a22, R.drawable.a23, R.drawable.a24, R.drawable.a25,
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


                return false;
            }
        });


        profileImage = (ImageView) findViewById(R.id.profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
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
                View popupView = inflater.inflate(R.layout.global_chat_popup, null);
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,900,1200,focusable);
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

