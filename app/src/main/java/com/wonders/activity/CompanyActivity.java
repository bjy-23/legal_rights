package com.wonders.activity;

import com.example.legal_rights.R;
import com.wonders.adapter.ViewPagerAdapter;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.fragment.CheckTypeInFragment;
import com.wonders.fragment.DiyFragment;
import com.wonders.fragment.UnqualifiedInfoFragment;
import com.wonders.fragment.CheckRecordFragment;
import com.wonders.fragment.CheckInfoFragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class CompanyActivity extends AppCompatActivity {
    private RelativeLayout layout_home;
    private RelativeLayout layout_back;
    private TextView titleTv;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    public int taskType;//0:日常检查；1：临时任务
    private HashMap params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        params = (HashMap) getIntent().getSerializableExtra(Constants.PARAMS);

        taskType = (int) params.get(Constants.TASK_TYPE);
        if (taskType == 1){
            params.put(Constants.PLAN_ID,System.currentTimeMillis() + "");
        }

        findView();

        initData();
    }

    private void findView() {
        layout_home = (RelativeLayout)findViewById(R.id.layout_home);
        layout_home.setVisibility(View.INVISIBLE);

        layout_back = (RelativeLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        titleTv = (TextView) findViewById(R.id.tv_title);
        titleTv.setText((String)params.get(Constants.ETPS_NAME));
    }

    public void initData(){
        ArrayList<String> titles = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS, params);

        CheckInfoFragment mCheckInfoFragment = new CheckInfoFragment();
        mCheckInfoFragment.setArguments(bundle);
        fragments.add(mCheckInfoFragment);
        titles.add("检查信息");

        CheckRecordFragment mCheckRecordFragment = new CheckRecordFragment();
        mCheckRecordFragment.setArguments(bundle);
        fragments.add(mCheckRecordFragment);
        titles.add("监管记录");

        if (taskType==0){
            CheckTypeInFragment mCheckTypeInFragment = new CheckTypeInFragment();
            mCheckTypeInFragment.setArguments(bundle);
            fragments.add(mCheckTypeInFragment);
            titles.add("巡查录入");

            if("".equals(Constants.TYPE)&& AppData.getInstance().isNetWork()){
                UnqualifiedInfoFragment mUnqualifiedInfoFragment = new UnqualifiedInfoFragment();
                mUnqualifiedInfoFragment.setArguments(bundle);
                fragments.add(mUnqualifiedInfoFragment);
                titles.add("历史不合格信息");
            }
        }else if (taskType == 1){
            bundle.putString("planId",System.currentTimeMillis() + "");
            DiyFragment mDiyFragment = new DiyFragment();
            mDiyFragment.setArguments(bundle);
            fragments.add(mDiyFragment);
            titles.add("巡查录入");
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(mViewPager);
    }
}
