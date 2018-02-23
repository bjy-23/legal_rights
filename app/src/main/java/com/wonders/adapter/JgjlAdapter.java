package com.wonders.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.wonders.bean.JgBean;

import java.util.List;

/**
 * Created by bjy on 2018/2/11.
 */

public class JgjlAdapter extends BaseAdapter {
    public JgjlAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_jg, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        JgBean jgBean = (JgBean) data.get(position);

        viewHolder.qymc.setText(jgBean.getEtpsName());
        viewHolder.qydz.setText(jgBean.getAddress());
        viewHolder.rq.setText(jgBean.getCheckDate());
        viewHolder.jg.setText(jgBean.getCheckType());
        viewHolder.jcry.setText(jgBean.getCheckUser());
        viewHolder.planDate.setText(jgBean.getCreateDate());//流通环节不显示
        switch (jgBean.getPlanType()){
            default:
            case "00":
                viewHolder.jclx.setText("全部");
                break;
            case "01":
                viewHolder.jclx.setText("日常检查");
                break;
            case "02":
                viewHolder.jclx.setText("专项任务");
                break;
            case "03":
                viewHolder.jclx.setText("回访检查");
                break;
            case "99":
                viewHolder.jclx.setText("临时任务");
                break;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView qymc;
        TextView qydz;
        TextView rq;
        TextView jg;
        TextView jcry;
        TextView jclx;
        TextView planDate;
        public ViewHolder(View itemView) {
            super(itemView);

            qymc = itemView.findViewById(R.id.qymc);
            qydz = itemView.findViewById(R.id.qydz);
            rq = itemView.findViewById(R.id.rq);
            jg = itemView.findViewById(R.id.jg);
            jcry = itemView.findViewById(R.id.jcry);
            jclx = itemView.findViewById(R.id.jclx);
            planDate = itemView.findViewById(R.id.planDate);
        }
    }
}
