package com.example.onlychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.Authetication.ForgotActivity;
import com.example.onlychat.Authetication.RegisterActivity;
import com.example.onlychat.ChatBot.MainScreen;
import com.example.onlychat.GlobalChat.ListChat;
import com.example.onlychat.EditProfile.EditProfile;
import com.example.onlychat.GroupChat.GroupChatSetting;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.DirectMessage.ChattingActivity;

public class MainActivity extends AppCompatActivity {
    private final Boolean isLogin = false;

    private ImageView showPasswordBtn;
    private EditText passwordInput;
    private TextView forgotPasswordBtn;

    private boolean isHidePassword = true;
    private Button RegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(MainActivity.this, MainScreen.class);
        startActivity(intent);

//        if (isLogin == false) {
//            setContentView(R.layout.activity_main);
//
//            Intent registerIntent = new Intent(this, RegisterActivity.class);
//            Intent forgotIntent = new Intent(this, ForgotActivity.class);
//
//            RegisterBtn = (Button) findViewById(R.id.registerBtn);
//            RegisterBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(registerIntent);
//                }
//            });
//
//            passwordInput = (EditText) findViewById(R.id.passwordInput);
//
//            forgotPasswordBtn = (TextView) findViewById(R.id.forgotPasswordBtn);
//
//            forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(forgotIntent);
//                }
//            });
//
//            showPasswordBtn = (ImageView) findViewById(R.id.showPassword);
//            showPasswordBtn.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//
//                    if (isHidePassword == true)
//                        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                    else
//                        passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
//
//                    passwordInput.setSelection(passwordInput.getText().toString().length());
//                    isHidePassword = !isHidePassword;
//                }
//            });
//        }
    }
}