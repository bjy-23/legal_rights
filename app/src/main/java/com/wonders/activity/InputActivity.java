package com.wonders.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import com.example.legal_rights.R;
import com.wonders.adapter.ViewPagerAdapter;
import com.wonders.constant.Constants;
import com.wonders.fragment.CheckBaseFragment;
import com.wonders.fragment.ItemWriteFragment;
import com.wonders.bean.SopListViewBean;
import java.util.ArrayList;

@SuppressLint("NewApi")
public class InputActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String planId;
    private String etpsId;
    private boolean isDiy = false;
    private int groupPosition;
    private int childPosition;
    private SopListViewBean sopBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isDiy = getIntent().getBooleanExtra("isDiy", false);
        planId = getIntent().getStringExtra("planId");
        etpsId = getIntent().getStringExtra("etpsId");
        sopBean = getIntent().getParcelableExtra("sopBean");
        groupPosition = getIntent().getIntExtra("groupPosition",0);
        childPosition = getIntent().getIntExtra("childPosition",0);

        findView();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_input;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint("ResourceAsColor")
    private void findView() {

        if ("".equals(Constants.TYPE)){
            if (isDiy) {
                tvTitle.setText("自定义检查项");
            } else {
                tvTitle.setText(sopBean.getCheckCode());
            }
        }else {
            tvTitle.setText(sopBean.getItemCode());
        }

        ArrayList<Fragment> fragments = new ArrayList<>();
        ItemWriteFragment fragment1 = new ItemWriteFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putParcelable("sopBean",sopBean);
        bundle1.putString("planId",planId);
        bundle1.putString("etpsId",etpsId);
        bundle1.putInt("groupPosition",groupPosition);
        bundle1.putInt("childPosition",childPosition);
        fragment1.setArguments(bundle1);
        fragments.add(fragment1);
        CheckBaseFragment fragment2 = new CheckBaseFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("sopBean",sopBean);
        bundle2.putBoolean("isDiy",isDiy);
        fragment2.setArguments(bundle2);
        fragments.add(fragment2);
        ArrayList<String> titles = new ArrayList<>();
        titles.add("检查项录入");
        titles.add("检查依据");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments,titles);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
    }
}
