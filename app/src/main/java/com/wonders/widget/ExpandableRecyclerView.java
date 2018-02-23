package com.wonders.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by bjy on 2018/2/17.
 */

public class ExpandableRecyclerView extends RecyclerView {
    public ExpandableRecyclerView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }
}
