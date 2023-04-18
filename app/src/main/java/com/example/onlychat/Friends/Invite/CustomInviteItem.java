package com.example.onlychat.Friends.Invite;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.onlychat.Friends.Friends;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;

import java.util.ArrayList;

public class CustomInviteItem extends ArrayAdapter<UserModel> {

    Context context;
    ImageView avatar;
    TextView name;

    Button addFriendBtn;
    Button removeBtn;


    public Button getAddFriendBtn() {
        return addFriendBtn;
    }

    public void setAddFriendBtn(Button addFriendBtn) {
        this.addFriendBtn = addFriendBtn;
    }

    public Button getRemoveBtn() {
        return removeBtn;
    }

    public void setRemoveBtn(Button removeBtn) {
        this.removeBtn = removeBtn;
    }
    GlobalPreferenceManager pref;
    ArrayList<UserModel> listInvite;

    public CustomInviteItem(@NonNull Context context, ArrayList<UserModel> list_invite) {
        super(context, R.layout.friends_invite_item,list_invite);
        this.context=context;
        this.listInvite = list_invite;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.friends_invite_item,null);



        avatar = (ImageView) row.findViewById(R.id.avatar);
        name = (TextView) row.findViewById(R.id.name);
//        Log.i("custom invite item", listInvite.get(position).getName());

        new HttpManager.GetImageFromServer(avatar).execute(listInvite.get(position).getAvatar());
        name.setText(listInvite.get(position).getName());

//        Log.i("Custom invite item", listInvite.get(position).get_id());

        addFriendBtn = (Button) row.findViewById(R.id.add_friend_btn);
        removeBtn = (Button) row.findViewById(R.id.remove_btn);

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Invite.addFriend(position);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Invite.removeRequest(position);
            }
        });


        return row;
    }
}
