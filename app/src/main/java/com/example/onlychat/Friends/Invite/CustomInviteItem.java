package com.example.onlychat.Friends.Invite;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.onlychat.R;

public class CustomInviteItem extends ArrayAdapter<String> {

    Context context;
    Integer avatars[];
    String names[];

    ImageView avatar;
    TextView name;

    public CustomInviteItem(@NonNull Context context,Integer[] avatars, String[] names) {
        super(context, R.layout.friends_invite_item,names);
        this.context=context;
        this.avatars=avatars;
        this.names= names;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.friends_invite_item,null);
        avatar = (ImageView) row.findViewById(R.id.avatar);
        name = (TextView) row.findViewById(R.id.name);

        avatar.setImageResource(avatars[position]);
        name.setText(names[position]);

        return row;
    }
}
