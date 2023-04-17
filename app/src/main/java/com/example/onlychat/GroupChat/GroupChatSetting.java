//package com.example.onlychat.GroupChat;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.app.DialogFragment;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.onlychat.DiaLog.BasicDialog;
//import com.example.onlychat.DiaLog.ChangeGroupNameDialog;
//import com.example.onlychat.DiaLog.ChangeNickNameDialog;
//import com.example.onlychat.GlobalChat.ListMessage.Options.CustomMemberItem;
//import com.example.onlychat.R;
//
//public class GroupChatSetting extends AppCompatActivity implements ChangeGroupNameDialog.OnInputListener {
//    Button addBtn;
//    ImageButton changeAvatarGroup;
//    ImageView avatar;
//    private final int GALLERY_REQ_CODE = 1000;
//    RelativeLayout share;
//    RelativeLayout members;
//    ListView listMembers;
//    ImageView backButton;
//    RelativeLayout groupNameSection;
//    TextView notificationText;
//    ImageView notificationImg;
//    Boolean onNoti = true;
//    RelativeLayout notifi, delete, block, report;
//    LinearLayout leave;
//    TextView groupName;
//    Integer avatars[] = {
//            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,R.drawable.global_chat_avatar,
//            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,R.drawable.global_chat_avatar,
//            R.drawable.global_chat_avatar,R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar2,R.drawable.global_chat_avatar
//
//    };
//    String names[] = {
//            "Adam M.Mathew","Albert Willson","Andrew McLeod", "Brittany Smith",
//            "Adam M.Mathew","Albert Willson","Andrew McLeod", "Brittany Smith",
//            "Adam M.Mathew","Albert Willson","Andrew McLeod", "Brittany Smith"
//    };
//    private static final String TAG = "MainActivity";
//    public String mInput;
//    private void setInputToTextView()
//    {
//        groupName.setText(mInput);
//    }
////    @Override
//    public void sendInput(String input)
//    {
//        Log.d(TAG, "sendInput: got the input: " + input);
//
//        mInput = input;
//
//        setInputToTextView();
//    }
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_group_chat_setting);
//
//        notifi = (RelativeLayout) findViewById(R.id.notification);
//        delete = (RelativeLayout) findViewById(R.id.delete_chat);
//        block = (RelativeLayout) findViewById(R.id.block);
////        leave = (LinearLayout) findViewById(R.id.leave);
//        report = (RelativeLayout) findViewById(R.id.report);
//        groupNameSection = (RelativeLayout) findViewById(R.id.group_name_layout);
//        groupName = (TextView) findViewById(R.id.group_name);
//
//        backButton = (ImageView) findViewById(R.id.backButton);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//                overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
//            }
//        });
//
//        share = (RelativeLayout) findViewById(R.id.share);
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//                // overlay
//                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
////                boolean focusable = true; // lets taps outside the popup also dismiss it
//                int width = LinearLayout.LayoutParams.MATCH_PARENT;
//                int height = LinearLayout.LayoutParams.MATCH_PARENT;
//                final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
//                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);
//
//                // Popup
//                View popupView = inflater.inflate(R.layout.layout_share_box, null);
//                boolean focusable = true; // lets taps outside the popup also dismiss it
//                final PopupWindow popupWindow = new PopupWindow(popupView,900,1070,focusable);
//                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        overlayWindow.dismiss();
//                    }
//                });
//            }
//        });
//
//        members = (RelativeLayout) findViewById(R.id.members);
//        members.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//                // overlay
//                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
////                boolean focusable = true; // lets taps outside the popup also dismiss it
//                int width = LinearLayout.LayoutParams.MATCH_PARENT;
//                int height = LinearLayout.LayoutParams.MATCH_PARENT;
//                final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
//                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);
//
//                // Popup
//                View popupView = inflater.inflate(R.layout.global_chat_popup_members, null);
//
//                // set list members
//                listMembers = (ListView)  popupView.findViewById(R.id.listMembers);
//                CustomMemberItem customMemberItem=new CustomMemberItem(popupView.getContext(),avatars,names);
//                listMembers.setAdapter(customMemberItem);
//                listMembers.setSelection(0);
//                listMembers.smoothScrollToPosition(0);
//                listMembers.setDivider(null);
//                listMembers.setDividerHeight(0);
//
//
//                boolean focusable = true; // lets taps outside the popup also dismiss it
//                final PopupWindow popupWindow = new PopupWindow(popupView,900,1250,focusable);
//                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        overlayWindow.dismiss();
//                    }
//                });
//            }
//        });
//
//        addBtn = (Button) findViewById(R.id.add_member_btn);
//
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent activity_add_member = new Intent(GroupChatSetting.this, AddMember.class);
//                startActivity(activity_add_member);
//            }
//        });
//
//        // Handling changing avatar of groupchat
//        avatar = (ImageView) findViewById(R.id.avatar);
//        changeAvatarGroup = (ImageButton) findViewById(R.id.up_image_btn);
//        changeAvatarGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent iGallery = new Intent(Intent.ACTION_PICK);
//                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(iGallery, GALLERY_REQ_CODE);
//            }
//        });
//
//        groupNameSection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ChangeGroupNameDialog dialog = new ChangeGroupNameDialog().newInstance("");
//                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());
//            }
//        });
//
//        notificationText = (TextView) findViewById(R.id.notificationText);
//        notificationImg = (ImageView) findViewById(R.id.notificationImg);
//
//        notifi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onNoti) {
//                    notificationImg.setImageResource(R.drawable.turn_off_notification);
//                    notificationText.setText("Turn on notification");
//                    notificationText.setTextColor(Color.parseColor("#B10000"));
//                }
//                else {
//                    notificationImg.setImageResource(R.drawable.global_chat_notification);
//                    notificationText.setText("Turn off notification");
//                    notificationText.setTextColor(Color.parseColor("#FFFFFF"));
//                }
//                onNoti = !onNoti;
//            }
//        });
//
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BasicDialog dialog = new BasicDialog().newInstance("Do you stil want to delete this message?");
//                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());
//            }
//        });
//
//        block.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BasicDialog dialog = new BasicDialog().newInstance("Do you stil want to block this message?");
//                dialog.show(getSupportFragmentManager().beginTransaction(), dialog.getTag());
//            }
//        });
//
//        backButton .setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//                overridePendingTransition(R.anim.fixed,R.anim.left_to_right);
//            }
//        });
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if(requestCode == GALLERY_REQ_CODE){
//                avatar.setImageURI(data.getData());
//            }
//        }
//    }
//
//}