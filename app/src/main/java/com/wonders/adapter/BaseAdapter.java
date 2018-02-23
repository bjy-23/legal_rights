package com.wonders.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by BJY on 2018/2/3.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter {
    protected Context context;
    protected List data;//泛型???
    protected OnClickListener onClickListener;

    public BaseAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnClickListener{
        void onItemClick(int position);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
