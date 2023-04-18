package com.example.onlychat.Friends.Invite;

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

import com.example.onlychat.Friends.AllFriends.CustomFriendItem;
import com.example.onlychat.Friends.Friends;
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

import io.socket.emitter.Emitter;

public class Invite extends Fragment {
    static ListView listInvites;

    static CustomInviteItem customInviteItem;
    static ArrayList<UserModel> invite_list = new ArrayList<>();
    static UserModel myInfo;
    GlobalPreferenceManager pref;

    public void setInvite_list(ArrayList<UserModel> invite_list){
        this.invite_list.clear();
        for(UserModel i:invite_list){
            this.invite_list.add(i);
        }
        customInviteItem.notifyDataSetChanged();
//        Log.i("Invite", Integer.toString(invite_list.size()));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout invite = (LinearLayout) inflater.inflate(R.layout.friends_fragment_invite, null);

        pref = new GlobalPreferenceManager(getContext());
        myInfo = pref.getUserModel();

        listInvites = (ListView) invite.findViewById(R.id.listInvites);

        listInvites.setSelection(0);
        listInvites.smoothScrollToPosition(0);
        listInvites.setDivider(null);
        listInvites.setDividerHeight(0);

        HttpManager httpManager = new HttpManager(getContext());

        customInviteItem=new CustomInviteItem(getActivity(),invite_list);
        listInvites.setAdapter(customInviteItem);

        listInvites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle myBundle = new Bundle();
                myBundle.putInt("index",i);
                myBundle.putString("user_id",invite_list.get(i).get_id());

                Intent intentToProfile = new Intent (listInvites.getContext(), Profile.class);
                intentToProfile.putExtras(myBundle);
                startActivity(intentToProfile);
            }
        });

        return invite;
    }


    public static void addFriend(int i){
        SocketManager.getInstance();
        SocketManager.acceptRequestAddFriend(invite_list.get(i).get_id(),myInfo);
        removeItem(i);
    }

    public static void addFriend(String id){
        SocketManager.getInstance();
        SocketManager.acceptRequestAddFriend(id,myInfo);
        for(int i=0;i<invite_list.size();i++){
            if(invite_list.get(i).get_id().equals(id))removeItem(i);
        }
    }

    public static void removeRequest(int i){
        SocketManager.getInstance();
        SocketManager.removeRequestAddFriend(invite_list.get(i).get_id(),myInfo);
        removeItem(i);
    }

    public static void removeSent(String id){
        UserModel u = new UserModel();
        u.set_id(id);
        SocketManager.getInstance();
        SocketManager.removeRequestAddFriend(myInfo.get_id(),u);
    }

    public static void removeItem(int i){

        invite_list.remove(i);
        customInviteItem.notifyDataSetChanged();

        Friends.updateUI();
    }


}