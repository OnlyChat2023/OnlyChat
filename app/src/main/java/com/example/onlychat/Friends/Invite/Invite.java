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
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Invite extends Fragment {
    static ListView listInvites;

    static CustomInviteItem customInviteItem;
    static ArrayList<UserModel> invite_list = new ArrayList<>();

    public void setInvite_list(ArrayList<UserModel> invite_list){
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
                Log.i("invite list click item", "============");
                httpManager.getUserById(invite_list.get(i).get_id(), new HttpResponse() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                        JSONObject profile = response.getJSONObject("data");
                        Log.i("invite list click item", profile.toString());

                        String profileJson = profile.toString();
                        UserModel userInfo = new Gson().fromJson(profileJson, UserModel.class);
                        Boolean isFriend = profile.getBoolean("isFriend");
                        Log.i("isFriend", isFriend.toString());

                        Bundle myBundle = new Bundle();
//                        System.out.println("RUN HERE " + userInfo.getName());
                        myBundle.putString("user_id", userInfo.getId());
                        myBundle.putString("name", userInfo.getName());
                        myBundle.putString("avatar", userInfo.getAvatar());
                        myBundle.putString("nickName", userInfo.getNickName());
                        myBundle.putString("phoneNumber", userInfo.getPhone());
                        myBundle.putString("university", userInfo.getUniversity());
                        myBundle.putString("email", userInfo.getEmail());
                        myBundle.putString("description", userInfo.getDescription());
                        myBundle.putString("facebook", userInfo.getFacebook());
                        myBundle.putString("instagram", userInfo.getInstagram());
                        myBundle.putBoolean("isFriend", isFriend);

                        Intent intentToProfile = new Intent (listInvites.getContext(), Profile.class);
                        intentToProfile.putExtras(myBundle);
                        startActivity(intentToProfile);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });

        return invite;
    }

    public static void removeItem(int i){
        invite_list.remove(i);
        customInviteItem.notifyDataSetChanged();
//        listInvites.setAdapter(customInviteItem);
    }


}