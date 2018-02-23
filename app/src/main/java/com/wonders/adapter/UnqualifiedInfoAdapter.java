package com.wonders.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.legal_rights.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by chan on 10/18/16.
 */
public class UnqualifiedInfoAdapter extends BaseAdapter {
    private Context context;
    private JSONArray items;
    private LayoutInflater mInflater;

    public UnqualifiedInfoAdapter(Context context, JSONArray items) {
        this.context = context;
        this.items = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.length();
    }

    @Override
    public JSONArray getItem(int position) {
        try {
            return (JSONArray) items.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_defect_info, parent, false);
            holder.mCheckDate = (TextView) convertView.findViewById(R.id.check_date);
            holder.mCheckNo = (TextView) convertView.findViewById(R.id.check_no);
            holder.mCheckPro = (TextView) convertView.findViewById(R.id.check_pro);
            holder.mCheckRemark = (TextView) convertView.findViewById(R.id.check_remark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.mCheckDate.setText("null".equals(getItem(position).get(0).toString())? "": getItem(position).get(0).toString());
            holder.mCheckNo.setText(getItem(position).get(1).toString());
            holder.mCheckPro.setText(getItem(position).get(2).toString());
            holder.mCheckRemark.setText(getItem(position).get(3).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        private TextView mCheckDate, mCheckNo, mCheckPro, mCheckRemark;
    }

}
