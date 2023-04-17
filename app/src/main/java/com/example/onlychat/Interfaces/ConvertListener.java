package com.example.onlychat.Interfaces;

import android.graphics.Bitmap;

import com.example.onlychat.Model.ImageModel;

import java.util.ArrayList;

public interface ConvertListener {
    public void onSuccess(ImageModel result);
    public void onDownloadSuccess(ArrayList<Bitmap> result);
}
