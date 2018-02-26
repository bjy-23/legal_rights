package com.wonders.fragment;

import java.util.HashMap;
import android.app.AlertDialog;
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
import com.example.legal_rights.R;
import com.wonders.constant.Constants;
import com.wonders.util.FragmentUtil;
import com.wonders.util.DateUtil;
import com.wonders.widget.LoadingDialog;

/*
  文书打印-查询页
 */
public class DocQueryFragment extends Fragment {
    private final static String TAG = DocQueryFragment.class.getName();
    private EditText wslxEt;//文书类型
    private EditText qymcEt;//企业名称
    private EditText jhlxEt;//计划类型
    private EditText jhnyEt;//年份
    private EditText yfEt;//月份
    private Button searchBtn;

    protected HashMap<String, String> params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc_print, container, false);

        initData();

        findView(view);

        return view;
    }

    private void findView(View view) {
        wslxEt = view.findViewById(R.id.wslx);
        qymcEt = view.findViewById(R.id.qymc);
        jhnyEt = view.findViewById(R.id.jhny);
        yfEt = view.findViewById(R.id.yf);
        jhlxEt = view.findViewById(R.id.jhlx);

        jhnyEt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int year = DateUtil.getThisYear();
                final String items[] = new String[3];
                for (int i = 0; i < 3; i++) {
                    items[i] = year - i + "";
                }
                AlertDialog.Builder bulder = new AlertDialog.Builder(getActivity(), R.style.alertDialog)
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setSingleChoiceItems(items, -1,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        params.put("planYear", items[which]);
                                        jhnyEt.setText(items[which]);
                                    }
                                });
                bulder.show();
            }
        });

        yfEt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final String items[] = {"1月", "2月", "3月", "4月", "5月", "6月",
                        "7月", "8月", "9月", "10月", "11月", "12月"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alertDialog)
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setSingleChoiceItems(items, -1,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        switch (which) {
                                            case 0:
                                                params.put("planMonth", "01");
                                                break;

                                            case 1:
                                                params.put("planMonth", "02");
                                                break;

                                            case 2:
                                                params.put("planMonth", "03");
                                                break;

                                            case 3:
                                                params.put("planMonth", "04");
                                                break;

                                            case 4:
                                                params.put("planMonth", "05");
                                                break;

                                            case 5:
                                                params.put("planMonth", "06");
                                                break;
                                            case 6:
                                                params.put("planMonth", "07");
                                                break;

                                            case 7:
                                                params.put("planMonth", "08");
                                                break;

                                            case 8:
                                                params.put("planMonth", "09");
                                                break;

                                            case 9:
                                                params.put("planMonth", "10");
                                                break;

                                            case 10:
                                                params.put("planMonth", "11");
                                                break;

                                            case 11:
                                                params.put("planMonth", "12");
                                                break;

                                            default:
                                                break;
                                        }
                                        yfEt.setText(items[which]);
                                    }
                                });

                builder.show();
            }
        });

        wslxEt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final String items[] = {"现场核查记录"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alertDialog)
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setSingleChoiceItems(items, -1,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        wslxEt.setText(items[which]);
                                    }
                                });
                builder.show();
            }
        });

        jhlxEt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final String items[] = {"日常监督检查", "专项整治", "回访计划", "全部"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alertDialog)
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setSingleChoiceItems(items, -1,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        switch (which) {
                                            default:
                                                break;
                                            case 0:
                                                params.put("planType", "01");
                                                break;
                                            case 1:
                                                params.put("planType", "02");
                                                break;
                                            case 2:
                                                params.put("planType", "03");
                                                break;
                                            case 3:
                                                params.put("planType", "");
                                                break;
                                        }
                                        jhlxEt.setText(items[which]);
                                    }
                                });
                builder.show();
            }
        });
        searchBtn = view.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LoadingDialog.show(getActivity());
                assembleParam();
                Bundle bundle = new Bundle();
                bundle.putSerializable("params", params);
                if ("".equals(Constants.TYPE)){
                    DocListScFragment fragment = new DocListScFragment();
                    fragment.setArguments(bundle);
                    FragmentUtil.replaceStack(getActivity().getSupportFragmentManager(), fragment, R.id.fragment);
                }else {
                    DocListLtFragment fragment = new DocListLtFragment();
                    fragment.setArguments(bundle);
                    FragmentUtil.replaceStack(getActivity().getSupportFragmentManager(), fragment, R.id.fragment);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG, "onResume");
    }

    public void initData(){
        params = new HashMap<String, String>();
        params.put("documentType", "1");
        params.put("planMonth", "");
        params.put("planType", "");
    }

    //组装参数
    private void assembleParam(){
        params.put("etpsName", qymcEt.getText().toString());
    }
}
