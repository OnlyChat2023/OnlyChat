package com.example.onlychat.EditProfile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.R;

public class EditProfile extends AppCompatActivity {

    private ImageButton upImgBtn;
    private ImageView avatar;
    private Button nextBtn;
    private final int GALLERY_REQ_CODE = 1000;
    EditText et_name;
    EditText et_phone;
    EditText et_email;
    EditText et_graduated;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nextBtn = (Button) findViewById(R.id.nextBtn);
        avatar = (ImageView) findViewById(R.id.avatar);
        upImgBtn = (ImageButton) findViewById(R.id.up_image_btn);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_email = (EditText) findViewById(R.id.et_email);
        et_graduated = (EditText) findViewById(R.id.et_graduated);

        // Get Data and Set dafault value
        Intent myCallerIntent = getIntent();
        Bundle myBundle = myCallerIntent.getExtras();

        et_name.setText(myBundle.getString("name"));
        et_phone.setText(myBundle.getString("phoneNumber"));
        et_email.setText(myBundle.getString("email"));
        System.out.println("FACEBOOK: " + myBundle.getString("facebook"));
        et_graduated.setText(myBundle.getString("university "));
        new HttpManager.GetImageFromServer(avatar).execute(myBundle.getString("avatar"));

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle myBundle2 = new Bundle();
                myBundle2.putString("name", String.valueOf(et_name.getText()));
                myBundle2.putString("phone", String.valueOf(et_phone.getText()));
                myBundle2.putString("email", String.valueOf(et_email.getText()));
                myBundle2.putString("graduated", String.valueOf(et_graduated.getText()));
                myBundle2.putString("facebook", myBundle.getString("facebook"));
                myBundle2.putString("instagram", myBundle.getString("instagram"));
                myBundle2.putString("description", myBundle.getString("description"));


                Intent step2 = new Intent(EditProfile.this, EditProfileStep2.class);
                step2.putExtras(myBundle2);
                startActivity(step2);

                finish();
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
//                avatar.setImageURI(data.getData());
                Log.i("TAG", data.getData().toString());
                new HttpManager.GetImageFromServer(avatar).execute((Runnable) data.getData());
            }
        }
    }
}