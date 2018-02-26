package com.wonders.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legal_rights.R;
import com.wonders.bean.LoginBean;
import com.wonders.bean.Result;
import com.wonders.application.AppData;
import com.wonders.bean.UserBean;
import com.wonders.constant.Constants;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.http.Retrofit2Helper;
import com.wonders.thread.LoadDataAsyncTask;
import com.wonders.util.ToastUtil;
import com.wonders.util.WorkStateUtils;
import com.wonders.widget.LoadingDialog;

import java.security.MessageDigest;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 登陆页面
 *
 * @author Administrator
 */

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private final String TAG = getClass().getName();
    private Context mContext;
    private SharedPreferences sp;
    private AppData appData;

    private Button loginBtn;
    private ImageView remPswIv;
    private TextView remPswTv;
    private ImageView autoLoginIv;
    private TextView autoLoginTv;

    private TextView groupEt;
    private TextView moodEt;
    private TextView phoneEt;
    private TextView nameEt;
    private TextView passwordEt;
    private TextView linkEt;
    private ImageView nameClear;
    private ImageView passwordClear;
    private ImageView phoneClear;

    private boolean remPswFlag;
    private boolean autoLoginFlag;
    //0：组员；1：组长
    private int isManager = 0;
    //0是流通环节 1是生产环节
    private int isCreate = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        appData = (AppData) getApplication();

        LoadDataAsyncTask loadDataAsyncTask = new LoadDataAsyncTask(mContext);
        loadDataAsyncTask.execute();

        setContentView(R.layout.activity_login);

        sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        remPswFlag = sp.getBoolean("remPsw", false);
        autoLoginFlag = sp.getBoolean("auto", false);
        isManager = sp.getInt("manager", 1);
        isCreate = sp.getInt("isCreate", 1);
        findView();

        //检测是否有网络 判断是否是单机版还是网络版
        if (WorkStateUtils.GetNetworkType(mContext).equals("没有网络")) {
            appData.setIsNetWork(false);
            moodEt.setText("单机模式");
        } else {
            appData.setIsNetWork(true);
            moodEt.setText("联网模式");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent");
        finish();
    }

    //发生点击事件,没被处理掉，则隐藏输入法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    this.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if (checkInput()){
                    if (!appData.isNetWork()) {
                        sendNativeLoginRequest();
                    } else {
                        getSectionRequest();
                    }
                }
                break;

            case R.id.jzmm_iv:
                changeRem();
                break;

            case R.id.zddl_iv:
                changeAuto();
                break;

            case R.id.et_mood:
                // 联网模式的切换
                final Dialog dialog_lt = new Dialog(LoginActivity.this,
                        R.style.dialog);
                dialog_lt.setContentView(R.layout.dialog_mood);
                dialog_lt.setCancelable(false);

                dialog_lt.show();

                Button one_mood = (Button) dialog_lt.findViewById(R.id.btn_one);
                Button two_mood = (Button) dialog_lt.findViewById(R.id.btn_two);
                Button three_mood = (Button) dialog_lt.findViewById(R.id.btn_three);

                one_mood.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialog_lt.dismiss();
                        moodEt.setText("联网模式");

                        appData.setIsNetWork(true);
                    }
                });

                two_mood.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog_lt.dismiss();
                        moodEt.setText("单机模式");

                        appData.setIsNetWork(false);
                    }
                });

                three_mood.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog_lt.dismiss();

                        return;
                    }
                });

                break;


            case R.id.link:
                // 流通生产环境切换
                final Dialog dialog_mood = new Dialog(LoginActivity.this,
                        R.style.dialog);
                dialog_mood.setContentView(R.layout.dialog_lt_sc);
                dialog_mood.setCancelable(false);

                dialog_mood.show();

                Button one_lt = (Button) dialog_mood.findViewById(R.id.btn_one);
                Button two_lt = (Button) dialog_mood.findViewById(R.id.btn_two);
                Button three_lt = (Button) dialog_mood.findViewById(R.id.btn_three);

                one_lt.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Constants.TYPE = "";
                        dialog_mood.dismiss();

                        linkEt.setText("生产环节");
                        isCreate = 1;
                    }
                });

                two_lt.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Constants.TYPE = "lt_";
                        dialog_mood.dismiss();

                        linkEt.setText("流通环节");
                        isCreate = 0;
                    }
                });

                three_lt.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog_mood.dismiss();

                        return;
                    }
                });

                break;

            case R.id.clear_name_img:
                nameEt.setText("");

                break;

            case R.id.clear_name_password:
                passwordEt.setText("");

                break;

            case R.id.clear_phone_img:
                phoneEt.setText("");

                break;

            case R.id.group:
                final Dialog dialog = new Dialog(LoginActivity.this, R.style.dialog);
                dialog.setContentView(R.layout.dialog_alert_ios);
                dialog.setCancelable(false);

                dialog.show();

                Button one = dialog.findViewById(R.id.btn_one);
                Button two = dialog.findViewById(R.id.btn_two);
                Button three = dialog.findViewById(R.id.btn_three);

                one.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        groupEt.setText("我是组员");
                        isManager = 0;
                        dialog.dismiss();
                    }
                });

                two.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        groupEt.setText("我是组长");
                        isManager = 1;
                        dialog.dismiss();
                    }
                });

                three.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                break;
            default:
                break;
        }
    }

    private void findView() {
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);
        moodEt = (TextView) findViewById(R.id.et_mood);
        moodEt.setOnClickListener(this);
        linkEt = (TextView) findViewById(R.id.link);
        linkEt.setOnClickListener(this);
        remPswIv = (ImageView) findViewById(R.id.jzmm_iv);
        remPswIv.setOnClickListener(this);
        remPswTv = (TextView) findViewById(R.id.jzmm_tv);
        remPswTv.setOnClickListener(this);
        autoLoginIv = (ImageView) findViewById(R.id.zddl_iv);
        autoLoginIv.setOnClickListener(this);
        autoLoginTv = (TextView) findViewById(R.id.zddl_tv);
        autoLoginTv.setOnClickListener(this);
        groupEt = (TextView) findViewById(R.id.group);
        groupEt.setOnClickListener(this);

        nameEt = (TextView) findViewById(R.id.username);
        passwordEt = (TextView) findViewById(R.id.password);
        phoneEt = (TextView) findViewById(R.id.phone_num);
        phoneClear = (ImageView) findViewById(R.id.clear_phone_img);
        phoneClear.setOnClickListener(this);

        nameClear = (ImageView) findViewById(R.id.clear_name_img);
        nameClear.setOnClickListener(this);
        passwordClear = (ImageView) findViewById(R.id.clear_name_password);
        passwordClear.setOnClickListener(this);

        if (remPswFlag) {
            remPswIv.setBackgroundResource(R.drawable.check);
            nameEt.setText(sp.getString("user", ""));
            passwordEt.setText(sp.getString("password", ""));
            phoneEt.setText(sp.getString("phone", ""));
        } else {
            remPswIv.setBackgroundResource(R.drawable.check_blank);
        }
        if (autoLoginFlag) {
            autoLoginIv.setBackgroundResource(R.drawable.check);
        } else {
            autoLoginIv.setBackgroundResource(R.drawable.check_blank);
        }
        if (isManager == 0) {
            groupEt.setText("我是组员");
        } else {
            groupEt.setText("我是组长");
        }

        if (isCreate == 1) {
            linkEt.setText("生产环节");
        } else {
            linkEt.setText("流通环节");
        }
    }

    private void changeRem() {
        remPswFlag = !remPswFlag;
        if (remPswFlag) {
            remPswIv.setBackgroundResource(R.drawable.check);
        } else {
            remPswIv.setBackgroundResource(R.drawable.check_blank);
        }
    }

    private void changeAuto() {
        autoLoginFlag = !autoLoginFlag;
        if (autoLoginFlag) {
            autoLoginIv.setBackgroundResource(R.drawable.check);
        } else {
            autoLoginIv.setBackgroundResource(R.drawable.check_blank);
        }
    }

    /**
     * 单机版发送登陆请求
     */
    private void sendNativeLoginRequest() {

        Editor editor = sp.edit();
        editor.putBoolean("remPsw", remPswFlag);
        editor.putBoolean("auto", autoLoginFlag);
        editor.putString("user", nameEt.getText().toString());
        editor.putString("phone", phoneEt.getText().toString());
        editor.putString("password", passwordEt.getText().toString());
        editor.putInt("manager", isManager);
        editor.putInt("isCreate", isCreate);
        editor.commit();

        // 流通生产
        if (isCreate == 1) {
            Constants.TYPE = "";
        } else {
            Constants.TYPE = "lt_";
        }

        //判断用户名，密码是否与本地的匹配
        DbHelper dbHelper = new DbHelper(mContext, DbConstants.TABLENAME, null, 1);
        UserBean userbean = dbHelper.queryUserInfo(nameEt.getText().toString());

        if (userbean.getLoginName() == null) {
            ToastUtil.show("没有此用户");
            return;
        }

        String nameString = nameEt.getText().toString();
        String passwordString = passwordEt.getText().toString();
        String str2 = md5(passwordString);

        if (nameString.equals(userbean.getLoginName())
                && str2.equals(userbean.getPassword())) {
            LoginBean loginBean = new LoginBean();
            if (isManager == 0) {
                loginBean.setManager(false);
            } else {
                loginBean.setManager(true);
            }
            loginBean.setUserName(userbean.getUserName());
            loginBean.setUserId(userbean.getUserId());

            appData.setLoginBean(loginBean);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        } else if (nameString.equals(userbean.getLoginName())
                && !str2.equals(userbean.getPassword())) {
            ToastUtil.show("用户名密码错误");
        }
    }

    private void getSectionRequest() {
        LoadingDialog.show(LoginActivity.this);

        Editor editor = sp.edit();
        editor.putBoolean("remPsw", remPswFlag);
        editor.putBoolean("auto", autoLoginFlag);
        editor.putString("user", nameEt.getText().toString());
        editor.putString("phone", phoneEt.getText().toString());
        editor.putString("password", passwordEt.getText().toString());
        editor.putInt("manager", isManager);
        editor.putInt("isCreate", isCreate);
        editor.commit();

        if (isCreate == 1) {
            Constants.TYPE = "";
        } else {
            Constants.TYPE = "lt_";
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("portal_login_loginName", nameEt.getText().toString());
        hashMap.put("portal_login_password", passwordEt.getText().toString());
        hashMap.put("loginType", "" + isManager);
        hashMap.put("flag", "android");
        hashMap.put("deptflag", "0");
        hashMap.put("phoneNo", phoneEt.getText().toString());

        Observable<Result<LoginBean>> call = Retrofit2Helper.getInstance().login(hashMap);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<LoginBean>>() {
                    @Override
                    public void onNext(Result<LoginBean> loginBeanResult) {
                        LoginBean loginBean = loginBeanResult.getObject();
                        if (loginBean != null) {
                            if (isManager == 0)
                                loginBean.setManager(false);
                            else
                                loginBean.setManager(true);

                            AppData.getInstance().setLoginBean(loginBean);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.dismiss();
                    }
                });
    }

    /**
     * MD5单向加密，32位，用于加密密码，因为明文密码在信道中传输不安全，明文保存在本地也不安全
     */
    public static String md5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        String jmh = hexValue.toString();

        //以下主要是给加密后每两位后面加一个"-"
        StringBuilder sb = new StringBuilder();
        //  调用String的toCharArray()方法，返回一个char数组
        char ch[] = jmh.toCharArray();

        return jmh;
    }

    public boolean checkInput() {
        if ("".equals(nameEt.getText().toString().trim())) {
            ToastUtil.show("用户名没有填写");
            return false;
        }

        if ("".equals(passwordEt.getText().toString().trim())) {
            ToastUtil.show("密码没有填写");
            return false;
        }

        return true;
    }
}
