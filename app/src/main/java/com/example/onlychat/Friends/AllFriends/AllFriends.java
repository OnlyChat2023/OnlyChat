package com.example.onlychat.Friends.AllFriends;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.onlychat.Friends.Friends;
import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.GlobalChat.MessageBottomDialogFragment;

import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllFriends extends Fragment {
    ListView listFriends;
    static CustomFriendItem customFriendItem;
    static ArrayList<UserModel> friend_list = new ArrayList<>();
    static FriendBottomDialogFragment friendBottomDialogFragment;

    GlobalPreferenceManager pref;
    static UserModel myInfo;

    public void setFriend_list(ArrayList<UserModel> friend_list){
        this.friend_list.clear();
        for(UserModel i:friend_list){
            this.friend_list.add(i);
        }
        customFriendItem.notifyDataSetChanged();
//        Log.i("All friends", Integer.toString(friend_list.size()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout allFriends = (LinearLayout) inflater.inflate(R.layout.friends_fragment_all_friends, null);

        Log.i("all friends", "onCreateView");


        pref = new GlobalPreferenceManager(getContext());
        myInfo = pref.getUserModel();

        listFriends = (ListView) allFriends.findViewById(R.id.listFriends);

        listFriends.setSelection(0);
        listFriends.smoothScrollToPosition(0);
        listFriends.setDivider(null);
        listFriends.setDividerHeight(0);

        HttpManager httpManager = new HttpManager(getContext());

        customFriendItem = new CustomFriendItem(getActivity(),friend_list);
        listFriends.setAdapter(customFriendItem);

        listFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle myBundle = new Bundle();
                myBundle.putInt("index",i);
                myBundle.putString("user_id",friend_list.get(i).get_id());

                Intent intentToProfile = new Intent (listFriends.getContext(), Profile.class);
                intentToProfile.putExtras(myBundle);
                startActivity(intentToProfile);
            }
        });

        listFriends.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                friendBottomDialogFragment = new FriendBottomDialogFragment();
                friendBottomDialogFragment.setI(i);
                friendBottomDialogFragment.show(getChildFragmentManager(), friendBottomDialogFragment.getTag());

                return true;
            }
        });

        return allFriends;
    }

    public static void removeFriend(String id){
        SocketManager.getInstance();
        SocketManager.deleteFriend(id,myInfo);
        for(int i=0;i<friend_list.size();i++){
            if(friend_list.get(i).get_id().equals(id))removeFriendUpdateUI(i);
        }
    }

    public static void removeFriend(int i){
        SocketManager.getInstance();
        SocketManager.deleteFriend(friend_list.get(i).get_id(),myInfo);
        removeFriendUpdateUI(i);
    }

    public static void removeFriendUpdateUI(int i){
        if(friendBottomDialogFragment != null) friendBottomDialogFragment.dismiss();
        friend_list.remove(i);
        customFriendItem.notifyDataSetChanged();
        Friends.getQuatity().setText(friend_list.size()+" available");
    }

    public static void blockFriend(int i){
        SocketManager.getInstance();
        SocketManager.blockFriend(friend_list.get(i).get_id(),myInfo);

        friendBottomDialogFragment.dismiss();
        friend_list.remove(i);
        customFriendItem.notifyDataSetChanged();
        Friends.getQuatity().setText(friend_list.size()+" available");
    }
}