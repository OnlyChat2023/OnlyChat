package com.example.onlychat.DirectMessage;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.onlychat.Async.DownloadImage;
import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.SeenMessageListener;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Model.ImageModel;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Interfaces.ProfileReceiver;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import io.socket.emitter.Emitter;

public class DirectMessage extends Fragment {
    TextView chatTitle;
    ImageView chatIcon;
    ImageView profile;
    ImageView addChat;
    ListView listChat;
    EditText main_content_searchbox;

    ImageView delete;
    private ProgressBar progressBar;
    private TextView loading;
    static CustomChatItem customChatItem;
    static ArrayList<RoomModel> roomModels = new ArrayList<>();
    GlobalPreferenceManager pref;
    public static ArrayList<RoomModel> getRoomModels() {
        return roomModels;
    }

    public static void addRoom(RoomModel roomModel){
        roomModels.add(roomModel);
        customChatItem.notifyDataSetChanged();
    }

    public static void removeRoom(String id){
        HttpManager httpManager = new HttpManager(customChatItem.getContext());
        httpManager.deleteRoom(id, new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException, InterruptedException, ParseException {

            }

            @Override
            public void onError(String error) {

            }
        });
        for(int i=0;i<roomModels.size();i++){
            if(roomModels.get(i).getId().equals(id)) {
                roomModels.remove(i);
                customChatItem.notifyDataSetChanged();
                break;
            }
        }
    }

    public DirectMessage(){}
    static UserModel myInfo;
    RelativeLayout globalChat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        globalChat = (RelativeLayout) inflater.inflate(R.layout.fragment_main_content, null);
        roomModels.clear();
        // set value for widget
        chatTitle=(TextView) globalChat.findViewById(R.id.header_title);
        chatIcon = (ImageView) globalChat.findViewById(R.id.chatIcon);
        profile=(ImageView) globalChat.findViewById(R.id.profile);
        addChat = (ImageView) globalChat.findViewById(R.id.addChat);
        addChat.setVisibility(View.GONE);
        listChat = (ListView) globalChat.findViewById(R.id.listChat);


        progressBar = (ProgressBar) globalChat.findViewById(R.id.progressBar);
        loading  = (TextView) globalChat.findViewById(R.id.loading);

        main_content_searchbox= (EditText)globalChat.findViewById(R.id.main_content_searchbox);
        delete = (ImageView) globalChat.findViewById(R.id.delete);



        pref = new GlobalPreferenceManager(getContext());
        new HttpManager.GetImageFromServer(profile).execute(new GlobalPreferenceManager(getContext()).getUserModel().getAvatar());

        myInfo = pref.getUserModel();

        roomModels.clear();

        // set list messages
        HttpManager httpManager = new HttpManager(getContext());
        httpManager.getDirectMessage(
            new HttpResponse(){
                @Override
                public void onSuccess(JSONObject Response) {
                    loading.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    try{
                        JSONArray chats = Response.getJSONObject("data").getJSONArray("directChat");
                        Log.i("Direct chat", Integer.toString(chats.length()));
                        if(chats.length()>0){
                            roomModels.addAll(MainScreen.getListRoom(chats));

                            for (RoomModel room : roomModels) {
                                final ArrayList<MessageModel> messages = room.getMessages();
                                if (messages.size() <= 0) continue;

                                final MessageModel lastMessage = messages.get(messages.size() - 1);

                                if (lastMessage.getUserId().equals(myInfo.getId())) {
                                    ArrayList<String> anothersSeen = new ArrayList<>();

                                    for (String user_id : lastMessage.getSeenUser())
                                        if (user_id.equals(myInfo.getId())) continue;
                                        else {
                                            anothersSeen.add(user_id);
                                        };

                                    final ArrayList<Member> members = room.getOptions().getMembers();
                                    final ArrayList<String> seenAvatar = new ArrayList<>();
                                    for (Member member : members) {
                                        if (anothersSeen.contains(member.getUser_id())) {
                                            seenAvatar.add(member.getAvatar());
                                        }
                                    }

                                    new DownloadImage(seenAvatar, new ConvertListener() {

                                        @Override
                                        public void onSuccess(ImageModel result) {

                                        }

                                        @Override
                                        public void onDownloadSuccess(ArrayList<Bitmap> result) {
                                            room.setSeenUser(result);
                                            customChatItem.notifyDataSetChanged();
                                        }
                                    }).execute();
                                }
                            }

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
                Bitmap backupAvt = roomModels.get(i).getBitmapAvatar();
                ArrayList<Bitmap> backupSeen = roomModels.get(i).getSeenUser();

                Intent intent = new Intent(listChat.getContext(), ChattingActivity.class);
//                intent.putParcelableArrayListExtra("RoomAvatar", new ArrayList<>(Collections.singleton(roomModels.get(i).getBitmapAvatar())));

                roomModels.get(i).setBitmapAvatar(null);
                roomModels.get(i).setSeenUser(null);
                intent.putExtra("roomChat", roomModels.get(i));

                startActivityForResult(intent, 0);

                roomModels.get(i).setBitmapAvatar(backupAvt);
                roomModels.get(i).setSeenUser(backupSeen);
                getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.fixed);
            }
        });

        listChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                DirectMessageBottomDialog messageBottomDialogFragment = new DirectMessageBottomDialog();
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

        main_content_searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>0) delete.setVisibility(View.VISIBLE);
                else delete.setVisibility(View.GONE);
                for(int i=0;i<roomModels.size();i++){
                    if(roomModels.get(i).getName().toLowerCase().contains(editable.toString().toLowerCase())){
                        if(listChat.getChildAt(i).getVisibility() == View.GONE) {
                            listChat.getChildAt(i).setVisibility(View.VISIBLE);
                            listChat.getChildAt(i).setLayoutParams(new AbsListView.LayoutParams(-1,-2));
                        }
                    }
                    else {
                        listChat.getChildAt(i).setVisibility(View.GONE);
                        listChat.getChildAt(i).setLayoutParams(new AbsListView.LayoutParams(-1,1));
                    }
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_content_searchbox.setText("");
            }
        });
        waitSetNickname();

        SocketManager.getInstance();
        SocketManager.seenMessageListener(new SeenMessageListener() {
            @Override
            public void onSeen(String RoomID, ArrayList<String> SeenUsers) {

                Log.i( "onSeen: ", RoomID);
//                Log.i( "onSeen2: ", UserID);
                for (RoomModel room : roomModels) {
                    if (room.get_id().equals(RoomID)) {
                        final ArrayList<MessageModel> messages = room.getMessages();
                        if (messages.size() <= 0) continue;
                        final MessageModel lastMessage = messages.get(messages.size() - 1);

                        if (lastMessage.getUserId().equals(myInfo.getId())) {

                            ArrayList<String> seenAvt = new ArrayList<>();

                            for (Member member : room.getOptions().getMembers()) {
                                if (member.getUser_id().equals(myInfo.getId())) continue;
                                if (SeenUsers.contains(member.getId()))
                                    seenAvt.add(member.getAvatar());
                            }

                            new DownloadImage(seenAvt, new ConvertListener() {

                                @Override
                                public void onSuccess(ImageModel result) {

                                }

                                @Override
                                public void onDownloadSuccess(ArrayList<Bitmap> result) {
                                    room.setSeenUser(result);
                                    customChatItem.notifyDataSetChanged();
                                }
                            }).execute();
                        }
                        break;
                    }
                }
            }
        });

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
            boolean update = data.getBooleanExtra("Update", true);
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
//                                    old_room.setSeenUser(new_room.getSeenUser());
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

                        for (RoomModel room : roomModels) {
                            final ArrayList<MessageModel> messages = room.getMessages();
                            if (room.getMessages().size() == 0) continue;
                            final MessageModel lastMessage = messages.get(messages.size() - 1);

                            if (!lastMessage.getUserId().equals(myInfo.getId())) {
                                room.setSeenUser(null);
                                customChatItem.notifyDataSetChanged();
                            }
                            else {
                                ArrayList<String> anothersSeen = new ArrayList<>();

                                for (String user_id : lastMessage.getSeenUser())
                                    if (user_id.equals(myInfo.getId())) continue;
                                    else {
                                        anothersSeen.add(user_id);
                                    };

                                final ArrayList<Member> members = room.getOptions().getMembers();
                                final ArrayList<String> seenAvatar = new ArrayList<>();
                                for (Member member : members) {
                                    if (anothersSeen.contains(member.getUser_id())) {
                                        seenAvatar.add(member.getAvatar());
                                    }
                                }

                                new DownloadImage(seenAvatar, new ConvertListener() {

                                    @Override
                                    public void onSuccess(ImageModel result) {

                                    }

                                    @Override
                                    public void onDownloadSuccess(ArrayList<Bitmap> result) {
                                        room.setSeenUser(result);
                                        customChatItem.notifyDataSetChanged();
                                    }
                                }).execute();
                            }
                        }

                        customChatItem.notifyDataSetChanged();
                    }
                    else {
                        roomModels.clear();
                        customChatItem.notifyDataSetChanged();
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
