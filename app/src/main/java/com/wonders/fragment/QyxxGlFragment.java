package com.wonders.fragment;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.example.legal_rights.R;
import com.wonders.constant.Constants;
import com.wonders.util.FragmentUtil;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;

public class QyxxGlFragment extends Fragment {
    private static final String TAG = QyxxGlFragment.class.getName();
    private EditText qymcEt;
    private EditText qydzEt;
    private EditText jclxEt;
    private Button searchBtn;
    private LinearLayout xkzhLayout;
    private EditText xkzhEt;

    private int problemFlag = 4;
    private int queryType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        queryType = getArguments().getInt("type", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        queryType = getArguments().getInt("type", 0);
        View view = View.inflate(getActivity(), R.layout.fragment_qyxxgl, null);
        findView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    private void findView(View view) {
        xkzhLayout = (LinearLayout) view.findViewById(R.id.xkzh_layout);
        xkzhEt = (EditText) view.findViewById(R.id.xkzh);

        if (Constants.TYPE.equals("")) {
            xkzhLayout.setVisibility(View.GONE);
        } else {
            xkzhLayout.setVisibility(View.VISIBLE);
        }

        qymcEt = (EditText) view.findViewById(R.id.qymc);
        qydzEt = (EditText) view.findViewById(R.id.qydz);
        jclxEt = (EditText) view.findViewById(R.id.jclx);

        jclxEt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final String items[] = {"A", "B", "C", "D", "全部"};
                AlertDialog.Builder bulder = new AlertDialog.Builder(getActivity(), R.style.alertDialog)
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setSingleChoiceItems(items, problemFlag,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();

                                        problemFlag = which;

                                        jclxEt.setText(items[which]);
                                    }
                                });
                bulder.show();
            }
        });
        searchBtn = (Button) view.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checks()) {
                    LoadingDialog.show(getActivity());

                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put(Constants.ETPS_NAME, qymcEt.getText().toString());
                    params.put(Constants.ADDRESS, qydzEt.getText().toString());
                    params.put(Constants.LIC_NO, xkzhEt.getText().toString());
                    params.put(Constants.QUERY_TYPE, queryType + "");

                    switch (problemFlag) {
                        case 0:
                            params.put(Constants.GRADE, "A");
                            break;

                        case 1:
                            params.put(Constants.GRADE, "B");
                            break;

                        case 2:
                            params.put(Constants.GRADE, "C");
                            break;

                        case 3:
                            params.put(Constants.GRADE, "D");
                            break;

                        case 4:
                            params.put(Constants.GRADE, "");
                            break;
                    }

                    QyxxListFragment qyxxListFragment = new QyxxListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("params", params);
                    qyxxListFragment.setArguments(bundle);

                    FragmentUtil.replaceStack(getFragmentManager(), qyxxListFragment, R.id.fragment);
                }
            }
        });
    }


    //检查参数是否完整
    public boolean checks() {
        if (!"".equals(Constants.TYPE)) {
            if ("".equals(qymcEt.getText().toString().trim())
                    && "".equals(xkzhEt.getText().toString().trim())) {
                ToastUtil.show("请输入许可证号或企业名称");
                return false;
            }
            if (qymcEt.getText().toString().trim().length() < 4
                    && "".equals(xkzhEt.getText().toString().trim())) {
                ToastUtil.show("至少填写企业完整注册名称4个字符");
                return false;
            }
        }
        return true;
    }
}
