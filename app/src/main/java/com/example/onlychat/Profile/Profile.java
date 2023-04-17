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
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;
import com.google.android.material.tabs.TabLayout;

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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        userName = (TextView) findViewById(R.id.username);
        avatar = (ImageView) findViewById(R.id.avatar);

        // Set data on Profile
        Intent myCallerIntent = getIntent();
        Bundle myBundle = myCallerIntent.getExtras();
//        System.out.println(myBundle.getString("name"));
        userName.setText(myBundle.getString("name"));

        new HttpManager.GetImageFromServer(avatar).execute(myBundle.getString("avatar"));
//        adapter = new CustomIconLabelAdapter(this, R.layout.info_item, contents, thumbnails);
//        setListAdapter(adapter);

        // Handle Button
        addFriendBtn = (Button) findViewById(R.id.add_friend_btn);
        editBtn = (Button) findViewById(R.id.edit_btn);
        sendChatBtn = (Button) findViewById(R.id.send_chat_btn);

        // If isFriend = true
        System.out.println("isFRIEND: " + myBundle.getBoolean("isFriend"));
        if (myBundle.getBoolean("isFriend")){
            Drawable img = addFriendBtn.getContext().getResources().getDrawable(R.drawable.ic_white_trash);
            addFriendBtn.setText("Remove");
            addFriendBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        }
        else {
            Drawable img = addFriendBtn.getContext().getResources().getDrawable(R.drawable.ic_check);
            addFriendBtn.setText("Confirm");
            addFriendBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        }
        Log.i("TAG", myBundle.getString("user_id") + "----" + (new GlobalPreferenceManager(Profile.this).getUserModel().get_id()) );
        // If click my own user profile
        if (!myBundle.getString("user_id").equals(new GlobalPreferenceManager(Profile.this).getUserModel().get_id())){
            editBtn.setVisibility(View.GONE);
        }
        else {
            addFriendBtn.setVisibility(View.GONE);
            sendChatBtn.setVisibility(View.GONE);
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userInfo = new GlobalPreferenceManager(Profile.this).getUserModel();
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

                Intent editProfile = new Intent(Profile.this, EditProfile.class);
                editProfile.putExtras(myBundle);
                startActivity(editProfile);
            }
        });
    }
}