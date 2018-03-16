package com.wonders.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.legal_rights.R;
import com.wonders.activity.PreviewScActivity;
import com.wonders.activity.PreviewLtActivity;
import com.wonders.adapter.BaseAdapter;
import com.wonders.adapter.DocScAdapter;
import com.wonders.constant.Constants;
import com.wonders.util.JsonHelper;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.bean.NoteBean;
import com.wonders.bean.PlanBean;
import com.wonders.bean.WsdyBean;
import com.wonders.util.DateUtil;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BJY on 2018/2/3.
 */

public class DocListScFragment extends RecyclerViewFragment{
    private DocScAdapter adapter;
    private List<WsdyBean> data;

    private String[] contentArr;
    private String planId = "";
    private JSONObject jsonObjectGaozhi;
    private PlanBean recordBean = new PlanBean();
    private String wsid;//文书id
    private JSONArray itemList2;
    private List<String> groups;
    private JSONArray group;
    private ArrayList<String> groupCode;
    private HashMap params;
    private JSONArray child;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = new ArrayList<>();
        adapter = new DocScAdapter(getActivity(), data);
        adapter.setOnClickListener(new BaseAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                getDocData(position);
            }
        });
        recyclerView.setAdapter(adapter);

        params = new HashMap();

        getData();
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView tvTitle = getActivity().findViewById(R.id.tv_title);
        tvTitle.setText("文书列表");

//        Handler handler = new Handler(){
//            @Override
//            public void dispatchMessage(Message msg) {
//                switch (msg.what){
//                    case 1:
//                        action_1();
//                        break;
//                }
//            }
//        };
//
//        handler.sendEmptyMessage(1);
//        action_2();
    }

    public void getData(){
        HashMap<String, String> params = (HashMap<String, String>) getArguments().getSerializable("params");
        Call<ResponseBody> call = Retrofit2Helper.getInstance().queryPrintDocument(Retrofit2Service.QUERY_PRINT_DOCUMENT, params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                LoadingDialog.dismiss();
                String result = "";
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (json == null) {
                    ToastUtil.show(getResources().getString(R.string.error_json));
                    return;
                }

                JSONArray array = null;
                try {
                    array = new JSONArray(json
                            .getString("object"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (array == null) {
                    ToastUtil.show(getResources().getString(R.string.error_json));
                    return;
                }
                for (int i = 0; i < array.length(); i++) {
                    WsdyBean bean = new WsdyBean();
                    try {
                        JsonHelper.parse(bean,
                                array.getString(i));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    data.add(bean);
                }

                if (data.size() != 0) {
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show("没有找到相关的信息");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoadingDialog.dismiss();
                if (!call.isCanceled()){
                    ToastUtil.show(getResources().getString(R.string.error_server));
                }
            }
        });
        calls.add(call);
    }

    public void getDocData(int position) {
        WsdyBean wsdyBean = data.get(position);
        String statusCode = wsdyBean.getStatus();
        if (!TextUtils.isEmpty(statusCode)
                && !statusCode.equals(Constants.PlanStatus.FINISHED_PLAN)
                && !statusCode.equals(Constants.PlanStatus.REVISIT_PLAN)) {
            contentArr = new String[]{"告知页"};
        } else {
            contentArr = new String[]{"告知页", "检查结果记录表", "监督检查要点表"};
        }
        planId = wsdyBean.getPlanId();
        switch (wsdyBean.getCodeType()) {
            default:
                Intent intent = new Intent(getActivity(), PreviewLtActivity.class);
                intent.putExtra("planId", planId);
                intent.putExtra("sub", "1");

                startActivity(intent);

                break;
            case "2":
                LoadingDialog.show(getActivity());
                Call<ResponseBody> call = Retrofit2Helper.getInstance().printThreeBooks(planId, wsdyBean.getEtpsId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        LoadingDialog.dismiss();
                        String result = "";
                        try {
                            result = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONObject results = new JSONObject(result);
                            JSONObject object = results.getJSONObject("object");
                            JSONObject fpsiEtpsInfo = object.getJSONObject("fpsiEtpsInfo");
                            JSONArray fpsiCertInfoList = fpsiEtpsInfo.getJSONArray("fpsiCertInfoList");
                            JSONObject fpsiCertInfo = fpsiCertInfoList.getJSONObject(0);
                            JSONObject superviseRecord = fpsiEtpsInfo.getJSONObject("superviseRecord");

                            jsonObjectGaozhi = new JSONObject();
                            jsonObjectGaozhi.put("etpsName", fpsiEtpsInfo.getString("etpsName"));
                            jsonObjectGaozhi.put("address", fpsiEtpsInfo.getString("address"));
                            jsonObjectGaozhi.put("checkUnit", fpsiEtpsInfo.getString("organName"));
                            if (!("null".equals(object.getString("informPage")))) {
                                JSONObject informPage = object.getJSONObject("informPage");
                                jsonObjectGaozhi.put("personName", informPage.getString("personName"));
                                jsonObjectGaozhi.put("personNo", informPage.getString("certNo"));
                                jsonObjectGaozhi.put("certName", informPage.getString("personNameTwo"));
                                jsonObjectGaozhi.put("certNo", informPage.getString("certNoTwo"));
                                Date date = DateUtil.formate3(informPage.getString("checkDate"));
                                jsonObjectGaozhi.put("checkYear", date.getYear() + 1900);
                                jsonObjectGaozhi.put("checkMonth", date.getMonth() + 1);
                                jsonObjectGaozhi.put("checkDay", date.getDay());
                                jsonObjectGaozhi.put("checkAddress", informPage.getString("checkAddress"));
                            } else {
                                jsonObjectGaozhi.put("personName", "");
                                jsonObjectGaozhi.put("personNo", "");
                                jsonObjectGaozhi.put("certName", "");
                                jsonObjectGaozhi.put("certNo", "");
                                jsonObjectGaozhi.put("checkYear", "");
                                jsonObjectGaozhi.put("checkMonth", "");
                                jsonObjectGaozhi.put("checkDay", "");
                                jsonObjectGaozhi.put("checkAddress", "");
                            }

                            recordBean.setExeOrgan(fpsiEtpsInfo.getString("organName"));
                            recordBean.setAddress(fpsiEtpsInfo.getString("address"));
                            recordBean.setEtpsName(fpsiEtpsInfo.getString("etpsName"));
                            recordBean.setCertNo(fpsiCertInfo.getString("certNo"));
                            recordBean.setStartDate(superviseRecord.getString("startDate"));
                            if (!("null".equals(object.getString("resultRecord")))) {
                                JSONObject resultRecord = object.getJSONObject("resultRecord");
                                wsid = resultRecord.getString("id");
                                recordBean.setLegalPerson(resultRecord.getString("legalPerson"));
                                recordBean.setPhoneNo(resultRecord.getString("phoneNo"));
                                recordBean.setCheckNo(resultRecord.getString("checkNo"));
                                recordBean.setRemark(resultRecord.getString("remark"));
                                recordBean.setOpinion(resultRecord.getString("opinion"));
                            } else {
                                recordBean.setLegalPerson(fpsiEtpsInfo.getString("personName"));
                                recordBean.setPhoneNo(fpsiEtpsInfo.getString("telephone"));
                                recordBean.setCheckNo("");
                                recordBean.setRemark("");
                                recordBean.setOpinion("");
                                wsid = "";
                            }

                            JSONArray countList = object.getJSONArray("countList");
                            CheckInputRcFragment.makeRecordData(countList);

                            itemList2 = object.getJSONArray("itemList2");
                            JSONObject jb0 = itemList2.getJSONObject(0);
                            if ("H".equals(jb0.getString("itemCode").substring(0, 1))){
                                params.put(Constants.PLAN_TYPE, "H");
                            }
                            group = new JSONArray();
                            groupCode = new ArrayList<String>();
                            child = new JSONArray();
                            for (int i = 0; i < itemList2.length(); i++) {
                                JSONObject jb = itemList2.getJSONObject(i);

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("content", jb.getString("checkContent"));
                                jsonObject.put("remark", jb.getString("remark"));
                                switch (jb.getString("result")) {
                                    case "1":
                                        jsonObject.put("isEdit", "1");
                                        jsonObject.put("isPass", "1");
                                        break;
                                    case "0":
                                        jsonObject.put("isEdit", "1");
                                        jsonObject.put("isPass", "0");
                                        break;
                                    default:
                                        jsonObject.put("isEdit", "0");
                                        jsonObject.put("isPass", "1");
                                        break;
                                }
                                String num = CheckInputRcFragment.getItemNum(jb.getString("itemCode"));
                                jsonObject.put("itemCode", num);
                                if (groupCode.size() != 0) {
                                    boolean isHave = false;
                                    int index = 0;
                                    for (int j = 0; j < groupCode.size(); j++) {
                                        if (jb.getString("parentCode").equals(groupCode.get(j))) {
                                            isHave = true;
                                            index = j;
                                            break;
                                        }
                                    }
                                    if (isHave) {
                                        JSONArray array = child.getJSONArray(index);

                                        array.put(jsonObject);
                                        child.put(index, array);
                                    } else {
                                        String parentCode = jb.getString("parentCode");
                                        groupCode.add(parentCode);
                                        String groupName = CheckInputRcFragment.getGroupName(parentCode);
                                        group.put(groupName);
                                        JSONArray array = new JSONArray();
                                        array.put(jsonObject);
                                        child.put(array);
                                    }
                                } else {
                                    String parentCode = jb.getString("parentCode");
                                    groupCode.add(parentCode);
                                    String groupName = CheckInputRcFragment.getGroupName(parentCode);
                                    group.put(groupName);
                                    JSONArray array = new JSONArray();
                                    array.put(jsonObject);
                                    child.put(array);
                                }
                            }

                            PreviewScActivity.notesJSONArray = getNotes();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        showDocPrintDialog(contentArr);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        LoadingDialog.dismiss();
                    }
                });
                break;
        }
    }

    public JSONArray getNotes() {
        JSONArray jsonArray = new JSONArray();
        makeNoteData(jsonArray, PreviewScActivity.highPros);
        makeNoteData(jsonArray, PreviewScActivity.lowPros);

        return jsonArray;
    }

    public void makeNoteData(JSONArray jsonArray, ArrayList<String> arrayList) {
        for (String itemCode : arrayList) {
            NoteBean bean = new NoteBean();
            String checkContent = CheckInputRcFragment.getItemNum(itemCode);
            String note = "";
            bean.setCheckContent(checkContent);
            bean.setNote(note);
            getRemark(itemList2, itemCode, bean);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("checkContent", bean.getCheckContent());
                jsonObject.put("note", bean.getNote());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
    }

    public void getRemark(JSONArray array, String code, NoteBean noteBean) {
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject jb = array.getJSONObject(i);
                if (code.equals(jb.getString("itemCode"))) {
                    noteBean.setNote(jb.getString("remark") == null ? "" : jb.getString("remark"));
                    String s = noteBean.getCheckContent();
                    noteBean.setCheckContent(s);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDocPrintDialog(final String[] items) {
        final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setContentView(R.layout.dialog_dept);
        dialog.setCancelable(false);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("文书选择");
        ListView mSelectList = (ListView) dialog.findViewById(R.id.listview);
        mSelectList.setAdapter(new android.widget.BaseAdapter() {
            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public Object getItem(int position) {
                return items[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = View.inflate(getActivity(), R.layout.item_dept, null);
                Button btn = (Button) view.findViewById(R.id.btn);
                btn.setText(items[position]);
                return view;
            }
        });

        mSelectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                params.put(Constants.PLAN_ID, planId);
                params.put("id", wsid);

                Intent intent = new Intent(getActivity(), PreviewScActivity.class);
                switch (position) {

                    case 0:
                        params.put(Constants.DOC_TYPE, 1);
                        params.put("gaozhi", jsonObjectGaozhi.toString());
                        intent.putExtra(Constants.PARAMS, params);
                        startActivity(intent);
                        break;
                    case 1:
                        params.put(Constants.DOC_TYPE, 4);
                        params.put("bean", recordBean);
                        intent.putExtra(Constants.PARAMS, params);
                        startActivity(intent);
                        break;
                    case 2:
                        params.put(Constants.DOC_TYPE, 5);
                        try {
                            for (int i = 0; i < group.length(); i++) {
                                String s = group.getString(i);
                                int length = s.length();
                                int j = 0;
                                for (; j < length; j++) {
                                    String content = s.substring(j, j + 1);
                                    if (content.matches("[\u4e00-\u9fa5]")) {
                                        break;
                                    }
                                }
                                String content = s.substring(j, length);
                                group.put(i, content);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        PreviewScActivity.groupJSONArray = group;
                        PreviewScActivity.childJSONArray = child;
                        intent.putExtra(Constants.PARAMS, params);
                        startActivity(intent);
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
