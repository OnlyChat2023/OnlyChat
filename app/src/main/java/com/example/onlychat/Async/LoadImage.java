package com.example.onlychat.Async;

import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.onlychat.Adapter.ImageChat;

import java.util.ArrayList;

//public class LoadImage extends AsyncTask<ArrayList<Uri>, Void, Void> {
//    private RecyclerView mImageView;
//
//    public LoadImage(RecyclerView imageView) {
//        mImageView = imageView;
//    }
//
//    @Override
//    protected Void doInBackground(ArrayList<Uri>... voids) {
//        ArrayList<Uri> uris = voids[0];
//
//        ImageChat myImageChat = new ImageChat(uris);
//        mImageView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mImageView.setAdapter(myImageChat);
//            }
//        }, 300);
//
//        return null;
//    }
//}
