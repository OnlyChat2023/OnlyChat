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
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import io.socket.emitter.Emitter;

public class DirectMessage extends Fragment {
    TextView chatTitle;
    ImageView chatIcon;
    ImageView profile;
    ImageView addChat;
    ListView listChat;

    CustomChatItem customChatItem;

    ArrayList<RoomModel> roomModels = new ArrayList<>();
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

    GlobalPreferenceManager pref;
    static UserModel myInfo;
    RelativeLayout globalChat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        globalChat = (RelativeLayout) inflater.inflate(R.layout.fragment_main_content, null);
        // set value for widget
        chatTitle=(TextView) globalChat.findViewById(R.id.header_title);
        chatIcon = (ImageView) globalChat.findViewById(R.id.chatIcon);
        profile=(ImageView) globalChat.findViewById(R.id.profile);
        addChat = (ImageView) globalChat.findViewById(R.id.addChat);
        addChat.setVisibility(View.GONE);
        listChat = (ListView) globalChat.findViewById(R.id.listChat);

        new HttpManager.GetImageFromServer(profile).execute(new GlobalPreferenceManager(getContext()).getUserModel().getAvatar());

        pref = new GlobalPreferenceManager(getContext());
        myInfo = pref.getUserModel();
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
                UserModel userInfo = new GlobalPreferenceManager(profile.getContext()).getUserModel();

                Bundle myBundle = new Bundle();
                myBundle.putString("user_id", userInfo.getId());
                myBundle.putString("name", userInfo.getName());
                myBundle.putString("avatar", userInfo.getAvatar());
                myBundle.putString("nickName", userInfo.getNickName());
                myBundle.putString("phoneNumber", userInfo.getPhone());
                myBundle.putString("university", userInfo.getUniversity());
                myBundle.putString("email", userInfo.getEmail());
                myBundle.putString("description", userInfo.getDescription());
                myBundle.putString("facebook", userInfo.getFacebook());
                myBundle.putString("instagram", userInfo.getInstagram());

                System.out.println("userInfo: " + userInfo.getDescription());
//                System.out.println("FB: " + userInfo.getEmail());
                Intent intentToProfile = new Intent (profile.getContext(), Profile.class);
                intentToProfile.putExtras(myBundle);
                startActivity(intentToProfile);
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
        waitSetNickname();
        return globalChat;
    }

    public void waitSetNickname(){
        SocketManager.getInstance();
        if(SocketManager.getSocket() !=null){
            SocketManager.getSocket().on("waitSetNickname", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    String myNickname = (String) args[0];
                    String friendNickname = (String) args[1];
                    String chat_id = (String) args[2];
                    Log.i("Socketttttttttttt", chat_id);
                    profile.post(new Runnable() {
                        @Override
                        public void run() {
                            for(RoomModel roomMode: roomModels){
                                if(roomMode.getId().equals(chat_id)){
                                    roomMode.setName(friendNickname);
//
                                    for(Member member:roomMode.getOptions().getMembers()){
                                        if(member.getUser_id().equals(myInfo.get_id())){
                                            member.setNickname(myNickname);

                                            Log.i("socket>>", myNickname);
                                        }else{
                                            member.setNickname(friendNickname);

                                            Log.i("socket>>>>", friendNickname);
                                        }

                                    }

                                }
                            }
                            customChatItem.notifyDataSetChanged();
//                            customChatItem.notifyDataSetInvalidated();
                        }
                    });
                }
            });
        }
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
