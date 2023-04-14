package com.example.onlychat.GlobalChat.ListMessage;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlychat.GlobalChat.ListMessage.Options.Options;
import com.example.onlychat.GroupChat.MessageBottomDialogFragmentChatting;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.R;

public class ListMessage extends AppCompatActivity {
    ListView listView;
    RelativeLayout chatLayout;
    ImageView enclose;
    ImageView image;
    ImageView icon;
    View gap;
    EditText chatText;
    Button optionButton;
    Button backButton;
    ImageView chatImage;
    TextView chatName;

//    String names[] = {
//            "Paimon","me","Xiao","Klee Bunbara","Paimon",
//            "me","Xiao","Klee Bunbara","me","Yae Miko",
//            "Paimon","me","Xiao","Klee Bunbara","Paimon",
//            "me","Xiao","Klee Bunbara","me","Yae Miko"
//    };
//    Integer avatars[]= {
//            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
//            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
//            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
//            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
//            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
//            R.drawable.global_chat_avatar1, R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar1,
//            R.drawable.global_chat_avatar1, R.drawable.global_chat_avatar1
//    };
//    Object messages[] = {
//            "Sorry to bother you. I have a question for you",
//            "I’ve been having a problem with my computer. I know you’re an engineer so I thought you might be able to help me.",
//            "I see",
//            "What’s the problem?",
//            "I have a file that I can’t open for some reason.",
//            "I have reason.",
//            "Yes, I was working on it last night and everything was fine, but this morning.",
//            "Sorry to bother you. I have a question for you",
//            "I see",
//            "I have a file that I can’t open for some reason.",
//            "Sorry to bother you. I have a question for you",
//            "I’ve been having a problem with my computer. I know you’re an engineer so I thought you might be able to help me.",
//            "I see",
//            "What’s the problem?",
//            "I have a file that I can’t open for some reason.",
//            "I have reason.",
//            "Yes, I was working on it last night and everything was fine, but this morning.",
//            "Sorry to bother you. I have a question for you",
//            R.drawable.global_chat_avatar1,
//            "I have a file that I can’t open for some reason."
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_chat_list_message);

        Intent intent = getIntent();
        RoomModel roomModel = (RoomModel) intent.getSerializableExtra("Data");
        listView=(ListView) findViewById(R.id.listMessages);
        CustomMessageItem customMessageItem = new CustomMessageItem(this,roomModel.getMessages());

        listView.setAdapter(customMessageItem);
        listView.setSelection(customMessageItem.getCount() - 1);
        listView.smoothScrollToPosition(customMessageItem.getCount() - 1);
        listView.setDivider(null);
        listView.setDividerHeight(0);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//
//                // Popup
//                View popupView = inflater.inflate(R.layout.global_chat_popup_above, null);
//
//                boolean focusable = true; // lets taps outside the popup also dismiss it
//                final PopupWindow popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT,600,focusable);
//                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//                popupView.setTranslationY(600);
//                popupView.animate().translationY(0).setDuration(200);
//
//                return false;
                MessageBottomDialogFragmentChatting messageBottomDialogFragmentChatting = new MessageBottomDialogFragmentChatting();
                messageBottomDialogFragmentChatting.show(getSupportFragmentManager(), messageBottomDialogFragmentChatting.getTag());

                return true;
            }
        });


        chatLayout = (RelativeLayout) findViewById(R.id.chatLayout);
        enclose = (ImageView) findViewById(R.id.encloseIcon);
        image = (ImageView) findViewById(R.id.imageIcon);
        icon =(ImageView) findViewById(R.id.iconIcon);
        gap =(View) findViewById(R.id.gap);
        chatText = (EditText) findViewById(R.id.chatText);
        optionButton = (Button) findViewById(R.id.optionButton);
        backButton = (Button) findViewById(R.id.backButton);
        chatImage = (ImageView) findViewById(R.id.avatar);
        chatName = (TextView) findViewById(R.id.textName);

//        chatImage.setImageResource(roomModel.getAvatar());
        chatName.setText(roomModel.getName());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fixed,R.anim.left_to_right);
            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(optionButton.getContext(), Options.class);
//                    Bundle userInf = new Bundle();
//                    TextView name = (TextView) v.findViewById(R.id.messageName);
//                    ImageView avatar = (ImageView) v.findViewById(R.id.messageAvatar);
//
//                    userInf.putString("name", name.getText().toString());
//                    avatar.setDrawingCacheEnabled(true);
//                    Bitmap b = avatar.getDrawingCache();
//                    intent.putExtras(userInf);
//                    intent.putExtra("Bitmap", b);
                intent.putExtra("Data",roomModel.getOptions());
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
                    listView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(customMessageItem.getCount() - 1);
                        }
                    },150);

                    listView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enclose.animate().translationX(-220).setDuration(120);
                            image.animate().translationX(-220).setDuration(120);
                            icon.animate().translationX(-220).setDuration(120);
                            gap.animate().translationX(-220).setDuration(120);
                            chatText.animate().translationX(-220).setDuration(120);
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
