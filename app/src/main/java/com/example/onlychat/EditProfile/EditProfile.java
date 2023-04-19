package com.example.onlychat.EditProfile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.onlychat.Async.ConvertImage;
import com.example.onlychat.GlobalChat.ListMessage.ListMessage;
import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.ImageModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;
import com.example.onlychat.ViewLargerImageMessage.ViewLargerImageMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class EditProfile extends AppCompatActivity {
    public static Activity editProfileActivity;
    private ImageButton upImgBtn;
    private ImageView avatar;
    private Button nextBtn;
    private final int GALLERY_REQ_CODE = 1000;
    EditText et_name;
    EditText et_phone;
    EditText et_email;
    EditText et_graduated;
    ImageView backBtn;
    String img_in_phone;
    private String user_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editProfileActivity = this;
        nextBtn = (Button) findViewById(R.id.nextBtn);
        avatar = (ImageView) findViewById(R.id.avatar);
        upImgBtn = (ImageButton) findViewById(R.id.up_image_btn);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_email = (EditText) findViewById(R.id.et_email);
        et_graduated = (EditText) findViewById(R.id.et_graduated);
        backBtn = (ImageView) findViewById(R.id.backButton);

        // Get Data and Set dafault value
        Intent myCallerIntent = getIntent();
        Bundle myBundle = myCallerIntent.getExtras();

        user_id = myBundle.getString("user_id");
        et_name.setText(myBundle.getString("name"));
        et_phone.setText(myBundle.getString("phone"));
        et_email.setText(myBundle.getString("email"));
        System.out.println("FACEBOOK: " + myBundle.getString("facebook"));
        et_graduated.setText(myBundle.getString("university "));
        new HttpManager.GetImageFromServer(avatar).execute(myBundle.getString("avatar"));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle myBundle2 = new Bundle();
                myBundle2.putString("user_id", myBundle.getString("user_id"));
                myBundle2.putString("name", String.valueOf(et_name.getText()));
                myBundle2.putString("avatar", String.valueOf(img_in_phone));
                myBundle2.putString("phone", String.valueOf(et_phone.getText()));
                myBundle2.putString("email", String.valueOf(et_email.getText()));
                myBundle2.putString("graduated", String.valueOf(et_graduated.getText()));
                myBundle2.putString("facebook", myBundle.getString("facebook"));
                myBundle2.putString("instagram", myBundle.getString("instagram"));
                myBundle2.putString("description", myBundle.getString("description"));


                Intent step2 = new Intent(EditProfile.this, EditProfileStep2.class);
                step2.putExtras(myBundle2);
                startActivityForResult(step2, 0);

//                finish();
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
                img_in_phone = String.valueOf(data.getData());
                avatar.setImageURI(data.getData());

                ArrayList<Uri> avt = new ArrayList<>();
                avt.add(data.getData());

                new ConvertImage(this, avt, new ConvertListener() {

                    @Override
                    public void onSuccess(ImageModel result) {
                        ArrayList<String> myAvt = result.getImagesListStr();
                        String avtBase64 = myAvt.get(0);

                        SocketManager.addNewAvatarToServer(EditProfile.this, avtBase64, user_id);
                        avatar.setImageBitmap(result.getImagesBM().get(0));
                    }

                    @Override
                    public void onDownloadSuccess(ArrayList<Bitmap> result) {

                    }
                }).execute();
            }
        }
    }
}