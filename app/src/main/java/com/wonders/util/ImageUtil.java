package com.wonders.util;

import android.content.Context;

import com.wonders.application.AppData;

/**
 * Created by 1229 on 2016/3/24.
 */
public class ImageUtil {
    private static ImageUtil ImageInstance;
    private Context mcontext;
    private AppData appData;
    private DbHelper dbHelper;
    private int sum = 0;


    private ImageUtil(Context context) {
        this.mcontext = context;
    }

    public static synchronized ImageUtil getInstance(Context context) {
        if (ImageInstance == null) {
            ImageInstance = new ImageUtil(context);
        }
        return ImageInstance;
    }

//    public void uploadImage(final ArrayList<PicBean> picList) {
//
//        dbHelper = new DbHelper(mcontext, DbConstants.TABLENAME, null, 1);
//        appData = AppData.getInstance();
//        HttpHelper hh = appData.getHttpHelper();
//        final AjaxParams ajaxParams = new AjaxParams();
//        int length = 0;
//        for (int i = 0; i < picList.size(); i++) {
//            PicBean picBean = picList.get(i);
//            File file = new File(picBean.getPicPath());
//            length += file.length();
//            if (length < 8388608) {//传输图片的总大小低于8M
//                try {
//                    ajaxParams.put(picBean.getPicName() + "_" + picBean.getPicNum(), file);
//                    sum++;
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                break;
//            }
//        }
//        hh.post(Constants.URL_UPLOAD_PIC, ajaxParams,
//                new AjaxCallBack<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        for (int j = 0; j < sum; j++) {
////                            dbHelper.finishPic(picList.get(j).getId());
//                        }
//                        sum = 0;
//                        ArrayList<PicBean> picList2 = null;
//                        if (picList2 != null && picList2.size() > 0) {
//                            uploadImage(picList2);
//                            Toast.makeText(mcontext, "回调成功", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(mcontext, "提交数据成功", Toast.LENGTH_SHORT).show();
//
//                            MyProgressDialog.dismiss();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t, int errorNo, String strMsg) {
//                        Toast.makeText(mcontext, "提交数据失败，请联系客服111", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                });
//    }

    public void imgDialogShow(){

    }
}
