package com.wonders.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.wonders.constant.Constants;
import com.wonders.bean.EnterpriseBean;

import java.util.List;

/**
 * Created by bjy on 2018/2/7.
 */

public class QyxxAdapter extends BaseAdapter {
    public QyxxAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_lzjl, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        EnterpriseBean enterpriseBean = (EnterpriseBean) data.get(position);

        viewHolder.qymc.setText(enterpriseBean.getEtpsName());
        viewHolder.qydz.setText(enterpriseBean.getAddress());
        viewHolder.jcjg.setText(enterpriseBean.getGrade());
        viewHolder.jcsj.setText(enterpriseBean.getFrequency());
        if (!Constants.TYPE.equals("")) {
            viewHolder.xkzhLayout.setVisibility(View.VISIBLE);
            viewHolder.xkzh.setText(enterpriseBean.getLicNo());
            viewHolder.layoutTime.setVisibility(View.VISIBLE);
            if (enterpriseBean.getStartDate() != null
                    && enterpriseBean.getStartDate().length()> 9)
                viewHolder.tvStartTime.setText(enterpriseBean.getStartDate().substring(0, 10));
            if (enterpriseBean.getEndDate() != null
                    && enterpriseBean.getEndDate().length()> 9)
                viewHolder.tvEndTime.setText(enterpriseBean.getEndDate().substring(0, 10));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(position);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView qymc;
        TextView jcjg;
        TextView qydz;
        TextView jcsj;
        TextView xkzh;
        TextView tvStartTime;
        TextView tvEndTime;
        LinearLayout xkzhLayout;
        LinearLayout layoutTime;

        public ViewHolder(View itemView) {
            super(itemView);

            qymc = itemView.findViewById(R.id.qymc);
            qydz = itemView.findViewById(R.id.qydz);
            jcsj = itemView.findViewById(R.id.jcsj);
            jcjg = itemView.findViewById(R.id.jcjg);
            xkzh = itemView.findViewById(R.id.xkzh);
            xkzhLayout = itemView.findViewById(R.id.xkzh_layout);
            layoutTime = itemView.findViewById(R.id.layout_time);
            tvStartTime = itemView.findViewById(R.id.tv_start_time);
            tvEndTime = itemView.findViewById(R.id.tv_end_time);
        }
    }
}
