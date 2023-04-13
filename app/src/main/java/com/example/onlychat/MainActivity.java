package com.example.onlychat;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
<<<<<<< HEAD
=======
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
>>>>>>> 49053b79cc700a3df2738c0f4f819a40ccdad1df

import com.example.onlychat.MainScreen.MainScreen;
<<<<<<< HEAD
import com.example.onlychat.Manager.HttpManager;
=======
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Profile.Profile;
>>>>>>> 49053b79cc700a3df2738c0f4f819a40ccdad1df

public class MainActivity extends AppCompatActivity {

    GlobalPreferenceManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new GlobalPreferenceManager(this);
        pref.ValidateRemember();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isLogin", pref.getIsLoggedIn());

        Intent intent = new Intent(MainActivity.this, MainScreen.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}