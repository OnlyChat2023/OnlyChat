package com.example.onlychat.Profile;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.onlychat.EditProfile.EditProfile;
import com.example.onlychat.Friends.AllFriends.AllFriends;
import com.example.onlychat.Friends.Friends;
import com.example.onlychat.Friends.Invite.Invite;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.ProfileCustomFragment.profile_description;
import com.example.onlychat.Profile.ProfileCustomFragment.profile_information;
import com.example.onlychat.Profile.ProfileCustomFragment.profile_social;
import com.example.onlychat.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class Profile extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView userName;
    private ImageView avatar;
    private TextView editText;
    private Button editBtn;
    private Button addFriendBtn;
    private Button sendChatBtn;
    private String user_id;
    UserModel user;
    private ImageView backBtn;

    public UserModel getUser() {
        return user;
    }

    profile_information profileInformation = new profile_information();
    profile_social profileSocial = new profile_social();
    profile_description profileDescription = new profile_description();

    Integer isFriend;

    static UserModel myInfo;
    GlobalPreferenceManager pref;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set data on Profile
        Intent myCallerIntent = getIntent();
        Bundle myBundle = myCallerIntent.getExtras();
        user_id = myBundle.getString("user_id");

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        pref = new GlobalPreferenceManager(this);
        myInfo = pref.getUserModel();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(profileInformation, "Information");
        viewPagerAdapter.addFragment(profileSocial,"Social media");
        viewPagerAdapter.addFragment(profileDescription,"Description");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        userName = (TextView) findViewById(R.id.username);
        avatar = (ImageView) findViewById(R.id.avatar);

        // Handle Button
        addFriendBtn = (Button) findViewById(R.id.add_friend_btn);
        editBtn = (Button) findViewById(R.id.edit_btn);
        sendChatBtn = (Button) findViewById(R.id.send_chat_btn);
        backBtn = (ImageView) findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
            }
        });

        HttpManager httpManager = new HttpManager(this);
        httpManager.getUserById(user_id, new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                JSONObject profile = response.getJSONObject("data");
                Log.i("all friends click item", profile.toString());

                user = new Gson().fromJson(profile.toString(), UserModel.class);
//                viewPagerAdapter.getItem(0);
                profileInformation.setData(user);
                profileSocial.setData(user);
                profileDescription.setData(user);




                isFriend = profile.getInt("isFriend");
                Log.i("Profile",isFriend.toString());


                        userName.setText(user.getName());
                new HttpManager.GetImageFromServer(avatar).execute(user.getAvatar());
                addFriendBtn.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.VISIBLE);
                sendChatBtn.setVisibility(View.VISIBLE);
                setButtonUI();
            }

            @Override
            public void onError(String error) {

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userInfo = new GlobalPreferenceManager(Profile.this).getUserModel();
                Bundle myBundle = new Bundle();

                Intent editProfile = new Intent(Profile.this, EditProfile.class);
                editProfile.putExtras(myBundle);
                startActivity(editProfile);

                finish();
            }
        });

        // add, remove, confirm
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // unfriend
                if (isFriend == 1){
                    AllFriends.removeFriend(user_id);
                    isFriend = 0;
                    setButtonUI();
                    Log.i("Profile", "1");
                }
                // add friend
                else if(isFriend == 0) {
                    SocketManager.getInstance();
                    SocketManager.sendRequestAddFriend(user_id,myInfo);
                    isFriend = 3;
                    setButtonUI();
                    Log.i("Profile", "2");
                }
                // accept
                else if(isFriend == 2){
                    Invite.addFriend(user_id);
                    isFriend = 1;
                    setButtonUI();
                    Log.i("Profile", "3");
                }
                // send invite click == unfriend
                else{
                    Invite.removeSent(user_id);
                    isFriend = 0;
                    setButtonUI();
                    Log.i("Profile", "4");
                }
            }
        });
    }

    public void setButtonUI(){
        // not me
        if (!user_id.equals(new GlobalPreferenceManager(Profile.this).getUserModel().get_id())){
            editBtn.setVisibility(View.GONE);
            sendChatBtn.setVisibility(View.GONE);
        }
        // me
        else {
            addFriendBtn.setVisibility(View.GONE);
        }

        // friend
        if (isFriend==1){
            Drawable img = addFriendBtn.getContext().getResources().getDrawable(R.drawable.ic_white_trash);
            addFriendBtn.setText("Remove");
            addFriendBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            sendChatBtn.setVisibility(View.VISIBLE);
        }
        // invite to me
        else if(isFriend == 2){
            Drawable img = addFriendBtn.getContext().getResources().getDrawable(R.drawable.ic_check);
            addFriendBtn.setText("Confirm");
            addFriendBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        }
        else if(isFriend == 0){
            Drawable img = addFriendBtn.getContext().getResources().getDrawable(R.drawable.ic_add_friend);
            addFriendBtn.setText("Add friend");
            addFriendBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        }
        else {
//            Drawable img = addFriendBtn.getContext().getResources().getDrawable(R.drawable.ic_add_friend);
            addFriendBtn.setText("Sent");
            addFriendBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

    }
}