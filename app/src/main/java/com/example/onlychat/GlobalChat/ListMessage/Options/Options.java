package com.example.onlychat.GlobalChat.ListMessage.Options;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DialogFragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlychat.Async.ConvertImage;
import com.example.onlychat.DiaLog.BasicDialog;
import com.example.onlychat.DiaLog.ChangeGroupNameDialog;
import com.example.onlychat.DiaLog.ChangeNickNameDialog;
import com.example.onlychat.DirectMessage.ChattingActivity;
import com.example.onlychat.DirectMessage.DirectMessage;
import com.example.onlychat.EditProfile.EditProfile;
import com.example.onlychat.GlobalChat.ListMessage.ListMessage;
import com.example.onlychat.GroupChat.AddMember;
import com.example.onlychat.GroupChat.GroupChat;
import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.RoomOptions;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.ImageModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class Options extends AppCompatActivity {
    RelativeLayout members;
    RelativeLayout notify;
    RelativeLayout delete;
    RelativeLayout leave;
    RelativeLayout block;
    RelativeLayout report;
    RelativeLayout edit;
    RelativeLayout group_name;
    ListView listMembers;
    ImageView backButton;

    static TextView name;
    TextView memberNumbers;
    ImageView avatar;
    ImageView notify_icon, upImgBtn;
    ImageView pencil_icon;
    TextView notify_txt;
    RoomOptions options;
    String typeChat;
    String GroupID;
    Button addMember;
    int FINISH = -5;
    int UPDATEOPTION = -6;
    int ADDMEMBER = -7, DELETEGR = 7;
    GlobalPreferenceManager pref;
    static UserModel myInfo;

    TextView memberQuantity;

    Integer avatarsImage[] = {
            R.raw.a_1, R.raw.a_2, R.raw.a_3, R.raw.a_4, R.raw.a_5,
            R.raw.a_6, R.raw.a_7, R.raw.a_8, R.raw.a_9, R.raw.a_10,
            R.raw.a_11, R.raw.a_12, R.raw.a_13, R.raw.a_14, R.raw.a_15,
            R.raw.a_16,R.raw.a_17, R.raw.a_18, R.raw.a_19, R.raw.a_20,
            R.raw.a_21, R.raw.a_22, R.raw.a_23, R.raw.a_24, R.raw.a_25,
    };

    GridView androidGridView;


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
        upImgBtn = (ImageButton) findViewById(R.id.up_image_btn);
//        pencil_icon = (ImageView) findViewById(R.id.pencil_ic);
        group_name = (RelativeLayout) findViewById(R.id.group_name_layout);
        delete = (RelativeLayout) findViewById(R.id.global_delete);
        leave = (RelativeLayout) findViewById(R.id.global_leave);
        block = (RelativeLayout) findViewById(R.id.global_block);
        report = (RelativeLayout) findViewById(R.id.global_report);
        addMember = (Button) findViewById(R.id.add_member_btn);
        edit = (RelativeLayout) findViewById(R.id.edit);

        if (typeChat.equals("globalChat")){
            addMember.setVisibility(View.GONE);
            leave.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            addMember.setVisibility(View.GONE);
            notify.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);

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

        pref = new GlobalPreferenceManager(this);
        myInfo = pref.getUserModel();

        edit.setOnClickListener(new View.OnClickListener() {
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
                View popupView = inflater.inflate(R.layout.global_chat_popup, null);
                EditText nickname = popupView.findViewById(R.id.nickname);


//                Log.i("anonymous avatar", myInfo.getAnonymous_avatar());


//                Log.i("anonymous avatar",part2[0]);
                final int[] avatarIndex = {1};

                HttpManager httpManager= new HttpManager(edit.getContext());
                httpManager.getUserById(myInfo.get_id(), new HttpResponse() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                        JSONObject profile = response.getJSONObject("data");

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

                        HttpManager httpManager = new HttpManager(edit.getContext());
                        httpManager.setAnonymousInformation(nickname.getText().toString(),"avatar/"+avatarIndex[0]+".png", new HttpResponse() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException, ParseException {
                                popupWindow.dismiss();
//                                new HttpManager.GetImageFromServer(edit).execute(response.getJSONObject("data").getString("anonymous_avatar"));
//                                JSONArray globalChat = response.getJSONObject("data").getJSONArray("globalChat");
////                                Log.i("OK BUTTON", Integer.toString(globalChat.length()));
//                                if(globalChat.length()>0){
////                                   setRoomModels(MainScreen.getListRoom(globalChat));
//                                }

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
                                    String channel = typeChat.equals("groupChat") ? "group_chat" : "global_chat";

                                    SocketManager.notifyUpdateRoom(GroupID, channel);

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ms = "Do you still want to delete this chat?";
                BasicDialog dialog = new BasicDialog().newInstance(ms);
                dialog.setActivity("DELETEGR");
                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());
            }
        });

        group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeGroupNameDialog dialog = new ChangeGroupNameDialog().newInstance(Options.this);
                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());
            }
        });

        upImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, 1000);
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

    public void setGroupName(ChangeGroupNameDialog current, String newGroupName){
        SocketManager.getInstance();
        SocketManager.changeGroupName(GroupID, newGroupName);

        name.setText(newGroupName);
        current.dismiss();
    }

    public void Delete(BasicDialog current){
        GroupChat.removeRoom(GroupID);
        setResult(DELETEGR, new Intent(block.getContext(), ListMessage.class));
        current.dismiss();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == 1000){
//                img_in_phone = String.valueOf(data.getData());
                avatar.setImageURI(data.getData());

                ArrayList<Uri> avt = new ArrayList<>();
                avt.add(data.getData());

                new ConvertImage(this, avt, new ConvertListener() {

                    @Override
                    public void onSuccess(ImageModel result) {
                        ArrayList<String> myAvt = result.getImagesListStr();
                        String avtBase64 = myAvt.get(0);

                        String channel = (typeChat.equals("groupChat")) ? "group_chat" : "global_chat";
                        SocketManager.getInstance();
                        SocketManager.uploadAvatarChat(channel, avtBase64, GroupID);
                        avatar.setImageBitmap(result.getImagesBM().get(0));
                    }

                    @Override
                    public void onDownloadSuccess(ArrayList<Bitmap> result) {

                    }
                }).execute();
            }
        }
    }

//    public static void waitSetGroupName(){
//        SocketManager.getInstance();
//        if(SocketManager.getSocket() !=null){
//            SocketManager.getSocket().on("waitSetGroupName", new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    String newGroupName = (String) args[0];
//
//                    Log.i("vvvvvvvvvvvvvvvvvvv", newGroupName);
//                    name.setText(newGroupName);
//                }
//            });
//        }
//    }

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

}