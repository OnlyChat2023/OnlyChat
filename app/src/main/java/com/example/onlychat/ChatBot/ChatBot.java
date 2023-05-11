package com.example.onlychat.ChatBot;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.GlobalChat.ListMessage.ListMessage;
import com.example.onlychat.GlobalChat.MessageBottomDialogFragment;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ChatBot extends Fragment {
    TextView chatTitle;
    ImageView chatIcon;
    ImageView profile;
    ImageView addChat;
    ListView listChat;
    CustomChatItem customChatItem;
    String typeChat = "botChat";
    GlobalPreferenceManager pref;

    private ProgressBar progressBar;
    private TextView loading;

    private RelativeLayout botChat;

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
        botChat = (RelativeLayout) inflater.inflate(R.layout.fragment_main_content_bot, null);

        // set value for widget
        chatTitle=(TextView) botChat.findViewById(R.id.header_title);
        chatIcon = (ImageView) botChat.findViewById(R.id.chatIcon);
        addChat = (ImageView) botChat.findViewById(R.id.addChat);
        listChat = (ListView) botChat.findViewById(R.id.listChat);

        progressBar = (ProgressBar) botChat.findViewById(R.id.progressBar);
        loading  = (TextView) botChat.findViewById(R.id.loading);

        Log.i("<<<<<<Bot chat>>>>>>>>>", Integer.toString(roomModels.size()));

        pref = new GlobalPreferenceManager(getContext());

        roomModels.clear();

        // set list messages
        HttpManager httpManager = new HttpManager(getContext());
        httpManager.getBotChat(
                new HttpResponse(){
                    @Override
                    public void onSuccess(JSONObject Response) {
                        loading.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);
                        try{
                            JSONArray chats = Response.getJSONObject("data").getJSONArray("botChat");
//                            Log.i("Direct chat", Integer.toString(chats.length()));
                            if(chats.length()>0){
                                roomModels.addAll(MainScreen.getListRoom(chats));
                                customChatItem.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e){
                            Log.e("HTTP Success 11111 Error",e.toString());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.i("HTTP Error",error);
                    }
                }
        );

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
                roomModels.get(i).setBitmapAvatar(null);
                intent.putExtra("Data", roomModels.get(i));
                intent.putExtra("typeChat", typeChat);
                intent.putExtra("Position", i);
                intent.putExtra("channel", "bot_chat");
                startActivityForResult(intent, 0);
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
                        if (!newName.equals("") && !TextUtils.isEmpty(newName)) {
                            HttpManager httpManager = new HttpManager(getContext());
                            httpManager.addBotChat(newName, pref.getUserModel().get_id(), new HttpResponse() {
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
        return botChat;
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
        botChat.post(new Runnable() {
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
        httpRequest.getBotChat(new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                try{
                    JSONArray globalChat = response.getJSONObject("data").getJSONArray("botChat");

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
