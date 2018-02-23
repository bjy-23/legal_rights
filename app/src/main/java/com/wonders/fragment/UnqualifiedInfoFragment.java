package com.wonders.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legal_rights.R;
import com.wonders.adapter.UnqualifiedInfoAdapter;
import com.wonders.constant.Constants;
import com.wonders.http.Retrofit2Helper;
import com.wonders.widget.LoadingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chan on 10/12/16.
 *
 * modify by bjy 2018-2-9
 * 历史不合格信息
 */
public class UnqualifiedInfoFragment extends Fragment {
    private String etpsId = "";
    private TextView mNoRecord;//无历史信息
    private ListView mListView;
    private boolean isOncreateView,isLoaded;
    private Call<ResponseBody> call;
    private LoadingLayout loadingLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != getArguments()) {
            etpsId = ((HashMap)getArguments().getSerializable(Constants.PARAMS)).get(Constants.ETPS_ID).toString();
        }
        isOncreateView = true;

        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        };
        View view = View.inflate(getActivity(), R.layout.fragment_defect_info,
                null);
        findView(view);

        if(getUserVisibleHint()){
            loadingLayout.setRefreshing(true);
            refreshListener.onRefresh();
        }
        return view;
    }


    private void findView(View view) {
        loadingLayout = (LoadingLayout) view.findViewById(R.id.loading_layout);
        loadingLayout.setOnRefreshListener(refreshListener);
        mNoRecord = (TextView) view.findViewById(R.id.no_record_tv);
        mListView = (ListView) view.findViewById(R.id.lv);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isOncreateView && isVisibleToUser && !isLoaded){
            loadingLayout.setRefreshing(true);
            refreshListener.onRefresh();
        }else if(!isVisibleToUser && call!=null && !isLoaded){
            call.cancel();
            loadingLayout.setRefreshing(false);
        }
    }

    /**
     * 显示没数据
     */
    private void showNoRecord() {
        mNoRecord.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }

    /**
     * 显示列表
     */
    private void showListView() {
        mNoRecord.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    private void getData() {
        call = Retrofit2Helper.getInstance().getUncheckItem(etpsId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String result = "";
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                isLoaded = true;
                loadingLayout.setRefreshing(false);

                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        int code = object.getInt("code");
                        if (code == 0) {
                            JSONArray arr = object.getJSONArray("object");
                            if (arr.length()!=0){
                                mListView.setAdapter(new UnqualifiedInfoAdapter(getActivity(), arr));
                                showListView();
                            }else {
                                showNoRecord();
                            }
                        } else {
                            showNoRecord();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        showNoRecord();
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(
                                        R.string.error_json),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNoRecord();
                    Toast.makeText(
                            getActivity(),
                            getResources().getString(R.string.error_server),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               loadingLayout.setRefreshing(false);
            }
        });
    }
}
