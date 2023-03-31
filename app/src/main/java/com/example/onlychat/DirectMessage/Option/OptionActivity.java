package com.example.onlychat.DirectMessage.Option;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.DirectMessage.ChattingActivity;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.R;

public class OptionActivity extends AppCompatActivity {
    ImageView avatar;
    Button btn_back, btn_nickname, btn_profile, btn_notify;
    TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_message_activity_option);

        avatar = (ImageView) findViewById(R.id.chat_option_avatar);
        txtName = (TextView) findViewById(R.id.chat_option_name);
        btn_back = (Button) findViewById(R.id.chat_option_back);
        btn_nickname = (Button) findViewById(R.id.chat_option_btn_nickname);
        btn_profile = (Button) findViewById(R.id.chat_option_btn_profile);
        btn_notify = (Button) findViewById(R.id.chat_option_btn_noify);

        Intent main_chat = getIntent();
        Bundle userInf = main_chat.getExtras();
        Bitmap bm_avatar = (Bitmap) main_chat.getParcelableExtra("Bitmap");
        avatar.setImageBitmap(bm_avatar);
        txtName.setText(userInf.getString("name"));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionActivity.this, ChattingActivity.class);
                startActivity(intent);
            }
        });
    }
}