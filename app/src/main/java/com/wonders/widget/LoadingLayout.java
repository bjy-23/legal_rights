package com.wonders.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by bjy on 2017/1/13.
 */

public class LoadingLayout extends SwipeRefreshLayout {
    public LoadingLayout(Context context) {
        super(context);
        init();
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setProgressViewOffset(true,100,200);
        setSize(SwipeRefreshLayout.LARGE);
        setProgressViewEndTarget(true,100);
    }
}
