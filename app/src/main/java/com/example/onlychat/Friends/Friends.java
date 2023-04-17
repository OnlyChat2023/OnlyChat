package com.example.onlychat.Friends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlychat.Friends.AllFriends.AllFriends;
import com.example.onlychat.Friends.Invite.Invite;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class Friends extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    static TextView quatity;

    static AllFriends allFriends = new AllFriends();
    Invite invite = new Invite();

    public Friends(){}
    public void setFriend(ArrayList<UserModel> friend_list,ArrayList<UserModel> invite_list){
        allFriends.setFriend_list(friend_list);
        invite.setInvite_list(invite_list);
        quatity.setText(friend_list.size()+" available");
    }

    public static TextView getQuatity() {
        return quatity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout friends = (LinearLayout) inflater.inflate(R.layout.friends, null);


        tabLayout=friends.findViewById(R.id.tabLayout);
        viewPager=friends.findViewById(R.id.viewPaper);

        quatity = (TextView) friends.findViewById(R.id.quantity);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),0);
        viewPagerAdapter.addFragment(allFriends,"All friends");
        viewPagerAdapter.addFragment(invite,"Invite");
        viewPager.setAdapter(viewPagerAdapter);

        waitAcceptFriend();
        waitDeleteFriend();

        return friends;
    }

    public static void waitAcceptFriend(){
        SocketManager.getInstance();
        if(SocketManager.getSocket() !=null){
            SocketManager.getSocket().on("waitAcceptFriend", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONArray friends = (JSONArray) args[0];

                    ArrayList<UserModel> friend_list = new ArrayList<>();
                    Log.i("Friends socket",Integer.toString(friends.length()));
                    try {
                        friend_list = getListFriends(friends);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    ArrayList<UserModel> finalFriend_list = friend_list;
                    quatity.post(new Runnable() {
                        @Override
                        public void run() {
                            quatity.setText(finalFriend_list.size()+" available");
                            allFriends.setFriend_list(finalFriend_list);
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
                    JSONArray friends = (JSONArray) args[0];

                    ArrayList<UserModel> friend_list = new ArrayList<>();
                    Log.i("Friends socket",Integer.toString(friends.length()));
                    try {
                        friend_list = getListFriends(friends);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    ArrayList<UserModel> finalFriend_list = friend_list;
                    quatity.post(new Runnable() {
                        @Override
                        public void run() {
                            quatity.setText(finalFriend_list.size()+" available");
                            allFriends.setFriend_list(finalFriend_list);
                        }
                    });
                }
            });
        }
    }

    public static void updateUI(){
        SocketManager.getInstance();
        Log.i("Friends", "onSuccess: ");
        if(SocketManager.getSocket() != null){
            SocketManager.getSocket().on("acceptRequestListener", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONArray friends = (JSONArray) args[0];
//                    Log.i("Friends>", Integer.toString(friends.length()));
                    ArrayList<UserModel> friend_list = new ArrayList<>();

                    try {
                        friend_list = getListFriends(friends);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    ArrayList<UserModel> finalFriend_list = friend_list;
                    quatity.post(new Runnable() {
                        @Override
                        public void run() {
                                    quatity.setText(finalFriend_list.size()+" available");
                                    allFriends.setFriend_list(finalFriend_list);
                        }
                    });
                }
            });
        }
    }
    public static ArrayList<UserModel> getListFriends(JSONArray friends) throws JSONException {
        ArrayList<UserModel> listFriends = new ArrayList<>();
        for(int i=0;i<friends.length();i++){
            JSONObject messageJson = (JSONObject) friends.getJSONObject(i);
            UserModel friend = new Gson().fromJson(String.valueOf(messageJson),UserModel.class);
            listFriends.add(friend);
        }
        return  listFriends;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        List<Fragment> fragments = new ArrayList<>();
        List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm,int behavior) {
            super(fm,behavior);
        }

        void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}