package com.wonders.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.google.gson.Gson;
import com.wonders.activity.CheckDetailsActivity;
import com.wonders.application.AppData;
import com.wonders.bean.Result;
import com.wonders.constant.Constants;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.bean.Db_message;
import com.wonders.bean.EnterpriseBean;
import com.wonders.bean.CheckRecordBean;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;
import com.wonders.widget.LoadingLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*
* 监管记录
* */
public class CheckRecordFragment extends Fragment {
    private Db_message dbMessage;
    private AppData appData;
    private Activity myActivity;
    private String etpsId;
    private ListView lv;
    public static boolean isTemp =false;
    private DbHelper dbHelper;
    private ArrayList<CheckRecordBean> dataList = new ArrayList<CheckRecordBean>();
    private boolean isOncreateView,isLoaded;
    private Call<Result<List<CheckRecordBean>>> call;
    private LoadingLayout loadingLayout;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isOncreateView = true;
        etpsId = ((HashMap)getArguments().getSerializable(Constants.PARAMS)).get(Constants.ETPS_ID).toString();
        if (getActivity() != null) {
            myActivity = getActivity();
            appData = (AppData) myActivity.getApplication();
            dbHelper = new DbHelper(myActivity, DbConstants.TABLENAME,null,1);
        }

        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        };

        View view = View.inflate(getActivity(), R.layout.fragment_qyjg, null);

        findView(view);

        if (getUserVisibleHint()){
            loadingLayout.post(new Runnable() {
                @Override
                public void run() {
                    loadingLayout.setRefreshing(true);
                }
            });
            onRefreshListener.onRefresh();
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
        if (isVisibleToUser && isOncreateView && !isLoaded) {
            loadingLayout.setRefreshing(true);
            onRefreshListener.onRefresh();
        }else if(!isVisibleToUser && call!=null && !isLoaded){
            call.cancel();
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        if (!appData.isNetWork()){
            //单机模式取得数据
            AppData appData = (AppData) myActivity.getApplication();
            JSONArray recordList = null;
            if(!isTemp){
            dbMessage = appData.getDb_message();
            dataList.clear();
            try {
                recordList = new JSONArray(dbMessage.getGet_superviseRecord());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                }
            }else {
                EnterpriseBean bean = null;
                if(Constants.TYPE==""){
                    bean = dbHelper.queryQyxxSc(etpsId);
                }else {
                    bean = dbHelper.queryQyxxLt(etpsId);
                }
                if(bean.getRecordInfo()!=null) {
                    try {
                        recordList = new JSONArray(bean.getRecordInfo());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        recordList = new JSONArray("[]");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (recordList == null) {

                return;
            }

            Gson gson = new Gson();
            for (int i = 0; i < recordList.length(); i++) {
                try {
                    CheckRecordBean record = gson.fromJson(recordList.getString(i),CheckRecordBean.class);
                    dataList.add(record);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (dataList.size() == 0) {
                ToastUtil.show("暂时没有该企业记录");
            }

            adapter.notifyDataSetChanged();

            loadingLayout.setRefreshing(false);
            loadingLayout.setEnabled(false);
            isLoaded = true;


            LoadingDialog.dismiss();

        }else {
            String url = "";
            if ("".equals(Constants.TYPE))
                url = Retrofit2Service.GET_SUPERVISE_RECORD;
            else
                url = Retrofit2Service.LT_GET_SUPERVISE_RECORD;
            call = Retrofit2Helper.getInstance().getSuperviseRecord(url,etpsId);
            call.enqueue(new Callback<Result<List<CheckRecordBean>>>() {
                @Override
                public void onResponse(Call<Result<List<CheckRecordBean>>> call, Response<Result<List<CheckRecordBean>>> response) {
                    if (response.body()!=null){
                        loadingLayout.setRefreshing(false);
                        loadingLayout.setEnabled(false);
                        isLoaded = true;

                        dataList.clear();
                        dataList.addAll(response.body().getObject());
                        if (dataList.size() == 0) {
                            ToastUtil.show("暂时没有该企业记录");
                        }

                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<Result<List<CheckRecordBean>>> call, Throwable t) {

                }
            });
        }
    }

    private void findView(View view) {
        loadingLayout = (LoadingLayout) view.findViewById(R.id.loading_layout);
        loadingLayout.setOnRefreshListener(onRefreshListener);

        lv = (ListView) view.findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                    Intent intent = new Intent(myActivity, CheckDetailsActivity.class);
                    intent.putExtra("planId",dataList.get(position).getPlanId());
                    startActivity(intent);

            }
        });
    }

    private BaseAdapter adapter = new BaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_qyjg,
                        null);

                holder = new ViewHolder();
                holder.jcry = (TextView) convertView.findViewById(R.id.jcry);
                holder.jcjg = (TextView) convertView.findViewById(R.id.jcjg);
                holder.jcrq = (TextView) convertView.findViewById(R.id.jcrq);
                holder.fprq = (TextView) convertView.findViewById(R.id.fprq);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.fprq.setText(dataList.get(position).getSubmitDate());
            holder.jcrq.setText(dataList.get(position).getCheckDate());
            holder.jcjg.setText(dataList.get(position).getCheckResult());
            holder.jcry.setText(dataList.get(position).getCheckPerson());

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }



        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataList.size();
        }
    };

    class ViewHolder {
        TextView jcrq;
        TextView jcjg;
        TextView jcry;
        TextView fprq;
    }

    public void setEtpsId(String etpsId) {
        this.etpsId = etpsId;
    }

}
