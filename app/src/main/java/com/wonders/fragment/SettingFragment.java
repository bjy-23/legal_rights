package com.wonders.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legal_rights.R;
import com.wonders.activity.AboutActivity;
import com.wonders.activity.MainActivity;
import com.wonders.activity.SplashActivity;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.http.CookieManager;
import com.wonders.http.Retrofit2Helper;
import com.wonders.bean.EnterpriseBean;
import com.wonders.widget.LoadingDialog;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment implements OnClickListener {
    private static final String TAG = SettingFragment.class.getName();
    private TextView changeSectionTv;
    private TextView exitAccountTv;
    private TextView picShowTv;
    private TextView clearTv;
    private TextView aboutUsTv;
    private TextView draftTv;
    private TextView updateTv;
    private TextView model_btn;
    private TextView download_btn;

    private AppData appData;
    private DbHelper dbHelper;
    private JSONArray resultList;
    private String pageCount = "";
    private String currPageNo = "";
    private JSONObject checkTypeMap;
    private JSONObject certTypeMap;

    private NotificationManager manager;
    private Notification notify;
    private int progress = 0;

    private final static int HANDLE_NUMBER_1 = 1;
    private final static int HANDLE_NUMBER_2 = 2;
    private final static int HANDLE_NUMBER_3 = 3;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLE_NUMBER_2:
                    Notification notify = new Notification.Builder(getActivity())
                            .setTicker("数据下载完成")
                            .setContentText("数据下载完成")
                            .setSmallIcon(R.drawable.icon)
                            .setProgress(100, 100, false)
                            .getNotification();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notify.defaults = Notification.DEFAULT_SOUND;
                    manager.notify(1, notify);
                    break;
                case HANDLE_NUMBER_1:
                    notify = new Notification.Builder(getActivity())
                            .setTicker("数据正在下载，请稍等")
                            .setContentText("数据下载" + progress + "%")
                            .setSmallIcon(R.drawable.icon)
                            .setProgress(100, progress, false)
                            .getNotification();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
              /* notify.defaults = Notification.DEFAULT_SOUND;*/
                    manager.notify(1, notify);
                    if (Constants.TYPE.equals("")) {
                        getOffLineData(String.valueOf(Integer.parseInt(currPageNo) + 1));
                    }
                    break;
                case HANDLE_NUMBER_3:

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null) {
            appData = (AppData) getActivity().getApplication();
            dbHelper = new DbHelper(getActivity(), DbConstants.TABLENAME, null, 1);

            manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        }

        View view = inflater.inflate(R.layout.fragment_setting, null);
        findView(view);

        return view;
    }

    private void findView(View view) {
        changeSectionTv = (TextView) view.findViewById(R.id.change_section_tv);
        changeSectionTv.setOnClickListener(this);
        exitAccountTv = (TextView) view.findViewById(R.id.exit_account_tv);
        exitAccountTv.setOnClickListener(this);
        picShowTv = (TextView) view.findViewById(R.id.show_pic_tv);
        picShowTv.setOnClickListener(this);
        clearTv = (TextView) view.findViewById(R.id.clear_pic_tv);
        clearTv.setOnClickListener(this);
        aboutUsTv = (TextView) view.findViewById(R.id.about_us_tv);
        aboutUsTv.setOnClickListener(this);
        updateTv = (TextView) view.findViewById(R.id.version_update);
        updateTv.setOnClickListener(this);
        draftTv = (TextView) view.findViewById(R.id.cgx);
        draftTv.setOnClickListener(this);
        updateTv = (TextView) view.findViewById(R.id.update_btn);
        updateTv.setOnClickListener(this);
        model_btn = (TextView) view.findViewById(R.id.model_btn);
        model_btn.setOnClickListener(this);
        download_btn = (TextView) view.findViewById(R.id.download_btn);
        download_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_section_tv:
                final Dialog dialog_lt = new Dialog(getActivity(), R.style.dialog);
                dialog_lt.setContentView(R.layout.dialog_lt_sc);
                dialog_lt.setCancelable(false);

                dialog_lt.show();

                TextView title_tv = (TextView) dialog_lt.findViewById(R.id.title);

                if (Constants.TYPE.equals("")) {
                    title_tv.setText("所在环节：生产");
                } else {
                    title_tv.setText("所在环节：流通");
                }

                Button one_lt = (Button) dialog_lt.findViewById(R.id.btn_one);
                Button two_lt = (Button) dialog_lt.findViewById(R.id.btn_two);
                Button three_lt = (Button) dialog_lt.findViewById(R.id.btn_three);

                one_lt.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constants.TYPE = "";
                        dialog_lt.dismiss();

                        ((MainActivity)getActivity()).change();
                    }
                });

                two_lt.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Constants.TYPE = "lt_";
                        dialog_lt.dismiss();

                        ((MainActivity)getActivity()).change();
                    }

                });

                three_lt.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog_lt.dismiss();

                        return;
                    }
                });

                break;

            case R.id.model_btn:
                final Dialog dialog_model = new Dialog(getActivity(), R.style.dialog);
                dialog_model.setContentView(R.layout.dialog_lt_sc);
                dialog_model.setCancelable(false);

                dialog_model.show();

                TextView title_model = (TextView) dialog_model.findViewById(R.id.title);

                if (appData.isNetWork()) {
                    title_model.setText("所在模式：联网");
                } else {
                    title_model.setText("所在模式：单机");
                }

                Button one_model = (Button) dialog_model.findViewById(R.id.btn_one);
                one_model.setText("联网模式");
                Button two_model = (Button) dialog_model.findViewById(R.id.btn_two);
                two_model.setText("单机模式");
                Button three_model = (Button) dialog_model.findViewById(R.id.btn_three);

                one_model.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        appData.setIsNetWork(true);

                        dialog_model.dismiss();

                        ((MainActivity)getActivity()).change();
                    }
                });

                two_model.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        appData.setIsNetWork(false);

                        dialog_model.dismiss();

                        ((MainActivity)getActivity()).change();
                    }

                });

                three_model.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog_model.dismiss();

                        return;
                    }
                });

                break;

            case R.id.exit_account_tv:
                final Dialog dialog3 = new Dialog(getActivity(), R.style.dialog);
                dialog3.setContentView(R.layout.dialog_exitaccout);
                TextView exitBtn = dialog3.findViewById(R.id.exit_tv);
                TextView cancelBtn = dialog3.findViewById(R.id.cancel_tv);

                cancelBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                    }
                });

                exitBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                        getActivity().finish();
                        CookieManager.removeCookies();
                    }
                });

                dialog3.show();

                break;

            case R.id.show_pic_tv:
                Toast.makeText(getActivity(), "保存设置", Toast.LENGTH_SHORT).show();
                break;

            case R.id.clear_pic_tv:
                Toast.makeText(getActivity(), "清除缓存成功", Toast.LENGTH_SHORT).show();
                break;

            case R.id.about_us_tv:
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);

                break;

            case R.id.version_update:
                SplashActivity.checkUpdate(getActivity(), new SplashActivity.UpdateListener() {

                    public void updateFail() {
                        Toast.makeText(getActivity(), "版本更新失败!", Toast.LENGTH_SHORT).show();
                    }

                    public void updateNoNeed() {
                        Toast.makeText(getActivity(), "当前版本为最新版本", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.download_btn:
                if (appData.isNetWork()) {
                    if (Constants.TYPE == "") {
                        notify = new Notification.Builder(getActivity())
                                .setTicker("数据正在下载，请稍等")
                                .setContentText("数据下载" + progress + "%")
                                .setSmallIcon(R.drawable.icon)
                                .setProgress(100, progress, false)
                                .getNotification();
                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                        notify.defaults = Notification.DEFAULT_SOUND;
                        manager.notify(1, notify);
                        getOffLineData("1");
                    } else {
                        final SharedPreferences sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
                        final String organId = sp.getString(appData.getLoginBean().getUserName(), "123");
                        Log.d(appData.getLoginBean().getUserName(), organId);

                        notify = new Notification.Builder(getActivity())
                                .setTicker("数据正在下载，请稍等")
                                .setContentText("数据下载" + progress + "%")
                                .setSmallIcon(R.drawable.icon)
                                .setProgress(100, progress, false)
                                .getNotification();
                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                        notify.defaults = Notification.DEFAULT_SOUND;
                        manager.notify(1, notify);
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    /*getFileFromServer("http://180.166.102.48:80/android/legal_rights.apk",organId);*/
                                    getFileFromServer(organId);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }

                } else {
                    Toast.makeText(getActivity(), "单机模式，无法下载！", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.update_btn:

                break;

            default:
                break;
        }

    }

    public void getOffLineData(final String pageNo) {
        Call<ResponseBody> call = Retrofit2Helper.getInstance().queryDataInfoOffline(pageNo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = "";
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject jsonObject = null;
                JSONObject jsonObject0 = null;
                JSONObject jsonObject1 = null;
                try {
                    jsonObject = new JSONObject(result);
                    jsonObject0 = new JSONObject(jsonObject.getString("object"));
                    checkTypeMap = jsonObject0.getJSONObject("checkTypeMap");
                    certTypeMap = jsonObject0.getJSONObject("certTypeMap");
                    jsonObject1 = new JSONObject(jsonObject0.getString("result"));
                    pageCount = jsonObject1.getString("pageCount");
                    currPageNo = jsonObject1.getString("currPageNo");
                    resultList = jsonObject1.getJSONArray("resultList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject == null) {
                    Toast.makeText(getActivity(), "服务器数据有误，请重新下载", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pageCount.equals("0")) {
                    Toast.makeText(getActivity(), "没有相关的数据要下载", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread() {
                    public void run() {
                        for (int i = 0; i < resultList.length(); i++) {
                            EnterpriseBean bean = new EnterpriseBean();
                            bean.setEtpsType("sc");
                            JSONObject jsonObject1 = null;
                            JSONObject jsonObject2 = new JSONObject();
                            JSONArray jsonArray = null;
                            JSONObject jsonObject3 = null;
                            try {
                                jsonObject1 = resultList.getJSONObject(i);
                                if (jsonObject1.get("grade").equals(null)) {
                                    bean.setGrade("");
                                } else {
                                    bean.setGrade(jsonObject1.getString("grade"));
                                }
                                if (jsonObject1.get("frequency").equals(null)) {
                                    bean.setFrequency("");
                                } else {
                                    bean.setFrequency(jsonObject1.getString("frequency"));
                                }
                                bean.setEtpsName(jsonObject1.getString("etpsName"));
                                bean.setEtpsId(jsonObject1.getString("etpsId"));
                                bean.setAddress(jsonObject1.getString("address"));
                                bean.setExeOrgan(jsonObject1.getString("organName"));

                                jsonObject2.put("etpsName", jsonObject1.getString("etpsName"));
                                jsonObject2.put("factoryAddr", jsonObject1.getString("address"));
                                if (jsonObject1.get("personName").equals(null)) {
                                    jsonObject2.put("legalPerson", "");
                                } else {
                                    jsonObject2.put("legalPerson", jsonObject1.getString("personName"));
                                }
                                if (jsonObject1.get("telephone").equals(null)) {
                                    jsonObject2.put("phoneNo", "");
                                } else {
                                    jsonObject2.put("phoneNo", jsonObject1.getString("telephone"));
                                }
                                jsonArray = jsonObject1.getJSONArray("fpsiCertInfoList");
                                JSONArray jsonArray1 = new JSONArray();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    jsonObject3 = jsonArray.getJSONObject(j);
                                    JSONObject jsonObject4 = new JSONObject();
                                    jsonObject4.put("address", jsonObject3.getString("addr"));
                                    jsonObject4.put("certNo", jsonObject3.getString("certNo"));
                                    jsonObject4.put("certType", certTypeMap.getString(jsonObject3.getString("certType")));
                                    jsonObject4.put("checkType", checkTypeMap.getString(jsonObject3.getString("checkType")));
                                    Date date = new Date(jsonObject3.getLong("expireDate"));
                                    String expireDate = dateFormat.format(date);
                                    jsonObject4.put("expireDate", expireDate);
                                    jsonObject4.put("etpsName", jsonObject3.getString("etpsName"));
                                    jsonObject4.put("factoryAddr", jsonObject3.getString("factoryAddr"));
                                    if (jsonObject3.get("provideOrgan").equals(null)) {
                                        jsonObject4.put("fzOrgan", "");
                                    } else {
                                        jsonObject4.put("fzOrgan", jsonObject3.getString("provideOrgan"));
                                    }
                                    jsonObject4.put("productName", jsonObject3.getString("productName"));

                                    jsonArray1.put(jsonObject4);
                                }
                                jsonObject2.put("certificateInfos", jsonArray1);
                                bean.setEtpsInfo(jsonObject2.toString());

                                JSONObject jsonObject5 = new JSONObject(jsonObject1.getString("superviseRecord"));
                                JSONObject jsonObject6 = new JSONObject();

                                if (jsonObject5.get("startDate").equals(null)) {
                                    jsonObject6.put("checkDate", "");
                                } else {
                                    Date date1 = new Date(jsonObject5.getLong("startDate"));
                                    String checkDate = dateFormat.format(date1);
                                    jsonObject6.put("checkDate", checkDate);
                                }

                                if (jsonObject5.get("checkDate").equals(null)) {
                                    jsonObject6.put("submitDate", "");
                                } else {
                                    Date date2 = new Date(jsonObject5.getLong("checkDate"));
                                    String submitDate = dateFormat.format(date2);
                                    jsonObject6.put("submitDate", submitDate);
                                }

                                if (jsonObject5.get("result").equals("1")) {
                                    jsonObject6.put("checkResult", "发现问题");
                                } else {
                                    jsonObject6.put("checkResult", "未发现问题");
                                }
                                jsonObject6.put("planId", jsonObject5.get("planId"));
                                jsonObject6.put("checkPerson", jsonObject5.get("userName"));

                                JSONArray jsonArray2 = new JSONArray();
                                jsonArray2.put(jsonObject6);
                                bean.setRecordInfo(jsonArray2.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dbHelper.insertEnterpriseBean(bean);
                        }

                        if (!currPageNo.equals(pageCount)) {
                            Message message = new Message();
                            progress = (int) (((double) Integer.parseInt(currPageNo) / Integer.parseInt(pageCount)) * 100);
                            int a = (int) (((double) Integer.parseInt(currPageNo) / Integer.parseInt(pageCount)) * 100);
                            message.what = HANDLE_NUMBER_1;
                            message.obj = a;

                            handler.sendMessage(message);
                        } else {
                            handler.sendEmptyMessage(HANDLE_NUMBER_2);
                        }
                    }
                }.start();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "服务器连接不上！", Toast.LENGTH_SHORT).show();
                LoadingDialog.dismiss();
            }
        });
    }

    public void getFileFromServer(final String name) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Call<ResponseBody> call = Retrofit2Helper.getInstance().ltQueryDataInfoOfflineDb();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    File f = new File(Environment.getExternalStorageDirectory() + "/生产流通移动执法/");
                    if (!f.exists())
                        f.mkdir();
                    File file = new File(Environment.getExternalStorageDirectory() + "/生产流通移动执法/", name + ".db");

                    InputStream inputStream = null;
                    OutputStream outputStream = null;

                    inputStream = response.body().byteStream();
                    try {
                        outputStream = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    long fileSize = response.body().contentLength();
                    long per = fileSize / 100;
                    long downloadSize = 0;
                    long number = 1;

                    byte[] fileReader = new byte[4096];
                    int read;
                    try {
                        while ((read = inputStream.read(fileReader)) != -1) {
                            outputStream.write(fileReader, 0, read);
                            downloadSize += read;
                            if ((downloadSize / per) > number) {
                                progress = (int) number;
                                handler.sendEmptyMessage(HANDLE_NUMBER_1);
                                number++;
                            }
                        }
                        inputStream.close();
                        outputStream.close();
                        handler.sendEmptyMessage(HANDLE_NUMBER_2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }
}
