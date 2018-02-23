package com.wonders.fragment;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legal_rights.R;
import com.wonders.constant.Constants;
import com.wonders.widget.DatePickerDialog;
import com.wonders.widget.ListDialog;
import com.wonders.http.Retrofit2Helper;
import com.wonders.util.FragmentUtil;
import com.wonders.widget.LoadingDialog;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QyjgcxFragment extends Fragment implements OnClickListener {
    private static final String TAG = QyjgcxFragment.class.getName();
    private EditText qymcEt;
    private EditText qydzEt;
    private EditText mInspectionPerson;//检查人员
    private TextView mInspectionDate;//检查时间
    private TextView mPlanningDate;//计划时间
    private TextView mEntType;//企业类型
    private TextView mEntLevel;//企业等级
    private TextView mCompleteStatus;//完成状态
    private TextView mCheckResult;//检查结果
    private TextView mPlanningUnit;//计划制定单位
    private TextView mInspectionUnit;//计划执行单位
    private Dialog mDialog;
    private Button searchBtn;

    private List<Map.Entry<String, String>> levelList;
    private List<Map.Entry<String, String>> mCheckResultList;
    private List<Map.Entry<String, String>> mCompleteStatusList;
    private List<Map.Entry<String, String>> mEntTypeList;
    private List<Map.Entry<String, String>> mParentNoLeaf;
    private List<Map.Entry<String, String>> mParentHasLeaf;
    private List<Map.Entry<String, String>> mParent;
    private List<List<Map.Entry<String, String>>> mLeaf;
    private ListDialog mListDialog;
    private ListDialog mChildUnitDialog;
    private String mUnitDialogTitle;
    private HashMap<String,String> params = new HashMap<>();
    private boolean getCondition;
    private Call<ResponseBody> call;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        View view = View.inflate(getActivity(), R.layout.fragment_qyjgcx, null);

        findView(view);

        initListener();
        initData();
        initNetDataList();
        //防止回退时再次调用
        if (!getCondition)
            getQueryCondition();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onCreateView");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
        //取消请求
        call.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach");
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initLevelList();
        initCheckResultList();
    }

    /**
     * 获取服务器查询条件数据
     */
    private void initNetData(JSONObject object) {
        try {
            mCompleteStatusList = getListData(new JSONObject(object.getString("planStatus")));
            mEntTypeList = getListData(new JSONObject(object.getString("etpsType")));
            JSONArray array = new JSONArray(object.getString("orgListJsonArray"));
            if (array.length() > 0) {
                JSONObject itemObject = (JSONObject) array.get(0);//第一个是根父节点
                String pId = (null != itemObject.getString("id") ? itemObject.getString("id") : "");
                mUnitDialogTitle = (null != itemObject.getString("name") ? itemObject.getString("name") : "");
                mParentNoLeaf = getParent(array, pId, true);
                mParentHasLeaf = getParent(array, pId, false);
                mParent.addAll(mParentNoLeaf);
                mParent.addAll(mParentHasLeaf);
                getLeaf(array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取第二层节点
     *
     * @param array
     * @param pId   hasLeaf=false表示获取有子节点的节点,true表示获取无子节点的节点
     */
    private List<Map.Entry<String, String>> getParent(JSONArray array, String pId, boolean hasLeaf) {
        List<Map.Entry<String, String>> result = new ArrayList<>();
        try {
            for (int i = 1; i < array.length(); i++) {
                JSONObject item = (JSONObject) array.get(i);
                if (item.getString("pId").equals(pId)) {
                    boolean hasChildLeaf = hasLeaf;
                    for (int j = 1; j < array.length(); j++) {
                        JSONObject itemObject = (JSONObject) array.get(j);
                        if (item.getString("id").equals(itemObject.getString("pId"))) {
                            hasChildLeaf = !hasLeaf;
                        }
                    }
                    if (hasChildLeaf) {
                        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry(item.getString("id"), item.getString("name"));
                        result.add(entry);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取叶子节点
     *
     * @param array
     */
    private void getLeaf(JSONArray array) {
        try {
            for (int j = 0; j < mParentHasLeaf.size(); j++) {
                String id = mParentHasLeaf.get(j).getKey();
                ArrayList<Map.Entry<String, String>> list = new ArrayList<>();
                for (int i = 1; i < array.length(); i++) {
                    JSONObject item = (JSONObject) array.get(i);
                    if (item.getString("pId").equals(id)) {
                        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry(item.getString("id"), item.getString("name"));
                        list.add(entry);
                    }
                }
                mLeaf.add(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LoadingDialog.dismiss();
        getCondition = true;
    }

    private void initNetDataList() {
        mCompleteStatusList = new ArrayList<>();
        mEntTypeList = new ArrayList<>();
        mParentNoLeaf = new ArrayList<>();
        mParentHasLeaf = new ArrayList<>();
        mParent = new ArrayList<>();
        mLeaf = new ArrayList<>();
    }

    /**
     * 初始化完成状态
     *
     * @param object
     */
    private List<Map.Entry<String, String>> getListData(JSONObject object) {
        List<Map.Entry<String, String>> result = new ArrayList<>();
        Iterator<String> iterator = object.keys();
        Map.Entry<String, String> item;
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                item = new AbstractMap.SimpleEntry(key, object.getString(key));
                result.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 初始化企业等级列表
     */
    private void initLevelList() {
        levelList = new ArrayList<>();
        Map.Entry<String, String> item = new AbstractMap.SimpleEntry("A", "A");
        levelList.add(item);
        item = new AbstractMap.SimpleEntry("B", "B");
        levelList.add(item);
        item = new AbstractMap.SimpleEntry("C", "C");
        levelList.add(item);
        item = new AbstractMap.SimpleEntry("D", "D");
        levelList.add(item);
    }

    /**
     * 检查结果
     */
    private void initCheckResultList() {
        mCheckResultList = new ArrayList<>();
        Map.Entry<String, String> checkResultItem = new AbstractMap.SimpleEntry("", "全部");
        mCheckResultList.add(checkResultItem);
        checkResultItem = new AbstractMap.SimpleEntry("0", "发现问题");
        mCheckResultList.add(checkResultItem);
        checkResultItem = new AbstractMap.SimpleEntry("1", "未发现问题");
        mCheckResultList.add(checkResultItem);
    }

    private void findView(View view) {
        qymcEt = (EditText) view.findViewById(R.id.qymc);
        qydzEt = (EditText) view.findViewById(R.id.qydz);
//        jclxEt = (EditText) view.findViewById(R.id.jclx);
        mInspectionPerson = (EditText) view.findViewById(R.id.inspection_person);
        mInspectionDate = (TextView) view.findViewById(R.id.inspection_date);
        mPlanningDate = (TextView) view.findViewById(R.id.planning_date);
        mEntType = (TextView) view.findViewById(R.id.ent_type);
        mEntLevel = (TextView) view.findViewById(R.id.ent_level);
        mCompleteStatus = (TextView) view.findViewById(R.id.complete_status);
        mCheckResult = (TextView) view.findViewById(R.id.check_result);
        mPlanningUnit = (TextView) view.findViewById(R.id.planning_unit);
        mInspectionUnit = (TextView) view.findViewById(R.id.inspection_unit);

//
        searchBtn = (Button) view.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.show(getActivity());

                params.put("etpsName", qymcEt.getText().toString());
                params.put("address", qydzEt.getText().toString());
                params.put("checkPerson", mInspectionPerson.getText().toString());

                JgjlListFragment jgjlListFragment = new JgjlListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PARAMS, params);
                jgjlListFragment.setArguments(bundle);
                FragmentUtil.replaceStack(getFragmentManager(), jgjlListFragment, R.id.fragment);
            }
        });

    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        mInspectionDate.setOnClickListener(this);
        mPlanningDate.setOnClickListener(this);
        mEntType.setOnClickListener(this);
        mEntLevel.setOnClickListener(this);
        mCompleteStatus.setOnClickListener(this);
        mCheckResult.setOnClickListener(this);
        mPlanningUnit.setOnClickListener(this);
        mInspectionUnit.setOnClickListener(this);
    }

    private void getQueryCondition() {
        LoadingDialog.show(getActivity());
        call = Retrofit2Helper.getInstance().toSuperviseRecord();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = "";
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject results = new JSONObject(result);
                        if (results.getInt("code") == 0) {
                            initNetData(new JSONObject(results.getString("object")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(),
                            getResources().getString(
                                    R.string.error_server),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoadingDialog.dismiss();
                if (!call.isCanceled()){
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_server),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inspection_date:
                if (null == mDialog || (null != mDialog && !mDialog.isShowing())) {
                    mDialog = new DatePickerDialog.Builder(getActivity()).setPositiveButton("确定", new DatePickerDialog.Builder.OnDatePickListener() {
                        @Override
                        public void onDatePick(DialogInterface dialog, int witch, int... args) {
                            String startDate = args[0] + "-" + args[1] + "-" + args[2];
                            mInspectionDate.setText(startDate);
                            params.put("startDate", startDate);
                        }
                    }).setNegativeButton("清空", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mInspectionDate.setText("");
                            params.put("startDate", "");
                        }
                    }).create();
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                }
                break;
            case R.id.planning_date:
                if (null == mDialog || (null != mDialog && !mDialog.isShowing())) {
                    mDialog = new DatePickerDialog.Builder(getActivity()).setPositiveButton("确定", new DatePickerDialog.Builder.OnDatePickListener() {
                        @Override
                        public void onDatePick(DialogInterface dialog, int witch, int... args) {
                            String endDate = args[0] + "-" + args[1] + "-" + args[2];
                            mPlanningDate.setText(endDate);
                            params.put("createDate", endDate);
                        }
                    }).setNegativeButton("清空", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPlanningDate.setText("");
                            params.put("createDate", "");
                        }
                    }).create();
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                }
                break;
            case R.id.ent_type:
                if (null == mListDialog || (null != mListDialog && !mListDialog.isVisible())) {
                    mListDialog = new ListDialog().setTitle("企业类型").setItems(mEntTypeList, new ListDialog.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View v, int position) {
                            params.put("typeId", mEntTypeList.get(position).getKey());
                            mEntType.setText(mEntTypeList.get(position).getValue());
                            mListDialog.dismiss();
                        }
                    }).setNegativeButton("清空", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            params.put("typeId", "");
                            mEntType.setText("");
                        }
                    });
                    mListDialog.setCancelable(false);
                    mListDialog.show(getChildFragmentManager());
                }
                break;
            case R.id.ent_level:
                if (null == mListDialog || (null != mListDialog && !mListDialog.isVisible())) {
                    mListDialog = new ListDialog().setTitle("企业等级").setItems(levelList, new ListDialog.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View v, int position) {
                            params.put("grade", levelList.get(position).getKey());
                            mEntLevel.setText(levelList.get(position).getValue());
                            mListDialog.dismiss();
                        }
                    }).setNegativeButton("清空", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            params.put("grade", "");
                            mEntLevel.setText("");
                        }
                    });
                    mListDialog.setCancelable(false);
                    mListDialog.show(getChildFragmentManager());
                }
                break;
            case R.id.complete_status:
                if (null == mListDialog || (null != mListDialog && !mListDialog.isVisible())) {
                    mListDialog = new ListDialog().setTitle("完成状态").setItems(mCompleteStatusList, new ListDialog.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View v, int position) {
                            params.put("status", mCompleteStatusList.get(position).getKey());
                            mCompleteStatus.setText(mCompleteStatusList.get(position).getValue());
                            mListDialog.dismiss();
                        }
                    }).setNegativeButton("清空", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCompleteStatus.setText("");
                            params.put("status", "");
                        }
                    });
                    mListDialog.setCancelable(false);
                    mListDialog.show(getChildFragmentManager());
                }
                break;
            case R.id.check_result:
                if (null == mListDialog || (null != mListDialog && !mListDialog.isVisible())) {
                    mListDialog = new ListDialog().setTitle("检查结果").setItems(mCheckResultList, new ListDialog.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View v, int position) {
                            params.put("resultType", mCheckResultList.get(position).getKey());
                            mCheckResult.setText(mCheckResultList.get(position).getValue());
                            mListDialog.dismiss();
                        }
                    }).setNegativeButton("清空", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCheckResult.setText("");
                            params.put("resultType", "");
                        }
                    });
                    mListDialog.setCancelable(false);
                    mListDialog.show(getChildFragmentManager());
                }
                break;
            case R.id.planning_unit:
                if (null == mListDialog || (null != mListDialog && !mListDialog.isVisible())) {
                    mListDialog = new ListDialog().setTitle(mUnitDialogTitle).setItems(mParent, new ListDialog.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View v, int position) {
                            if (position > mParentNoLeaf.size() - 1) {
                                final List<Map.Entry<String, String>> mChildList = mLeaf.get(mParent.size() - 1 - position);
                                mChildUnitDialog = new ListDialog().setTitle(mParent.get(position).getValue()).setItems(mChildList, new ListDialog.OnItemClickListener() {
                                    @Override
                                    public void OnItemClick(View v, int position) {
                                        mPlanningUnit.setText(mChildList.get(position).getValue());
                                        params.put("inspOrgan", mChildList.get(position).getKey());
                                        mChildUnitDialog.dismiss();
                                        mListDialog.dismiss();
                                    }
                                }).setNegativeButton("取消", null);
                                mChildUnitDialog.setCancelable(false);
                                mChildUnitDialog.show(getChildFragmentManager());
                            } else {
                                mPlanningUnit.setText(mParent.get(position).getValue());
                                params.put("inspOrgan", mParent.get(position).getKey());
                                mListDialog.dismiss();
                            }
                        }
                    }).setNegativeButton("清空", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPlanningUnit.setText("");
                            params.put("inspOrgan", "");
                            mListDialog.dismiss();
                        }
                    });
                    mListDialog.setCancelable(false);
                    mListDialog.show(getChildFragmentManager());
                }
                break;
            case R.id.inspection_unit:
                if (null == mListDialog || (null != mListDialog && !mListDialog.isVisible())) {
                    mListDialog = new ListDialog().setTitle(mUnitDialogTitle).setItems(mParent, new ListDialog.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View v, int position) {
                            if (position > mParentNoLeaf.size() - 1) {
                                final List<Map.Entry<String, String>> mChildList = mLeaf.get(mParent.size() - 1 - position);
                                mChildUnitDialog = new ListDialog().setTitle(mParent.get(position).getValue()).setItems(mChildList, new ListDialog.OnItemClickListener() {
                                    @Override
                                    public void OnItemClick(View v, int position) {
                                        mInspectionUnit.setText(mChildList.get(position).getValue());
                                        params.put("exeOrgan", mChildList.get(position).getKey());
                                        mChildUnitDialog.dismiss();
                                        mListDialog.dismiss();
                                    }
                                }).setNegativeButton("取消", null);
                                mChildUnitDialog.setCancelable(false);
                                mChildUnitDialog.show(getChildFragmentManager());
                            } else {
                                mInspectionUnit.setText(mParent.get(position).getValue());
                                params.put("exeOrgan", mParent.get(position).getKey());
                                mListDialog.dismiss();
                            }
                        }
                    }).setNegativeButton("清空", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mInspectionUnit.setText("");
                            params.put("exeOrgan", "");
                            mListDialog.dismiss();
                        }
                    });
                    mListDialog.setCancelable(false);
                    mListDialog.show(getChildFragmentManager());
                }
                break;
            default:
                break;
        }
    }
}
