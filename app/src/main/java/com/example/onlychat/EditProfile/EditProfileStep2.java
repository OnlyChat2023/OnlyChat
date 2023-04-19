package com.example.onlychat.EditProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileStep2 extends AppCompatActivity {
    EditText et_facebook;
    EditText et_instagram;
    EditText et_twitter;
    EditText et_description;
    Button finishBtn;
    ImageView backBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_step2);

        Intent intent = getIntent();
        Bundle myBundle = intent.getExtras();

        et_facebook = (EditText) findViewById(R.id.et_facebook);
        et_instagram = (EditText) findViewById(R.id.et_instagram);
        et_twitter = (EditText) findViewById(R.id.et_twitter);
        et_description = (EditText) findViewById(R.id.et_description);
        finishBtn = (Button) findViewById(R.id.finishBtn);
        backBtn = (ImageView) findViewById(R.id.backButton);

        et_facebook.setText(myBundle.getString("facebook"));
        et_instagram.setText(myBundle.getString("instagram"));
//        et_twitter.setText(userInfo.getTw());
        et_description.setText(myBundle.getString("description"));

        UserModel user = new GlobalPreferenceManager(EditProfileStep2.this).getUserModel();
//        Log.i("RUN", user.getName());
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send new data to server
                user.set_id(myBundle.getString("user_id"));
                user.setName(myBundle.getString("name"));
                user.setPhone(myBundle.getString("phone"));
                user.setEmail(myBundle.getString("email"));
                user.setUniversity(myBundle.getString("graduated"));
                user.setFacebook(et_facebook.getText().toString());
                user.setInstagram(et_instagram.getText().toString());
                user.setDescription(et_description.getText().toString());
                user.setAvatar(myBundle.getString("avatar"));

//                System.out.println("user: " + user.getName());

                HttpManager httpManager = new HttpManager(EditProfileStep2.this);
                httpManager.updateProfile(user);

                EditProfile.editProfileActivity.finish();

                new GlobalPreferenceManager(EditProfileStep2.this).saveUser(user);
                finish();

//                System.out.println("HERE: " + myBundle.getString("user_id"));
//                Intent intentToProfile = new Intent (finishBtn.getContext(), Profile.class);
//                intentToProfile.putExtras(myBundle);
//                startActivity(intentToProfile);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}