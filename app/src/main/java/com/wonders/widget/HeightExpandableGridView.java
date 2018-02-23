package com.wonders.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by 1229 on 2016/8/30.
 */
public class HeightExpandableGridView extends GridView {
    public HeightExpandableGridView(Context context) {
        super(context);
    }

    public HeightExpandableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeightExpandableGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
