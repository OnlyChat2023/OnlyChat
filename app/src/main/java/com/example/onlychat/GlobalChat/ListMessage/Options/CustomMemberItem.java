package com.example.onlychat.GlobalChat.ListMessage.Options;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.R;

public class CustomMemberItem extends ArrayAdapter<String> {
    Context context;
    Integer[] avatars;
    String[] names;

    ImageView avatar;
    TextView name;


    public CustomMemberItem(Context context,Integer[] avatars, String[] names){
        super(context, R.layout.global_chat_custom_chat_item,names);
        this.context = context;
        this.avatars = avatars;
        this.names = names;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.global_chat_custom_member_item,null);
        avatar = (ImageView) row.findViewById(R.id.avatar);
        name = (TextView) row.findViewById(R.id.name);

        avatar.setImageResource(avatars[position]);
        name.setText(names[position]);

        return row;
    }
}