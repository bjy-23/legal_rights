package com.wonders.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.bean.Db_message;
import com.wonders.bean.PlanBean;

import java.util.ArrayList;

/**
 * Created by bjy on 2016/10/13.
 */
public class DbsxAdapter extends RecyclerView.Adapter {
    private static final String TAG = DbsxAdapter.class.getName();
    private ArrayList<PlanBean> data;
    private Context mContext;
    private OnclickListener onclickListener;

    public DbsxAdapter(ArrayList<PlanBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dbsx,parent,false);
        return new DbsxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DbsxViewHolder viewHolder = (DbsxViewHolder) holder;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickListener.click0(position);
            }
        });

        viewHolder.qymc.setText(data.get(position).getEtpsName());
        viewHolder.dd.setText(data.get(position).getAddress());
        viewHolder.jcyf.setText(data.get(position).getPlanMonth()+"月");
        viewHolder.jcry.setText(data.get(position).getAllUserName());

        if (!"".equals(Constants.TYPE)){
            viewHolder.gaozhiye.setVisibility(View.GONE);
        }

        if (AppData.getInstance().isNetWork()) {
            viewHolder.selectLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.selectLayout.setVisibility(View.GONE);
        }

        //检查是不是有本地数据
        DbHelper dbhelper = new DbHelper(mContext, DbConstants.TABLENAME, null, 1);

        Db_message message = dbhelper.query_Db_message(data.get(position).getPlanId());

        // 如果已经下载了并且已经分配了 不能选择
        if (message.getPlanId() != null && !(data.get(position).getType() == 0
                || data.get(position).getType() == 1
                || data.get(position).getType() == 5)) {
            viewHolder.selectImg.setVisibility(View.GONE);
        } else {
            viewHolder.selectImg.setVisibility(View.VISIBLE);
        }

        // 设置图片勾选框
        if (data.get(position).isSelect()) {
            viewHolder.selectImg.setImageResource(R.drawable.choose);
        } else {
            viewHolder.selectImg.setImageResource(R.drawable.unchoose);
        }
        viewHolder.selectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickListener.click1(position);
            }
        });
        viewHolder.gaozhiye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickListener.click2(position);
            }
        });

        if (data.get(position).getShowType()==0){
            viewHolder.disPatch.setVisibility(View.GONE);
            viewHolder.gaozhiye.setVisibility(View.GONE);
        }else {
            if ((data.get(position).getType() == 0
                    || data.get(position).getType() == 1 || data.get(
                    position).getType() == 5)) {
                viewHolder.disPatch.setBackgroundResource(R.drawable.dispatch);
            } else {
                viewHolder.disPatch.setBackgroundResource(R.drawable.undispatch);
                viewHolder.gaozhiye.setVisibility(View.GONE);
            }
        }

        // 是否有管理人
        if (data.get(position).getAllUserName().equals("")) {
            viewHolder.jcryLayout.setVisibility(View.GONE);
        } else {
            viewHolder.jcryLayout.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private class DbsxViewHolder extends RecyclerView.ViewHolder{
        private TextView qymc;
        private TextView dd;
        private TextView jcyf;
        private TextView jcry;
        private TextView gaozhiye;
        private LinearLayout jcryLayout;
        private ImageView selectImg;
        private ImageView disPatch;
        private LinearLayout selectLayout;

        public DbsxViewHolder(View itemView) {
            super(itemView);

            qymc = (TextView) itemView.findViewById(R.id.qymc);
            dd = (TextView) itemView.findViewById(R.id.dz);
            jcry = (TextView) itemView.findViewById(R.id.jcry);
            jcyf = (TextView) itemView.findViewById(R.id.jcyf);
            gaozhiye = (TextView) itemView.findViewById(R.id.gaozhiye);
            jcryLayout = (LinearLayout) itemView
                    .findViewById(R.id.jcry_layout);
            selectImg = (ImageView) itemView
                    .findViewById(R.id.select_img);
            selectLayout = (LinearLayout) itemView
                    .findViewById(R.id.select_layout);
            disPatch = (ImageView) itemView
                    .findViewById(R.id.img_dispatch);

        }
    }

    public void addData(int position,PlanBean planBean){
        data.add(position,planBean);
        notifyItemInserted(position);
    }

    public interface OnclickListener{
        void click0(int position);

        void click1(int position);

        void click2(int position);
    }

    public void setOnclickListener(OnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }
}
