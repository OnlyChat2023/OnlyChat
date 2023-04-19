package com.example.onlychat.ViewLargerImageMessage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.onlychat.R;

public class ViewLargerImageMessage extends AppCompatActivity {
    ImageView backBtn;
    ImageView bigImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_larger_image_message);

        backBtn = (ImageView) findViewById(R.id.backButton);
        bigImage = (ImageView) findViewById(R.id.image);

        Intent intent = getIntent();
//        Bundle myBundle = intent.getExtras();
        bigImage.setImageBitmap(intent.getParcelableExtra("data"));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}