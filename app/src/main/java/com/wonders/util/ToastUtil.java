package com.wonders.util;

import android.view.Gravity;
import android.widget.Toast;

import com.wonders.application.AppData;

public class ToastUtil {
    public static void show(CharSequence message) {
        Toast.makeText(AppData.getInstance(), message, Toast.LENGTH_SHORT).show();
    }
      
    /** 
     * 居中显示
     */  
    public static void showMid(CharSequence message){
        Toast toast = Toast.makeText(AppData.getInstance(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();  
    }
}
