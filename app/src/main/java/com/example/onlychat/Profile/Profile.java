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
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.UserModel;
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

    public UserModel getUser() {
        return user;
    }

    Integer isFriend;
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

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        userName = (TextView) findViewById(R.id.username);
        avatar = (ImageView) findViewById(R.id.avatar);

        // Handle Button
        addFriendBtn = (Button) findViewById(R.id.add_friend_btn);
        editBtn = (Button) findViewById(R.id.edit_btn);
        sendChatBtn = (Button) findViewById(R.id.send_chat_btn);

        HttpManager httpManager = new HttpManager(this);
        httpManager.getUserById(user_id, new HttpResponse() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                JSONObject profile = response.getJSONObject("data");
                Log.i("all friends click item", profile.toString());

                user = new Gson().fromJson(profile.toString(), UserModel.class);
                isFriend = profile.getInt("isFriend");
                Log.i("isFriend", isFriend.toString());

                userName.setText(user.getName());
                new HttpManager.GetImageFromServer(avatar).execute(user.getAvatar());

                setButtonUI();
            }

            @Override
            public void onError(String error) {

            }
        });



//        Log.i("TAG", myBundle.getString("user_id") + "----" + (new GlobalPreferenceManager(Profile.this).getUserModel().get_id()) );
        // If click my own user profile


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userInfo = new GlobalPreferenceManager(Profile.this).getUserModel();
                Bundle myBundle = new Bundle();

                Intent editProfile = new Intent(Profile.this, EditProfile.class);
                editProfile.putExtras(myBundle);
                startActivity(editProfile);
            }
        });

        // add, remove, confirm
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFriend == 1){
//                    AllFriends.removeFriend(myBundle.getInt("index"));
                    isFriend = 0;
                    setButtonUI();
                }
                else if(isFriend == 0) {
//                    AllFriends.adFriend(myBundle.getInt("index"));
                    isFriend = 3;
                    setButtonUI();
                }
                else if(isFriend == 2){
//                    AllFriends.removeFriend(myBundle.getInt("index"));
                    isFriend = 1;
                    setButtonUI();
                }
                else{
                    isFriend = 0;
                    setButtonUI();
                }
            }
        });
    }

    public void setButtonUI(){
        Log.i("button ui", isFriend.toString());
        // friend
        if (isFriend==1){
            Drawable img = addFriendBtn.getContext().getResources().getDrawable(R.drawable.ic_white_trash);
            addFriendBtn.setText("Remove");
            addFriendBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
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
            addFriendBtn.setText("Sended");
            addFriendBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        // not me
        if (!user_id.equals(new GlobalPreferenceManager(Profile.this).getUserModel().get_id())){
            editBtn.setVisibility(View.GONE);
        }
        // me
        else {
            addFriendBtn.setVisibility(View.GONE);
            sendChatBtn.setVisibility(View.GONE);
        }

    }
}