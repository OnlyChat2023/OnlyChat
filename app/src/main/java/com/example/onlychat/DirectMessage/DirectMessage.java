package com.example.onlychat.DirectMessage;

import static android.app.Activity.RESULT_OK;
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
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.RoomOptions;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
                roomModels.get(i).setBitmapAvatar(null);
                intent.putExtra("roomChat", roomModels.get(i));

                startActivityForResult(intent, 0);
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
//                LayoutInflater inflater = (LayoutInflater) globalChat.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//                // overlay
//                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
////                boolean focusable = true; // lets taps outside the popup also dismiss it
//                int width = LinearLayout.LayoutParams.MATCH_PARENT;
//                int height = LinearLayout.LayoutParams.MATCH_PARENT;
//                final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
//                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);
//
//                // Popup
//                View popupView = inflater.inflate(R.layout.global_chat_popup, null);
//                androidGridView = (GridView) popupView.findViewById(R.id.gridview_android_example);
//                androidGridView.setAdapter(new DirectMessage.ImageAdapterGridView(popupView.getContext()));
//
//                androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//
//                    }
//                });
//
//                boolean focusable = true; // lets taps outside the popup also dismiss it
//                final PopupWindow popupWindow = new PopupWindow(popupView,900,1360,focusable);
//
//                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        overlayWindow.dismiss();
//                    }
//                });
//
//                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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
        return globalChat;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            String roomID = data.getStringExtra("RoomModelID");
            String lastMess = data.getStringExtra("LastMessage");
            Date lastTime = (Date) data.getSerializableExtra("LastTime");
            boolean update = data.getBooleanExtra("Update", false);
            boolean change = data.getBooleanExtra("Change", false);

            if (update) {
                if (roomID != null && lastMess != null) {
                    for (RoomModel model : roomModels)
                        if (model.getId().equals(roomID)) {
                            if (change) {
                                roomModels.remove(model);
                                model.getMessages().add(new MessageModel("tmp", "", "", "", "", lastMess, lastTime, null));
                                roomModels.add(0, model);
                                customChatItem.notifyDataSetChanged();
                            }
                            break;
                        }
                }
                updateListRoom();
            }
        }
    }

    public void updateListRoom() {
        HttpManager httpRequest = new HttpManager(getContext());
        httpRequest.getDirectMetaData(new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                try{
                    JSONArray globalChat = response.getJSONObject("data").getJSONArray("directChat");

                    ArrayList<String> founded = new ArrayList<String>();

                    if(globalChat.length()>0){
                        ArrayList<RoomModel> rooms = getListRoom(globalChat);

                        for (RoomModel old_room : roomModels) {
                            boolean found = false;
                            for (RoomModel new_room : rooms) {
                                if (old_room.getId().equals(new_room.getId())) {
                                    old_room.setMessages(new_room.getMessages());
                                    old_room.setAvatar(new_room.getAvatar());
                                    old_room.setName(new_room.getName());
                                    old_room.setOptions(new_room.getOptions());

                                    found = true;
                                    founded.add(new_room.getId());
                                    break;
                                }
                            }
                            if (!found) {
                                roomModels.remove(old_room);
                            }
                        }
                        for (RoomModel new_room : rooms) {
                            if (!founded.contains(new_room.getId())) {
                                roomModels.add(new_room);
                            }
                        }
                        customChatItem.notifyDataSetChanged();
                        customChatItem.notifyDataSetInvalidated();
                    }
                }
                catch (Exception e){
                    Log.i("HTTP Success 11111 Error",e.toString());
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public ArrayList<RoomModel> getListRoom(JSONArray channel) throws JSONException, ParseException {
        // create list room
        ArrayList<RoomModel> listRoom = new ArrayList<>();

        // set item for list room
        for(int i=0;i<channel.length();i++){
            //create room
            RoomModel roomModel = new RoomModel();
            //set id, avatar, name for room
            roomModel.setId(channel.getJSONObject(i).getString("_id"));
            roomModel.setAvatar(channel.getJSONObject(i).getString("avatar"));
            roomModel.setName(channel.getJSONObject(i).getString("name"));

            // create list message
            ArrayList<MessageModel> listMessage = new ArrayList<>();
            // set information for message
            for(int j=0;j<channel.getJSONObject(i).getJSONArray("chats").length();j++){
                JSONObject messageJson = (JSONObject) channel.getJSONObject(i).getJSONArray("chats").get(j);

                // set information type String for message
                MessageModel messageModel = new Gson().fromJson(String.valueOf(messageJson), MessageModel.class);

                // set time message send
                String dtStart = messageJson.getString("time");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                try {
                    java.util.Date date = format.parse(dtStart);
                    messageModel.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // add message to list message
                listMessage.add(messageModel);
            }

            // set options
            RoomOptions roomOptions = null;
            if(channel.getJSONObject(i).getJSONArray("options").length()>0){
                roomOptions = new Gson().fromJson(String.valueOf(channel.getJSONObject(i).getJSONArray("options").get(0)),RoomOptions.class);

                //set members
                ArrayList<Member> members = new ArrayList<>();
                Log.i("================= main screen group =================", roomModel.getName());

                for(int l=0;l<channel.getJSONObject(i).getJSONArray("members").length();l++){
                    Member member = new Gson().fromJson(String.valueOf(channel.getJSONObject(i).getJSONArray("members").get(l)),Member.class);
                    Log.i("main screen", member.getUser_id());
                    Log.i("main screen", member.getName());
                    Log.i("main screen", member.getNickname());
                    Log.i("main screen", member.getAvatar());
                    Log.i("main screen >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>","");
                    members.add(member);
                }
                roomOptions.setMembers(members);
            }
            //set time of last message
            String abc = channel.getJSONObject(i).getString("update_time");
            java.util.Date date1=  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(abc);

            // set update_time, options, messages to room
            roomModel.setUpdate_time(date1);
            roomModel.setOptions(roomOptions);
            roomModel.setMessages(listMessage);

            // add room to list room
            listRoom.add(roomModel);
        }

        return listRoom;
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
