package com.example.onlychat.GlobalChat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlychat.Adapter.ImageChat;
import com.example.onlychat.Adapter.SeenUser;
import com.example.onlychat.Async.DownloadImage;
import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.ImageModel;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class CustomChatItem extends ArrayAdapter<RoomModel> {
    Context context;
    ImageView messageAvatar;
    TextView messageName;
    TextView messageContent;
    TextView messageTime;
    ShapeableImageView seenAvt;
    ArrayList<RoomModel> listRooms;
    RecyclerView seenUser;

    public CustomChatItem(Context context, ArrayList<RoomModel> listRooms){
        super(context,R.layout.global_chat_custom_chat_item,listRooms);
        this.context = context;
        this.listRooms = listRooms;
    }
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        row = inflater.inflate(R.layout.global_chat_custom_chat_item,null);
        messageName = (TextView) row.findViewById(R.id.messageName);
        messageContent = (TextView) row.findViewById(R.id.messageContent);
        messageTime = (TextView) row.findViewById(R.id.messageTime);

        RoomModel roomModel = listRooms.get(position);
//        if(!roomModel.getShow()) row.setVisibility(View.GONE);

//        new HttpManager.GetImageFromServer(messageAvatar).execute(listRooms.get(position).getAvatar());
        if (roomModel.hasSeenUser()) {
            seenUser = (RecyclerView)row.findViewById(R.id.seenUser);
//            seenAvt = (ShapeableImageView) row.findViewById(R.id.seenAvatar);
            seenUser.setItemAnimator(null);

            int num_col = Math.max(1, roomModel.getSeenUser().size());

            seenUser.setLayoutManager(new GridLayoutManager(context, num_col) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
//                new LoadImage(imageLayout).execute(messageItem.getTempImages());
            SeenUser myImageChat = new SeenUser(roomModel.getSeenUser());
            seenUser.setAdapter(myImageChat);
//            seenAvt.setImageBitmap(roomModel.getSeenUser());
        }
        else {
            GlobalPreferenceManager pref = new GlobalPreferenceManager(context);
            UserModel myInfo = pref.getUserModel();
            if (listRooms.get(position).getMessages().size() > 0) {
                if (listRooms.get(position).getMessages().get(listRooms.get(position).getMessages().size() - 1).getUserId().equals(myInfo.getId())) {
                    seenAvt = (ShapeableImageView) row.findViewById(R.id.seenAvatar);
                    seenAvt.setImageResource(R.drawable.alsend_icon);
                }
                else {
                    seenUser = (RecyclerView)row.findViewById(R.id.seenUser);
    //            seenAvt = (ShapeableImageView) row.findViewById(R.id.seenAvatar);
                    seenUser.setItemAnimator(null);
    
                    int num_col = 1;
    
                    seenUser.setLayoutManager(new GridLayoutManager(context, num_col) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    });
    //                new LoadImage(imageLayout).execute(messageItem.getTempImages());
                    SeenUser myImageChat = new SeenUser(new ArrayList<>());
                    seenUser.setAdapter(myImageChat);
                }
            }
        }

        if (roomModel.hasBitmapAvatar()) {
            messageAvatar = (ImageView) row.findViewById(R.id.messageAvatar);
            messageAvatar.setImageBitmap(roomModel.getBitmapAvatar());
        }
        else if (roomModel.hasAvatar()){
            ArrayList<String> userAvt = new ArrayList<String>();
            userAvt.add(roomModel.getAvatar());
            new DownloadImage(userAvt, new ConvertListener() {
                @Override
                public void onSuccess(ImageModel result) {

                }

                @Override
                public void onDownloadSuccess(ArrayList<Bitmap> result) {
                    for (int i = 0; i < result.size(); ++i) {
                        roomModel.setBitmapAvatar(result.get(i));
                        messageAvatar = (ImageView) row.findViewById(R.id.messageAvatar);
                        messageAvatar.setImageBitmap(result.get(i));
                    }
                }
            }).execute();
        }


        messageName.setText(listRooms.get(position).getName());
        if (listRooms.get(position).getMessages().size() != 0) {
            MessageModel lastMessage = listRooms.get(position).getMessages().get(listRooms.get(position).getMessages().size() - 1);

            String nickname = lastMessage.getNickName();
            if (nickname != null && nickname.length() > 0)
                nickname += ": ";
            else
                nickname = "";

            if (lastMessage.getMessage().isEmpty()) {
                messageContent.setText(nickname + "Đã gửi hình ảnh");
            }
            else
                messageContent.setText(nickname + lastMessage.getMessage());

            SimpleDateFormat writeDate = new SimpleDateFormat("HH:mm");
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
            String s = writeDate.format(lastMessage.getTime());

            messageTime.setText(s);

            if (lastMessage.getId().equals("tmp")) {
                listRooms.get(position).getMessages().remove(lastMessage);
            }

//            if(lastMessage.getTime().getMinutes()<10){
//                messageTime.setText(lastMessage.getTime().getHours()+":0"+lastMessage.getTime().getMinutes());
//            }
//            else{
//                messageTime.setText(lastMessage.getTime().getHours()+":"+lastMessage.getTime().getMinutes());
//            }
        }
        else{
            messageContent.setText("");

            SimpleDateFormat writeDate = new SimpleDateFormat("HH:mm");
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
            String s = writeDate.format(listRooms.get(position).getUpdate_time());

            messageTime.setText(s);
        }
        return row;
    }
}
