package com.example.onlychat.Friends.AllFriends;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.GlobalChat.MessageBottomDialogFragment;

import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllFriends extends Fragment {
    ListView listFriends;
    CustomFriendItem customFriendItem;
    ArrayList<UserModel> friend_list = new ArrayList<>();

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
                httpManager.getUserById(friend_list.get(i).get_id(), new HttpResponse() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                        JSONObject profile = response.getJSONObject("data");
                        Log.i("all friends click item", profile.toString());

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });

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