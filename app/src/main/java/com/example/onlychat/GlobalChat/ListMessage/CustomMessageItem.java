package com.example.onlychat.GlobalChat.ListMessage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.onlychat.R;

public class CustomMessageItem extends ArrayAdapter<String> {
    Context context;Integer[] avatars;String names[];Object messages[];

    TextView name;
    TextView message;
    ImageView imageView;
    ImageView chatImage;
    public CustomMessageItem(Context context,Integer[] avatars,String names[],Object messages[]){
        super(context,R.layout.global_chat_custom_chat_item,names);
        this.context= context;
        this.avatars=avatars;
        this.names=names;
        this.messages=messages;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row ;
        LayoutInflater inflater=((Activity) context).getLayoutInflater();
        if(names[position].equals("me")){
            row = inflater.inflate(R.layout.global_chat_custom_message_item_me,null);
            message = (TextView)row.findViewById(R.id.message);
            chatImage = (ImageView) row.findViewById(R.id.chatImage);
            if(messages[position].getClass().getName().equals("java.lang.String")){
                message.setText(messages[position].toString());
            }
            else{
                chatImage.setImageResource(Integer.parseInt(messages[position].toString()));
            }
        }else{
            row = inflater.inflate(R.layout.global_chat_custom_message_item,null);
            message = (TextView) row.findViewById(R.id.chatContent);
            name = (TextView) row.findViewById(R.id.name);
            imageView = (ImageView) row.findViewById(R.id.avatar);
            message.setText(messages[position].toString());
            name.setText(names[position]);
            imageView.setImageResource(avatars[position]);
        }
//        if(position== names.length-1) row.setPadding(0,0,0,120);
        return row;
    }
}
