package com.example.onlychat.DirectMessage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.onlychat.Async.ConvertImage;
import com.example.onlychat.Async.DownloadImage;
import com.example.onlychat.DiaLog.DMBottomDialog;
import com.example.onlychat.DirectMessage.Option.OptionActivity;
import com.example.onlychat.GlobalChat.ListMessage.CustomMessageItem;
import com.example.onlychat.GlobalChat.ListMessage.ListMessage;
import com.example.onlychat.GroupChat.ListMessage.MainAdp;
import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.MessageListener;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.ImageModel;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;
import com.vanniktech.emoji.EmojiPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ChattingActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    ImageView imgAvatar, btnFile, btnImage, btnIcon, btnSend;
    String me_id;
    View gap;
    Button btnBack, btnSetting;
    TextView txtName, txtOnline;
    EditText chatMessage;
    ListView chatContent;
    RelativeLayout chatLayout;
    RoomModel userInf;
    GlobalPreferenceManager pref;
    UserModel myInfo;
    MessageReceive adapter;
    ArrayList<Uri> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    MainAdp mainAdapter;
    int position;
    boolean change = false;
    ImageModel myModel;
    RelativeLayout blockLayout;
    TextView txtBlockLayout;
    int OPTION = 1;
    Integer CHANGENOTIFY = -7;
    Integer CHANGEBLOCK = -8;
    Integer CHANGEFRNN = 5;
    Integer CHANGEMENN = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        setContentView(R.layout.global_chat_list_message);
        btnBack = (Button) findViewById(R.id.backButton);
        btnSetting = (Button) findViewById(R.id.optionButton);
        imgAvatar = (ImageView) findViewById(R.id.avatar);
        txtName = (TextView) findViewById(R.id.textName);
        txtOnline = (TextView) findViewById(R.id.textSubName);
        blockLayout = (RelativeLayout) findViewById(R.id.block_relative);
        txtBlockLayout = (TextView) findViewById(R.id.block_text_layout);

        chatContent = (ListView) findViewById(R.id.listMessages);

        btnFile = (ImageView) findViewById(R.id.encloseIcon);
        btnImage = (ImageView) findViewById(R.id.imageIcon);
        btnIcon = (ImageView) findViewById(R.id.iconIcon);
        gap = (View) findViewById(R.id.gap);
        btnSend = (ImageView) findViewById(R.id.sendIcon);
        chatMessage = (EditText) findViewById(R.id.chatText);
        chatLayout = (RelativeLayout) findViewById(R.id.chatLayout);

        Intent main_chat = getIntent();
        userInf = (RoomModel) main_chat.getSerializableExtra("roomChat");

        pref = new GlobalPreferenceManager(this);
        myInfo = pref.getUserModel();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mainAdapter = new MainAdp(arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setAdapter(mainAdapter);

//        Log.i("CHATTING", userInf.getOptions().getMembers().get(0).getName());
//        imgAvatar.setImageResource(userInf.getAvatar());
        new HttpManager.GetImageFromServer(imgAvatar).execute(userInf.getAvatar());
        me_id = userInf.getOptions().getUser_id();
        txtName.setText(userInf.getName());
        txtOnline.setText("Online");
        txtOnline.setTextColor(getResources().getColor(R.color.online_green));

        //Set block
        if (userInf.getOptions().getBlock()){
            chatLayout.setVisibility(View.INVISIBLE);
            blockLayout.setVisibility(View.VISIBLE);
            txtBlockLayout.setText("Your chatting feature is blocked");
        }else{
            Member friendssss = new Member("", "", "", "");
            for (Member mem : userInf.getOptions().getMembers()){
                if (!mem.getUser_id().equals(me_id)){
                    friendssss = mem;
                    break;
                }
            }

            new HttpManager(blockLayout.getContext()).getBlockDM(friendssss.getUser_id(), userInf.getId(), new HttpResponse() {
                @Override
                public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                    if ((Boolean) response.getBoolean("data")) {
                        chatLayout.setVisibility(View.INVISIBLE);
                        blockLayout.setVisibility(View.VISIBLE);
                        txtBlockLayout.setText("You blocked chatting feature");
                        txtBlockLayout.setTextColor(getResources().getColor(R.color.online_green));
                    }
                }
                @Override
                public void onError(String error) {
                    Log.i("Show block Layout", error);
                }
            });
        }

        // adapter = new MessageReceive(this, userInf.getAvatar(), me_id, userInf.getMessages());
        adapter = new MessageReceive(this, me_id, userInf);

        loadAvatar();

        chatContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView timeMsg = (TextView) v.findViewById(R.id.timeMessage);

                if (timeMsg.getVisibility() == View.GONE) {
                    timeMsg.setVisibility(View.VISIBLE);
                }
                else {
                    timeMsg.setVisibility(View.GONE);
                }
            }
        });

        chatContent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DMBottomDialog dialog = new DMBottomDialog().newInstance(i);
                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());
                return true;
            }
        });

        chatContent.setScrollingCacheEnabled(false);
        chatContent.setAdapter(adapter);
        chatContent.setSelection(adapter.getCount() - 1);

        chatContent.setMotionEventSplittingEnabled(true);
        chatContent.smoothScrollToPosition(adapter.getCount() - 1);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fixed,R.anim.left_to_right);
            }
        });

        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                requestPermission();
                String[] strings = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                if(EasyPermissions.hasPermissions(ChattingActivity.this, strings)) {
                    imagePicker();
                }
                else {
                    EasyPermissions.requestPermissions(
                            ChattingActivity.this,
                            "App needs to access your camera & storage",
                            100,
                            strings);
                }
            }
        });

        // EMOJI
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(
                findViewById(R.id.root_view)
        ).build(chatMessage);

        btnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    emojiPopup.toggle();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                final String chatTXT = chatMessage.getText().toString();

                if (!TextUtils.isEmpty(chatTXT)) {
                    MessageModel newMessageModel = new MessageModel("", myInfo.getId(), myInfo.getAvatar(), myInfo.getName(), myInfo.getName(), chatTXT, new Date(), new ArrayList<String>());

                    userInf.pushMessage(newMessageModel);
                    adapter.notifyDataSetChanged();

                    SocketManager.sendMessage(chatTXT, userInf.getMessages().size() - 1, myInfo);
                    chatMessage.setText("");
                }

                if (myModel != null && myModel.getImagesBM() != null && !arrayList.isEmpty()) {
                    MessageModel newMessageModel = new MessageModel("", myInfo.getId(), myInfo.getAvatar(), myInfo.getName(), myInfo.getName(), new ArrayList<>(myModel.getImagesBM()), new Date(), new ArrayList<String>());

                    userInf.getMessages().add(newMessageModel);
                    adapter.notifyDataSetChanged();

                    SocketManager.sendImageMessage(ChattingActivity.this, myModel.getImagesListStr(), userInf.getMessages().size() - 1, myInfo);

                    arrayList.clear();
                    mainAdapter.notifyDataSetChanged();

                    myModel = null;
                }
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChattingActivity.this, OptionActivity.class);
                for (Member mem : userInf.getOptions().getMembers()){

                    if (!mem.getUser_id().equals(me_id)){
                        intent.putExtra("friend", mem);
                    }
                    else{
                        intent.putExtra("me", mem);
                    }
                }
                intent.putExtra("DM_id", userInf.getId());
                intent.putExtra("option", userInf.getOptions());
                startActivityForResult(intent, OPTION);
                overridePendingTransition(R.anim.right_to_left, R.anim.fixed);
            }
        });

        final boolean[] state = {true};


        chatLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                chatLayout.getWindowVisibleDisplayFrame(r);

                int heightDiff = chatLayout.getRootView().getHeight() - r.height();
                if (state[0] && heightDiff > 0.25*chatLayout.getRootView().getHeight()) {
                    chatContent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            chatContent.setSelection(adapter.getCount() - 1);
                        }
                    },150);

                    chatContent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnFile.animate().translationX(-220).setDuration(120);
                            btnImage.animate().translationX(-220).setDuration(120);
                            btnIcon.animate().translationX(-220).setDuration(120);
                            gap.animate().translationX(-220).setDuration(120);
                            chatMessage.animate().translationX(-220).setDuration(120);
                            chatMessage.setPadding(0,0,0,0);
                        }
                    }, 160);
                    state[0] = false;
                }
                else if (!state[0] && heightDiff <= 0.25*chatLayout.getRootView().getHeight()) {
                    state[0] = true;
                    chatContent.post(new Runnable() {
                        @Override
                        public void run() {
                            btnFile.animate().translationX(-0).setDuration(100);
                            btnImage.animate().translationX(-0).setDuration(100);
                            btnIcon.animate().translationX(-0).setDuration(100);
                            gap.animate().translationX(-0).setDuration(100);
                            chatMessage.animate().translationX(-0).setDuration(100);
                            chatMessage.setPadding(0,0,230,0);
                        }
                    });
                }
            }
        });

        initSocket();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        for (Member mem : this.userInf.getOptions().getMembers()) {
//            if (!mem.getId().equals(me_id)) {
//                txtName.setText(mem.getNickname());
//                break;
//            }
//        }
    }

    public void setNickname(String frNN, String meNN) {
        for(Member mem : this.userInf.getOptions().getMembers()){
            if (mem.getUser_id().equals(me_id)){
                mem.setNickname(meNN);
            } else{
                mem.setNickname(frNN);
            }
        }
    }

    private void imagePicker(){
        System.out.println("RUN HERE");
        FilePickerBuilder.getInstance()
                .setActivityTitle("Select Images")
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN, 3)
                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN, 4)
                .setMaxCount(4)
                .setSelectedFiles(arrayList)
                .pickPhoto(ChattingActivity.this);
        System.out.println();
//        .setActivityTheme(Integer.parseInt("FFFFFF"))
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("<<<<<<<<REQUEST CODE>>>>>>>>>>>>>>>", Integer.toString(resultCode));
        if (resultCode == CHANGENOTIFY && data != null){
            Boolean temp = (Boolean) data.getSerializableExtra("data");
            userInf.getOptions().setNotify(temp);
        }
        if (resultCode == CHANGEBLOCK  && data != null){
            Boolean temp = (Boolean) data.getSerializableExtra("data");
            //Show layout block chat.
            if (temp){
                chatLayout.setVisibility(View.INVISIBLE);
                blockLayout.setVisibility(View.VISIBLE);
                txtBlockLayout.setText("You blocked chatting feature");
                txtBlockLayout.setTextColor(getResources().getColor(R.color.online_green));
            } else {
                chatLayout.setVisibility(View.VISIBLE);
                blockLayout.setVisibility(View.GONE);
            }
        }
        if (resultCode == CHANGEFRNN && data != null){
            String nn = (String) data.getSerializableExtra("data");
            for (Member mem : userInf.getOptions().getMembers()){
                if (!mem.getUser_id().equals(me_id)){
                    mem.setNickname(nn);
                    userInf.setName(nn);
                    txtName.setText(nn);
                    break;
                }
            }
        }

        if (resultCode == CHANGEMENN && data != null){
            String nn = (String) data.getSerializableExtra("data");
            for (Member mem : userInf.getOptions().getMembers()){
                if (mem.getUser_id().equals(me_id)){
                    mem.setNickname(nn);
                    break;
                }
            }
        }
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
                ArrayList<Uri> images = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                arrayList.clear();
                arrayList.addAll(images);
                Collections.reverse(arrayList);

                mainAdapter.notifyDataSetChanged();

                AsyncTask<String, Void, ImageModel> image_convert = new ConvertImage(ChattingActivity.this, arrayList, new ConvertListener() {
                    @Override
                    public void onSuccess(ImageModel result) {
                        ArrayList<String> imageStr = new ArrayList<String>(result.getImagesListStr());
                        ArrayList<String> newImageStr = new ArrayList<String>();

                        for (int i = 0; i < imageStr.size(); i++) {
                            newImageStr.add("data:image/png;base64," + imageStr.get(i));
                        }
                        myModel = new ImageModel(result.getImagesBM(), newImageStr);
                    }

                    @Override
                    public void onDownloadSuccess(ArrayList<Bitmap> result) {

                    }
                }).execute();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == 100 && perms.size() == 2) {
            imagePicker();
        }
//        imagePicker();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }else {
            Toast.makeText(getApplicationContext(),
                    "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateMessage() {
        ArrayList<MessageModel> currentListMessage = userInf.getMessages();
        String idLastMessage = (currentListMessage.size() > 0)
                ? currentListMessage.get(currentListMessage.size() - 1).getId()
                : "";
        SocketManager.notifyUpdateMessage(idLastMessage);
    }

    public void initSocket() {
        SocketManager.getInstance();
        SocketManager.joinRoom(userInf.getId() + "::" + "direct_message", myInfo);

        SocketManager.waitMessage(new MessageListener() {
            @Override
            public void onMessage(MessageModel message, int position) {
                chatContent.post(new Runnable() {
                    @Override
                    public void run() {

                        if (position != -2) {
                            if (message.getUserId().equals(myInfo.getId())) {
                                userInf.getMessages().get(position).setId(message.getId());
                                userInf.getMessages().get(position).setTime(message.getTime());
                            } else {
                                userInf.pushMessage(message);
                                adapter.notifyDataSetChanged();

                                chatContent.setSelection(adapter.getCount() - 1);
                                chatContent.smoothScrollToPosition(adapter.getCount() - 1);

                            }
                            change = true;
                        }
                        else {
                            userInf.pushMessage(message);
                            adapter.notifyDataSetChanged();

                            chatContent.setSelection(adapter.getCount() - 1);
                            chatContent.smoothScrollToPosition(adapter.getCount() - 1);
                        }
                    }
                });
            }
        });

        updateMessage();
    }

    private void loadAvatar() {
        if (!userInf.hasBitmapAvatar()) {
            if (userInf.hasAvatar()) {
                ArrayList<String> userAvt = new ArrayList<String>();
                userAvt.add(userInf.getAvatar());

                new DownloadImage(userAvt, new ConvertListener() {
                    @Override
                    public void onSuccess(ImageModel result) {

                    }

                    @Override
                    public void onDownloadSuccess(ArrayList<Bitmap> result) {
                        if (!userInf.hasBitmapAvatar()) {
                            for (int i = 0; i < result.size(); ++i) {
                                userInf.setBitmapAvatar(result.get(i));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }).execute();
            }
        }
    }

    @Override
    public void finish() {
        Intent output = new Intent();

        output.putExtra("RoomModelID", userInf.getId());
        output.putExtra("Change", change);

        ArrayList<MessageModel> mess = userInf.getMessages();
        if (mess.size() - 1 >= 0) {

            MessageModel lastMessage = mess.get(mess.size() - 1);

            String message = "";
            message += (lastMessage.hasImagesStr()) ? "Đã gửi hình ảnh" : lastMessage.getMessage();

            output.putExtra("LastMessage", message);
            output.putExtra("LastTime", lastMessage.getTime());
        }
        output.putExtra("Update", true);

        setResult(RESULT_OK, output);

        SocketManager.leaveRoom();
        super.finish();
    }
}