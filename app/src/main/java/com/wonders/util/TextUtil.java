package com.wonders.util;

import android.text.TextUtils;

/**
 * Created by bjy on 2017/3/29.
 */

public class TextUtil {

    public static String transformNul(CharSequence charSequence){
        if (TextUtils.isEmpty(charSequence))
            return "";
        else
            return charSequence.toString();
    }
}
