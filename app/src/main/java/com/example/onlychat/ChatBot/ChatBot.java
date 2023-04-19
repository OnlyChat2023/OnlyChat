package com.example.onlychat.ChatBot;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.GlobalChat.ListMessage.ListMessage;
import com.example.onlychat.GlobalChat.MessageBottomDialogFragment;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatBot extends Fragment {
    TextView chatTitle;
    ImageView chatIcon;
    ImageView profile;
    ImageView addChat;
    ListView listChat;
    CustomChatItem customChatItem;
    String typeChat = "botChat";
    GlobalPreferenceManager pref;

    ArrayList<RoomModel> roomModels = new ArrayList<>();

    public ArrayList<RoomModel> getRoomModels() {
        return roomModels;
    }

    public void setRoomModels(ArrayList<RoomModel> roomModels) {
//        this.roomModels = roomModels;
        for(RoomModel i:roomModels){
            this.roomModels.add(i);
        }
        customChatItem.notifyDataSetChanged();
//        Log.i("SET - Bot", roomModels.get(0).getName());
    }

    public ChatBot(){}

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

        Log.i("<<<<<<Bot chat>>>>>>>>>", Integer.toString(roomModels.size()));

        profile.setVisibility(View.GONE);
        pref = new GlobalPreferenceManager(getContext());


        chatTitle.setText("Bot Chat Channel");

        chatIcon.setImageResource(R.drawable.botchat_icon);

        listChat.setSelection(0);
        listChat.smoothScrollToPosition(0);

        customChatItem=new CustomChatItem(botChat.getContext(),roomModels);
        listChat.setAdapter(customChatItem);

        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(listChat.getContext(), ListMessage.class);
                intent.putExtra("Data", roomModels.get(i));
                intent.putExtra("typeChat", typeChat);
                intent.putExtra("Position", i);
                intent.putExtra("channel", "bot_chat");
                startActivityForResult(intent, 5);
                getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.fixed);
            }
        });

        listChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                MessageBottomDialogFragment messageBottomDialogFragment = new MessageBottomDialogFragment();
                messageBottomDialogFragment.show(getChildFragmentManager(), messageBottomDialogFragment.getTag());
                return true;
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
                TextView tt = (TextView) popupView.findViewById(R.id.title);
                tt.setText("New Bot Chat");
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT,focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // After set name for new groupChat name ---> switch to add member activity
                Button okBtn = (Button) popupView.findViewById(R.id.okBtn);
                EditText newGroupName = (EditText) popupView.findViewById(R.id.newGroupName);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String newName = newGroupName.getText().toString();
                        if (!newName.equals("")){
                            HttpManager httpManager = new HttpManager(getContext());
                            httpManager.addBotChat(newName, pref.getUserModel().get_id(), new HttpResponse() {
                                @Override
                                public void onSuccess(JSONObject response) throws JSONException {
//                                    Reload();
                                    overlayWindow.dismiss();
                                    popupWindow.dismiss();
                                }

                                @Override
                                public void onError(String error) {
                                    Log.i("HTTP Error",error);
                                }
                            });
                        }
                        else {
                            overlayWindow.dismiss();
                            popupWindow.dismiss();
                        }
//                        Intent addMember = new Intent(popupView.getContext(), AddMember.class);
//                        startActivity(addMember);
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
