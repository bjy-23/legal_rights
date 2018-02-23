package com.wonders.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.wonders.bean.WsdyBean;

import java.util.List;

/**
 * Created by BJY on 2018/2/3.
 */

public class DocScAdapter extends BaseAdapter {

    public DocScAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ws, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        WsdyBean wsdyBean = (WsdyBean) data.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.qymc.setText(wsdyBean.getEtpsName());
        String text = "2".equals(wsdyBean.getCodeType())? "告知页  检查结果记录表  检查监督要点表" : wsdyBean.getPlanType();
        viewHolder.qylx.setText(text);
        if(wsdyBean.getCreateDate()!=null){
            if(wsdyBean.getCreateDate().length()>9)
                viewHolder.rq.setText(wsdyBean.getCreateDate().substring(0,10));
            else
                viewHolder.rq.setText(wsdyBean.getCreateDate());
        }
        if(wsdyBean.getCheckDate()!=null){
            if(wsdyBean.getCheckDate().length()>9)
                viewHolder.tvCheckDate.setText(wsdyBean.getCheckDate().substring(0,10));
            else
                viewHolder.tvCheckDate.setText(wsdyBean.getCheckDate());
        }

        viewHolder.mBtnPrint.setOnClickListener(new View.OnClickListener() {
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
        TextView tvCheckDate;
        Button mBtnPrint;
        public ViewHolder(View itemView) {
            super(itemView);

            qymc = itemView.findViewById(R.id.qymc);
            qylx = itemView.findViewById(R.id.wslx);
            rq = itemView.findViewById(R.id.rq);
            tvCheckDate = itemView.findViewById(R.id.tv_check_date);
            mBtnPrint = itemView.findViewById(R.id.print_btn);
        }
    }

}
