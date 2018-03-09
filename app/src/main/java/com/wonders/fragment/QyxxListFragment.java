package com.wonders.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wonders.activity.CompanyActivity;
import com.wonders.activity.PreviewScActivity;
import com.wonders.adapter.BaseAdapter;
import com.wonders.adapter.QyxxAdapter;
import com.wonders.application.AppData;
import com.wonders.bean.SopCheckItemLt;
import com.wonders.constant.Constants;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.bean.EnterpriseBean;
import com.wonders.thread.FastDealExecutor;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bjy on 2018/2/7.
 */

public class QyxxListFragment extends RecyclerViewFragment {
    private static final String TAG = QyxxListFragment.class.getName();
    private List<EnterpriseBean> data;
    private QyxxAdapter adapter;
    private HashMap params;
    private int position;
    private MyHandler handler = new MyHandler(this);

    class MyHandler extends Handler{
        private WeakReference<Fragment> fragmentWeakReference;
        public MyHandler(Fragment fragment) {
            fragmentWeakReference = new WeakReference<Fragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    LoadingDialog.dismiss();
                    showDialog((List<String>) msg.obj);
                    break;
                case 1:
                    LoadingDialog.dismiss();
                    startNext();
                    break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach");
        params = (HashMap) getArguments().getSerializable("params");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView tvTitle = getActivity().findViewById(R.id.tv_title);
        tvTitle.setText("企业列表");
        data = new ArrayList<>();
        adapter = new QyxxAdapter(getActivity(), data);
        adapter.setOnClickListener(new BaseAdapter.OnClickListener() {
            @Override
            public void onItemClick(final int position) {
                QyxxListFragment.this.position = position;
                if ("".equals(Constants.TYPE)) {
                    startNext();
                } else {
                    //获取列表
                    ArrayList<String> list = Hawk.get(Constants.SOP_LT_ITEM);
                    if (list == null || list.size() == 0) {
                        LoadingDialog.show(getActivity(), false);
                        FastDealExecutor.run(new Runnable() {
                            @Override
                            public void run() {
                                List list = new ArrayList<>();
                                ArrayList<SopCheckItemLt> sops = Hawk.get(Constants.SOP_LT);
                                for (SopCheckItemLt sopCheckItemLt : sops) {
                                    list.add(sopCheckItemLt.getDicName());
                                }
                                Hawk.put(Constants.SOP_LT_ITEM, list);
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = list;
                                handler.sendMessage(msg);
                            }
                        });
                    }else {
                        showDialog(list);
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);

        getData();
    }

    public void getData() {
        if (AppData.getInstance().isNetWork())
            getOnlineData();
        else
            getOfflineData();
    }

    public void getOnlineData() {
        String url = "".equals(Constants.TYPE) ? Retrofit2Service.QUERY_ETPS_INFO : Retrofit2Service.LT_QUERY_ETPS_INFO;
        Call<ResponseBody> call = Retrofit2Helper.getInstance().queryEtpsInfo(url, params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = "";
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (json == null) {
                    ToastUtil.show(getResources().getString(R.string.error_json));

                    return;
                }

                JSONArray array = null;
                try {
                    array = new JSONArray(json.getString("object"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (array == null) {
                    ToastUtil.show(getResources().getString(R.string.error_json));

                    return;
                }

                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    EnterpriseBean bean = new EnterpriseBean();
                    try {
                        bean = gson.fromJson(array.getString(i), EnterpriseBean.class);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    data.add(bean);
                }

                if (data.size() != 0) {
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(getResources().getString(R.string.queryNoAnswers));
                }

                LoadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoadingDialog.dismiss();
                if (!call.isCanceled()) {
                    ToastUtil.show(getResources().getString(R.string.error_server));
                }
            }
        });
        calls.add(call);
    }

    public void getOfflineData() {
        EnterpriseBean enterpriseBean = new EnterpriseBean();
        if (Constants.TYPE == "")
            enterpriseBean.setEtpsType("sc");
        else
            enterpriseBean.setEtpsType("lt");

        enterpriseBean.setLicNo(params.get(Constants.LIC_NO).toString());
        enterpriseBean.setAddress(params.get(Constants.ADDRESS).toString());
        enterpriseBean.setEtpsName(params.get(Constants.ETPS_NAME).toString());
        enterpriseBean.setGrade(params.get(Constants.GRADE).toString());
        DbHelper dbHelper = new DbHelper(getActivity(), DbConstants.TABLENAME, null, 1);
        data = dbHelper.queryEnterpriseBean(enterpriseBean);
        if (data.size() != 0) {
            adapter.notifyDataSetChanged();
        } else {
            ToastUtil.show("没有找到相关的信息");
        }
    }

    public void showDialog(List<String> list){
        new AlertDialog.Builder(getActivity(), R.style.alertDialog)
                .setSingleChoiceItems(list.toArray(new String[list.size()]), -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        LoadingDialog.show(getActivity());
                        //每次根据选择重新赋值
                        FastDealExecutor.run(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<SopCheckItemLt> sops = Hawk.get(Constants.SOP_LT);
                                Hawk.put(Constants.SOP_LT_ITEM_LIST, sops.get(which).getDicLtcheckTypes());
                                handler.sendEmptyMessage(1);
                            }
                        });
                        dialog.dismiss();
                    }
                }).create().show();
    }

    public void startNext() {
        params.put(Constants.ETPS_ID, data.get(position).getEtpsId());
        params.put(Constants.ETPS_NAME, data.get(position).getEtpsName());
        params.put(Constants.ALL_USER_NAME, "");
        params.put(Constants.TASK_TYPE, 1);
        Intent intent = new Intent(getActivity(), CompanyActivity.class);
        intent.putExtra(Constants.PARAMS, params);
        startActivity(intent);

        CheckInfoFragment.isTemp = true;
        CheckRecordFragment.isTemp = true;
        PreviewScActivity.checkUnit = data.get(position).getExeOrgan();
    }
}
