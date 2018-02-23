package com.wonders.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.legal_rights.R;

/**
 * Created by bjy on 2016/10/12.
 */
public class NotesActivity extends AppCompatActivity {
    private TextView note1;
    private TextView note2;

    private int second = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notes);

        getView();

        getData();
    }

    public void getView(){
        note1 = (TextView) findViewById(R.id.note1);
        note2 = (TextView) findViewById(R.id.note2);
    }

    public void getData(){
        note1.setText("     首次录入检查项时记录开始时间和地点，即开始检查");
        note2.setText(second+"秒后自动关闭");
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                second--;
                if (second==0){
                    finish();
                    handler.removeCallbacks(this);
                }else{
                    note2.setText(second+"秒后自动关闭");
                    handler.postDelayed(this,1000);
                }

            }
        };
        handler.postDelayed(runnable,1000);
    }
}
