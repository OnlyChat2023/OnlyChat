package com.example.onlychat.DirectMessage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.R;

import java.util.ArrayList;
import java.util.Date;

public class MessageReceive extends ArrayAdapter<MessageModel> {
    Context context; Integer avatar; ArrayList<MessageModel> message; String me_id;
    public MessageReceive(Context context, Integer avatar, String me_id, ArrayList<MessageModel> message) {
        super(context, R.layout.main_chat_content_item, message);
        this.avatar = avatar;
        this.message = message;
        this.me_id = me_id;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.message.get(position).getUserId().equals(this.me_id)) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View row = inflater.inflate(R.layout.chat_message_send, null);
            TextView msg = (TextView) row.findViewById(R.id.messageSend);
            TextView time = (TextView) row.findViewById(R.id.timeMessage);
            msg.setText(this.message.get(position).getMessage());
            Date timeMsg = this.message.get(position).getTime();
            time.setText("Sent at " + timeMsg.getHours() + ":" + timeMsg.getMinutes() + " " + timeMsg.getDate());
            time.setVisibility(View.GONE);
            return row;
        } else {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View row = inflater.inflate(R.layout.chat_message_receive, null);
            TextView msg = (TextView) row.findViewById(R.id.chatContent);
            ImageView avt = (ImageView) row.findViewById(R.id.avatar);
            TextView time = (TextView) row.findViewById(R.id.timeMessage);
            avt.setImageResource(this.avatar);
            msg.setText(this.message.get(position).getMessage());
            Date timeMsg = this.message.get(position).getTime();
            time.setText("Sent at " + timeMsg.getHours() + ":" + timeMsg.getMinutes() + " " + timeMsg.getDate());
            time.setVisibility(View.GONE);
            return row;
        }
    }

    public void AddMessage(MessageModel msg){
        this.message.add(msg);
        this.notifyDataSetChanged();
    }
}
