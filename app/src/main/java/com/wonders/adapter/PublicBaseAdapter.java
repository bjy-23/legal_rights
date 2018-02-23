package com.wonders.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.wonders.bean.WsdyBean;

import java.util.ArrayList;

/**
 * Created by bjy on 2016/9/29.
 */
public class PublicBaseAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<WsdyBean> dataListWSDY;

    private int TYPE = 0;
    private final static int WSDY = 1;//文书打印


    @Override
    public int getCount() {

        return dataListWSDY.size();
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
        switch (TYPE){
            case WSDY:
                ViewHolderWSDY holder = new ViewHolderWSDY();
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_ws,
                            null);

                    holder.qymc = (TextView) convertView.findViewById(R.id.qymc);
                    holder.qylx = (TextView) convertView.findViewById(R.id.wslx);
                    holder.rq = (TextView) convertView.findViewById(R.id.rq);
                    holder.mBtnPrint = (Button) convertView.findViewById(R.id.print_btn);

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolderWSDY) convertView.getTag();
                }
        }

        return null;
    }

    class ViewHolderWSDY {
        TextView qymc;
        TextView qylx;
        TextView rq;
        Button mBtnPrint, mBtnEdit;
    }
}
