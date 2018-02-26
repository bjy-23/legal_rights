package com.wonders.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.google.gson.Gson;
import com.wonders.adapter.QyztAdapter;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.bean.Db_message;
import com.wonders.bean.EnterpriseBean;
import com.wonders.bean.ZtBaseInfoBean;
import com.wonders.bean.ZtXkzBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
* 检查信息
* */
public class CheckInfoFragment extends Fragment {
    private Db_message dbMessage;

    private Activity myActivity;
    private AppData appData;
    private LinearLayout layoutCompanyInfo;
    private TextView qymc;
    private TextView scdz;
    private TextView fddbr;
    private TextView lxdh;
    private ListView lv;

    private String etpsId;

    private ArrayList<ZtXkzBean> xkzList = new ArrayList<ZtXkzBean>();
    public static ZtBaseInfoBean ztInfo = new ZtBaseInfoBean();
    public static boolean isTemp = false;
    private DbHelper dbHelper;
    private QyztAdapter qyztAdapter;

    private boolean isOncreateView,isLoaded;
    private Call<ResponseBody> call;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private LinearLayout layoutFailure;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isOncreateView = true;
        etpsId = ((HashMap)getArguments().getSerializable(Constants.PARAMS)).get(Constants.ETPS_ID).toString();
        myActivity = getActivity();
        appData = (AppData) myActivity.getApplication();
        dbHelper = new DbHelper(myActivity, DbConstants.TABLENAME, null, 1);
        qyztAdapter = new QyztAdapter(myActivity, xkzList);

        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        };

        View view = inflater.inflate(R.layout.fragment_qyzt, container, false);
        findView(view);

        if (getUserVisibleHint()){
            swipeRefreshLayout.setRefreshing(true);
            refreshListener.onRefresh();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isOncreateView && !isLoaded){
            swipeRefreshLayout.setRefreshing(true);
            refreshListener.onRefresh();
        }else if (!isVisibleToUser && call!=null&& !isLoaded){
            swipeRefreshLayout.setRefreshing(false);
            call.cancel();
        }
    }

    private void findView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.loading_layout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        layoutFailure = (LinearLayout) view.findViewById(R.id.loaded_failure);

        layoutCompanyInfo = (LinearLayout) view.findViewById(R.id.layout_company_info);
        layoutCompanyInfo.setVisibility(View.INVISIBLE);

        qymc = (TextView) view.findViewById(R.id.qymc);
        scdz = (TextView) view.findViewById(R.id.scdz);
        fddbr = (TextView) view.findViewById(R.id.fddbr);
        lxdh = (TextView) view.findViewById(R.id.lxdh);

        lv = (ListView) view.findViewById(R.id.lv);
        lv.setAdapter(qyztAdapter);
    }

    /**
     * 获取数据
     */
    private void getData() {

        JSONObject object = null;
        if (!appData.isNetWork()) {
            //单机版的数据读取
            AppData appData = (AppData) myActivity.getApplication();

            if (!isTemp) {
                dbMessage = appData.getDb_message();
                try {
                    object = new JSONObject(dbMessage.getGet_etpCheckInfo());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (object == null) {
                    layoutFailure.setVisibility(View.VISIBLE);

                    return;
                }
            } else {
                EnterpriseBean enterpriseBean = null;
                if (Constants.TYPE == "") {
                    enterpriseBean = dbHelper.queryQyxxSc(etpsId);
                } else {
                    enterpriseBean = dbHelper.queryQyxxLt(etpsId);
                }
                try {
                    object = new JSONObject(enterpriseBean.getEtpsInfo());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            getZtData(object);

            updateData();

            layoutCompanyInfo.setVisibility(View.VISIBLE);

            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
        } else {
            String url = "";
            if ("".equals(Constants.TYPE))
                url = Retrofit2Service.GET_ETP_CHECK_INFO;
            else
                url = Retrofit2Service.LT_GET_ETP_CHECK_INFO;
            call = Retrofit2Helper.getInstance().getEtpCheckInfo(url,etpsId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    isLoaded = true;
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(false);
                    layoutCompanyInfo.setVisibility(View.VISIBLE);

                    String result = "";
                    try {
                        result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("zt", result);

                    JSONObject json = null;
                    try {
                        json = new JSONObject(result);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (json == null) {

                        layoutFailure.setVisibility(View.VISIBLE);

                        return;
                    }

                    JSONObject object = null;
                    try {
                        object = new JSONObject(json.getString("object"));

                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    if (object == null) {

                        layoutFailure.setVisibility(View.VISIBLE);

                        return;
                    }
                    getZtData(object);

                    updateData();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                   swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    public void getZtData(JSONObject object) {
        Gson gson = new Gson();
        ztInfo = gson.fromJson(object.toString(), ZtBaseInfoBean.class);

        AppData data = (AppData) myActivity.getApplication();

        if (ztInfo.getSopType() != null) {
            data.setSopType(ztInfo.getSopType());
        }

        if(ztInfo.getCertificateInfos()!=null){
            for (ZtXkzBean bean : ztInfo.getCertificateInfos()){
                xkzList.add(bean);
            }
        }
    }

    private void updateData() {
        qymc.setText(ztInfo.getEtpsName() == null ? "" : ztInfo.getEtpsName());
        fddbr.setText(ztInfo.getLegalPerson() == null ? "" : ztInfo.getLegalPerson());
        scdz.setText(ztInfo.getFactoryAddr() == null ? "" : ztInfo.getFactoryAddr());
        lxdh.setText(ztInfo.getPhoneNo() == null ? "" : ztInfo.getPhoneNo());

        qyztAdapter.notifyDataSetChanged();
    }

}
