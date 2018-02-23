package com.wonders.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.FileDescriptor;

/**
 * Created by bjy on 2017/1/17.
 */

public class ImageResizer {
    private static final String TAG = "ImageResizer";

    public ImageResizer(){

    }

    public Bitmap decodeSampledBitmapFromBase64(String picSource,int reqWidth,int reqHeight){
        byte[] bitmapArray = Base64.decode(picSource,Base64.DEFAULT);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bitmapArray,0,bitmapArray.length,options);

        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(bitmapArray,0,bitmapArray.length,options);
    }

    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);

        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        final int width  = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if(width > reqWidth || height > reqHeight){
            final int halWidth = width / 2;
            final int halHeight = height / 2;
            while ((halWidth / inSampleSize) >=reqWidth &&(halHeight / inSampleSize) >= reqHeight ){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
