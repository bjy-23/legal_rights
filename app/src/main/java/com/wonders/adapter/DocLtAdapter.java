package com.wonders.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.wonders.bean.WsdyBean;

import java.util.List;

/**
 * Created by BJY on 2018/2/2.
 */

public class DocLtAdapter extends BaseAdapter {

    public DocLtAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_doc_print_lt, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        WsdyBean wsdyBean = (WsdyBean) data.get(position);

        viewHolder.qymc.setText(wsdyBean.getEtpsName());
        viewHolder.qylx.setText(wsdyBean.getPlanType());
        viewHolder.rq.setText(wsdyBean.getPlanYearAndMonth());
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null){
                    onClickListener.onItemClick(position);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView qymc;
        TextView qylx;
        TextView rq;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            qymc = itemView.findViewById(R.id.qymc);
            qylx = itemView.findViewById(R.id.wslx);
            rq = itemView.findViewById(R.id.rq);
        }
    }
}
