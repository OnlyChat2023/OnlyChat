package com.example.onlychat.GlobalChat.ListMessage.Options;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DialogFragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlychat.DiaLog.BasicDialog;
import com.example.onlychat.GlobalChat.ListMessage.ListMessage;
import com.example.onlychat.GroupChat.AddMember;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.RoomOptions;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class Options extends AppCompatActivity {

    RelativeLayout share;
    RelativeLayout members;
    RelativeLayout notify;
    RelativeLayout delete;
    RelativeLayout leave;
    RelativeLayout block;
    RelativeLayout report;
    ListView listMembers;
    ImageView backButton;

    TextView name;
    TextView memberNumbers;
    ImageView avatar;
    ImageView notify_icon;
    TextView notify_txt;
    RoomOptions options;
    String typeChat;
    String GroupID;
    Button addMember;
    int FINISH = -5;
    int UPDATEOPTION = -6;
    int ADDMEMBER = -7;

    TextView memberQuantity;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_chat_chat_options);

        Intent intent = getIntent();
        options = (RoomOptions) intent.getSerializableExtra("Data");
        GroupID = (String) intent.getSerializableExtra("GroupID");
        String names = (String) intent.getSerializableExtra("Name");
        String avatars = (String) intent.getSerializableExtra("Avatar");
        typeChat = (String) intent.getSerializableExtra("typeChat");
        Log.i("Options", typeChat);

        notify = (RelativeLayout) findViewById(R.id.global_notify);
        notify_txt = (TextView) findViewById(R.id.notify_txt);
        notify_icon = (ImageView) findViewById(R.id.imageView14);
        delete = (RelativeLayout) findViewById(R.id.global_delete);
        leave = (RelativeLayout) findViewById(R.id.global_leave);
        block = (RelativeLayout) findViewById(R.id.global_block);
        report = (RelativeLayout) findViewById(R.id.global_report);
        addMember = (Button) findViewById(R.id.add_member_btn);
        if (typeChat.equals("globalChat")){
            addMember.setVisibility(View.GONE);
        }
        block.setVisibility(View.GONE);

        name = (TextView) findViewById(R.id.group_name);
        memberNumbers = (TextView) findViewById(R.id.memberQuantity);
        avatar = (ImageView) findViewById(R.id.avatar);
        memberQuantity =  (TextView) findViewById(R.id.memberQuantity);
        memberQuantity.setText("Members ("+options.getMembers().size()+")");
        name.setText(names);
        memberNumbers.setText("Members (" + Integer.toString(options.getMembers().size()) + ")");
        new HttpManager.GetImageFromServer(avatar).execute(avatars);
        if (options.getNotify() == true){
            notify_txt.setText("Turn off notification");
            notify_icon.setImageResource(R.drawable.dm_option_icon_on_notification);
        }else{
            notify_txt.setText("Turn on notification");
            notify_icon.setImageResource(R.drawable.dm_option_icon_off_notifycation);
        }


        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
            }
        });

        share = (RelativeLayout) findViewById(R.id.share);
        if (typeChat.equals("groupChat")){
            share.setVisibility(View.GONE);
        }
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                // overlay
                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
//                boolean focusable = true; // lets taps outside the popup also dismiss it
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                // Popup
                View popupView = inflater.inflate(R.layout.global_chat_popup_share, null);
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        overlayWindow.dismiss();
                    }
                });
            }
        });

        members = (RelativeLayout) findViewById(R.id.members);
        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                // overlay
                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
//                boolean focusable = true; // lets taps outside the popup also dismiss it
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                // Popup
                View popupView = inflater.inflate(R.layout.global_chat_popup_members, null);
                TextView title = (TextView) popupView.findViewById(R.id.quantity);
                title.setText("Friends (" + Integer.toString(options.getMembers().size()) + ")");

                // set list members
                listMembers = (ListView)  popupView.findViewById(R.id.listMembers);
                TextView quantity = (TextView) popupView.findViewById(R.id.quantity);
                quantity.setText("Members ("+options.getMembers().size()+")");

                CustomMemberItem customMemberItem=new CustomMemberItem(popupView.getContext(),options.getMembers());
                if (typeChat.equals("groupChat")){
                    customMemberItem.setIsGroupChat(true);
                }else{
                    customMemberItem.setIsGroupChat(false);
                }
                listMembers.setAdapter(customMemberItem);
                listMembers.setSelection(0);
                listMembers.smoothScrollToPosition(0);
                listMembers.setDivider(null);
                listMembers.setDividerHeight(0);

                // listMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //     @Override
                //     public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //         Bundle myBundle = new Bundle();
                //         myBundle.putInt("index",i);
                //         myBundle.putString("user_id",options.getMembers().get(i).getId());



                //         Intent intentToProfile = new Intent (listMembers.getContext(), Profile.class);
                //         intentToProfile.putExtras(myBundle);
                //         startActivity(intentToProfile);
                //     }
                // });
                if(typeChat.equals("groupChat")){
                    listMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.i("Option", "===========>>>>>>");

                            Bundle myBundle = new Bundle();
                            myBundle.putInt("index",i);
                            myBundle.putString("user_id",options.getMembers().get(i).getId());


                            Intent intentToProfile = new Intent (listMembers.getContext(), Profile.class);
                            intentToProfile.putExtras(myBundle);
                            startActivity(intentToProfile);
                        }
                    });
                }



                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,750, LinearLayout.LayoutParams.WRAP_CONTENT,focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        overlayWindow.dismiss();
                    }
                });
            }
        });

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            new HttpManager(addMember.getContext()).GetListNewMember(typeChat, options.getUser_id(), GroupID, new HttpResponse() {
                @Override
                public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                    JSONArray listFriends = response.getJSONArray("data");

                    ArrayList<Member> friendMms = new ArrayList<Member>();
                    for (int i = 0; i < listFriends.length(); i++){
                        JSONObject mem = listFriends.getJSONObject(i);
                        Member temp = new Member(mem.getString("_id"), mem.getString("name"), mem.getString("nickname"), mem.getString("avatar"));
                        friendMms.add(temp);
                    }

                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    // overlay
                    View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
//                boolean focusable = true; // lets taps outside the popup also dismiss it
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.MATCH_PARENT;
                    final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
                    overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                    // Popup
                    View popupView = inflater.inflate(R.layout.global_chat_popup_members, null);
                    TextView title = (TextView) popupView.findViewById(R.id.quantity);


                    // set list members
                    listMembers = (ListView)  popupView.findViewById(R.id.listMembers);

                    title.setText("Suggested Friends (" + Integer.toString(friendMms.size()) + ")");
                    CustomMemberItem customMemberItem = new CustomMemberItem(popupView.getContext(), friendMms);
                    if (typeChat.equals("groupChat")){
                        customMemberItem.setIsGroupChat(true);
                    }else{
                        customMemberItem.setIsGroupChat(false);
                    }
                    customMemberItem.setIsAddMember(true);
                    listMembers.setAdapter(customMemberItem);
                    listMembers.setSelection(0);
                    listMembers.smoothScrollToPosition(0);
                    listMembers.setDivider(null);
                    listMembers.setDividerHeight(0);

                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,focusable);
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            overlayWindow.dismiss();
                        }
                    });

                    listMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            new HttpManager(listMembers.getContext()).addMemberGroup(typeChat, friendMms.get(i).getId(), GroupID, friendMms.get(i).getName(), friendMms.get(i).getNickname(), friendMms.get(i).getAvatar(), new HttpResponse() {
                                @Override
                                public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                                    popupWindow.dismiss();
                                    overlayWindow.dismiss();
                                    setResult(ADDMEMBER, new Intent(listMembers.getContext(), ListMessage.class).putExtra("data", friendMms.get(i)));
                                    finish();
                                }

                                @Override
                                public void onError(String error) {
                                    Log.i("<<GET LIST SUGGETED MEMBER ERROR>>:", error);
                                }
                            });
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    Log.i("<<GET LIST SUGGETED MEMBER ERROR>>:", error);
                }
            });
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicDialog basicDialog = new BasicDialog().newInstance("Do you still want to leave this group?");
                basicDialog.setActivity("LEAVEGROUP");
                basicDialog.show(getSupportFragmentManager(), basicDialog.getTag());
            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (options.getNotify() == true){
                    new HttpManager(Options.this).UpdateGroupNotufy(typeChat, options.getUser_id(), GroupID, false, options.getBlock(), new HttpResponse() {
                        @Override
                        public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                            notify_txt.setText("Turn on notification");
                            notify_icon.setImageResource(R.drawable.dm_option_icon_off_notifycation);
                            options.setNotify(false);
                            setResult(UPDATEOPTION, new Intent(notify.getContext(), ListMessage.class).putExtra("data", options));
                        }

                        @Override
                        public void onError(String error) {
                            Log.i("<Change Group Notify Error>", error);
                        }
                    });

                }else{
                    new HttpManager(Options.this).UpdateGroupNotufy(typeChat, options.getUser_id(), GroupID, true, options.getBlock(), new HttpResponse() {
                        @Override
                        public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                            notify_txt.setText("Turn off notification");
                            notify_icon.setImageResource(R.drawable.dm_option_icon_on_notification);
                            options.setNotify(true);
                            setResult(UPDATEOPTION, new Intent(notify.getContext(), ListMessage.class).putExtra("data", options));
                        }

                        @Override
                        public void onError(String error) {
                            Log.i("<Change Group Notify Error>", error);
                        }
                    });
                }
            }
        });
    }




    public void LeaveGroup(BasicDialog basicDialog){
        HttpManager httpManager = new HttpManager(Options.this);

        httpManager.LeaveGroupChat(typeChat, options.getUser_id(), GroupID, new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                basicDialog.dismiss();
                setResult(FINISH);
                finish();

            }

            @Override
            public void onError(String error) {
                Log.i("HTTP Leave Group Chat Error",error);
                basicDialog.dismiss();
                finish();
            }
        });
    }
}