package com.example.onlychat.GlobalChat.ListMessage.Options;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
    ArrayList<Member> memberList;
    ImageView avatar;
    TextView name;
    Boolean isAddMember = false;


    public CustomMemberItem(Context context, ArrayList<Member> members){
        super(context, R.layout.global_chat_custom_chat_item,members);
        this.context = context;
        memberList = members;
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

//        avatar.setImageResource(memberList.get(position).getAvatar());
        new HttpManager.GetImageFromServer(avatar).execute(memberList.get(position).getAvatar());
        name.setText(memberList.get(position).getNickname());

        return row;
    }

    public void setIsAddMember(Boolean addMember){
        this.isAddMember = addMember;
    }
}