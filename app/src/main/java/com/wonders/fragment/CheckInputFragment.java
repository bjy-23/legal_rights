package com.wonders.fragment;

import android.support.v4.app.Fragment;

import com.wonders.bean.PicBean;
import com.wonders.bean.Result;
import com.wonders.constant.Constants;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.thread.FastDealExecutor;
import com.wonders.util.DbHelper;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bjy on 2018/3/15.
 * 巡查录入-日常检查
 */

public abstract class CheckInputFragment extends Fragment{

    public void uploadPics(ArrayList<PicBean> picList) {
        uploadPics(picList, null);
    }

    public void uploadPics(final ArrayList<PicBean> picList, final PicUploadListener picUploadListener) {
        String url = "".equals(Constants.TYPE)? Retrofit2Service.SAVE_PLAN_CHECK_CONTENT_PICTURE_TEMP: Retrofit2Service.LT_SAVE_PLAN_CHECK_CONTENT_PICTURE_TEMP;

        for (int i = 0; i < picList.size(); i++) {
            final PicBean picBean = picList.get(i);
            final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(picBean.getPicPath()));
            MultipartBody.Part part = MultipartBody.Part.createFormData(picBean.getPicName() + "_" + picBean.getPicNum(), "", requestBody);
            Call<Result> call = Retrofit2Helper.getInstance().savePlanCheckContentPictureTemp(url, part);
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if (response.body() != null && response.body().getCode() == 0) {
                        picList.remove(picList.size() - 1);
                        if (picList.size() == 0) {
                            LoadingDialog.dismiss();
                            ToastUtil.show("提交数据成功");
                            if (picUploadListener != null)
                                picUploadListener.success();
                        }

                        deleteUploadedPics(picBean);
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {

                }
            });
        }
    }

    /**
     * 删除已上传的图片-异步
     */
    public void deleteUploadedPics(final PicBean picBean) {
        FastDealExecutor.run(new Runnable() {
            @Override
            public void run() {
                DbHelper.getInstance().deletePicAfterUpload(picBean);
            }
        });
    }


    /**
     * 图片上传的监听器
     */
    public interface PicUploadListener{
        void success();
    }
}
