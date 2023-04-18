package com.example.onlychat.GroupChat;

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
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

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
        roomModels.sort(new Comparator<RoomModel>() {
            @Override
            public int compare(RoomModel roomModel, RoomModel t1) {
                return roomModel.getUpdate_time().compareTo(t1.getUpdate_time());
            }
        }.reversed());

        for(RoomModel i:roomModels){
            this.roomModels.add(i);
        }
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
        chatIcon.setImageResource(R.drawable.global_chat_icon);

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
                intent.putExtra("Data",roomModels.get(i));
                intent.putExtra("channel", "group_chat");
                startActivity(intent);
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
                                    Reload();
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
        return groupChat;
    }


    public void Reload() {

        HttpManager httpManager = new HttpManager(getContext());
        httpManager.GetListGroupChat(typeChat, pref.getUserModel().get_id(), new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                try{
                    JSONArray groupChat = response.getJSONArray("data");
                    ArrayList<RoomModel> rooms = ((MainScreen) getActivity()).getListRoom(groupChat);
                    roomModels.clear();
                    setRoomModels(rooms);
                }
                catch (Exception e){
                    Log.i("HTTP Group Chat Success Error",e.toString());
                }
            }

            @Override
            public void onError(String error) {
                Log.i("HTTP Error",error);
            }
        });
    }

    public void LeaveGroup(MessageBottomDialogFragment current, String id){
        HttpManager httpManager = new HttpManager(getContext());
        httpManager.LeaveGroupChat(typeChat, pref.getUserModel().get_id(), id, new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                Reload();
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
            Reload();
        }else {
            isCreate = false;
        }
    }
}