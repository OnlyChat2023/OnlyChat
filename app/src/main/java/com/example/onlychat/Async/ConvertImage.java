package com.example.onlychat.Async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;

import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Model.ImageModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ConvertImage extends AsyncTask<String, Void, ImageModel> {

    private Context ctx;
    private ArrayList<Uri> arrayList;
    private ConvertListener listener;

    public ConvertImage(Context _ctx, ArrayList<Uri> _arrayList, ConvertListener _listener) {
        ctx = _ctx;
        arrayList = _arrayList;
        listener = _listener;
    }

    @Override
    protected ImageModel doInBackground(String ...params) {

        ArrayList<String> imageList = new ArrayList<String>();
        ArrayList<Bitmap> imageListBitmap = new ArrayList<Bitmap>();

        for (int i = arrayList.size() - 1; i >= 0; i--) {
            Bitmap bitmap = null;

            try {
                // Set options to decode only the dimensions of the image, not the whole image
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(arrayList.get(i)), null, options);

                // Calculate the scale factor for the image to fit into memory
                int scaleFactor = calculateInSampleSize(options, 120, 120);

                // Set options to decode the full image
                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888; // Optional, can be changed depending on your needs

                // Decode the image into a Bitmap
                bitmap = BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(arrayList.get(i)), null, options);

                ExifInterface exif = new ExifInterface(ctx.getContentResolver().openInputStream(arrayList.get(i)));
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                Matrix matrix = new Matrix();
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                    default:
                        break;
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), arrayList.get(i));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

            if (bitmap != null) {
                imageListBitmap.add(bitmap);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);

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

        return new ImageModel(imageListBitmap, imageList);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    protected void onPostExecute(ImageModel result) {
        listener.onSuccess(result);
    }
}
