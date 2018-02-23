package com.wonders.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.legal_rights.R;


/**
 * Created by bjy on 2016/9/18.
 *类似微信的图片查看，尚未
 */
public class PicLookActivitiy extends AppCompatActivity {
    private String picPath = "";

    private ImageView img;
    private LinearLayout layoutImg;
    private RelativeLayout layoutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_piclook);
        picPath = getIntent().getStringExtra("picPath");

        findView();

        setPic(picPath);
    }

    public void findView(){
        img = (ImageView) findViewById(R.id.img);

        layoutTitle = (RelativeLayout)findViewById(R.id.layout_title);

        layoutImg = (LinearLayout)findViewById(R.id.layout_img);
        layoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutTitle.getVisibility()==View.VISIBLE)
                    layoutTitle.setVisibility(View.INVISIBLE);
                else
                    layoutTitle.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setPic(String picPath){
        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
        img.setImageBitmap(bitmap);
    }
}
