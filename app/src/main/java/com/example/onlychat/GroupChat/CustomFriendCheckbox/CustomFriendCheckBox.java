package com.example.onlychat.GroupChat.CustomFriendCheckbox;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlychat.R;

public class CustomFriendCheckBox extends ArrayAdapter<String> {
    Context context;
    Integer[] avatars;
    String[] names;
    String[] phoneNumbers;

    ImageView avatar;
    TextView name;
    TextView phoneNumber;
    CheckBox checkBox;
    public CustomFriendCheckBox(Context context, Integer[] avatars, String[] names, String[] phoneNumbers){
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
//        checkBox = (CheckBox) row.findViewById(R.id.checkbox);

//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                System.out.println("CHECKED");
//                if (checkBox.isChecked()) {
//                    checkBox.setChecked(false);
//                }
//                else {
//                    checkBox.setChecked(true);
//                }
//            }
//        });

        avatar.setImageResource(avatars[position]);
        name.setText(names[position]);
        phoneNumber.setText(phoneNumbers[position]);

//        row.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("CLICKED");
//                name.setText("???");
////                checkBox.setChecked(!checkBox.isChecked());
//            }
//        });

        return row;
    }
}
