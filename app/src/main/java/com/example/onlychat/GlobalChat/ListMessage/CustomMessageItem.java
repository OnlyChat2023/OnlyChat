package com.example.onlychat.GlobalChat.ListMessage;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;

import java.util.ArrayList;

public class CustomMessageItem extends ArrayAdapter<MessageModel> {

    ArrayList<MessageModel> messageModels;
    GlobalPreferenceManager pref;
    UserModel myInfo;
    Context context;
    TextView message;
    ImageView chatImage;
    TextView name;
    ImageView imageView;

    public CustomMessageItem(Context context, ArrayList<MessageModel> messageModels) {
        super(context, R.layout.global_chat_custom_chat_item, messageModels);
        this.context = context;
        this.messageModels = messageModels;

        pref = new GlobalPreferenceManager(context);
        myInfo = pref.getUserModel();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        LayoutInflater inflater=((Activity) context).getLayoutInflater();

        if (messageModels.get(position).getUserId().equals(myInfo.get_id())) {
            row = inflater.inflate(R.layout.global_chat_custom_message_item_me,null);
            message = (TextView)row.findViewById(R.id.message);
            chatImage = (ImageView) row.findViewById(R.id.chatImage);

            message.setText(messageModels.get(position).getMessage());
        }
        else {
            row = inflater.inflate(R.layout.global_chat_custom_message_item,null);
            message = (TextView) row.findViewById(R.id.chatContent);
            name = (TextView) row.findViewById(R.id.name);
            imageView = (ImageView) row.findViewById(R.id.avatar);

            message.setText(messageModels.get(position).getMessage());
            name.setText(messageModels.get(position).getNickName());
            // set image
//            Log.i("Custom message user", messageModels.get(position).getAvatar());
            new HttpManager.GetImageFromServer(imageView).execute(messageModels.get(position).getAvatar());
//            imageView.setImageResource(messageModels.get(position).getAvatar());

        }
//        if(position== names.length-1) row.setPadding(0,0,0,120);
        return row;
    }
}
