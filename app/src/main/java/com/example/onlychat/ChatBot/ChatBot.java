package com.example.onlychat.ChatBot;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.GlobalChat.MessageBottomDialogFragment;
import com.example.onlychat.R;

public class ChatBot extends Fragment {
    TextView chatTitle;
    ImageView chatIcon;
    ImageView profile;
    ImageView addChat;
    ListView listChat;

    String names[] = {
            "English Study Chat Bot","English Study Chat Bot","English Study Chat Bot",
            "English Study Chat Bot","English Study Chat Bot","English Study Chat Bot",
            "English Study Chat Bot","English Study Chat Bot","English Study Chat Bot",
            "English Study Chat Bot","English Study Chat Bot","English Study Chat Bot",
            "English Study Chat Bot","English Study Chat Bot","English Study Chat Bot",
            "English Study Chat Bot","English Study Chat Bot","English Study Chat Bot",
    };
    Integer avatars[]={
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

    Integer avatarsImage[] = {
            R.raw.a_1, R.raw.a_2, R.raw.a_3, R.raw.a_4, R.raw.a_5,
            R.raw.a_6, R.raw.a_7, R.raw.a_8, R.raw.a_9, R.raw.a_10,
            R.raw.a_11, R.raw.a_12, R.raw.a_13, R.raw.a_14, R.raw.a_15,
            R.raw.a_16,R.raw.a_17, R.raw.a_18, R.raw.a_19, R.raw.a_20,
            R.raw.a_21, R.raw.a_22, R.raw.a_23, R.raw.a_24, R.raw.a_25,
    };

    GridView androidGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout botChat = (RelativeLayout) inflater.inflate(R.layout.fragment_main_content, null);

        // set value for widget
        chatTitle=(TextView) botChat.findViewById(R.id.header_title);
        chatIcon = (ImageView) botChat.findViewById(R.id.chatIcon);
        profile=(ImageView) botChat.findViewById(R.id.profile);
        addChat = (ImageView) botChat.findViewById(R.id.addChat);
        listChat = (ListView) botChat.findViewById(R.id.listChat);

        chatTitle.setText("Bot Chat Channel");

        chatIcon.setImageResource(R.drawable.botchat_icon);

        listChat.setSelection(0);
        listChat.smoothScrollToPosition(0);

//        CustomChatItem customChatItem=new CustomChatItem(botChat.getContext(), avatars,names,messages,times);
//        listChat.setAdapter(customChatItem);

//        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(listChat.getContext(), ChattingActivity.class);
//                Bundle userInf = new Bundle();
//                TextView name = (TextView) view.findViewById(R.id.messageName);
//                ImageView avatar = (ImageView) view.findViewById(R.id.messageAvatar);
//
//                userInf.putString("name", name.getText().toString());
//                avatar.setDrawingCacheEnabled(true);
//                Bitmap b = avatar.getDrawingCache();
//                intent.putExtras(userInf);
//                intent.putExtra("Bitmap", b);
//                startActivity(intent);
//            }
//        });

        listChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                MessageBottomDialogFragment messageBottomDialogFragment = new MessageBottomDialogFragment();
                messageBottomDialogFragment.show(getChildFragmentManager(), messageBottomDialogFragment.getTag());
                return false;
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) botChat.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                // overlay
                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
//                boolean focusable = true; // lets taps outside the popup also dismiss it
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                // Popup
                View popupView = inflater.inflate(R.layout.global_chat_popup_new_group, null);
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,900,500,focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        overlayWindow.dismiss();
                    }
                });
            }
        });
        return botChat;
    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return avatarsImage.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;

            if (convertView == null) {
                mImageView = new ImageView(mContext);
                mImageView.setLayoutParams(new GridView.LayoutParams(160, 160));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(16, 16, 16, 16);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(avatarsImage[position]);
            return mImageView;
        }
    }
}
