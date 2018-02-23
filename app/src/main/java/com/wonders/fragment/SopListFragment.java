package com.wonders.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wonders.adapter.SopListAdapter;
import com.wonders.bean.SopTitleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjy on 2018/2/12.
 * 巡查录入
 */

public class SopListFragment extends RecyclerViewFragment {
    private List<SopTitleBean> data;
    private SopListAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = new ArrayList<>();
        //封装第一层头部
        addTitle();
    }

    public void addTitle(){
        SopTitleBean bean1 = new SopTitleBean("计划检查项", false);

        SopTitleBean bean2 = new SopTitleBean("新增SOP检查项", true);
    }
}
