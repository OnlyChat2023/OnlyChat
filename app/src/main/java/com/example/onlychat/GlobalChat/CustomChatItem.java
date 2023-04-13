package com.example.onlychat.GlobalChat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.Manager.Model.MessageModel;
import com.example.onlychat.Manager.Model.RoomModel;
import com.example.onlychat.R;

import java.util.ArrayList;

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
    ArrayList<RoomModel> listRooms;



    public CustomChatItem(Context context, ArrayList<RoomModel> listRooms){

        super(context,R.layout.global_chat_custom_chat_item,new String[listRooms.size()]);
        this.context = context;
        this.listRooms = listRooms;
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

        messageAvatar.setImageResource(listRooms.get(position).getAvatar());
        messageName.setText(listRooms.get(position).getName());
        MessageModel lastMessage = listRooms.get(position).getMessages().get(listRooms.get(position).getMessages().size()-1);
        messageContent.setText(lastMessage.getMessage());
        messageTime.setText(lastMessage.getTime().getHours()+":"+lastMessage.getTime().getMinutes());

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
