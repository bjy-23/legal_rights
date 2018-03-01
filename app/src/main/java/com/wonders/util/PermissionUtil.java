package com.wonders.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bjy on 2018/3/1.
 * 6.0权限管理工具
 */

public class PermissionUtil {
    private static final String TAG = PermissionUtil.class.getName();
    /**
     * 检查是否包含以下权限
     * @param context
     * @param permissions
     * @return
     */
    public static boolean checkPermissions(Context context, String[] permissions){
        boolean result = true;
        for (int i=0; i<permissions.length; i++){
            if (ActivityCompat.checkSelfPermission(context, permissions[i]) == PackageManager.PERMISSION_DENIED){
                Log.e(TAG, permissions[i] + "无权限");
                result = result & false;
                break;
            } else
                result = result & true;
        }
        return result;
    }

}
