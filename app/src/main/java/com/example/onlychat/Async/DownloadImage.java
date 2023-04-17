package com.example.onlychat.Async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.utils.Utils;

import java.io.InputStream;
import java.util.ArrayList;

public class DownloadImage extends AsyncTask<Void, Void, ArrayList<Bitmap>> {

    private ArrayList<String> imageUrls;
    private ConvertListener listener;

    public DownloadImage(ArrayList<String> images, ConvertListener _listener) {
        this.imageUrls = images;
        this.listener = _listener;
    }

    @Override
    protected ArrayList<Bitmap> doInBackground(Void... urls) {
        ArrayList<Bitmap> imageBM = new ArrayList<Bitmap>();

        for (String url : imageUrls) {
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL("http://"+Utils.ip+"/assets/" + url).openStream();
                imageBM.add(BitmapFactory.decodeStream(in));
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }

        return imageBM;
    }

    @Override
    protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
        listener.onDownloadSuccess(bitmaps);
    }
}