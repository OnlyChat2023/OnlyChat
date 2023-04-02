package com.example.onlychat.GlobalChat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.R;

public class CustomChatItem extends ArrayAdapter<String> {
    Context context;
    Integer[] avatars;
    String[] names;
    String[] messages;
    String[] times;

    ImageView messageAvatar;
    TextView messageName;
    TextView messageContent;
    TextView messageTime;

    public CustomChatItem(Context context,Integer[] avatars, String[] names,String[] messages,String[] times){
        super(context,R.layout.global_chat_custom_chat_item,names);
        this.context = context;
        this.avatars = avatars;
        this.names = names;
        this.messages = messages;
        this.times = times;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.global_chat_custom_chat_item,null);
        messageAvatar = (ImageView) row.findViewById(R.id.messageAvatar);
        messageName = (TextView) row.findViewById(R.id.messageName);
        messageContent = (TextView) row.findViewById(R.id.messageContent);
        messageTime = (TextView) row.findViewById(R.id.messageTime);

        messageAvatar.setImageResource(avatars[position]);
        messageName.setText(names[position]);
        messageContent.setText(messages[position]);
        messageTime.setText(times[position]);

        return row;
    }

    public void changeData(Integer[] avatars, String[] names,String[] messages,String[] times){
        this.avatars = avatars;
        this.names = names;
        this.messages = messages;
        this.times = times;
        this.notifyDataSetChanged();
    }
}
