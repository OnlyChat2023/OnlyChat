package com.example.onlychat.DirectMessage.Option;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlychat.DiaLog.BasicDialog;
import com.example.onlychat.DiaLog.ChangeNickNameDialog;
import com.example.onlychat.DirectMessage.ChattingActivity;
import com.example.onlychat.DirectMessage.DirectMessage;
import com.example.onlychat.GlobalChat.ListMessage.Options.Options;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.RoomOptions;
import com.example.onlychat.MainActivity;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class OptionActivity extends AppCompatActivity {
    ChattingActivity preChat;
    ImageView avatar, notify_icon;
    Button btn_back, btn_nickname, btn_profile, btn_notify;
    TextView txtName;
    static TextView txtBlock;
    RelativeLayout nick_name;
    RelativeLayout notify;
    RelativeLayout profile;
    RelativeLayout delete;
    static RelativeLayout block;
    RelativeLayout report;
    RoomOptions options;
    static Member meInf;
    static Member friendInf;
    Boolean valueBlock;
    String DM_id;
    Integer CHANGENOTIFY = -7;
    static Integer CHANGEBLOCK = -8;
    Integer CHANGEFRNN = 5;
    Integer CHANGEMENN = 6, DELETEDM = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_message_activity_option);

        avatar = (ImageView) findViewById(R.id.chat_option_avatar);
        txtName = (TextView) findViewById(R.id.chat_option_name);
        btn_back = (Button) findViewById(R.id.chat_option_back);
        btn_nickname = (Button) findViewById(R.id.chat_option_btn_nickname);
        btn_profile = (Button) findViewById(R.id.chat_option_btn_profile);
        btn_notify = (Button) findViewById(R.id.chat_option_btn_noify);
        notify_icon = (ImageView) findViewById(R.id.imageView14);

        nick_name = (RelativeLayout) findViewById(R.id.change_nickname);
        notify = (RelativeLayout) findViewById(R.id.on_off_notify);
        profile = (RelativeLayout) findViewById(R.id.profile_page);
        delete = (RelativeLayout) findViewById(R.id.delete_chat);
        block = (RelativeLayout) findViewById(R.id.block);
        txtBlock = (TextView)findViewById(R.id.block_text);
        report = (RelativeLayout) findViewById(R.id.report);

        Intent main_chat = getIntent();
        preChat = (ChattingActivity) getParent();
        DM_id = (String) main_chat.getSerializableExtra("DM_id");
        options = (RoomOptions) main_chat.getSerializableExtra("option");
        friendInf = (Member) main_chat.getSerializableExtra("friend");
        meInf = (Member) main_chat.getSerializableExtra("me");
//        avatar.setImageResource(friendInf.getAvatar());
        new HttpManager.GetImageFromServer(avatar).execute(friendInf.getAvatar());

        txtName.setText(friendInf.getName());
        if (options.getNotify()){
            btn_notify.setBackgroundResource(R.drawable.dm_icon_on_notify_nav);
            notify_icon.setBackgroundResource(R.drawable.dm_option_icon_on_notification);
        } else {
            btn_notify.setBackgroundResource(R.drawable.dm_icon_off_notify);
            notify_icon.setBackgroundResource(R.drawable.dm_option_icon_off_notifycation);
        }
        new HttpManager(OptionActivity.this).getBlockDM(friendInf.getUser_id(), DM_id, new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                valueBlock = (Boolean) response.getBoolean("data");
                if (valueBlock){
                    txtBlock.setText("Unblock");
                } else{
                    txtBlock.setText("Block");
                }
            }

            @Override
            public void onError(String error) {
                Log.i("Get Block Option Error: ", error);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
            }
        });

        btn_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeNickNameDialog dialog = new ChangeNickNameDialog().newInstance(OptionActivity.this, friendInf.getName(), friendInf.getNickname(), meInf.getNickname());
                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());
            }
        });

        nick_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeNickNameDialog dialog = new ChangeNickNameDialog().newInstance(OptionActivity.this, friendInf.getName(), friendInf.getNickname(), meInf.getNickname());
                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());
            }
        });

        btn_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (options.getNotify()){
                    new HttpManager(btn_notify.getContext()).changeOptionDM(meInf.getUser_id(), DM_id, false, options.getBlock(), new HttpResponse() {
                        @Override
                        public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                            setResult(CHANGENOTIFY, new Intent(btn_notify.getContext(), ChattingActivity.class).putExtra("data", false));
                            options.setNotify(false);
                            btn_notify.setBackgroundResource(R.drawable.dm_icon_off_notify);
                            notify_icon.setBackgroundResource(R.drawable.dm_option_icon_off_notifycation);
                        }

                        @Override
                        public void onError(String error) {
                            Log.i("CHANGE NOTIFY: ", error);
                        }
                    });

                } else{

                    new HttpManager(btn_notify.getContext()).changeOptionDM(meInf.getUser_id(), DM_id, true, options.getBlock(), new HttpResponse() {
                        @Override
                        public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                            setResult(CHANGENOTIFY, new Intent(btn_notify.getContext(), ChattingActivity.class).putExtra("data", true));
                            options.setNotify(true);
                            btn_notify.setBackgroundResource(R.drawable.dm_icon_on_notify_nav);
                            notify_icon.setBackgroundResource(R.drawable.dm_option_icon_on_notification);
                        }

                        @Override
                        public void onError(String error) {
                            Log.i("CHANGE NOTIFY: ", error);
                        }
                    });
                }
            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (options.getNotify()){
                    new HttpManager(btn_notify.getContext()).changeOptionDM(meInf.getUser_id(), DM_id, false, options.getBlock(), new HttpResponse() {
                        @Override
                        public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                            setResult(CHANGENOTIFY, new Intent(notify.getContext(), ChattingActivity.class).putExtra("data", false));
                            options.setNotify(false);
                            btn_notify.setBackgroundResource(R.drawable.dm_icon_off_notify);
                            notify_icon.setBackgroundResource(R.drawable.dm_option_icon_off_notifycation);
                        }

                        @Override
                        public void onError(String error) {
                            Log.i("CHANGE NOTIFY: ", error);
                        }
                    });

                } else{

                    new HttpManager(btn_notify.getContext()).changeOptionDM(meInf.getUser_id(), DM_id, true, options.getBlock(), new HttpResponse() {
                        @Override
                        public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                            setResult(CHANGENOTIFY, new Intent(notify.getContext(), ChattingActivity.class).putExtra("data", true));
                            options.setNotify(true);
                            btn_notify.setBackgroundResource(R.drawable.dm_icon_on_notify_nav);
                            notify_icon.setBackgroundResource(R.drawable.dm_option_icon_on_notification);
                        }

                        @Override
                        public void onError(String error) {
                            Log.i("CHANGE NOTIFY: ", error);
                        }
                    });
                }
            }
        });

        waitSetNickname();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicDialog dialog = new BasicDialog().newInstance("Do you stil want to delete this message?");
                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());
            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ms = (valueBlock) ? "Do you still want to unblock this person?" : "Do you still want to block this person?";
                BasicDialog dialog = new BasicDialog().newInstance(ms);
                dialog.setActivity("BLOCKDM");
                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ms = "Do you still want to delete this chat?";
                BasicDialog dialog = new BasicDialog().newInstance(ms);
                dialog.setActivity("DELETEDM");
                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle myBundle = new Bundle();
                myBundle.putInt("index",0);
                myBundle.putString("user_id",friendInf.getUser_id());

                Intent intentToProfile = new Intent (btn_profile.getContext(), Profile.class);
                intentToProfile.putExtras(myBundle);
                startActivity(intentToProfile);
                overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle myBundle = new Bundle();
                myBundle.putInt("index",0);
                myBundle.putString("user_id",friendInf.getUser_id());

                Intent intentToProfile = new Intent (btn_profile.getContext(), Profile.class);
                intentToProfile.putExtras(myBundle);
                startActivity(intentToProfile);
            }
        });

    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    public void setNickname(ChangeNickNameDialog current, String frNN, String meNN){
        SocketManager.getInstance();
        SocketManager.changeNickname(meInf.getUser_id(),meNN,friendInf.getUser_id(),frNN,DM_id);

        current.dismiss();
    }

    public static void waitSetNickname(){
        SocketManager.getInstance();
        if(SocketManager.getSocket() !=null){
            SocketManager.getSocket().on("waitSetNickname", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    String myNickname = (String) args[0];
                    String friendNickname = (String) args[1];

                    Log.i("Option activity", myNickname);
                    Log.i("Option activity", friendNickname);
                    meInf.setNickname(myNickname);
                    friendInf.setNickname(friendNickname);
                }
            });
        }
    }

    public void Block(BasicDialog current){
        valueBlock = (valueBlock) ? false : true;
        if (valueBlock){
            txtBlock.setText("Unblock");
            SocketManager.getInstance();
            SocketManager.blockFriend(friendInf.getUser_id(),meInf.getUser_id());
        }else{
            txtBlock.setText("Block");
            SocketManager.getInstance();
            SocketManager.unblockFriend(friendInf.getUser_id(),meInf.getUser_id());
        }
//        setResult(CHANGEBLOCK, new Intent(block.getContext(), ChattingActivity.class).putExtra("data", valueBlock));
        if(current != null) current.dismiss();
    }

    public void Delete(BasicDialog current){
        DirectMessage.removeRoom(DM_id);
        setResult(DELETEDM, new Intent(block.getContext(), ChattingActivity.class));
        current.dismiss();
        finish();
    }
}