package com.wonders.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.legal_rights.R;

/**
 * Created by bjy on 2018/2/7.
 */

public class TextBar extends FrameLayout {
    private TextView tvName;
    private TextView tvContent;

    public TextBar(@NonNull Context context) {
        super(context, null);
    }

    public TextBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.text_bar, null);
        tvName = view.findViewById(R.id.tv_name);
        tvContent = view.findViewById(R.id.tv_content);
        addView(view);
    }

    public void setName(String name) {
        this.tvName.setText(name);
    }

    public void setContent(String content) {
        this.tvContent.setText(content);
    }
}
