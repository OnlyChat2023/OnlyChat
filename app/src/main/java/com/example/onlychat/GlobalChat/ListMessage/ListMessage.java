package com.example.onlychat.GlobalChat.ListMessage;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlychat.R;

public class ListMessage extends AppCompatActivity {
    ListView listView;
    RelativeLayout chatLayout;
    ImageView enclose;
    ImageView image;
    ImageView icon;
    View gap;
    EditText chatText;


    String names[] = {
            "Paimon","me","Xiao","Klee Bunbara","Paimon",
            "me","Xiao","Klee Bunbara","me","Yae Miko",
            "Paimon","me","Xiao","Klee Bunbara","Paimon",
            "me","Xiao","Klee Bunbara","me","Yae Miko"
    };
    Integer avatars[]= {
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
            R.drawable.global_chat_avatar1, R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
            R.drawable.global_chat_avatar1, R.drawable.global_chat_avatar1
    };
    Object messages[] = {
            "Sorry to bother you. I have a question for you",
            "I’ve been having a problem with my computer. I know you’re an engineer so I thought you might be able to help me.",
            "I see",
            "What’s the problem?",
            "I have a file that I can’t open for some reason.",
            "I have reason.",
            "Yes, I was working on it last night and everything was fine, but this morning.",
            "Sorry to bother you. I have a question for you",
            "I see",
            "I have a file that I can’t open for some reason.",
            "Sorry to bother you. I have a question for you",
            "I’ve been having a problem with my computer. I know you’re an engineer so I thought you might be able to help me.",
            "I see",
            "What’s the problem?",
            "I have a file that I can’t open for some reason.",
            "I have reason.",
            "Yes, I was working on it last night and everything was fine, but this morning.",
            "Sorry to bother you. I have a question for you",
            R.drawable.global_chat_avatar1,
            "I have a file that I can’t open for some reason."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_chat_list_message);

        listView=(ListView) findViewById(R.id.listMessages);
        CustomMessageItem customMessageItem = new CustomMessageItem(this,avatars,names,messages);
        listView.setAdapter(customMessageItem);
        listView.setSelection(0);
        listView.smoothScrollToPosition(0);
        listView.setDivider(null);
        listView.setDividerHeight(0);

        chatLayout = (RelativeLayout) findViewById(R.id.chatLayout);
        enclose = (ImageView) findViewById(R.id.encloseIcon);
        image = (ImageView) findViewById(R.id.imageIcon);
        icon =(ImageView) findViewById(R.id.iconIcon);
        gap =(View) findViewById(R.id.gap);
        chatText = (EditText) findViewById(R.id.chatText);

        final boolean[] state = {true};


        chatLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                chatLayout.getWindowVisibleDisplayFrame(r);

                int heightDiff = chatLayout.getRootView().getHeight() - r.height();
                if (state[0] && heightDiff > 0.25*chatLayout.getRootView().getHeight()) {
                    listView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(customMessageItem.getCount() - 1);
                        }
                    },150);

                    listView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enclose.animate().translationX(-235).setDuration(120);
                            image.animate().translationX(-235).setDuration(120);
                            icon.animate().translationX(-235).setDuration(120);
                            gap.animate().translationX(-235).setDuration(120);
                            chatText.animate().translationX(-235).setDuration(120);
                            chatText.setPadding(0,0,0,0);
                        }
                    }, 160);
                    state[0] = false;
                }
                else if (!state[0] && heightDiff <= 0.25*chatLayout.getRootView().getHeight()) {
                    state[0] = true;
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            enclose.animate().translationX(-0).setDuration(100);
                            image.animate().translationX(-0).setDuration(100);
                            icon.animate().translationX(-0).setDuration(100);
                            gap.animate().translationX(-0).setDuration(100);
                            chatText.animate().translationX(-0).setDuration(100);
                            chatText.setPadding(0,0,230,0);
                        }
                    });
                }
            }
        });
    }
}
