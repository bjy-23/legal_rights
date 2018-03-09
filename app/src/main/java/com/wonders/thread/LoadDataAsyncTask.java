package com.wonders.thread;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;
import com.wonders.application.AppData;
import com.wonders.bean.SopCheckItemLt;
import com.wonders.constant.Constants;
import com.wonders.bean.SopBean;
import com.wonders.bean.SopItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by bjy on 2017/1/4.
 * 读取配置文件
 */

public class LoadDataAsyncTask extends AsyncTask {

    private LoadDataAsyncTask() {
    }

    public static void run(){
        new LoadDataAsyncTask().execute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        loadData();
        return null;
    }

    private void loadData() {
        AppData appData = AppData.getInstance();
        AssetManager manager =appData.getAssets();
        // 获取流通字典
        try {
            InputStream stream = manager.open("check_info_lt.txt");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = -1;
            while ((i = stream.read()) != -1) {
                baos.write(i);
            }
            String resultJson = baos.toString("UTF-8");
            JSONObject json = new JSONObject(resultJson);
            JSONArray array = new JSONArray(json.getString("object"));

            for (int j = 0; j < array.length(); j++) {
                JSONObject biginfo = (JSONObject) array.get(j);
                JSONArray arrayBiginfo = new JSONArray(
                        biginfo.getString("bigInfo"));
                Gson gson = new Gson();

                for (int k = 0; k < arrayBiginfo.length(); k++) {

                    SopBean bean = gson.fromJson(arrayBiginfo.getString(k), SopBean.class);
                    JSONObject temp = new JSONObject(
                            arrayBiginfo.getString(k));

                    bean.setItems((ArrayList<SopItemModel>) gson.fromJson(temp.getString("childList"),new TypeToken<ArrayList<SopItemModel>>(){}.getType()));

                    appData.getSopList_lt().get(j).add(bean);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 获取生产字典

        try {
            InputStream stream = manager.open("SOPInfo.txt");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = -1;
            while ((i = stream.read()) != -1) {
                baos.write(i);
            }

            String resultJson = baos.toString("UTF-8");
            JSONObject json = new JSONObject(resultJson);
            JSONArray array = new JSONArray(json.getString("object"));

            Gson gson = new Gson();
            for (int j = 0; j < array.length(); j++) {
                SopBean bean = gson.fromJson(array.getString(j), SopBean.class);
                JSONObject temp = new JSONObject(array.getString(j));

                bean.setItems((ArrayList<SopItemModel>) gson.fromJson(temp.getString("childList"),new TypeToken<ArrayList<SopItemModel>>(){}.getType()));

                appData.getSopList().add(bean);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            InputStream inputStream = manager.open("sop_lt_check_item.txt");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = -1;
            while ((i = inputStream.read()) != -1) {
                baos.write(i);
            }
            String result = baos.toString("UTF-8");
            Gson gson = new Gson();
            ArrayList<SopCheckItemLt> sops = gson.fromJson(result,new TypeToken<ArrayList<SopCheckItemLt>>(){}.getType());

            Hawk.put(Constants.SOP_LT,sops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
