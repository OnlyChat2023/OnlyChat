package com.example.onlychat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;

public class MainActivity extends AppCompatActivity {
    GlobalPreferenceManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SocketManager.disconnect();

        pref = new GlobalPreferenceManager(this);
        pref.ValidateRemember();

//        pref.SignOut();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isLogin", pref.getIsLoggedIn());

        UserModel user = pref.getUserModel();

        Intent intent = new Intent(MainActivity.this, MainScreen.class);
        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }
}