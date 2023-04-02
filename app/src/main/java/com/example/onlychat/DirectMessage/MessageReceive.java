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
    Context context; Bitmap avatar; ArrayList<String> msgs; ArrayList<String> types; ArrayList<String> times;
    public MessageReceive(Context context, Bitmap avatar, ArrayList<String> msgs, ArrayList<String> types, ArrayList<String> times) {
        super(context, R.layout.main_chat_content_item, msgs);
        this.avatar = avatar;
        this.msgs = msgs;
        this.types = types;
        this.context = context;
        this.times = times;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (types.get(position) == "SEND") {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View row = inflater.inflate(R.layout.chat_message_send, null);
            TextView msg = (TextView) row.findViewById(R.id.messageSend);
            TextView time = (TextView) row.findViewById(R.id.timeMessage);
            msg.setText(msgs.get(position));
            time.setText(times.get(position));
            time.setVisibility(View.GONE);
            return row;
        } else {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View row = inflater.inflate(R.layout.chat_message_receive, null);
            TextView msg = (TextView) row.findViewById(R.id.chatContent);
            ImageView avt = (ImageView) row.findViewById(R.id.avatar);
            TextView time = (TextView) row.findViewById(R.id.timeMessage);
            avt.setImageBitmap(this.avatar);
            msg.setText(msgs.get(position));
            time.setText(times.get(position));
            time.setVisibility(View.GONE);
            return row;
        }
    }

    public void AddMessage(String msg, String type, String time){
        msgs.add(msg);
        types.add(type);
        times.add(time);
        this.notifyDataSetChanged();
    }
}
