package com.example.onlychat.GroupChat.CustomFriendCheckbox;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.R;

public class CustomeFriendCheckBox extends ArrayAdapter<String> {
    Context context;
    Integer[] avatars;
    String[] names;
    String[] phoneNumbers;

    ImageView avatar;
    TextView name;
    TextView phoneNumber;
    public CustomeFriendCheckBox(Context context,Integer[] avatars, String[] names,String[] phoneNumbers){
        super(context, R.layout.checkbox_member,names);
        this.context = context;
        this.avatars = avatars;
        this.names = names;
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.checkbox_member,null);
        avatar = (ImageView) row.findViewById(R.id.avatar);
        name = (TextView) row.findViewById(R.id.name);
        phoneNumber = (TextView) row.findViewById(R.id.phoneNumber);

        avatar.setImageResource(avatars[position]);
        name.setText(names[position]);
        phoneNumber.setText(phoneNumbers[position]);

        return row;
    }
}
