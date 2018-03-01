package com.wonders.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.legal_rights.R;
import com.wonders.activity.ImageShowActivity;
import com.wonders.adapter.ImageGridViewAdapter;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.bean.PicBean;

/**
 * Created by bjy on 2016/9/13.
 */
public class PicUtil {
    private Context context;

    //生产的图片操作
    public final static int PIC_SHOW = 0;
    public final static int PIC_ADD = 1;
    public final static int PIC_CHANGE = 2;
    public final static int PIC_DELETE = 3;
    //流通图片操作
    public final static int PIC_LT = 4;

    public final static int PIC_MAX_SC = 9;
    public final static int PIC_MAX_LT = 3;

    private PicListener picListener;

    public PicUtil(Context context){
        this.context = context;
    }

    public void imgDialogShow(final int position, final int size, final PicBean bean){
        final Dialog dialog = new Dialog(context,
                R.style.dialog);
        dialog.setContentView(R.layout.dialog_crema_dialog);
        dialog.setCancelable(false);
        dialog.show();

        Button btn_datu =  (Button)dialog.findViewById(R.id.btn_datu);
        btn_datu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(bean.getPicPath())){
                    if (bean.getType()==0){
                        if (bean.getModel()==0){

                        }else {
                            imageShow(bean);
                        }
                    }else {
                        imageShow(bean);
                    }
                }
                dialog.dismiss();
            }
        });

        Button btn_shanchu = (Button)dialog.findViewById(R.id.btn_shanchu);
        btn_shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(Constants.TYPE))
                    ImageGridViewAdapter.type = PIC_DELETE;
                else
                    ImageGridViewAdapter.type = PIC_LT;

                ImageGridViewAdapter.operationPosition = position;
                if(picListener!=null){
                    picListener.deletePic(position);
                }
                dialog.dismiss();
            }
        });

        if("".equals(bean.getPicPath())){
            btn_datu.setVisibility(View.GONE);
            btn_shanchu.setVisibility(View.GONE);
        }

        Button btn_paizhao = (Button) dialog.findViewById(R.id.btn_paizhao);
        btn_paizhao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if("".equals(Constants.TYPE)){
                    if(size-position==1&&size!= PIC_MAX_SC){
                        ImageGridViewAdapter.type = PIC_ADD;
                    }else {
                        ImageGridViewAdapter.type = PIC_CHANGE;
                        ImageGridViewAdapter.operationPosition = position;
                    }
                }else {
                    ImageGridViewAdapter.type = PIC_LT;
                    ImageGridViewAdapter.operationPosition = position;
                }

                picListener.paiZhao(position);

                dialog.dismiss();
            }
        });

        Button btn_xuanqu = (Button) dialog.findViewById(R.id.btn_xuanqu);
        btn_xuanqu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("".equals(Constants.TYPE)){
                    if(size-position==1&&size!= PIC_MAX_SC){
                        ImageGridViewAdapter.type = PIC_ADD;
                    }else {
                        ImageGridViewAdapter.type = PIC_CHANGE;
                        ImageGridViewAdapter.operationPosition = position;
                    }
                }else {
                    ImageGridViewAdapter.type = PIC_LT;
                    ImageGridViewAdapter.operationPosition = position;
                }
                picListener.xuanQu(position+100);
                dialog.dismiss();
            }
        });

        Button btn_quxiao = (Button) dialog.findViewById(R.id.btn_quxaio);
        btn_quxiao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public interface PicListener{
        void deletePic(int position);

        void paiZhao(int position);

        void xuanQu(int position);
    }

    public void setPicListener(PicListener picListener){
        this.picListener = picListener;
    }

    public BitmapFactory.Options setOptions(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        WindowManager wm = (WindowManager) AppData.getInstance().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int heigt = wm.getDefaultDisplay().getHeight();
        options.inSampleSize = calculateInsampleSize(options,width,heigt);
        options.inJustDecodeBounds = false;
        options.inPurgeable=true;

        return options;
    }

    public static int calculateInsampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        final int width  = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if(width > reqWidth || height > reqHeight){
            final int halWidth = width / 2;
            final int halHeight = height / 2;
            while ((halWidth / inSampleSize) >=reqWidth &&(halHeight / inSampleSize) >= reqHeight ){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public void imageShow(PicBean bean){
        Intent intent = new Intent(context, ImageShowActivity.class);
        intent.putExtra("picBean",bean);
        context.startActivity(intent);
    }

}
