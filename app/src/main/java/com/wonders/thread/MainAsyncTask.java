package com.wonders.thread;

import android.content.Context;
import android.os.AsyncTask;

import com.wonders.application.AppData;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.bean.SopListViewBean;
import com.wonders.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by bjy on 2017/1/4.
 */

public class MainAsyncTask extends AsyncTask<Void,Void,Boolean> {
    private Context mContext;

    public MainAsyncTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Boolean doInBackground(Void[] params) {
        return new Boolean(checkUploads());
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result)
            ToastUtil.show(mContext, "有离线数据没有上传");
    }

    // 检查是否有本地数据要提交
    private boolean checkUploads(){
        DbHelper dbHelper = new DbHelper(AppData.getInstance(), DbConstants.TABLENAME,null,1);

        //拿到需要做的列表
        ArrayList<SopListViewBean> list = dbHelper.querySops(AppData.getInstance().getLoginBean().getUserId());

        if (list.size() != 0)
            return true;

        return false;
    }
}
