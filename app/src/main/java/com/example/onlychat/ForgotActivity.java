package com.example.onlychat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ForgotActivity extends AppCompatActivity {

    private Button LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_form);

        Intent loginIntent = new Intent(this, MainActivity.class);

        LoginBtn = (Button) findViewById(R.id.signInBtn);

        LoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
                finishAffinity();
            }
        });
    }
}