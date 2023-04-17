package com.example.onlychat.Friends.Invite;

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
import com.example.onlychat.R;

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