package com.example.onlychat.Friends.AllFriends;

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
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;

import java.util.ArrayList;

public class CustomFriendItem extends ArrayAdapter<UserModel> {
    Context context;
    ImageView avatar;
    TextView name;
    TextView phoneNumber;
    ArrayList<UserModel> listFriend;

    public CustomFriendItem(Context context, ArrayList<UserModel> list_friend){
        super(context, R.layout.friends_friend_item,list_friend);
        this.context = context;
        listFriend = list_friend;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.friends_friend_item,null);
        avatar = (ImageView) row.findViewById(R.id.avatar);
        name = (TextView) row.findViewById(R.id.name);
        phoneNumber = (TextView) row.findViewById(R.id.phoneNumber);

        new HttpManager.GetImageFromServer(avatar).execute(listFriend.get(position).getAvatar());
//        Log.i("custom friend item", listFriend.get(position).getName());
        name.setText(listFriend.get(position).getName());
        phoneNumber.setText(listFriend.get(position).getPhone());

        return row;
    }
}
