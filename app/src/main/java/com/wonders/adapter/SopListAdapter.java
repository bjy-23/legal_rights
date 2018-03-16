package com.wonders.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.legal_rights.R;

import java.util.List;

/**
 * Created by bjy on 2018/2/12.
 */

public class SopListAdapter extends BaseAdapter {

    public SopListAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_sop_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ImageView imgAdd;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            imgAdd = itemView.findViewById(R.id.img_add);
        }
    }

}
