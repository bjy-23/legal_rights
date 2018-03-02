package com.wonders.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.example.legal_rights.R;

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

    public static void showAlert(Context context, String permissionName){
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append("需要提供 ");
        ssb.append(permissionName + "权限");
        ssb.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.blue_1)), 5, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.alertDialog)
                .setMessage(ssb)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
