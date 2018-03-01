package com.wonders.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by bjy on 2018/3/1.
 * 6.0权限管理工具
 */

public class PermissionUtil {

    /**
     * 检查是否包含以下权限
     * @param activity
     * @param permissions
     * @return
     */
    public static boolean checkPermissions(Activity activity, String... permissions){
        boolean result = true;
        for (int i=0; i<permissions.length; i++){
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) == PackageManager.PERMISSION_DENIED){
                result = result & false;
                break;
            } else
                result = result & true;
        }
        return result;
    }

    /**
     * 申请权限
     * @param activity
     * @param permissions
     */
    public static void getPermissions(Activity activity, String... permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                permissions[i] = "";
            }else {
                Log.e(activity.getLocalClassName(), permissions[i] + "无权限");
            }
        }
        ActivityCompat.requestPermissions(activity, permissions, 0);
    }

    /**
     * 申请权限
     * @param fragment
     * @param permissions
     */
    public static void getPermissions(Fragment fragment, String... permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(fragment.getActivity(), permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                permissions[i] = "";
            }else {
                Log.e(fragment.getActivity().getLocalClassName(), permissions[i] + "无权限");
            }
        }
        fragment.requestPermissions(permissions, 0);
    }
}
