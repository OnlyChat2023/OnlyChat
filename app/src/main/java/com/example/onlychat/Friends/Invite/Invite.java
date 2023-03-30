package com.example.onlychat.Friends.Invite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.onlychat.Friends.AllFriends.CustomFriendItem;
import com.example.onlychat.R;

public class Invite extends Fragment {
    ListView listInvites;

    String names[] = {
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
            "Anonymous","Anonymous Private","Anonymous Publish",
    };
    Integer avatars[]={
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar,R.drawable.global_chat_avatar2,
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar,R.drawable.global_chat_avatar2,
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar,R.drawable.global_chat_avatar2,
            R.drawable.global_chat_avatar1,R.drawable.global_chat_avatar,R.drawable.global_chat_avatar2
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout invite = (LinearLayout) inflater.inflate(R.layout.friends_fragment_invite, null);

        listInvites = (ListView) invite.findViewById(R.id.listInvites);

        listInvites.setSelection(0);
        listInvites.smoothScrollToPosition(0);
        listInvites.setDivider(null);
        listInvites.setDividerHeight(0);

        CustomInviteItem customInviteItem=new CustomInviteItem(getActivity(),avatars,names);
        listInvites.setAdapter(customInviteItem);

        return invite;
    }
}