package com.example.onlychat.Friends.AllFriends;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.onlychat.Friends.Friends;
import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.GlobalChat.MessageBottomDialogFragment;

import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class AllFriends extends Fragment {
    static ListView listFriends;
    static CustomFriendItem customFriendItem;
    static ArrayList<UserModel> friend_list = new ArrayList<>();
    static FriendBottomDialogFragment friendBottomDialogFragment;
    ImageView delete;

    GlobalPreferenceManager pref;
    EditText search;
    static UserModel myInfo;

    public void setFriend_list(ArrayList<UserModel> friend_list){
        AllFriends.friend_list.clear();
        for(UserModel i:friend_list){
            AllFriends.friend_list.add(i);
        }
        customFriendItem.notifyDataSetChanged();
    }

    public static void addFriend(UserModel newFriend){
        friend_list.add(newFriend);
        customFriendItem.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout allFriends = (LinearLayout) inflater.inflate(R.layout.friends_fragment_all_friends, null);
        friend_list.clear();
        Log.i("all friends", "onCreateView");

        pref = new GlobalPreferenceManager(getContext());
        myInfo = pref.getUserModel();

        search = (EditText) allFriends.findViewById(R.id.search_bar);
        delete = (ImageView) allFriends.findViewById(R.id.delete);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>0) delete.setVisibility(View.VISIBLE);
                else delete.setVisibility(View.GONE);
                for(int i=0;i<friend_list.size();i++){
                    if(friend_list.get(i).getName().toLowerCase().contains(editable.toString().toLowerCase()) || friend_list.get(i).getPhone().contains(editable.toString())){
                        if(listFriends.getChildAt(i).getVisibility() == View.GONE) {
                            listFriends.getChildAt(i).setVisibility(View.VISIBLE);
                            listFriends.getChildAt(i).setLayoutParams(new AbsListView.LayoutParams(-1,-2));
                        }
                    }
                    else {
                        listFriends.getChildAt(i).setVisibility(View.GONE);
                        listFriends.getChildAt(i).setLayoutParams(new AbsListView.LayoutParams(-1,1));
                    }
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
            }
        });

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

        waitAcceptFriend();
        waitDeleteFriend();

        return allFriends;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void waitAcceptFriend(){
        SocketManager.getInstance();
        if(SocketManager.getSocket() !=null){
            SocketManager.getSocket().on("waitAcceptFriend", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject new_friend = (JSONObject) args[0];
                    UserModel friend = new Gson().fromJson(String.valueOf(new_friend), UserModel.class);

                    Friends.getQuatity().post(new Runnable() {
                        @Override
                        public void run() {

                            addFriend(friend);
                            Friends.getQuatity().setText(friend_list.size()+" available");
                        }
                    });
                }
            });
        }
    }

    public static void waitDeleteFriend(){
        SocketManager.getInstance();
        if(SocketManager.getSocket() !=null){
            SocketManager.getSocket().on("waitDeleteFriend", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    listFriends.post(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<friend_list.size();i++){
                                if(friend_list.get(i).get_id().equals(args[0]))removeFriendUpdateUI(i);
                            }
                        }
                    });
                }
            });
        }
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