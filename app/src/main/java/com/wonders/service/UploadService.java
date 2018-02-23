package com.wonders.service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.wonders.application.AppData;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.bean.PicBean;

public class UploadService extends Service {
    private Context mContext;

    private Timer mTimer;
    private MyTimerTask mTimerTask;
    private Handler handler;
    private DbHelper dbHelper;
    ArrayList<PicBean> picList;
    private AppData appData;

    private boolean singleTaskFlag = true;

    private static int Session = 10000;
    public static boolean Session_flag = true;

    private int i = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                singleTaskFlag = true;
//                dbHelper.finishPic(msg.what);
//                picList = dbHelper.selectPic1();

//                if (CompanyActivity.isOpen){
//                    CompanyActivity.handler.sendMessage(msg1);
//                }else {
//                    MainActivity.handler.sendMessage(msg1);
//                }

                super.handleMessage(msg);
            }
        };

        dbHelper = new DbHelper(this, DbConstants.TABLENAME, null, 1);
        // dbHelper.deleteTable();
        appData = (AppData) this.getApplication();


        Session_flag = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTimer = new Timer();
        mTimerTask = new MyTimerTask(); // 新建一个任务
        mTimer.scheduleAtFixedRate(mTimerTask, 1000, 5 * 1000);

        return super.onStartCommand(intent, flags, startId);
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
//            final HttpHelper hh = appData.getHttpHelper();
//            final HashMap<String, String> map = new HashMap<String, String>();
//            AppData appdata = (AppData) getApplication();
//
//            picList = dbHelper.selectPic1();
//
//            for (int i = 0; i < picList.size(); i++) {
//                final PicBean picBean = picList.get(i);
//                if (picList.get(i).getIsFinish() == 0 && singleTaskFlag) {
//                    singleTaskFlag = false;
//
//                    final JSONObject result = new JSONObject();
//                    try {
//                        result.put("processId", picList.get(i).getProcessId());
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                    try {
//                        result.put("userId", appdata.getLoginBean().getUserId());
//                    } catch (JSONException e2) {
//                        // TODO Auto-generated catch block
//                        e2.printStackTrace();
//                    }
//
//                    try {
//                        result.put("picNum", picList.get(i).getPicNum());
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    try {
//                        result.put("isProduct", picList.get(i).getIsProduct());
//                    } catch (JSONException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//
//                    File file = new File(picList.get(i).getPath());
//                    if (!file.exists()) {
//                        dbHelper.finishPic(picList.get(i).getId());
//
//                        return;
//                    }
//
//                    String imageUri = "file://" + picList.get(i).getPath();
//                    Log.i("imageUri", imageUri);
//
//                    // ImageSize mImageSize = new ImageSize(720, 1280);
//
//                    ImageLoader.getInstance().loadImage(imageUri,
//                            new ImageLoadingListener() {
//
//                                @Override
//                                public void onLoadingStarted(String arg0,
//                                                             View arg1) {
//
//                                }
//
//                                @Override
//                                public void onLoadingFailed(String arg0,
//                                                            View arg1, FailReason arg2) {
//                                    Log.i("fail", arg2.toString());
//                                }
//
//                                @Override
//                                public void onLoadingComplete(String arg0,
//                                                              View arg1, Bitmap arg2) {
//                                    try {
//                                        result.put("pic", BitmapHelper
//                                                .bitmaptoString(arg2, 100));
//                                    } catch (JSONException e) {
//                                        // TODO Auto-generated catch block
//                                        e.printStackTrace();
//                                    }
//
//                                    map.put("planInfoJson", result.toString());
//                                    NetManager netManager = NetManager
//                                            .getInstance();
//                                    netManager.setSession(hh.SESSION_ID);
//                                    ResultBean resultBean = netManager
//                                            .getResultBean();
//                                    resultBean
//                                            .setConnectMethod(netManager.POST);
//                                    resultBean.setHandler(handler);
//                                    resultBean.setHandlerMsg(picBean.getId());
//                                    resultBean.setParameters(map);
//                                    try {
//                                        if (result.getInt("isProduct") == 1) {
//
//                                            resultBean
//                                                    .setUrl(Constants.CHG_URL
//                                                            + "save_planCheckContentPictureTempBefore");
//                                        } else {
//
//                                            resultBean
//                                                    .setUrl(Constants.CHG_URL
//                                                            + "lt_save_planCheckContentPictureTemp");
//                                        }
//                                    } catch (JSONException e) {
//                                        // TODO Auto-generated catch block
//                                        e.printStackTrace();
//                                    }
//
//                                    Log.i("picresult", resultBean.getUrl());
//
//                                    netManager.execute(resultBean);
//
//                                    Log.i("compate", "compate");
//                                }
//
//                                @Override
//                                public void onLoadingCancelled(String arg0,
//                                                               View arg1) {
//                                    // TODO Auto-generated method stub
//
//                                }
//                            });
//
//                    break;
//                }
//            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mTimer.cancel();
    }
}
