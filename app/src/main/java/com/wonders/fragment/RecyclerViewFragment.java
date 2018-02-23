package com.wonders.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.legal_rights.R;
import com.wonders.recyclerView.DiyItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by bjy on 2017/3/28.
 */

public class RecyclerViewFragment extends Fragment {
    private static final String TAG = RecyclerViewFragment.class.getName();
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected final List<Call> calls = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        findView(view);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        //不可见时，销毁请求
        for (Call call: calls){
            call.cancel();
        }
    }

    protected void findView(View view){
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DiyItemDecoration(getActivity(),DiyItemDecoration.VERTICAL_LIST));
    }
}
