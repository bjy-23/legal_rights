package com.wonders.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.legal_rights.R;
import com.squareup.picasso.Picasso;
import com.wonders.util.BitmapHelper;
import com.wonders.bean.PicBean;
import com.wonders.util.PicUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 1229 on 2016/8/30.
 */
public class ImageGridViewAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<PicBean> arrayList;
    public static int operationPosition = 0;
    public static int type = 0;

    public ImageGridViewAdapter(Context context,ArrayList arrayList){
        this.mContext = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (type) {
            case PicUtil.PIC_SHOW:
                return getConvertView(position, convertView);
            case PicUtil.PIC_ADD:
                if (arrayList.size() - position >= 3) {
                    return convertView;
                } else {
                    return getConvertView(position, convertView);
                }
            case PicUtil.PIC_CHANGE:
                if (position != operationPosition) {
                    return convertView;
                } else {
                    return getConvertView(position, convertView);
                }
            case PicUtil.PIC_DELETE:
                if (position < operationPosition) {
                    return convertView;
                } else {
                    return getConvertView(position, convertView);
                }
            case PicUtil.PIC_LT:
                if (position==operationPosition)
                    return getConvertView(position, convertView);
                else
                    return convertView;
        }
        return convertView;
    }

    private View getConvertView(int position,View convertView){
        convertView = View.inflate(mContext, R.layout.image_item,null);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.img);
        if (arrayList.get(position).getType()==0){
            Bitmap bitmap = BitmapHelper.stringtoBitmap(arrayList.get(position).getPicSource());
            imageView.setImageBitmap(bitmap);
        }else {
            String picPath = arrayList.get(position).getPicPath();
            if(!"".equals(picPath)){
                Picasso.with(mContext)
                        .load(new File(picPath))
                        .skipMemoryCache()
                        .resize(120, 120)
                        .centerCrop()
                        .into(imageView);
            }
        }

        return convertView;
    }

}
