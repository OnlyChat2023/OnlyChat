package com.example.onlychat.Authetication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.onlychat.MainActivity;
import com.example.onlychat.R;

public class ForgotActivity extends AppCompatActivity {

    private Button LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_to_left, R.anim.fixed);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
    }
}