package com.wonders.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import com.example.legal_rights.R;
import com.wonders.activity.PreviewLtActivity;
import com.wonders.adapter.BaseAdapter;
import com.wonders.adapter.DocLtAdapter;
import com.wonders.util.JsonHelper;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.bean.WsdyBean;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bjy on 2016/12/12.
 */

public class DocListLtFragment extends RecyclerViewFragment {
    private DocLtAdapter adapter;
    private List<WsdyBean> data;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = new ArrayList<WsdyBean>();
        adapter = new DocLtAdapter(getActivity(), data);
        adapter.setOnClickListener(new BaseAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), PreviewLtActivity.class);
                intent.putExtra("planId", data.get(position).getPlanId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        getData();
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView tvTitle = getActivity().findViewById(R.id.tv_title);
        tvTitle.setText("文书列表");
    }

    public void getData() {
        HashMap<String, String> params = (HashMap<String, String>) getArguments().getSerializable("params");
        Call<ResponseBody> call = Retrofit2Helper.getInstance().queryPrintDocument(Retrofit2Service.LT_QUERY_PRINT_DOCUMENT, params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                LoadingDialog.dismiss();

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
                    e.printStackTrace();
                }

                if (array == null) {
                    ToastUtil.show(getResources().getString(R.string.error_json));
                    return;
                }

                data.clear();

                for (int i = 0; i < array.length(); i++) {
                    WsdyBean bean = new WsdyBean();
                    try {JsonHelper.parse(bean, array.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    data.add(bean);
                }
                if (data.size() != 0) {
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show("没有找到相关的信息");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoadingDialog.dismiss();
                if (!call.isCanceled()){
                    ToastUtil.show(getResources().getString(R.string.error_server));
                }
            }
        });
        calls.add(call);
    }
}
