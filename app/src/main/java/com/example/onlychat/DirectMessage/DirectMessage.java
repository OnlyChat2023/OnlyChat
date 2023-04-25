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
import com.example.onlychat.GroupChat.GroupChat;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.RoomOptions;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.MessageModel;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.socket.emitter.Emitter;

public class DirectMessage extends Fragment {
    TextView chatTitle;
    ImageView chatIcon;
    ImageView profile;
    ImageView addChat;
    ListView listChat;
    CustomChatItem customChatItem;
    static ArrayList<RoomModel> roomModels = new ArrayList<>();
    GlobalPreferenceManager pref;
    public static ArrayList<RoomModel> getRoomModels() {
        return roomModels;
    }

    public void addRoom(RoomModel roomModel){
        roomModels.add(roomModel);
        customChatItem.notifyDataSetChanged();
    }

    public DirectMessage(){}
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

        pref = new GlobalPreferenceManager(getContext());
        new HttpManager.GetImageFromServer(profile).execute(new GlobalPreferenceManager(getContext()).getUserModel().getAvatar());

        myInfo = pref.getUserModel();

        // set list messages
        HttpManager httpManager = new HttpManager(getContext());
        httpManager.getDirectMessage(
            new HttpResponse(){
                @Override
                public void onSuccess(JSONObject Response) {
                    try{
                        JSONArray chats = Response.getJSONObject("data").getJSONArray("directChat");
                        Log.i("Direct chat", Integer.toString(chats.length()));
                        if(chats.length()>0){
                            roomModels.addAll(MainScreen.getListRoom(chats));
                            customChatItem.notifyDataSetChanged();
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
                messageBottomDialogFragment.setId(roomModels.get(i).getId());
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

                Intent intentToProfile = new Intent (getContext(), Profile.class);
                intentToProfile.putExtras(myBundle);
                startActivity(intentToProfile);
                getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.fixed);


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
        waitSetNickname();
        return globalChat;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        Log.i("Direct message", "updateListRoom");
        HttpManager httpRequest = new HttpManager(getContext());
        httpRequest.getDirectMetaData(new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                try{
                    JSONArray globalChat = response.getJSONObject("data").getJSONArray("directChat");

                    ArrayList<String> founded = new ArrayList<String>();

                    if(globalChat.length()>0){
                        ArrayList<RoomModel> rooms = MainScreen.getListRoom(globalChat);

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

}
