package com.example.onlychat.GroupChat;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlychat.DirectMessage.DirectMessage;
import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.GlobalChat.ListMessage.ListMessage;
import com.example.onlychat.GlobalChat.MessageBottomDialogFragment;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.RoomOptions;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

public class GroupChat extends Fragment {
    TextView chatTitle;
    ImageView chatIcon;
    ImageView profile;
    ImageView addChat;
    ListView listChat;
    EditText newGroupName;
    Button okBtn;
    CustomChatItem customChatItem;

    RelativeLayout groupChat;
    ArrayList<RoomModel> roomModels = new ArrayList<>();
    GlobalPreferenceManager pref;
    Boolean isCreate = true;
    String typeChat = "groupChat";

    public ArrayList<RoomModel> getRoomModels() {
        return roomModels;
    }

    public void setRoomModels(ArrayList<RoomModel> roomModels) {
        this.roomModels.clear();

        this.roomModels.addAll(roomModels);
        customChatItem.notifyDataSetChanged();
    }

    public GroupChat(){}

    public GroupChat(ArrayList<RoomModel> roomModels){
        this.roomModels = roomModels;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        groupChat = (RelativeLayout) inflater.inflate(R.layout.fragment_main_content, null);
        // set value for widget
        chatTitle = (TextView) groupChat.findViewById(R.id.header_title);
        chatTitle.setText("group chat channel");
        chatIcon = (ImageView) groupChat.findViewById(R.id.chatIcon);
        profile=(ImageView) groupChat.findViewById(R.id.profile);
        addChat = (ImageView) groupChat.findViewById(R.id.addChat);
        listChat = (ListView) groupChat.findViewById(R.id.listChat);
//        chatIcon.setImageResource(R.drawable.global_chat_icon);
        chatIcon.setImageResource(R.drawable.ic_groupchat);

        new HttpManager.GetImageFromServer(profile).execute(new GlobalPreferenceManager(getContext()).getUserModel().getAvatar());
        pref = new GlobalPreferenceManager(getContext());
        listChat.setSelection(0);
        listChat.smoothScrollToPosition(0);
        Log.i("Group chat", Integer.toString(roomModels.size()));
        customChatItem = new CustomChatItem(groupChat.getContext(),roomModels );
        listChat.setAdapter(customChatItem);

        listChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                MessageBottomDialogFragment messageBottomDialogFragment = new MessageBottomDialogFragment();
                messageBottomDialogFragment.setActivity(GroupChat.this);
                messageBottomDialogFragment.setId(roomModels.get(i).getId());
                messageBottomDialogFragment.show(getChildFragmentManager(), messageBottomDialogFragment.getTag());

                return true;
            }
        });

        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(listChat.getContext(), ListMessage.class);
                intent.putExtra("typeChat", typeChat);
                roomModels.get(i).setBitmapAvatar(null);
                intent.putExtra("Data",roomModels.get(i));
                intent.putExtra("channel", "group_chat");
                startActivityForResult(intent, 0);
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
                final PopupWindow popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


                // After set name for new groupChat name ---> switch to add member activity
                okBtn = (Button) popupView.findViewById(R.id.okBtn);
                newGroupName = (EditText) popupView.findViewById(R.id.newGroupName);
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


//    public void Reload() {
//
//        HttpManager httpManager = new HttpManager(getContext());
//        httpManager.GetListGroupChat(pref.getUserModel().get_id(), new HttpResponse() {
//            @Override
//            public void onSuccess(JSONObject response) throws JSONException {
//                try{
//                    JSONArray groupChat = response.getJSONArray("data");
//                    ArrayList<RoomModel> rooms = ((MainScreen) getActivity()).getListRoom(groupChat);
//                    roomModels.clear();
//                    setRoomModels(rooms);
//                }
//                catch (Exception e){
//                    Log.i("HTTP Group Chat Success Error",e.toString());
//                }
//            }
//
//            @Override
//            public void onError(String error) {
//                Log.i("HTTP Error",error);
//            }
//        });
//    }

    public void LeaveGroup(MessageBottomDialogFragment current, String id){
        HttpManager httpManager = new HttpManager(getContext());
        httpManager.LeaveGroupChat(typeChat, pref.getUserModel().get_id(), id, new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
//                Reload();
                current.dismiss();
            }

            @Override
            public void onError(String error) {
                Log.i("HTTP Leave Group Chat Error",error);
                current.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isCreate == false){
//            Reload();
        }else {
            isCreate = false;
        }
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
        httpRequest.getGroupMetaData(new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                try {
                    JSONArray groupChat = response.getJSONObject("data").getJSONArray("groupChat");

                    ArrayList<String> founded = new ArrayList<String>();

                    if (groupChat.length() > 0) {
                        //                                roomModels.clear();
                        ArrayList<RoomModel> rooms = getListRoom(groupChat);

                        for (RoomModel old_room : roomModels) {
                            boolean found = false;
                            for (RoomModel new_room : rooms) {
                                if (old_room.getId().equals(new_room.getId())) {
                                    old_room.setMessages(new_room.getMessages());
                                    old_room.setAvatar(new_room.getAvatar());
                                    old_room.setName(new_room.getName());
                                    old_room.setOptions(new_room.getOptions());
                                    customChatItem.notifyDataSetChanged();
                                    customChatItem.notifyDataSetInvalidated();
                                    found = true;
                                    founded.add(new_room.getId());
                                    break;
                                }
                            }
                            if (!found) {
                                roomModels.remove(old_room);
                                customChatItem.notifyDataSetChanged();
                                customChatItem.notifyDataSetInvalidated();
                            }
                        }
                        for (RoomModel new_room : rooms) {
                            if (!founded.contains(new_room.getId())) {
                                roomModels.add(new_room);
                                customChatItem.notifyDataSetChanged();
                                customChatItem.notifyDataSetInvalidated();
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.i("HTTP Success 11111 Error", e.toString());
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
}