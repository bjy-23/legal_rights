package com.wonders.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class BitmapUtil {
	private static final String TAG = BitmapUtil.class.getName();

	public static String bitmaptoString(Bitmap bitmap, int bitmapQuality) {
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, bitmapQuality, bStream);
		byte[] bytes = bStream.toByteArray();

		Log.i("size++", bytes.length + "");
		string = Base64.encodeToString(bytes, Base64.DEFAULT);

		bitmap.recycle();
		return string;
	}

	public static Bitmap stringtoBitmap(String string) {
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			Log.e(TAG, "base64 转 Bitmap 失败！");
			e.printStackTrace();
		}
		return bitmap;
	}

    public static Bitmap decodeBitmapFromResource(Resources resources, int resId, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return  BitmapFactory.decodeResource(resources, resId, options);
    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return 获得采样率
     */
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

	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
}
