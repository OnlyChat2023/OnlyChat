package com.example.onlychat.GlobalChat.ListMessage.Options;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;

import java.util.ArrayList;

public class CustomMemberItem extends ArrayAdapter<Member> {
    Context context;
    ArrayList<Member> userModels;
    Boolean isAddMember = false;


    public CustomMemberItem(Context context, ArrayList<Member> userList){
        super(context, R.layout.global_chat_custom_chat_item, userList);
        this.context = context;
        this.userModels = userList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.global_chat_custom_member_item,null);
        ImageView avatar = (ImageView) row.findViewById(R.id.avatar);
        TextView name = (TextView) row.findViewById(R.id.name);
        ImageView icon = (ImageView) row.findViewById(R.id.imageView22);
        if (isAddMember == true){
            icon.setVisibility(View.INVISIBLE);
        }

        new HttpManager.GetImageFromServer(avatar).execute(userModels.get(position).getAvatar());
        name.setText(userModels.get(position).getName());

        return row;
    }

    public void setIsAddMember(Boolean addMember){
        this.isAddMember = addMember;
    }
}