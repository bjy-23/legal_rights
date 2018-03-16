package com.wonders.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.legal_rights.R;
import com.wonders.constant.Constants;
import com.wonders.util.PermissionUtil;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 生产、流通-预览
 */
public abstract class PreviewActivity extends AppCompatActivity {
    protected WebView webView;
    protected Button btn_print, btn_change, btn_close;

    protected static final String PDF_PATH = Environment.getExternalStorageDirectory() + "/Download/1229.pdf";

    protected MyHandler handler = new MyHandler(this);

    class MyHandler extends Handler{
        private WeakReference<Activity> activityWeakReference;

        public MyHandler(Activity activity){
            activityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //生成文书ok
                case 0:
                    LoadingDialog.dismiss();
                    printPDF();
                    break;
                //生成文书fail
                case 1:
                    LoadingDialog.dismiss();
                    ToastUtil.showMid("文书有误，无法打印！");
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        findView();

        setOnclick();

        configWebView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //保存pdf的权限
        if (requestCode == 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                createPDF();
            else {
                PermissionUtil.showAlert(PreviewActivity.this, "存储");
            }

        }
    }

    protected void findView(){
        webView = findViewById(R.id.webView);
        btn_print = findViewById(R.id.btn_print);
        btn_change = findViewById(R.id.btn_change);
        btn_close = findViewById(R.id.btn_close);
    }

    protected void setOnclick(){
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SplashActivity.isAvailable(PreviewActivity.this, Constants.PRINT_SOFTWARE_NAME)) {
                    SplashActivity.showPrintDialog(PreviewActivity.this);
                } else {
                    //需要存储pdf的权限
                    if (!PermissionUtil.checkPermissions(PreviewActivity.this
                            , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})){
                        ActivityCompat.requestPermissions(PreviewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }else {
                        LoadingDialog.show(PreviewActivity.this);
                        createPDF();
                    }
                }

            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 配置webView
     */
    private void configWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient());
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        // 扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(10);

        webView.setWebViewClient(new WebViewClient());
    }

    /**
     * 生成PDF-异步
     */
    protected abstract boolean createPDF();
    /**
     * 打印PDF
     */
    protected void printPDF(){
        File file = new File(PDF_PATH);
        ComponentName comp = new ComponentName("com.dynamixsoftware.printershare", "com.dynamixsoftware.printershare.ActivityPrintPDF");
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setComponent(comp);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        this.startActivity(intent);
    }
}
