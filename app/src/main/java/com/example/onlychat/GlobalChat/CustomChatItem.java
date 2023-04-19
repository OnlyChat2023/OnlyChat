package com.example.onlychat.GlobalChat;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class CustomChatItem extends ArrayAdapter<RoomModel> {
    Context context;
    ImageView messageAvatar;
    TextView messageName;
    TextView messageContent;
    TextView messageTime;
    ArrayList<RoomModel> listRooms;

    public CustomChatItem(Context context, ArrayList<RoomModel> listRooms){
        super(context,R.layout.global_chat_custom_chat_item,listRooms);
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

        new HttpManager.GetImageFromServer(messageAvatar).execute(listRooms.get(position).getAvatar());

        messageName.setText(listRooms.get(position).getName());
        if (listRooms.get(position).getMessages().size() != 0) {
            MessageModel lastMessage = listRooms.get(position).getMessages().get(listRooms.get(position).getMessages().size() - 1);

            if (lastMessage.getMessage().isEmpty()) {
                messageContent.setText("Đã gửi hình ảnh");
            }
            else
                messageContent.setText(lastMessage.getMessage());

            SimpleDateFormat writeDate = new SimpleDateFormat("HH:mm");
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
            String s = writeDate.format(lastMessage.getTime());

            messageTime.setText(s);
//            if(lastMessage.getTime().getMinutes()<10){
//                messageTime.setText(lastMessage.getTime().getHours()+":0"+lastMessage.getTime().getMinutes());
//            }
//            else{
//                messageTime.setText(lastMessage.getTime().getHours()+":"+lastMessage.getTime().getMinutes());
//            }
        }
        else{
            messageContent.setText("");
 
//            if(listRooms.get(position).getUpdate_time().getMinutes()<10){
//                messageTime.setText((listRooms.get(position).getUpdate_time().getHours()+":0"+(listRooms.get(position).getUpdate_time().getMinutes())));
//            }
//            else{
//                messageTime.setText((listRooms.get(position).getUpdate_time().getHours()+":"+(listRooms.get(position).getUpdate_time().getMinutes())));
//            }
        }
        return row;
    }
}
