package com.wonders.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.util.Base64;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bjy on 2017/1/17.
 */

public class ImageLoader {
    private static ImageLoader instance;
    private static final String TAG = "ImageLoader";

    public static final int MESSAGE_POST_RESULT = 1;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;

    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;
    private static final int IO_BUFFER_SIZE = 1024 * 8;
    private static final int DISK_CACHE_INDEX = 0;
    private boolean mIsDiskLruCacheCreated;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"ImageLoader#" + mCount.getAndIncrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),sThreadFactory);

    private Handler mMainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private Context mContext;
    private ImageResizer mImageResizer = new ImageResizer();
    private LruCache<String,Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;

    private ImageLoader(Context context){
        mContext = context.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory /8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext,"bitmap");
        if (!diskCacheDir.exists()){
            diskCacheDir.mkdirs();
        }
        if (getUseableSpace(diskCacheDir) > DISK_CACHE_SIZE){
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir,1,1,DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ImageLoader build(Context context){
        if (instance == null){
            instance = new ImageLoader(context);
        }
        return instance;
    }

    public boolean bindBitmap(ImageView imageView,String key,int reqWidth,int reqHeight){
        Bitmap bitmap = getBitmapFromMemoryCache(key);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return true;
        }

        bitmap = getBitmapFromDiskCache(key,reqWidth,reqHeight);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return true;
        }

        return false;
    }

    private void addBitmapToMemoryCache(String key,Bitmap bitmap){
        if (getBitmapFromMemoryCache(key) == null){
            mMemoryCache.put(key,bitmap);
        }
    }

    private Bitmap getBitmapFromMemoryCache(String key){
        return mMemoryCache.get(key);
    }

    public void setBitmapToDiskCache(String picSource,String key){
        byte[] bitmapArray = Base64.decode(picSource,Base64.DEFAULT);

        InputStream inputStream = new ByteArrayInputStream(bitmapArray);
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null){
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);

                BufferedInputStream bis = new BufferedInputStream(inputStream,IO_BUFFER_SIZE);
                BufferedOutputStream bos = new BufferedOutputStream(outputStream,IO_BUFFER_SIZE);
                int b;
                while ((b = bis.read()) != -1){
                    bos.write(b);
                }
                bis.close();
                bos.close();
                editor.commit();
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBitmapToDiskCache(File file,String key){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null){
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);

                BufferedInputStream bis = new BufferedInputStream(inputStream,IO_BUFFER_SIZE);
                BufferedOutputStream bos = new BufferedOutputStream(outputStream,IO_BUFFER_SIZE);
                int b;
                while ((b = bis.read()) != -1){
                    bos.write(b);
                }
                bis.close();
                bos.close();
                editor.commit();
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromDiskCache(String key,int reqWidth,int reqHeight){
        if (mDiskLruCache == null){
            return null;
        }

        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null){
                FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                FileDescriptor fileDescriptor = fileInputStream.getFD();
                bitmap =mImageResizer.decodeSampledBitmapFromFileDescriptor(fileDescriptor,reqWidth,reqHeight);
                if (bitmap != null){
                    addBitmapToMemoryCache(key,bitmap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    public File getDiskCacheDir(Context context, String uniqueName){
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable){
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private long getUseableSpace(File path){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            return path.getUsableSpace();
        }
        final StatFs statFs = new StatFs(path.getPath());
        return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
    }

}
