package com.wonders.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.wonders.constant.Constants;
import com.wonders.bean.ZtXkzBean;

import java.util.ArrayList;

/**
 * Created by bjy on 2016/10/9.
 */
public class QyztAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ZtXkzBean> datas;

    public QyztAdapter(Context context,ArrayList<ZtXkzBean> datas){
        this.context = context;
        this.datas = datas;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (Constants.TYPE.equals("")) {
            // 生产环节
            ViewHolder viewHolder = null;

            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_qyxkz,
                        null);

                viewHolder = new ViewHolder();

                viewHolder.zszl = (TextView) convertView
                        .findViewById(R.id.zszl);
                viewHolder.gsmc = (TextView) convertView
                        .findViewById(R.id.gsmc);
                viewHolder.cpmc = (TextView) convertView
                        .findViewById(R.id.cpmc);
                viewHolder.zs = (TextView) convertView
                        .findViewById(R.id.zs);
                viewHolder.scdz = (TextView) convertView
                        .findViewById(R.id.scdz);
                viewHolder.jyfs = (TextView) convertView
                        .findViewById(R.id.jyfs);
                viewHolder.zsbh = (TextView) convertView
                        .findViewById(R.id.zsbh);
                viewHolder.yxqz = (TextView) convertView
                        .findViewById(R.id.yxqz);
                viewHolder.fzjg = (TextView) convertView
                        .findViewById(R.id.fzjg);
                viewHolder.fzrq = (TextView) convertView
                        .findViewById(R.id.fzrq);
                viewHolder.xkfw = (TextView) convertView
                        .findViewById(R.id.xkfw);
                viewHolder.fzr = (TextView) convertView
                        .findViewById(R.id.fzr);
                viewHolder.ztlx = (TextView) convertView
                        .findViewById(R.id.ztlx);
                viewHolder.fzrqLayout = (LinearLayout) convertView
                        .findViewById(R.id.fzrq_linearlayout);
                viewHolder.xkfwLayout = (LinearLayout) convertView
                        .findViewById(R.id.xkfw_linearlayout);
                viewHolder.fzrLayout = (LinearLayout) convertView
                        .findViewById(R.id.fzr_linearlayout);
                viewHolder.ztlxLayout = (LinearLayout) convertView
                        .findViewById(R.id.ztlx_linearlayout);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.zszl.setText(datas.get(position).getCertType());
            viewHolder.gsmc.setText(datas.get(position).getEtpsName());
            viewHolder.cpmc.setText(datas.get(position).getProductName());
            viewHolder.zs.setText(datas.get(position).getAddress());
            viewHolder.scdz.setText(datas.get(position).getFactoryAddr());
            viewHolder.jyfs.setText(datas.get(position).getCheckType());
            viewHolder.zsbh.setText(datas.get(position).getCertNo());
            viewHolder.yxqz.setText(datas.get(position).getExpireDate());
            viewHolder.fzjg.setText(datas.get(position).getFzOrgan());

        } else {
            // 流通环节
            ViewHolder_sc viewHolder = null;

            if (convertView == null) {
                convertView = View.inflate(context,
                        R.layout.item_qyxkz_sc, null);

                viewHolder = new ViewHolder_sc();

                viewHolder.zszl = (TextView) convertView
                        .findViewById(R.id.zszl);
                viewHolder.xkzbh = (TextView) convertView
                        .findViewById(R.id.xkzbh);
                viewHolder.mc = (TextView) convertView
                        .findViewById(R.id.mc);
                viewHolder.jycs = (TextView) convertView
                        .findViewById(R.id.jycs);
                viewHolder.fzr = (TextView) convertView
                        .findViewById(R.id.fzr);
                viewHolder.ztlx = (TextView) convertView
                        .findViewById(R.id.ztlx);
                viewHolder.xkfw = (TextView) convertView
                        .findViewById(R.id.xkfw);
                viewHolder.yxqxqsrq = (TextView) convertView
                        .findViewById(R.id.yxqxqsrq);
                viewHolder.yxqxjzrq = (TextView) convertView
                        .findViewById(R.id.yxqxjzrq);
                viewHolder.fzjg = (TextView) convertView
                        .findViewById(R.id.fzjg);
                viewHolder.fzrq = (TextView) convertView
                        .findViewById(R.id.fzrq);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder_sc) convertView.getTag();
            }

            viewHolder.zszl.setText(datas.get(position).getCertType());
            viewHolder.xkzbh.setText(datas.get(position).getCertNo());
            viewHolder.mc.setText(datas.get(position).getEtpsName());
            viewHolder.jycs.setText(datas.get(position).getAddress());
            viewHolder.fzr.setText(datas.get(position).getPePerson());
            viewHolder.ztlx.setText(datas.get(position).getFdTypeId());
            viewHolder.xkfw.setText(datas.get(position).getTradeScope());
            viewHolder.yxqxqsrq.setText(datas.get(position)
                    .getStartDate());
            viewHolder.yxqxjzrq.setText(datas.get(position).getEndDate());
            viewHolder.fzjg.setText(datas.get(position).getFzOrgan());
            viewHolder.fzrq.setText(datas.get(position).getProvideDate());

        }
        return convertView;
    }

    class ViewHolder {
        TextView zszl;
        TextView gsmc;
        TextView cpmc;
        TextView zs;
        TextView scdz;
        TextView jyfs;
        TextView zsbh;
        TextView yxqz;
        TextView fzjg;
        TextView fzrq;
        TextView xkfw;
        TextView fzr;
        TextView ztlx;

        LinearLayout fzrqLayout;
        LinearLayout xkfwLayout;
        LinearLayout fzrLayout;
        LinearLayout ztlxLayout;
    }

    class ViewHolder_sc {
        TextView zszl;
        TextView xkzbh;
        TextView mc;
        TextView jycs;
        TextView fzr;
        TextView ztlx;
        TextView xkfw;
        TextView yxqxqsrq;
        TextView yxqxjzrq;
        TextView fzjg;
        TextView fzrq;
    }
}
