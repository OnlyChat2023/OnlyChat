package com.example.onlychat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageReceive extends ArrayAdapter<String> {
    Context context; Integer avatar; String[] msgs; String[] types;
    public MessageReceive(Context context,Integer avatar, String[] msgs, String[] types) {
        super(context, R.layout.main_chat_content_item, msgs);
        this.avatar = avatar;
        this.msgs = msgs;
        this.types = types;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (types[position] == "SEND") {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View row = inflater.inflate(R.layout.chat_message_send, null);
            TextView msg = (TextView) row.findViewById(R.id.chat_message_send_content);
            msg.setText(msgs[position]);
            return row;
        } else {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View row = inflater.inflate(R.layout.chat_message_receive, null);
            TextView msg = (TextView) row.findViewById(R.id.chat_message_receive_content);
            ImageView avt = (ImageView) row.findViewById(R.id.chat_message_avatar);
            avt.setImageResource(avatar);
            msg.setText(msgs[position]);
            return row;
        }
    }
}
