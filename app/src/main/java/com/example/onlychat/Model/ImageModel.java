package com.example.onlychat.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ImageModel {
    private ArrayList<Bitmap> imagesListBM;
    private ArrayList<String> imagesListStr;

    public ImageModel(ArrayList<Bitmap> imagesListBM, ArrayList<String> images) {
        this.imagesListBM = imagesListBM;
        this.imagesListStr = images;
    }

    public ArrayList<Bitmap> getImagesBM() {
        return imagesListBM;
    }

    public ArrayList<String> getImagesListStr() {
        return imagesListStr;
    }
}
