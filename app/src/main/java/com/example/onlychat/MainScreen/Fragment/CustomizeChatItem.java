package com.example.onlychat.MainScreen.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.onlychat.R;
import com.example.onlychat.MainScreen.Fragment.MainContent;

public class CustomizeChatItem extends ArrayAdapter<String> {
    Context context; Integer[] thumbnails; String[] names; String[] phones;
    public CustomizeChatItem(Context context,Integer[] thumbnails, String[] names, String[] phones) {
        super(context, R.layout.main_chat_content_item, names);
        this.context = context;
        this.thumbnails = thumbnails;
        this.names = names;
        this.phones = phones;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.main_chat_content_item, null);
        TextView name = (TextView) row.findViewById(R.id.main_chat_item_name);
        TextView phonenum = (TextView) row.findViewById(R.id.main_chat_item_phonenum);
        ImageView avatar = (ImageView) row.findViewById(R.id.main_chat_item_avatar);

        name.setText(names[position]);
        phonenum.setText(phones[position]);
        avatar.setImageResource(thumbnails[position]);


        return row;
    }
}
