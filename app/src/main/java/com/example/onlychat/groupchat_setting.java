package com.example.onlychat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class groupchat_setting extends ListActivity {
    private CustomIconLabelAdapterGroupChatSetting adapter;
    String[] contents = {"Share", "Add people", "Turn off notification", "Members", "Delete chat", "Leave"};
    Integer[] thumbnails = { R.drawable.ic_share,R.drawable.ic_add_friend,R.drawable.ic_notification,R.drawable.ic_member,R.drawable.ic_trash,R.drawable.ic_leave };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat_setting);

        adapter = new CustomIconLabelAdapterGroupChatSetting(this, R.layout.setting_item, contents, thumbnails);
        setListAdapter(adapter);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(position == 0){
            openDialog(Gravity.CENTER, R.layout.layout_share_box);
        }
        if(position == 3){
            openDialog(Gravity.CENTER, R.layout.fragment_members_list_in_chat);
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

class CustomIconLabelAdapterGroupChatSetting extends ArrayAdapter<String> {
    Context context;
    Integer[] thumbnails;
    String[] contents;
    public CustomIconLabelAdapterGroupChatSetting( Context context, int layoutToBeInflated, String[] contents, Integer[] thumbnails) {
        super(context, R.layout.setting_item, contents);
        this.context = context;
        this.thumbnails = thumbnails;
        this.contents = contents;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View row = inflater.inflate(R.layout.setting_item, null);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView contentL = (TextView) row.findViewById(R.id.content);
        ImageView icon = (ImageView) row.findViewById(R.id.icon);
        contentL.setText(contents[position]);
        icon.setImageResource(thumbnails[position]);
        return (row);
    }
}