package com.wonders.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.wonders.application.AppData;

public class AboutActivity extends BaseActivity {
	private TextView edtion_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		edtion_name = (TextView) findViewById(R.id.edtion_name);
		edtion_name.setText(AppData.getAppVersion());
		tvTitle.setText("关于");
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_about;
	}
}
