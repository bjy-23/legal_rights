package com.wonders.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.legal_rights.R;

public abstract class BaseActivity extends AppCompatActivity {
    private RelativeLayout layoutBack;
    private FrameLayout layoutContent;
    protected TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        layoutBack = findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layoutContent = findViewById(R.id.layout_content);
        layoutContent.addView(View.inflate(this, getLayoutId(), null));

        tvTitle = findViewById(R.id.tv_title);
    }

    protected abstract int getLayoutId();
}
