package com.wonders.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.legal_rights.BuildConfig;
import com.example.legal_rights.R;
import com.wonders.application.AppData;
import com.wonders.bean.Result;
import com.wonders.bean.UpdateBean;
import com.wonders.constant.Constants;
import com.wonders.http.Retrofit2Helper;
import com.wonders.thread.FastDealExecutor;
import com.wonders.util.NetCheck;
import com.wonders.util.ToastUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 启动页面
 *
 * @author Administrator
 */

public class SplashActivity extends AppCompatActivity {
    private final static String TAG = SplashActivity.class.getName();
    private Button splashBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        splashBtn = findViewById(R.id.splash_btn);
        splashBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        applyPermission();

        if (!isAvailable(SplashActivity.this, Constants.PRINT_SOFTWARE_NAME)) {
            showPrintDialog(this);
        }

        sendError();

        getApk();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    /*
    * 权限申请的回调
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    /**
     * 发送错误日志
     */
    public void sendError() {
        FastDealExecutor.run(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();

                final File file = new File(Environment.getExternalStorageDirectory() + "/crash/errorLog.txt");
                if (file.isFile() && file.exists()) { //判断文件是否存在
                    try {
                        InputStreamReader read = new InputStreamReader(new FileInputStream(file));
                        BufferedReader bufferedReader = new BufferedReader(read);
                        String lineTxt = null;
                        while ((lineTxt = bufferedReader.readLine()) != null) {
                            sb.append(lineTxt + "\n");
                        }
                        read.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Call<ResponseBody> call = Retrofit2Helper.getInstance().sendError(String.valueOf(sb));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            file.delete();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void getApk() {
        if (!NetCheck.isNetwork(SplashActivity.this)
                || !NetCheck.isNetworkAvailable(SplashActivity.this)) {
            ToastUtil.show("当前环境无网络,只能进入单机模式");
            AppData.getInstance().setIsNetWork(false);
            splashBtn.setVisibility(View.VISIBLE);
        } else {
            // 版本更新
            checkUpdate(SplashActivity.this, new UpdateListener() {
                @Override
                public void updateFail() {
                    splashBtn.setVisibility(View.VISIBLE);
                }

                @Override
                public void updateRefuse() {
                    splashBtn.setVisibility(View.VISIBLE);
                }

                @Override
                public void updateNoNeed() {
                    splashBtn.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public static void checkUpdate(final Activity activity, final UpdateListener updateListener) {
        Call<Result<UpdateBean>> call = Retrofit2Helper.getInstance().getAppInfo();
        call.enqueue(new Callback<Result<UpdateBean>>() {
            @Override
            public void onResponse(Call<Result<UpdateBean>> call, Response<Result<UpdateBean>> response) {
                if (response.body() != null && response.body().getObject() != null) {
                    UpdateBean updateBean = response.body().getObject();
                    Log.e(TAG, "服务器版本：" + updateBean.getVersion());
                    if (updateBean.getVersion() != null
                            && AppData.getAppVersion().compareTo(updateBean.getVersion()) < 0) {
                        showDownloadDialog(activity, updateBean.getDownLoadAddress(), updateListener);
                    } else {
                        updateListener.updateNoNeed();
                    }
                } else {
                    updateListener.updateFail();
                }
            }

            @Override
            public void onFailure(Call<Result<UpdateBean>> call, Throwable t) {
                updateListener.updateFail();
            }
        });
    }

    private static void showDownloadDialog(final Activity activity, final String url, final UpdateListener updateListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.alertDialog)
                .setCancelable(false)
                .setTitle("发现新版本")
                .setMessage("是否更新")
                .setPositiveButton("现在升级",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                downLoadApk(activity, url);
                            }
                        })
                .setNegativeButton("下次再说",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                updateListener.updateRefuse();
                            }
                        });
        builder.show();
    }

    protected static void downLoadApk(final Activity activity, final String url) {
        final ProgressDialog pd; //进度条对话框
        pd = new ProgressDialog(activity, R.style.alertDialog);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        pd.setProgressNumberFormat("");
        pd.setMessage("正在下载更新");
        pd.show();

        if (BuildConfig.DEBUG){
            Observable.create(new ObservableOnSubscribe<okhttp3.Response>() {
                @Override
                public void subscribe(ObservableEmitter e) throws Exception {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    okhttp3.Call call = okHttpClient.newCall(request);
                    okhttp3.Response response = call.execute();
                    e.onNext(response);
                    Log.e(TAG, "onNext");
                    e.onComplete();
                    Log.e(TAG, "onComplete");
                }
            })
                    .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<okhttp3.Response>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(okhttp3.Response response) {
                            try {
                                InputStream is = response.body().byteStream();
                                int length = (int) response.body().contentLength();
                                pd.setMax(length);
                                File file = new File(Environment.getExternalStorageDirectory(), "legal_rights.apk");
                                FileOutputStream fos = new FileOutputStream(file);
                                BufferedInputStream bis = new BufferedInputStream(is);
                                byte[] buffer = new byte[1024];
                                int len;
                                int total = 0;
                                while ((len = bis.read(buffer)) != -1) {
                                    fos.write(buffer);
                                    //获取当前下载量
                                    total += len;
                                    pd.setProgress(total);
                                }
                                pd.dismiss();
                                fos.close();
                                bis.close();
                                is.close();
                                installApk(activity, file);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {
            getFileFromServer(activity, url, pd);
        }
    }

    //安装apk
    protected static void installApk(Activity activity, File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void getFileFromServer(Activity activity, String path, ProgressDialog pd) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .build();
        okhttp3.Call call = okHttpClient.newCall(request);

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                InputStream is = response.body().byteStream();
                int length = (int) response.body().contentLength();
                pd.setMax(length);
                File file = new File(Environment.getExternalStorageDirectory(), "legal_rights.apk");
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer);
                    //获取当前下载量
                    total += len;
                    pd.setProgress(total);
                }
                installApk(activity, file);
                pd.dismiss();
                fos.close();
                bis.close();
                is.close();
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }
        });
    }

    //是否已安装了打印软件
    public static boolean isAvailable(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            if (packageInfos.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    public static void showPrintDialog(final Activity activity) {
        new AlertDialog.Builder(activity, R.style.alertDialog)
                .setTitle("提示")
                .setMessage("未检测到打印服务，请点击安装!")
                .setPositiveButton("安装",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/printershare.apk";
                                if (copyApkFromAssets(activity, "printershare.apk", filePath)) {
                                    installApk(activity, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/printershare.apk"));
                                    dialog.dismiss();
                                }
                            }
                        })
                .show();
    }

    //将Asserts中的资源拷贝到磁盘中
    private static boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    /**
     * 申请权限
     */
    private void applyPermission() {
        ActivityCompat.requestPermissions(SplashActivity.this
                , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.CAMERA}, 0);
    }

    public abstract static class UpdateListener {
        //接口数据返回失败
        void updateFail() {
        }

        //用户拒绝更新
        void updateRefuse() {
        }

        //版本不支持更新
        void updateNoNeed() {
        }
    }
}
