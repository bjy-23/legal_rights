package com.wonders.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.legal_rights.R;
import com.wonders.activity.CheckDetailsActivity;
import com.wonders.adapter.BaseAdapter;
import com.wonders.adapter.JgjlAdapter;
import com.wonders.bean.Result;
import com.wonders.constant.Constants;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.bean.JgBean;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bjy on 2018/2/11.
 */

public class JgjlListFragment extends RecyclerViewFragment {
    private final static String TAG = JgjlListFragment.class.getName();
    private ArrayList<JgBean> data;
    private JgjlAdapter adapter;
    private HashMap params;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e(TAG, "onAttach");
        params = (HashMap) getArguments().getSerializable(Constants.PARAMS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
//        params = (HashMap) getArguments().getSerializable(Constants.PARAMS);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG, "onActivityCreated");
        TextView tvTitle = getActivity().findViewById(R.id.tv_title);
        tvTitle.setText("监管记录列表");
        data = new ArrayList<>();
        adapter = new JgjlAdapter(getActivity(), data);
        adapter.setOnClickListener(new BaseAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), CheckDetailsActivity.class);
                intent.putExtra(Constants.PLAN_ID, data.get(position).getPlanId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        getData();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach");
    }

    public void getData(){
        Call<Result<List<JgBean>>> call = Retrofit2Helper.getInstance().querySuperviseRecord(
                Retrofit2Service.QUERY_SUPERVISE_RECORD,params);
        call.enqueue(new Callback<Result<List<JgBean>>>() {
            @Override
            public void onResponse(Call<Result<List<JgBean>>> call, Response<Result<List<JgBean>>> response) {
                LoadingDialog.dismiss();
                if (response.body() != null
                        && response.body().getObject()!= null
                        && response.body().getObject().size() != 0){
                    data.addAll(response.body().getObject());
                    adapter.notifyDataSetChanged();
                }else {
                    ToastUtil.show("没有找到相关的信息");
                }
            }

            @Override
            public void onFailure(Call<Result<List<JgBean>>> call, Throwable t) {
                LoadingDialog.dismiss();
                if (!call.isCanceled()){
                    ToastUtil.show(getResources().getString(R.string.error_server));
                }
            }
        });
        calls.add(call);
    }
}
