package com.wonders.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.legal_rights.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wonders.adapter.ChecksWriteAdapter;
import com.wonders.bean.Node;
import com.wonders.bean.SopItemBean;
import com.wonders.constant.Constants;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bjy on 2017/3/10.
 *
 */

public class CheckInputFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChecksWriteAdapter adapter;
    private List<Node> data;
    private String planId, etpsId,userId,allUserName;
    private ArrayList<SopItemBean> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_checks_write,container,false);
        findView(view);

        planId = getArguments().getString("planId");
        etpsId = getArguments().getString("etpsId");
        allUserName = getArguments().getString("allUserName");

        getdata();

        return inflater.inflate(R.layout.fragment_checks_write,container,false);
    }

    private void findView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        adapter = new ChecksWriteAdapter(data,getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void getdata(){
        String url = "";
        if ("".equals(Constants.TYPE))
            url = Retrofit2Service.GET_PLAN_CHECK_CONTENT;
        else
            url = Retrofit2Service.LT_GET_PLAN_CHECK_CONTENT;
        Call<ResponseBody> call = Retrofit2Helper.getInstance().getPlanCheckContent(url,planId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = "";
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                try {
                    JSONObject json = new JSONObject(result);
                    if ("".equals(Constants.TYPE)){
                        JSONObject object = json.getJSONObject("object");
                        items = gson.fromJson(object.getString("items"),new TypeToken<ArrayList<SopItemBean>>(){}.getType());
//                            arrayList = gson.fromJson(json.getString("planPic"),new TypeToken<ArrayList<PicBean>>(){}.getType());
                    }else {
                        items = gson.fromJson(json.getString("object"),new TypeToken<ArrayList<SopItemBean>>(){}.getType());
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
