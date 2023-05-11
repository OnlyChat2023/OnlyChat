package com.example.onlychat.GlobalChat;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.example.onlychat.GlobalChat.ListMessage.ListMessage;
import com.example.onlychat.GroupChat.GroupChat;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.MessageListener;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.MessageListener;
import com.example.onlychat.Interfaces.RoomOptions;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.Profile.ProfileNotFound;
import com.example.onlychat.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class GlobalChat extends Fragment {
    TextView chatTitle;
    ImageView chatIcon;
    String typeChat = "globalChat";
    ArrayList<RoomModel> roomModels = new ArrayList<>();

    CustomChatItem customChatItem;
    GlobalPreferenceManager pref;
    private ProgressBar progressBar;
    private TextView loading;

    private Button enterBtn;

    static UserModel myInfo;
    RelativeLayout globalChat;
    RelativeLayout main_layout;

    public ArrayList<RoomModel> getRoomModels() {
        return roomModels;
    }

    public void setRoomModels(ArrayList<RoomModel> roomModels) {
        this.roomModels.clear();

        this.roomModels.addAll(roomModels);
    }

    public GlobalChat(){}

    @Override
    public void onResume() {
        super.onResume();
//        customChatItem.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        globalChat = (RelativeLayout) inflater.inflate(R.layout.fragment_main_content_global, null);

        // set value for widget
        chatTitle=(TextView) globalChat.findViewById(R.id.header_title);
        chatTitle.setText("global chat channel");
        chatIcon = (ImageView) globalChat.findViewById(R.id.chatIcon);

        main_layout = (RelativeLayout) globalChat.findViewById(R.id.main_layout);

        pref = new GlobalPreferenceManager(getContext());

        progressBar = (ProgressBar) globalChat.findViewById(R.id.progressBar);
        loading  = (TextView) globalChat.findViewById(R.id.loading);

        enterBtn = globalChat.findViewById(R.id.enterBtn);

//        addChat.setVisibility(View.GONE);
//
//        new HttpManager.GetImageFromServer(profile).execute(new GlobalPreferenceManager(getContext()).getUserModel().getAnonymous_avatar());

        pref = new GlobalPreferenceManager(getContext());
        myInfo = pref.getUserModel();



        // set list messages
        HttpManager httpManager = new HttpManager(getContext());
        httpManager.getGlobalChat(
                new HttpResponse(){
                    @Override
                    public void onSuccess(JSONObject Response) {
                        loading.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);
                        main_layout.setVisibility(View.VISIBLE);
                        try{
                            JSONArray chats = Response.getJSONObject("data").getJSONArray("globalChat");
                            Log.i("Group chat", Integer.toString(chats.length()));
                            if(chats.length()>0){
                                roomModels.addAll(MainScreen.getListRoom(chats));
                            }
                        }
                        catch (Exception e){
                            Log.i("HTTP Success 11111 Error",e.toString());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.i("HTTP Error",error);
                    }
                }
        );

        chatIcon.setImageResource(R.drawable.global_chat_icon);

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ListMessage.class);
                intent.putExtra("typeChat", typeChat);
                roomModels.get(0).setBitmapAvatar(null);
                intent.putExtra("Data", roomModels.get(0));
                intent.putExtra("Position", 0);
                intent.putExtra("channel", "global_chat");
                startActivityForResult(intent, 0);
                getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.fixed);
            }
        });

//
//    public void updateListRoom() {
//        HttpManager httpRequest = new HttpManager(getContext());
//        httpRequest.getGlobalMetaData(new HttpResponse() {
//            @Override
//            public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
//                try{
//                    JSONArray globalChat = response.getJSONObject("data").getJSONArray("globalChat");
//
//                    ArrayList<String> founded = new ArrayList<String>();
//
//                    if(globalChat.length()>0){
//                        ArrayList<RoomModel> rooms = getListRoom(globalChat);
//
//                        for (RoomModel old_room : roomModels) {
//                            boolean found = false;
//                            for (RoomModel new_room : rooms) {
//                                if (old_room.getId().equals(new_room.getId())) {
//                                    old_room.setMessages(new_room.getMessages());
//                                    old_room.setAvatar(new_room.getAvatar());
//                                    old_room.setName(new_room.getName());
//                                    old_room.setOptions(new_room.getOptions());
//
//                                    found = true;
//                                    founded.add(new_room.getId());
//                                    break;
//                                }
//                            }
//                            if (!found) {
//                                roomModels.remove(old_room);
//                            }
//                        }
//                        for (RoomModel new_room : rooms) {
//                            if (!founded.contains(new_room.getId())) {
//                                roomModels.add(0, new_room);
//                            }
//                        }
//                        customChatItem.notifyDataSetChanged();
//                        customChatItem.notifyDataSetInvalidated();
//                    }
//                }
//                catch (Exception e){
//                    Log.i("HTTP Success 11111 Error",e.toString());
//                }
//            }
//
//            @Override
//            public void onError(String error) {
//
//            }
//        });
//    }
//
//    public class ImageAdapterGridView extends BaseAdapter {
//        private Context mContext;
//
//        public ImageAdapterGridView(Context c) {
//            mContext = c;
//        }
//
//        public int getCount() {
//            return avatarsImage.length;
//        }
//
//        public ImageView getItem(int position) {
//            return null;
//        }
//
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView mImageView;
//            if (convertView == null) {
//                mImageView = new ImageView(mContext);
//                mImageView.setLayoutParams(new GridView.LayoutParams(160, 160));
//                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                mImageView.setPadding(16, 16, 16, 16);
//            } else {
//                mImageView = (ImageView) convertView;
//            }
//            mImageView.setImageResource(avatarsImage[position]);
//            return mImageView;
//        }
//    }
//
//    public ArrayList<RoomModel> getListRoom(JSONArray channel) throws JSONException, ParseException {
//        // create list room
//        ArrayList<RoomModel> listRoom = new ArrayList<>();
//
//        // set item for list room
//        for(int i=0;i<channel.length();i++){
//            //create room
//            RoomModel roomModel = new RoomModel();
//            //set id, avatar, name for room
//            roomModel.setId(channel.getJSONObject(i).getString("_id"));
//            roomModel.setAvatar(channel.getJSONObject(i).getString("avatar"));
//            roomModel.setName(channel.getJSONObject(i).getString("name"));
//
//            // create list message
//            ArrayList<MessageModel> listMessage = new ArrayList<>();
//            // set information for message
//            for(int j=0;j<channel.getJSONObject(i).getJSONArray("chats").length();j++){
//                JSONObject messageJson = (JSONObject) channel.getJSONObject(i).getJSONArray("chats").get(j);
//
//                // set information type String for message
//                MessageModel messageModel = new Gson().fromJson(String.valueOf(messageJson), MessageModel.class);
//
//                // set time message send
//                String dtStart = messageJson.getString("time");
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                format.setTimeZone(TimeZone.getTimeZone("GMT"));
//                try {
//                    java.util.Date date = format.parse(dtStart);
//                    messageModel.setTime(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                // add message to list message
//                listMessage.add(messageModel);
//            }
//
//            // set options
//            RoomOptions roomOptions = null;
//            if(channel.getJSONObject(i).getJSONArray("options").length()>0){
//                roomOptions = new Gson().fromJson(String.valueOf(channel.getJSONObject(i).getJSONArray("options").get(0)),RoomOptions.class);
//
//                //set members
//                ArrayList<Member> members = new ArrayList<>();
//                Log.i("================= main screen group =================", roomModel.getName());
//
//                for(int l=0;l<channel.getJSONObject(i).getJSONArray("members").length();l++){
//                    Member member = new Gson().fromJson(String.valueOf(channel.getJSONObject(i).getJSONArray("members").get(l)),Member.class);
//                    Log.i("main screen", member.getUser_id());
//                    Log.i("main screen", member.getName());
//                    Log.i("main screen", member.getNickname());
//                    Log.i("main screen", member.getAvatar());
//                    Log.i("main screen >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>","");
//                    members.add(member);
//                }
//                roomOptions.setMembers(members);
//            }
//            //set time of last message
//            String abc = channel.getJSONObject(i).getString("update_time");
//            java.util.Date date1=  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(abc);
//
//            // set update_time, options, messages to room
//            roomModel.setUpdate_time(date1);
//            roomModel.setOptions(roomOptions);
//            roomModel.setMessages(listMessage);
//
//            // add room to list room
//            listRoom.add(roomModel);
//        }
//
//        return listRoom;
//    }
        return globalChat;
    }
}

