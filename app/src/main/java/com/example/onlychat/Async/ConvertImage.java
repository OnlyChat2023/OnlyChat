package com.example.onlychat.Async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;

import com.example.onlychat.Interfaces.ConvertListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ConvertImage extends AsyncTask<String, Void, ArrayList<String>> {

    private Context ctx;
    private ArrayList<Uri> arrayList;
    private ConvertListener listener;

    public ConvertImage(Context _ctx, ArrayList<Uri> _arrayList, ConvertListener _listener) {
        ctx = _ctx;
        arrayList = _arrayList;
        listener = _listener;
    }

    @Override
    protected ArrayList<String> doInBackground(String ...params) {

        ArrayList<String> imageList = new ArrayList<String>();

        for (int i = 0; i < arrayList.size(); i++) {
            Bitmap bitmap = null;

            try {
                // Set options to decode only the dimensions of the image, not the whole image
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(arrayList.get(i)), null, options);

                // Calculate the scale factor for the image to fit into memory
                int scaleFactor = Math.min(options.outWidth / 400, options.outHeight / 150);

                // Set options to decode the full image
                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888; // Optional, can be changed depending on your needs

                // Decode the image into a Bitmap
                bitmap = BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(arrayList.get(i)), null, options);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), arrayList.get(i));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

            if (bitmap != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 40, outputStream);

                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                byte[] byteArray = outputStream.toByteArray();
                String encodedString = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                imageList.add(encodedString);
            }
        }

        return imageList;
    }

    protected void onPostExecute(ArrayList<String> result) {
        listener.onSuccess(result);
    }
}
