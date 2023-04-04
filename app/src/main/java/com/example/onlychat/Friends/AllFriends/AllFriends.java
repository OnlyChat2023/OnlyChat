package com.example.onlychat.Friends.AllFriends;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.example.onlychat.GroupChat.CustomFriendCheckbox.CustomFriendCheckBox;

import com.example.onlychat.R;

public class AllFriends extends Fragment {
    ListView listFriends;

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
    String phoneNumbers[] = {
            "0973667324",
            "0937687267",
            "0776272828",
            "0973667324",
            "0937687267",
            "0776272828",
            "0973667324",
            "0937687267",
            "0776272828",
            "0973667324",
            "0937687267",
            "0776272828",
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout allFriends = (LinearLayout) inflater.inflate(R.layout.friends_fragment_all_friends, null);

        listFriends = (ListView) allFriends.findViewById(R.id.listFriends);

        listFriends.setSelection(0);
        listFriends.smoothScrollToPosition(0);
        listFriends.setDivider(null);
        listFriends.setDividerHeight(0);


        CustomFriendItem customFriendItem=new CustomFriendItem(getActivity(),avatars,names,phoneNumbers);
        listFriends.setAdapter(customFriendItem);

        listFriends.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                FriendBottomDialogFragment friendBottomDialogFragment = new FriendBottomDialogFragment();
                friendBottomDialogFragment.show(getChildFragmentManager(), friendBottomDialogFragment.getTag());
                return false;
            }
        });

        return allFriends;
    }
}