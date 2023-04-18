package com.example.onlychat.MainScreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.onlychat.Authetication.LoginActivity;
import com.example.onlychat.ChatBot.ChatBot;
import com.example.onlychat.DirectMessage.DirectMessage;
import com.example.onlychat.Friends.Friends;
import com.example.onlychat.GlobalChat.GlobalChat;
import com.example.onlychat.GroupChat.GroupChat;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.RoomOptions;

import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.navbar_direct_chat,
            R.drawable.navbar_group_chat,
            R.drawable.navbar_global_chat,
            R.drawable.navbar_bot_chat,
            R.drawable.navbar_friends
    };

    private Boolean isLogin = false;
    private ArrayList<RoomModel> global_list = new ArrayList<>();
    private ArrayList<RoomModel> group_list= new ArrayList<>();
    private ArrayList<RoomModel> direct_list= new ArrayList<>();
    private ArrayList<RoomModel> chatbot_list= new ArrayList<>();

    private ArrayList<UserModel> friend_list = new ArrayList<>();
    private ArrayList<UserModel> invite_list = new ArrayList<>();

    GlobalChat globalChatFragment = new GlobalChat();
    DirectMessage directChatFragment = new DirectMessage();
    GroupChat groupChatFragment = new GroupChat();
    ChatBot botChatFragment = new ChatBot();
    Friends friendsFragment = new Friends();
    GlobalPreferenceManager pref;
    static UserModel myInfo;


    public void getMetaData() {
        HttpManager httpManager = new HttpManager(this);
        httpManager.getListChat(
            new HttpResponse(){
                @Override
                public void onSuccess(JSONObject Response) {
                    try{
                        JSONArray directChat = Response.getJSONObject("data").getJSONArray("directChat");
                        JSONArray groupChat = Response.getJSONObject("data").getJSONArray("groupChat");
                        JSONArray globalChat = Response.getJSONObject("data").getJSONArray("globalChat");
                        JSONArray botChat = Response.getJSONObject("data").getJSONArray("botChat");


                        
                        if(directChat.length()>0){
                            direct_list = getListRoom(directChat);
                            directChatFragment.setRoomModels(direct_list);
                        }

                        if(groupChat.length()>0){
                            group_list = getListRoom(groupChat);
                            groupChatFragment.setRoomModels(group_list);
                        }

                        if(globalChat.length()>0){
                            global_list = getListRoom(globalChat);
                            globalChatFragment.setRoomModels(global_list);
                        }

                        if(botChat.length()>0){
                            chatbot_list = getListRoom(botChat);
                            botChatFragment.setRoomModels(chatbot_list);
                        }
                    }
                    catch (Exception e){
                        Log.i("HTTP Success 11111 Error",e.toString());
                    }
                }

                @Override
                public void onError(String error) {
                    Log.i("HTTP Error",error);
                }
            }
        );

        httpManager.getListFriends(
                new HttpResponse() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                        JSONArray friends = response.getJSONObject("data").getJSONArray("friends");
                        JSONArray friend_request = response.getJSONObject("data").getJSONArray("friend_requests");

                        friend_list = getListFriends(friends);
                        invite_list = getListInvite(friend_request);

                        friendsFragment.setFriend(friend_list,invite_list);
                    }

                    @Override
                    public void onError(String error) {

                    }
                }
        );
    }

    @Override
    protected void onResume() {
//        if (isLogin)
//            getMetaData();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        isLogin = bundle.getBoolean("isLogin", false);

//        GlobalPreferenceManager pref = new GlobalPreferenceManager(this);
//        pref.SignOut();

        if (isLogin) {
            pref = new GlobalPreferenceManager(this);
            myInfo = pref.getUserModel();
            SocketManager.getInstance();
            SocketManager.register(myInfo);
            
            getMetaData();
            setContentView(R.layout.main_screen);

            viewPager = (ViewPager) findViewById(R.id.viewPaper);
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(4);
            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);
            //        setupTabIcons();
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.main_navbar, null);

                ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
                if (i == 0) {
                    tab_icon.setImageResource(tabIcons[i]);
                    tab_icon.setBackgroundColor(Color.parseColor("#352159"));
                } else {
                    tab_icon.setImageResource(tabIcons[i]);
                }
                tabLayout.getTabAt(i).setCustomView(tab);
            }

            tabLayout.setOnTabSelectedListener(
                    new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            super.onTabSelected(tab);
                            View tabView = tab.getCustomView();
                            ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
                            tab_icon.setBackgroundColor(Color.parseColor("#352159"));
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                            super.onTabUnselected(tab);
                            View tabView = tab.getCustomView();
                            ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
                            tab_icon.setBackgroundColor(Color.TRANSPARENT);
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                            super.onTabReselected(tab);
                        }
                    }
            );
        }
        else {
            Intent LoginActivity = new Intent(this, com.example.onlychat.Authetication.LoginActivity.class);
            startActivity(LoginActivity);
            finishAffinity();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),0);

        adapter.addFragment(directChatFragment,"direct chat");
        adapter.addFragment(groupChatFragment, "group chat");
        adapter.addFragment(globalChatFragment, "global chat");
        adapter.addFragment(botChatFragment, "bot chat");
        adapter.addFragment(friendsFragment, "friends");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments = new ArrayList<>();
        List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
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

    public ArrayList<UserModel> getListFriends(JSONArray friends) throws JSONException {
        ArrayList<UserModel> listFriends = new ArrayList<>();
        for(int i=0;i<friends.length();i++){
            JSONObject messageJson = (JSONObject) friends.getJSONObject(i);
            UserModel friend = new Gson().fromJson(String.valueOf(messageJson),UserModel.class);
            listFriends.add(friend);
        }
        return  listFriends;
    }

    public ArrayList<UserModel> getListInvite(JSONArray invite) throws JSONException {
        ArrayList<UserModel> listInvite= new ArrayList<>();
        for(int i=0;i<invite.length();i++){
            JSONObject messageJson = (JSONObject) invite.getJSONObject(i);
            UserModel friend = new Gson().fromJson(String.valueOf(messageJson),UserModel.class);
            listInvite.add(friend);
        }
        return  listInvite;
    }


    public ArrayList<RoomModel> getListRoom(JSONArray channel) throws JSONException, ParseException {
        // create list room
        ArrayList<RoomModel> listRoom = new ArrayList<>();

        // set item for list room
        for(int i=0;i<channel.length();i++){
            //create room
            RoomModel roomModel = new RoomModel();
            //set id, avatar, name for room
            roomModel.setId(channel.getJSONObject(i).getString("_id"));
            roomModel.setAvatar(channel.getJSONObject(i).getString("avatar"));
            roomModel.setName(channel.getJSONObject(i).getString("name"));

            // create list message
            ArrayList<MessageModel> listMessage = new ArrayList<>();
            // set information for message
            for(int j=0;j<channel.getJSONObject(i).getJSONArray("chats").length();j++){
                JSONObject messageJson = (JSONObject) channel.getJSONObject(i).getJSONArray("chats").get(j);

                // set information type String for message
                MessageModel messageModel = new Gson().fromJson(String.valueOf(messageJson), MessageModel.class);

                // set time message send
                String dtStart = messageJson.getString("time");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    java.util.Date date = format.parse(dtStart);
                    messageModel.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // add message to list message
                listMessage.add(messageModel);
            }

            // set options
            RoomOptions roomOptions = null;
            if(channel.getJSONObject(i).getJSONArray("options").length()>0){
                roomOptions = new Gson().fromJson(String.valueOf(channel.getJSONObject(i).getJSONArray("options").get(0)),RoomOptions.class);

                //set members
                ArrayList<Member> members = new ArrayList<>();
                Log.i("================= main screen group =================", roomModel.getName());

                for(int l=0;l<channel.getJSONObject(i).getJSONArray("members").length();l++){
                    Member member = new Gson().fromJson(String.valueOf(channel.getJSONObject(i).getJSONArray("members").get(l)),Member.class);
                    Log.i("main screen", member.getUser_id());
                    Log.i("main screen", member.getName());
                    Log.i("main screen", member.getNickname());
                    Log.i("main screen", member.getAvatar());
                    Log.i("main screen >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>","");
                    members.add(member);
                }
                roomOptions.setMembers(members);
            }
            //set time of last message
            String abc = channel.getJSONObject(i).getString("update_time");
            java.util.Date date1=  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(abc);

            // set update_time, options, messages to room
            roomModel.setUpdate_time(date1);
            roomModel.setOptions(roomOptions);
            roomModel.setMessages(listMessage);

            // add room to list room
            listRoom.add(roomModel);
        }

        return listRoom;
    }
}