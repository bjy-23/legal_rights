package com.wonders.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.wonders.bean.SopItemBean;
import com.wonders.util.BitmapUtil;

public class InputLookActivity extends BaseActivity {
    private TextView jcnrTv;
    private TextView jcjgTv;
    private TextView bzTv;

    private SopItemBean sopBean;

    private LinearLayout picLayout;
    private ArrayList<Bitmap> bitmapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sopBean = (SopItemBean) getIntent().getSerializableExtra("sopBean");
        bitmapList = new ArrayList<Bitmap>();

        findView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_input_look;
    }

    @SuppressLint("NewApi")
    private void findView() {
        tvTitle.setText(sopBean.getItemCode());

        jcnrTv = (TextView) findViewById(R.id.jcnr_tv);
        jcnrTv.setText(sopBean.getCheckContent());

        jcjgTv = (TextView) findViewById(R.id.jcjg_tv);
        if ("1".equals(sopBean.getResult())) {
            jcjgTv.setText("未发现问题");
        } else {
            jcjgTv.setText("发现问题");
        }

        bzTv = (TextView) findViewById(R.id.remark_et);
        bzTv.setText(sopBean.getRemark());

        picLayout = (LinearLayout) findViewById(R.id.pic_linearlayout);

        for (int i = 0; i < sopBean.getPictureUrl().size(); i++) {
            ImageView temp = new ImageView(this);
            temp.setTag((i + 1) + "");

            final int finalI = i;
            temp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(160,
                    160);
            p.setMargins(30, 0, 0, 0);
            temp.setLayoutParams(p);
            Bitmap bitmap = BitmapUtil.stringtoBitmap(sopBean.getPictureUrl()
                    .get(i).substring(1));
            bitmapList.add(bitmap);
            temp.setImageBitmap(bitmap);
            temp.setScaleType(ScaleType.CENTER_CROP);

            picLayout.addView(temp);
        }

    }

    @Override
    protected void onDestroy() {
        for (int i = 0; i < bitmapList.size(); i++) {
            bitmapList.get(i).recycle();
        }
        super.onDestroy();
    }
}
