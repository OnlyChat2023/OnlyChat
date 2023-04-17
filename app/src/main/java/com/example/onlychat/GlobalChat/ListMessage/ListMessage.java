package com.example.onlychat.GlobalChat.ListMessage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlychat.Async.ConvertImage;
import com.example.onlychat.GlobalChat.GlobalChat;
import com.example.onlychat.GlobalChat.ListMessage.Options.Options;
import com.example.onlychat.GroupChat.ListMessage.MainAdp;
import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.MessageListener;
import com.example.onlychat.Interfaces.RoomOptions;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.ImageModel;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.GroupChat.MessageBottomDialogFragmentChatting;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;
import com.vanniktech.emoji.EmojiPopup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ListMessage extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    ListView listView;
    RelativeLayout chatLayout;
    ImageView enclose;
    ImageView image;
    ImageView icon;
    ImageView sendBtn;
    View gap;
    EditText chatText;
    Button optionButton;
    Button backButton;
    ImageView chatImage;
    TextView chatName;
    TextView memberNumber;
    GlobalPreferenceManager pref;
    UserModel myInfo;
    CustomMessageItem customMessageItem;
    ArrayList<Uri> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    MainAdp mainAdapter;
    RoomModel roomModel;
    int position;
    boolean update = false;
    ImageModel myModel;
    int FINISH = -5;
    int UPDATEOPTION = -6;
    int ADDMEMBER = -7;
    String channel;
    boolean notifyUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_chat_list_message);

        Intent intent = getIntent();
        roomModel = (RoomModel) intent.getSerializableExtra("Data");
        channel = intent.getStringExtra("channel");
//        position = (int) intent.getSerializableExtra("Position");

        pref = new GlobalPreferenceManager(this);
        myInfo = pref.getUserModel();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mainAdapter = new MainAdp(arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setAdapter(mainAdapter);

        listView=(ListView) findViewById(R.id.listMessages);
        customMessageItem = new CustomMessageItem(this, roomModel.getMessages());
        listView.setScrollingCacheEnabled(false);
        listView.setAdapter(customMessageItem);
        listView.setSelection(customMessageItem.getCount() - 1);
        listView.smoothScrollToPosition(customMessageItem.getCount() - 1);
        listView.setDivider(null);
        listView.setDividerHeight(0);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageBottomDialogFragmentChatting messageBottomDialogFragmentChatting = new MessageBottomDialogFragmentChatting();
                messageBottomDialogFragmentChatting.show(getSupportFragmentManager(), messageBottomDialogFragmentChatting.getTag());

                return true;
            }
        });

        chatLayout = (RelativeLayout) findViewById(R.id.chatLayout);
        enclose = (ImageView) findViewById(R.id.encloseIcon);
        image = (ImageView) findViewById(R.id.imageIcon);
        icon =(ImageView) findViewById(R.id.iconIcon);
        sendBtn = (ImageView) findViewById(R.id.sendIcon);
        gap =(View) findViewById(R.id.gap);
        chatText = (EditText) findViewById(R.id.chatText);
        optionButton = (Button) findViewById(R.id.optionButton);
        backButton = (Button) findViewById(R.id.backButton);
        chatImage = (ImageView) findViewById(R.id.avatar);
        chatName = (TextView) findViewById(R.id.textName);
        memberNumber = (TextView) findViewById(R.id.textSubName);
        // set image
        new HttpManager.GetImageFromServer(chatImage).execute(roomModel.getAvatar());

        chatName.setText(roomModel.getName());
        memberNumber.setText(Integer.toString(roomModel.getOptions().getMembers().size()) + " members");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fixed,R.anim.left_to_right);
            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(optionButton.getContext(), Options.class);
                intent.putExtra("Name",roomModel.getName());
                intent.putExtra("GroupID", roomModel.getId());
                intent.putExtra("Avatar",roomModel.getAvatar());
                intent.putExtra("Data",roomModel.getOptions());
                startActivityForResult(intent, 1);
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
                    listView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(customMessageItem.getCount() - 1);
                        }
                    },150);

                    listView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enclose.animate().translationX(-200).setDuration(120);
                            image.animate().translationX(-200).setDuration(120);
                            icon.animate().translationX(-200).setDuration(120);
                            gap.animate().translationX(-200).setDuration(120);
                            chatText.animate().translationX(-200).setDuration(120);
                            chatText.setPadding(0,0,0,0);
                        }
                    }, 160);
                    state[0] = false;
                }
                else if (!state[0] && heightDiff <= 0.25*chatLayout.getRootView().getHeight()) {
                    state[0] = true;
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            enclose.animate().translationX(-0).setDuration(100);
                            image.animate().translationX(-0).setDuration(100);
                            icon.animate().translationX(-0).setDuration(100);
                            gap.animate().translationX(-0).setDuration(100);
                            chatText.animate().translationX(-0).setDuration(100);
                            chatText.setPadding(0,0,230,0);
                        }
                    });
                }
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                final String chatTXT = chatText.getText().toString();

                if (!TextUtils.isEmpty(chatTXT)) {
                    MessageModel newMessageModel = new MessageModel("", myInfo.getId(), myInfo.getAvatar(), myInfo.getName(), myInfo.getName(), chatTXT, new Date(), new ArrayList<String>());

                    roomModel.pushMessage(newMessageModel);
                    customMessageItem.notifyDataSetChanged();

                    SocketManager.sendMessage(chatTXT, roomModel.getMessages().size() - 1, myInfo);
                    chatText.setText("");
                }

                if (myModel != null && myModel.getImagesBM() != null) {
                    MessageModel newMessageModel = new MessageModel("", myInfo.getId(), myInfo.getAvatar(), myInfo.getName(), myInfo.getName(), new ArrayList<>(myModel.getImagesBM()), new Date(), new ArrayList<String>());

                    roomModel.getMessages().add(newMessageModel);
                    customMessageItem.notifyDataSetChanged();

                    SocketManager.sendImageMessage(ListMessage.this, new ArrayList<>(myModel.getImagesListStr()), roomModel.getMessages().size() - 1, myInfo);

                    arrayList.clear();
                    mainAdapter.notifyDataSetChanged();

                    myModel = null;
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                requestPermission();
                String[] strings = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(ListMessage.this, strings)) {
                    imagePicker();
                }
                else {
                    EasyPermissions.requestPermissions(
                            ListMessage.this,
                            "App needs to access your camera & storage",
                            100,
                            strings);
                }
            }
        });

        EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.root_view)).build(chatText);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiPopup.toggle();
            }
        });

        initSocket();
    }

    private void imagePicker(){
        FilePickerBuilder.getInstance()
                .setActivityTitle("Select Images")
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN, 3)
                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN, 4)
                .setMaxCount(4)
                .setSelectedFiles(arrayList)
                .pickPhoto(ListMessage.this);
//                .setActivityTheme(Integer.parseInt("#FFFFFF"))
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == ADDMEMBER && data != null){
            Member newMem = (Member) data.getSerializableExtra("data");
            if (newMem != null){
                roomModel.getOptions().getMembers().add(newMem);
                memberNumber.setText(Integer.toString(roomModel.getOptions().getMembers().size()) + " members");
            }
            else
                Log.i("<<ADD Member>>:", "fail");
        }

        if (resultCode == UPDATEOPTION && data != null){
            RoomOptions newOption = (RoomOptions) data.getSerializableExtra("data");
            if (newOption != null) {
                roomModel.setOptions(newOption);
            }
            else
                Log.i("<<CHANGE OPTION>>:", "fail");
        }
        if (resultCode == FINISH){
            finish();
        }
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
                ArrayList<Uri> images = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                arrayList.clear();
                arrayList.addAll(images);
                Collections.reverse(arrayList);

                mainAdapter.notifyDataSetChanged();

                AsyncTask<String, Void, ImageModel> image_convert = new ConvertImage(ListMessage.this, arrayList, new ConvertListener() {
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

    public void initSocket() {
        SocketManager.getInstance();
        SocketManager.joinRoom(roomModel.getId() + "::" + channel, myInfo);

        SocketManager.waitMessage(new MessageListener() {
            @Override
            public void onMessage(MessageModel message, int position) {
                listView.post(new Runnable() {
                    @Override
                    public void run() {

                        if (position != -2) {
                            if (message.getUserId().equals(myInfo.getId())) {
                                roomModel.getMessages().get(position).setId(message.getId());
                                roomModel.getMessages().get(position).setTime(message.getTime());
                            } else {
                                roomModel.pushMessage(message);
                                customMessageItem.notifyDataSetChanged();
                            }
                            update = true;
                        }
                        else {
                            roomModel.pushMessage(message);
                            customMessageItem.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        updateMessage();
    }

    public void updateMessage() {
        ArrayList<MessageModel> currentListMessage = roomModel.getMessages();
        String idLastMessage = (currentListMessage.size() > 0)
                ? currentListMessage.get(currentListMessage.size() - 1).getId()
                : "";
        SocketManager.notifyUpdateMessage(idLastMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
//        Intent output = new Intent();
//
//        output.putExtra("Position", position);
//        output.putExtra("RoomModel", roomModel);
//        output.putExtra("Update", update);
//
//        setResult(RESULT_OK, output);
        super.finish();
    }


}
