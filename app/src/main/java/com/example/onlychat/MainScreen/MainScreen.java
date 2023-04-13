package com.example.onlychat.MainScreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.onlychat.Authetication.ForgotActivity;
import com.example.onlychat.Authetication.LoginActivity;
import com.example.onlychat.Authetication.RegisterActivity;
import com.example.onlychat.ChatBot.ChatBot;
import com.example.onlychat.DirectMessage.DirectMessage;
import com.example.onlychat.Friends.Friends;
import com.example.onlychat.GlobalChat.GlobalChat;
import com.example.onlychat.GroupChat.GroupChat;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.RoomOptions;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.GlobalPreferenceManager;
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

    private ImageView showPasswordBtn;
    private EditText passwordInput;
    private TextView forgotPasswordBtn;

    private boolean isHidePassword = true;
    private Button RegisterBtn;

    private Boolean isLogin = false;
    private UserModel user;
    private ArrayList<RoomModel> global_list;
    private ArrayList<RoomModel> group_list;
    private ArrayList<RoomModel> direct_list;
    private ArrayList<RoomModel> chatbot_list;

    GlobalChat globalChatFragment = new GlobalChat();
    DirectMessage directChatFragment = new DirectMessage();
    GroupChat groupChatFragment = new GroupChat();
    ChatBot botChatFragment = new ChatBot();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLogin=true;
        HttpManager httpManager = new HttpManager(this);
        httpManager.getListChat(
            new HttpResponse(){
                @Override
                public void onSuccess(JSONObject Response) {
                    try{
//                        JSONObject information = Response.getJSONObject("data").getJSONObject("user");
                        JSONArray directChat = Response.getJSONObject("data").getJSONArray("directChat");
                        JSONArray groupChat = Response.getJSONObject("data").getJSONArray("groupChat");
                        JSONArray globalChat = Response.getJSONObject("data").getJSONArray("globalChat");
                        JSONArray botChat = Response.getJSONObject("data").getJSONArray("botChat");
//                        user = new Gson().fromJson(String.valueOf(information), UserModel.class);
//                    Log.i("friend",information.getJSONArray("friend").toString());
//                    Log.i("friend_request",information.getJSONArray("friend_request").toString());
                        direct_list = getListRoom(directChat);
                        group_list = getListRoom(groupChat);
                        global_list = getListRoom(globalChat);
                        chatbot_list = getListRoom(botChat);

                        Log.i("TAG", global_list.get(0).getName());

                        directChatFragment.setRoomModels(direct_list);
                        globalChatFragment.setRoomModels(global_list);
                        groupChatFragment.setRoomModels(group_list);
                        botChatFragment.setRoomModels(chatbot_list);



                    }
                    catch (Exception e){
                        Log.i("HTTP Success Error",e.toString());
                    }
                }

                @Override
                public void onError(String error) {
                    Log.i("HTTP Error",error);
                }
            });

//        httpManager.getUserById("6430c86d1b48c829004aa89b");
//        try {
//            httpManager.getDirectChat();
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }

        Bundle bundle = getIntent().getExtras();
        isLogin = bundle.getBoolean("isLogin", false);

        if (isLogin) {

            setContentView(R.layout.main_screen);

            viewPager = (ViewPager) findViewById(R.id.viewPaper);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);
            //        setupTabIcons();
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.main_navbarrrrrrrrrrrrrrr, null);

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

        adapter.addFragment(new DirectMessage(),"direct chat");
        adapter.addFragment(new GroupChat(), "group chat");
        adapter.addFragment(globalChatFragment, "global chat");
        adapter.addFragment(new ChatBot(), "bot chat");
        adapter.addFragment(new Friends(), "friends");

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

    public ArrayList<RoomModel> getListRoom(JSONArray channel) throws JSONException, ParseException {
        ArrayList<RoomModel> listRoom = new ArrayList<>();
        Log.i("Chat",Integer.toString(channel.length()));

        for(int i=0;i<channel.length();i++){
            RoomModel roomModel = new RoomModel();
            roomModel.setId(channel.getJSONObject(i).getString("_id"));
            roomModel.setAvatar(channel.getJSONObject(i).getString("avatar"));
            roomModel.setName(channel.getJSONObject(i).getString("name"));
            ArrayList<MessageModel> listMessage = new ArrayList<>();
            for(int j=0;j<channel.getJSONObject(i).getJSONArray("chats").length();j++){
                MessageModel messageModel = new MessageModel();
                JSONObject messageJson = (JSONObject) channel.getJSONObject(i).getJSONArray("chats").get(j);
                messageModel = new Gson().fromJson(String.valueOf(messageJson),MessageModel.class);
                String dtStart = messageJson.getString("time");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    java.util.Date date = format.parse(dtStart);
                    messageModel.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                listMessage.add(messageModel);
            }
            RoomOptions roomOptions = new Gson().fromJson(String.valueOf(channel.getJSONObject(i).getJSONArray("options").get(0)),RoomOptions.class);
            String abc = channel.getJSONObject(i).getString("update_time");
            java.util.Date date1=  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(abc);
            roomModel.setUpdate_time(date1);


            ArrayList<Member> members = new ArrayList<>();
            for(int l=0;l<channel.getJSONObject(i).getJSONArray("members").length();l++){
                    Member member = new Gson().fromJson(String.valueOf(channel.getJSONObject(i).getJSONArray("members").get(l)),Member.class);
                    members.add(member);
            }
            roomOptions.setMembers(members);
            roomModel.setOptions(roomOptions);
            roomModel.setMessages(listMessage);
            listRoom.add(roomModel);
        }
        return listRoom;
    }
}