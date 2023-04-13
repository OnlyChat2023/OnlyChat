package com.example.onlychat.MainScreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Interfaces.RoomOptions;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

    private Boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLogin=true;
        HttpManager httpManager = new HttpManager(this);
        httpManager.getUser();
//        httpManager.getUserById("6430c86d1b48c829004aa89b");
//        try {
//            httpManager.getDirectChat();
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
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

        // test data
        ArrayList<RoomModel> listRoom = new ArrayList<>();

        for(int i=0;i<10;i++){
            ArrayList<MessageModel> listMessage = new ArrayList<>();
            for(int j=0;j<10;j++){
                ArrayList<String> seenUser= new ArrayList<>();
                seenUser.add("1");
                seenUser.add("2");
                seenUser.add("3");
                MessageModel messageModel = new MessageModel(Integer.toString(i),Integer.toString(i),R.drawable.global_chat_avatar,"test","test","Sorry to bother you. I have a question for you",new Date(),seenUser);
                listMessage.add(messageModel);
            }

            ArrayList<Member> members = new ArrayList<>();
            members.add(new Member("1","anonymous","anonymous",R.drawable.global_chat_avatar));
            RoomOptions roomOptions= new RoomOptions(false,false,"cccc",R.drawable.global_chat_avatar,members);
            listRoom.add(new RoomModel(Integer.toString(i),R.drawable.global_chat_avatar,"Anonymous",listMessage,roomOptions));
        }
        /////////////////////////////

        adapter.addFragment(new DirectMessage(),"direct chat");
        adapter.addFragment(new GroupChat(), "group chat");
        adapter.addFragment(new GlobalChat(listRoom), "global chat");
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

}