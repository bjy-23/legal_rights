package com.wonders.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.wonders.constant.Constants;
import com.wonders.bean.SopListViewBean;

import java.util.ArrayList;

/**
 * Created by 1229 on 2016/5/31.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<SopListViewBean> groupArray;
    private ArrayList<ArrayList<SopListViewBean>> childArray;
    private DeleteListener deleteListener;

    public MyExpandableListAdapter(Context context, ArrayList<SopListViewBean> groupArray,
                                   ArrayList<ArrayList<SopListViewBean>> childArray ){
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.groupArray = groupArray;
        this.childArray = childArray;
    }


    @Override
    public int getGroupCount() {
        if(groupArray!=null){
            return groupArray.size();
        }else {
            return 0;
        }

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(childArray!=null){
            return childArray.get(groupPosition).size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup viewHolderGroup = null;
        if(convertView==null){
            viewHolderGroup = new ViewHolderGroup();
            convertView = View.inflate(mContext, R.layout.item_xclr_group, null);
            viewHolderGroup.textGroup = (TextView) convertView.findViewById(R.id.content);

            convertView.setTag(viewHolderGroup);
        }else {
            viewHolderGroup = (ViewHolderGroup) convertView.getTag();
        }

        String s = groupArray.get(groupPosition).getContent();
        if ("".equals(Constants.TYPE)){
            viewHolderGroup.textGroup.setText(s.substring(1,s.length()));
        }else {
            viewHolderGroup.textGroup.setText(s);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild viewHolderChild = null;
        if (convertView==null){
            viewHolderChild = new ViewHolderChild();
            convertView = View.inflate(mContext, R.layout.item_xclr_cell, null);
            viewHolderChild.textChild = (TextView) convertView.findViewById(R.id.content);
            viewHolderChild.picImg = (ImageView) convertView.findViewById(R.id.pic_img);
            viewHolderChild.penImg = (ImageView) convertView.findViewById(R.id.pen_img);
            viewHolderChild.delBtn = (ImageView) convertView.findViewById(R.id.del_btn);

            convertView.setTag(viewHolderChild);
        }else {
            viewHolderChild = (ViewHolderChild)convertView.getTag();
        }

        String text = childArray.get(groupPosition).get(childPosition).getContent();
        if("".equals(Constants.TYPE)&&childArray.get(groupPosition).get(childPosition).getCheckCode()!=null)
            text = childArray.get(groupPosition).get(childPosition).getCheckCode()+
                    childArray.get(groupPosition).get(childPosition).getContent();


        if("1".equals(childArray.get(groupPosition).get(childPosition).getIsHavePic())){
            viewHolderChild.picImg.setVisibility(View.VISIBLE);
        }else {
            viewHolderChild.picImg.setVisibility(View.INVISIBLE);
        }

        if("0".equals(childArray.get(groupPosition).get(childPosition).getIsKey())){
            if ("1".equals(childArray.get(groupPosition).get(childPosition).getIsEdit())){
                viewHolderChild.penImg.setVisibility(View.VISIBLE);
                if ("1".equals(childArray.get(groupPosition).get(childPosition).getIsPass())){
                    viewHolderChild.textChild.setText(Html.fromHtml(text
                            +"<font color=#71ba2d>(未发现问题)"));
                }else {
                    viewHolderChild.textChild.setText(Html.fromHtml(text
                            +"<font color=red>(发现问题)"));
                }
            }else {
                viewHolderChild.textChild.setText(text);
                viewHolderChild.penImg.setVisibility(View.INVISIBLE);
            }
        }else{
            if ("1".equals(childArray.get(groupPosition).get(childPosition).getIsEdit())){
                viewHolderChild.penImg.setVisibility(View.VISIBLE);
                if ("1".equals(childArray.get(groupPosition).get(childPosition).getIsPass())){
                    viewHolderChild.textChild.setText(Html.fromHtml("<font color=red>"+text+"</font>"
                            +"<font color=#71ba2d>(未发现问题)</font>"));
                }else {
                    viewHolderChild.textChild.setText(Html.fromHtml("<font color=red>"+text+"</font>"
                            +"<font color=red>(发现问题)</font>"));
                }
            }else {
                viewHolderChild.textChild.setText(Html.fromHtml("<font color=red>"+text+"</font>"));
                viewHolderChild.penImg.setVisibility(View.INVISIBLE);
            }
        }

        if(childArray.get(groupPosition).get(childPosition).getKind()==4||
                childArray.get(groupPosition).get(childPosition).getKind()==5){
            viewHolderChild.delBtn.setVisibility(View.VISIBLE);
        }

        final SopListViewBean bean = childArray.get(groupPosition).get(childPosition);

        viewHolderChild.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(deleteListener!=null){
                   deleteListener.deleteData(bean);
               }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface DeleteListener {
        void deleteData(SopListViewBean bean);
    }

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    class ViewHolderGroup{
        TextView textGroup;
    }

    class ViewHolderChild{
        TextView textChild;
        ImageView picImg;
        ImageView penImg;
        ImageView delBtn;
    }
}
