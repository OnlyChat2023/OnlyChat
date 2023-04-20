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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.example.onlychat.GlobalChat.ListMessage.ListMessage;
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
    ImageView profile;
    ImageView addChat;
    ListView listChat;
    String typeChat = "globalChat";
    ArrayList<RoomModel> roomModels = new ArrayList<>();

    CustomChatItem customChatItem;
    GlobalPreferenceManager pref;

    static UserModel myInfo;
    RelativeLayout globalChat;

    public ArrayList<RoomModel> getRoomModels() {
        return roomModels;
    }

    public void setRoomModels(ArrayList<RoomModel> roomModels) {
        this.roomModels.clear();

        this.roomModels.addAll(roomModels);
        customChatItem.notifyDataSetChanged();
    }

    Integer avatarsImage[] = {
            R.raw.a_1, R.raw.a_2, R.raw.a_3, R.raw.a_4, R.raw.a_5,
            R.raw.a_6, R.raw.a_7, R.raw.a_8, R.raw.a_9, R.raw.a_10,
            R.raw.a_11, R.raw.a_12, R.raw.a_13, R.raw.a_14, R.raw.a_15,
            R.raw.a_16,R.raw.a_17, R.raw.a_18, R.raw.a_19, R.raw.a_20,
            R.raw.a_21, R.raw.a_22, R.raw.a_23, R.raw.a_24, R.raw.a_25,
    };

    GridView androidGridView;

    public GlobalChat(){}

    @Override
    public void onResume() {
        super.onResume();
//        customChatItem.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        globalChat = (RelativeLayout) inflater.inflate(R.layout.fragment_main_content, null);

        // set value for widget
        chatTitle=(TextView) globalChat.findViewById(R.id.header_title);
        chatTitle.setText("global chat channel");
        chatIcon = (ImageView) globalChat.findViewById(R.id.chatIcon);
        profile=(ImageView) globalChat.findViewById(R.id.profile);
        addChat = (ImageView) globalChat.findViewById(R.id.addChat);
        listChat = (ListView) globalChat.findViewById(R.id.listChat);
        pref = new GlobalPreferenceManager(getContext());

        addChat.setVisibility(View.GONE);

        new HttpManager.GetImageFromServer(profile).execute(new GlobalPreferenceManager(getContext()).getUserModel().getAnonymous_avatar());

        pref = new GlobalPreferenceManager(getContext());
        myInfo = pref.getUserModel();
        chatIcon.setImageResource(R.drawable.global_chat_icon);

        listChat.setSelection(0);
        listChat.smoothScrollToPosition(0);
        Log.i("Global chat", Integer.toString(roomModels.size()));
        customChatItem = new CustomChatItem(globalChat.getContext(),roomModels );
        listChat.setAdapter(customChatItem);

//        listChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                MessageBottomDialogFragment messageBottomDialogFragment = new MessageBottomDialogFragment();
//                messageBottomDialogFragment.show(getChildFragmentManager(), messageBottomDialogFragment.getTag());
//
//                return true;
//            }
//        });

        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(listChat.getContext(), ListMessage.class);
                intent.putExtra("typeChat", typeChat);
                roomModels.get(i).setBitmapAvatar(null);
                intent.putExtra("Data", roomModels.get(i));
                intent.putExtra("Position", i);
                intent.putExtra("channel", "global_chat");
                startActivityForResult(intent, 0);
                getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.fixed);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
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
                View popupView = inflater.inflate(R.layout.global_chat_popup, null);
                EditText nickname = popupView.findViewById(R.id.nickname);


//                Log.i("anonymous avatar", myInfo.getAnonymous_avatar());


//                Log.i("anonymous avatar",part2[0]);
                final int[] avatarIndex = {1};

                HttpManager httpManager= new HttpManager(globalChat.getContext());
                httpManager.getUserById(myInfo.get_id(), new HttpResponse() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                        JSONObject profile = response.getJSONObject("data");
                        Log.i("all friends click item", profile.toString());


                        Log.i("global chat", profile.toString());

                        UserModel user = new Gson().fromJson(profile.toString(), UserModel.class);
                        nickname.setText(user.getNickName());
                        String[] part1 =user.getAnonymous_avatar().split("/");
                        String[] part2= part1[1].split("\\.");
                        avatarIndex[0] = Integer.parseInt(part2[0]);

                    }

                    @Override
                    public void onError(String error) {

                    }
                });



                Button okBtn = popupView.findViewById(R.id.okBtn);

                BaseAdapter baseAdapter = new ImageAdapterGridView(popupView.getContext());

                androidGridView = (GridView) popupView.findViewById(R.id.gridview_android_example);
                androidGridView.setAdapter(baseAdapter);
                Log.i("TAG=======", Integer.toString(androidGridView.getChildCount()));;

//                Log.i("TAG=======", androidGridView.getChildAt(Integer.parseInt(part2[0])-1).toString());;
                for(int i=0;i<androidGridView.getChildCount();i++){
                    androidGridView.getChildAt(i).setBackgroundColor(Color.BLACK);
                }
                androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        for(int i=0;i<androidGridView.getChildCount();i++){
                            androidGridView.getChildAt(i).setBackgroundColor(0);
                        }
                        avatarIndex[0] = position+1;
                        v.setBackgroundColor(Color.parseColor("#adb5bd"));

                    }
                });
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,900,1360,focusable);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        HttpManager httpManager = new HttpManager(globalChat.getContext());
                        httpManager.setAnonymousInformation(nickname.getText().toString(),"avatar/"+avatarIndex[0]+".png", new HttpResponse() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException, ParseException {
                                popupWindow.dismiss();
                                new HttpManager.GetImageFromServer(profile).execute(response.getJSONObject("data").getString("anonymous_avatar"));
                                JSONArray globalChat = response.getJSONObject("data").getJSONArray("globalChat");
//                                Log.i("OK BUTTON", Integer.toString(globalChat.length()));
                                if(globalChat.length()>0){
                                   setRoomModels(MainScreen.getListRoom(globalChat));
                                }

                            }

                            @Override
                            public void onError(String error) {
                                Log.i("popup click error", error);
                            }
                        });
                    }

                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        overlayWindow.dismiss();
                    }
                });

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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
                TextView tt = (TextView) popupView.findViewById(R.id.title);
                tt.setText("New Global Chat");
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT,focusable);
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
                            httpManager.AddGroupChat(typeChat, newName, pref.getUserModel().get_id(), new HttpResponse() {
                                @Override
                                public void onSuccess(JSONObject response) throws JSONException {
//                                    Reload();
                                    overlayWindow.dismiss();
                                    popupWindow.dismiss();
                                    
                                    updateListRoom();
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

//                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button searchBtn = (Button) popupView.findViewById(R.id.searchBtn);
//                searchBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.i("popup", "onClick: ");
//                    }
//                });

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
                                roomModels.add(model);
                                customChatItem.notifyDataSetChanged();
                            }
                            break;
                        }
                }
                updateListRoom();
            }
        }
    }

    public void pushFirst(String roomID) {
        globalChat.post(new Runnable() {
            @Override
            public void run() {
                for (RoomModel room : roomModels)
                    if (room.getId().equals(roomID)) {
                        roomModels.remove(room);
                        roomModels.add(0, room);
                        customChatItem.notifyDataSetChanged();
                        return;
                    }
            }
        });
    }

    public void updateListRoom() {
        HttpManager httpRequest = new HttpManager(getContext());
        httpRequest.getGlobalMetaData(new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                try{
                    JSONArray globalChat = response.getJSONObject("data").getJSONArray("globalChat");

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
                                roomModels.add(0, new_room);
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

    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return avatarsImage.length;
        }

        public ImageView getItem(int position) {
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
}

