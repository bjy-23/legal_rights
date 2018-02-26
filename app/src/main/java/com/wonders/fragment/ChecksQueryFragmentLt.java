package com.wonders.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.legal_rights.R;
import com.wonders.constant.Constants;
import com.wonders.util.FragmentUtil;
import com.wonders.widget.LoadingDialog;

import java.util.HashMap;

/**
 * Created by bjy on 2016/12/12.
 */

public class ChecksQueryFragmentLt extends Fragment {
    private EditText jcitemEt;
    private EditText qymcEt;
    private EditText qydzEt;
    private EditText jclxEt;
    private Button searchBtn;

    private int typeFlag = 0;
    private int problemFlag = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_checks_query_lt, null);

        findView(view);

        return view;
    }

    private void findView(View view) {
        jcitemEt = view.findViewById(R.id.jcitem);
        qymcEt = view.findViewById(R.id.qymc);
        qydzEt = view.findViewById(R.id.qydz);
        jclxEt = view.findViewById(R.id.jclx);
        jcitemEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder bulder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);

                bulder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                            }
                        });

                final String items[] = {"全部", "日常检查", "专项检查", "回访任务", "临时任务"};

                bulder.setSingleChoiceItems(items, typeFlag,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                                typeFlag = which;

                                jcitemEt.setText(items[which]);
                            }
                        });

                bulder.show();
            }
        });
        jclxEt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder bulder = new AlertDialog.Builder(
                        getActivity(), R.style.alertDialog);

                bulder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                            }
                        });

                final String items[] = {"发现问题", "未发现问题", "全部"};

                bulder.setSingleChoiceItems(items, problemFlag,
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
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.show(getActivity());

                HashMap<String, String> params = new HashMap();
                params.put("etpsName", qymcEt.getText().toString());
                params.put("address", qydzEt.getText().toString());

                switch (typeFlag) {
                    case 0:
                        params.put("searchType", "00");
                        break;
                    case 1:
                        params.put("searchType", "01");
                        break;
                    case 2:
                        params.put("searchType", "02");
                        break;
                    case 3:
                        params.put("searchType", "03");
                        break;
                    case 4:
                        params.put("searchType", "99");
                        break;
                }

                switch (problemFlag) {
                    case 0:
                        params.put("resultType", "0");
                        break;

                    case 1:
                        params.put("resultType", "1");
                        break;

                    case 2:
                        params.put("resultType", "");
                        break;
                }

                JgjlListFragment jgjlListFragment = new JgjlListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PARAMS, params);
                jgjlListFragment.setArguments(bundle);
                FragmentUtil.replaceStack(getFragmentManager(), jgjlListFragment, R.id.fragment);
            }
        });
    }

}

