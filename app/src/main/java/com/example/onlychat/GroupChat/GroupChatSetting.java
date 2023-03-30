package com.example.onlychat.GroupChat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ListActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.onlychat.GroupChat.CustomComponents.CustomIconLabelAdapterGroupChatSetting;
import com.example.onlychat.R;

public class GroupChatSetting extends ListActivity {

    private CustomIconLabelAdapterGroupChatSetting adapter;
    String[] contents = {"Share", "Add people", "Turn off notification", "Members", "Delete chat", "Leave"};
    Integer[] thumbnails = { R.drawable.ic_share,R.drawable.ic_add_friend,R.drawable.ic_notification,R.drawable.ic_member,R.drawable.ic_trash,R.drawable.ic_leave };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_setting);

        adapter = new CustomIconLabelAdapterGroupChatSetting(this, R.layout.setting_item, contents, thumbnails);
        setListAdapter((ListAdapter) adapter);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(position == 0){
            openDialog(Gravity.CENTER, R.layout.layout_share_box);
        }
        if(position == 3){
            openDialog(Gravity.CENTER, R.layout.fragment_members);
        }
    }

    private void openDialog(int gravity, int layout) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);

        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        // Click ben ngoai tat dialog
        dialog.setCancelable(true);

        // show dialog
        dialog.show();
    }
}