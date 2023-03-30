package com.example.onlychat.DirectMessage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.R;

import java.util.ArrayList;

public class MessageReceive extends ArrayAdapter<String> {
    Context context; Bitmap avatar; ArrayList<String> msgs; ArrayList<String> types;
    public MessageReceive(Context context, Bitmap avatar, ArrayList<String> msgs, ArrayList<String> types) {
        super(context, R.layout.main_chat_content_item, msgs);
        this.avatar = avatar;
        this.msgs = msgs;
        this.types = types;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (types.get(position) == "SEND") {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View row = inflater.inflate(R.layout.chat_message_send, null);
            TextView msg = (TextView) row.findViewById(R.id.chat_message_send_content);
            msg.setText(msgs.get(position));
            return row;
        } else {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View row = inflater.inflate(R.layout.chat_message_receive, null);
            TextView msg = (TextView) row.findViewById(R.id.chat_message_receive_content);
            ImageView avt = (ImageView) row.findViewById(R.id.chat_message_avatar);
            avt.setImageBitmap(this.avatar);
            msg.setText(msgs.get(position));
            return row;
        }
    }

    public void AddMessage(String msg, String type){
        msgs.add(msg);
        types.add(type);
        this.notifyDataSetChanged();
    }
}
