package com.wonders.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wonders.bean.SopItemBean;
import com.wonders.constant.Constants;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bjy on 2016/12/12.
 * 监管记录详情
 */

public class CheckDetailsActivity extends BaseActivity {
    private ListView lv;
    private String planId;

    private TextView jcrqTv;
    private TextView jcryTv;
    private ArrayList<SopItemBean> datas = new ArrayList<>();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = CheckDetailsActivity.this;

        initView();
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_details;
    }

    public void initView(){

        tvTitle.setText("监管记录详情");
        jcrqTv = (TextView) findViewById(R.id.jcrq_tv);
        jcryTv = (TextView) findViewById(R.id.jcry_tv);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(mContext, InputLookActivity.class);
                intent.putExtra("sopBean",datas.get(position));
                startActivity(intent);
            }
        });

    }

    public void initData(){
        planId = getIntent().getStringExtra("planId");
        LoadingDialog.show(mContext);
        String url = "";
        if ("".equals(Constants.TYPE)){
            url = Retrofit2Service.GET_PLAN_CHECK_CONTENT_DETAIL;
        }else {
            url = Retrofit2Service.LT_GET_PLAN_CHECK_CONTENT_DETAIL;
        }

        Call<ResponseBody> call = Retrofit2Helper.getInstance().getPlanCheckContentDetail(url,planId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = "";
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                LoadingDialog.dismiss();

                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                JSONObject object = null;

                try {
                    object = new JSONObject(json.getString("object"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (object == null) {
                    return;
                }
                String checkContentDtoList = "";
                try {
                    jcrqTv.setText(object.getString("checkDate"));
                    jcryTv.setText(object.getString("checkUsers"));
                    checkContentDtoList = object.getString("checkContentDtoList");
                } catch (JSONException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }

                Gson gson = new Gson();
                datas = gson.fromJson(checkContentDtoList,new TypeToken<ArrayList<SopItemBean>>(){}.getType());

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoadingDialog.dismiss();
                ToastUtil.show(getResources().getString(R.string.error_server));
            }
        });

    }

    private BaseAdapter adapter = new BaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_xclr_cell, null);

                holder = new ViewHolder();
                holder.contentTv = convertView.findViewById(R.id.content);
                holder.penImg = convertView.findViewById(R.id.pen_img);
                holder.picImg = convertView.findViewById(R.id.pic_img);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            convertView.setBackgroundColor(Color.WHITE);

            SopItemBean itemBean = datas.get(position);
            if ("1".equals(itemBean.getResult())){
                holder.contentTv.setText(Html.fromHtml("\u3000\u3000"+itemBean.getCheckContent()+"<font color=#71ba2d>(未发现问题)"));
            }else {
                holder.contentTv.setText(Html.fromHtml("\u3000\u3000"+itemBean.getCheckContent()+"<font color=red>(发现问题)"));
            }
            if (datas.get(position).getPictureUrl().size()==0)
                holder.picImg.setVisibility(View.GONE);
            else
                holder.picImg.setVisibility(View.VISIBLE);
            holder.penImg.setVisibility(View.GONE);

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return datas.get(position);
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    };

    class ViewHolder {
        TextView contentTv;
        ImageView penImg;
        ImageView picImg;
    }
}
