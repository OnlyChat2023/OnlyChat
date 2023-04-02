package com.example.onlychat.EditProfile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.onlychat.EditProfileStep2;
import com.example.onlychat.MainActivity;
import com.example.onlychat.R;

public class EditProfile extends AppCompatActivity {

    private ImageButton upImgBtn;
    private ImageView avatar;
    private Button nextBtn;
    private final int GALLERY_REQ_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nextBtn = (Button) findViewById(R.id.nextBtn);
        avatar = (ImageView) findViewById(R.id.avatar);
        upImgBtn = (ImageButton) findViewById(R.id.up_image_btn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent step2 = new Intent(EditProfile.this, EditProfileStep2.class);
                startActivity(step2);
            }
        });

        upImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == GALLERY_REQ_CODE){
                avatar.setImageURI(data.getData());
            }
        }
    }
}