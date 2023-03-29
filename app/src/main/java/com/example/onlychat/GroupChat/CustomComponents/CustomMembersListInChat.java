package com.example.onlychat.GroupChat.CustomComponents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.R;

public class CustomMembersListInChat extends ArrayAdapter<String> {
    Context context;
    Integer[] thumbnails;
    String[] names;

    public CustomMembersListInChat(Context context, int layoutToBeInflated, String[] names, Integer[] thumbnails) {
        super(context, R.layout.member_item, names);
        this.context = context;
        this.thumbnails = thumbnails;
        this.names = names;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View row = inflater.inflate(R.layout.member_item, null);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView name = (TextView) row.findViewById(R.id.name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView icon = (ImageView) row.findViewById(R.id.avatar);
//        System.out.println(contents[position]);
        name.setText(names[position]);
        icon.setImageResource(thumbnails[position]);
        return (row);
    }
}
