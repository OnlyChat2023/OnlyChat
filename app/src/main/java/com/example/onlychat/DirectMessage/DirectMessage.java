package com.example.onlychat.DirectMessage;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentTransaction;

import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.GlobalChat.MessageBottomDialogFragment;
import com.example.onlychat.Interfaces.ProfileReceiver;
import com.example.onlychat.MainActivity;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;
import com.google.gson.Gson;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

import kotlinx.coroutines.internal.SegmentOrClosed;

public class DirectMessage extends Fragment {
    TextView chatTitle;
    ImageView chatIcon;
    ImageView profile;
    ImageView addChat;
    ListView listChat;
    CustomChatItem customChatItem;
    ArrayList<RoomModel> roomModels = new ArrayList<>();
    GlobalPreferenceManager pref;
    public ArrayList<RoomModel> getRoomModels() {
        return roomModels;
    }

    public void setRoomModels(ArrayList<RoomModel> roomModels) {
        this.roomModels.clear();
//        this.roomModels = roomModels;
        this.roomModels.addAll(roomModels);
        customChatItem.notifyDataSetChanged();
//        Log.i("SET - Direct", roomModels.get(0).getName());
    }
    GridView androidGridView;

    public DirectMessage(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout globalChat = (RelativeLayout) inflater.inflate(R.layout.fragment_main_content, null);
        // set value for widget
        chatTitle=(TextView) globalChat.findViewById(R.id.header_title);
        chatIcon = (ImageView) globalChat.findViewById(R.id.chatIcon);
        profile=(ImageView) globalChat.findViewById(R.id.profile);
        addChat = (ImageView) globalChat.findViewById(R.id.addChat);
        addChat.setVisibility(View.GONE);
        listChat = (ListView) globalChat.findViewById(R.id.listChat);

        pref = new GlobalPreferenceManager(getContext());
        new HttpManager.GetImageFromServer(profile).execute(new GlobalPreferenceManager(getContext()).getUserModel().getAvatar());

        Log.i("Direct chat", Integer.toString(roomModels.size()));

        chatTitle.setText("direct message channel");
        chatIcon.setImageResource(R.drawable.direct_message_icon);

        listChat.setSelection(0);
        listChat.smoothScrollToPosition(0);

        customChatItem=new CustomChatItem(globalChat.getContext(), roomModels);
        listChat.setAdapter(customChatItem);

        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(listChat.getContext(), ChattingActivity.class);
                intent.putExtra("roomChat", roomModels.get(i));

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.fixed);
            }
        });

        listChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                MessageBottomDialogFragment messageBottomDialogFragment = new MessageBottomDialogFragment();
                messageBottomDialogFragment.leave.setVisibility(View.GONE);
                messageBottomDialogFragment.show(getChildFragmentManager(), messageBottomDialogFragment.getTag());

                return true;
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userInfo = pref.getUserModel();

                Bundle myBundle = new Bundle();
                myBundle.putInt("index", 0);
                myBundle.putString("user_id", userInfo.get_id());
//                System.out.println("DM: " + userInfo.getId());
//                myBundle.putString("name", userInfo.getName());
//                myBundle.putString("avatar", userInfo.getAvatar());
//                myBundle.putString("nickName", userInfo.getNickName());
//                myBundle.putString("phoneNumber", userInfo.getPhone());
//                myBundle.putString("university", userInfo.getUniversity());
//                myBundle.putString("email", userInfo.getEmail());
//                myBundle.putString("description", userInfo.getDescription());
//                myBundle.putString("facebook", userInfo.getFacebook());
//                myBundle.putString("instagram", userInfo.getInstagram());

//                System.out.println("userInfo: " + userInfo.getDescription());
//                System.out.println("FB: " + userInfo.getEmail());

                Intent intentToProfile = new Intent (getContext(), Profile.class);
                intentToProfile.putExtras(myBundle);
                startActivity(intentToProfile);

                SocketManager.waitFinishEditProfile(new ProfileReceiver() {
                    @Override
                    public void onSuccess(String avt) {
                        UserModel user = new GlobalPreferenceManager(getContext()).getUserModel();
                        user.setAvatar(avt);

                        new GlobalPreferenceManager(getContext()).saveUser(user);
                        new HttpManager.GetImageFromServer(profile).execute(user.getAvatar());
                    }
                });
            }
        });

        addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) globalChat.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
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
        return globalChat;
    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return roomModels.size();
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
//            mImageView.setImageResource(roomChat.get(position).getAvatar());
            return mImageView;
        }
    }
}
