package com.example.onlychat.GroupChat;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.GlobalChat.ListMessage.Options.Options;
import com.example.onlychat.GroupChat.ListMessage.ListMessage;
import com.example.onlychat.GlobalChat.MessageBottomDialogFragment;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.R;

import java.util.ArrayList;

public class GroupChat extends Fragment {
    TextView chatTitle;
    ImageView chatIcon;
    ImageView profile;
    ImageView addChat;
    ListView listChat;
    Button okBtn;

    ArrayList<RoomModel> roomModels = new ArrayList<>();

    public ArrayList<RoomModel> getRoomModels() {
        return roomModels;
    }

    public void setRoomModels(ArrayList<RoomModel> roomModels) {
        this.roomModels = roomModels;
    }

    public GroupChat(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout groupChat = (RelativeLayout) inflater.inflate(R.layout.activity_group_chat, null);

        // set value for widget
        chatTitle = (TextView) groupChat.findViewById(R.id.header_title);
        chatTitle.setText("group chat channel");

        chatIcon = (ImageView) groupChat.findViewById(R.id.chatIcon);
//        chatIcon.setImageResource(R.drawable.ic_groupchat);

        profile = (ImageView) groupChat.findViewById(R.id.profile);
        addChat = (ImageView) groupChat.findViewById(R.id.addChat);
        listChat = (ListView) groupChat.findViewById(R.id.listChat);

        listChat.setSelection(0);
        listChat.smoothScrollToPosition(0);

        CustomChatItem customChatItem = new CustomChatItem(groupChat.getContext(),roomModels );
        listChat.setAdapter(customChatItem);

        listChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                MessageBottomDialogFragment messageBottomDialogFragment = new MessageBottomDialogFragment();
                messageBottomDialogFragment.show(getChildFragmentManager(), messageBottomDialogFragment.getTag());

                return true;
            }
        });

        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(listChat.getContext(), ListMessage.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.fixed);
            }
        });

        addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) groupChat.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
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


                // After set name for new groupChat name ---> switch to add member activity
                okBtn = (Button) popupView.findViewById(R.id.okBtn);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent addMember = new Intent(popupView.getContext(), AddMember.class);
                        startActivity(addMember);
                    }
                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        overlayWindow.dismiss();
                    }
                });
            }
        });
        return groupChat;
    }
}