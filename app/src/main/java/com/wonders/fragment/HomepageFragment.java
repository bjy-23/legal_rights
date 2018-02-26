package com.wonders.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.legal_rights.R;
import com.wonders.bean.Result;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.util.FragmentUtil;
import com.wonders.http.Retrofit2Helper;
import com.wonders.bean.Db_message;
import com.wonders.widget.LoadingDialog;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomepageFragment extends Fragment {
    private static final String TAG = HomepageFragment.class.getName();

    private LinearLayout rcjcBtn;
    private LinearLayout hfrwBtn;
    private LinearLayout zxjcBtn;
    private LinearLayout lsrwBtn;

    private TextView rcjcTv;
    private TextView hfrwTv;
    private TextView zxjcTv;
    private FragmentManager fm;
    private Bundle bundle = new Bundle();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        fm = getFragmentManager();
        View view = inflater.inflate(R.layout.fragment_homepage, null);

        findView(view);
        getData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    private void findView(View view) {
        rcjcTv = (TextView) view.findViewById(R.id.rcjc_tv);
        hfrwTv = (TextView) view.findViewById(R.id.hfrw_tv);
        zxjcTv = (TextView) view.findViewById(R.id.zxjc_tv);

        rcjcBtn = (LinearLayout) view.findViewById(R.id.rcxc_layout);
        rcjcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbsxFragment dbsxFragment = new DbsxFragment();
                bundle.putString("type", Constants.RCJC);
                dbsxFragment.setArguments(bundle);
                FragmentUtil.replaceStack(fm, dbsxFragment, R.id.fragment);
            }
        });

        hfrwBtn = (LinearLayout) view.findViewById(R.id.hfrw_layout);
        hfrwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbsxFragment dbsxFragment = new DbsxFragment();
                bundle.putString("type", Constants.HFRW);
                dbsxFragment.setArguments(bundle);
                FragmentUtil.replaceStack(fm, dbsxFragment, R.id.fragment);
            }
        });

        zxjcBtn = (LinearLayout) view.findViewById(R.id.zxjc_layout);
        zxjcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        lsrwBtn = (LinearLayout) view.findViewById(R.id.lsrw_layout);
        lsrwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QyxxGlFragment fqy = new QyxxGlFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.TITLE, getResources().getString(R.string.enterpriseInformationQuery));
                fqy.setArguments(bundle);
                FragmentUtil.replaceStack(fm, fqy, R.id.fragment);
            }
        });
    }

    public void getData() {
        LoadingDialog.show(getActivity());
        if (AppData.getInstance().isNetWork()) {
            Call<Result<String>> call = Retrofit2Helper.getInstance().getCheckNumber();
            call.enqueue(new Callback<Result<String>>() {
                @Override
                public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                    String checkNum = response.body().getObject();
                    if (!TextUtils.isEmpty(checkNum)) {
                        String[] list = checkNum.split(",");
                        if (list.length > 6) {
                            if (Constants.TYPE.equals("")) {
                                rcjcTv.setText("日常检查(" + list[0] + ")");
                                hfrwTv.setText("回访任务(" + list[1] + ")");
                                zxjcTv.setText("专项检查(" + list[2] + ")");
                            } else {
                                rcjcTv.setText("日常检查(" + list[4] + ")");
                                hfrwTv.setText("回访任务(" + list[5] + ")");
                                zxjcTv.setText("专项检查(" + list[6] + ")");
                            }
                        }
                    }
                    LoadingDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Result<String>> call, Throwable t) {
                    LoadingDialog.dismiss();
                }
            });
        } else {
            DbHelper dbHelper = new DbHelper(AppData.getInstance(), DbConstants.TABLENAME, null, 1);
            String isLt = "";
            if (Constants.TYPE.equals("")) {
                isLt = "0";
            } else {
                isLt = "1";
            }
            ArrayList<Db_message> list = dbHelper.queryDbMessageAll(isLt, AppData.getInstance().getLoginBean().getUserId());

            int rcjcSum = 0;
            int hfrwSum = 0;
            int zxjcSum = 0;

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getType().equals("2")) {
                    rcjcSum++;
                } else if (list.get(i).getType().equals("3")) {
                    zxjcSum++;
                } else if (list.get(i).getType().equals("4")) {
                    hfrwSum++;
                }
            }
            rcjcTv.setText("日常检查(" + rcjcSum + ")");
            hfrwTv.setText("回访任务(" + hfrwSum + ")");
            zxjcTv.setText("专项检查(" + zxjcSum + ")");

            LoadingDialog.dismiss();
        }
    }
}
