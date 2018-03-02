package com.wonders.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.google.gson.Gson;
import com.wonders.activity.CompanyActivity;
import com.wonders.activity.PreviewScActivity;
import com.wonders.adapter.DbsxAdapter;
import com.wonders.bean.DbsxBean;
import com.wonders.bean.GroupMemberBean;
import com.wonders.bean.Result;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.bean.Db_message;
import com.wonders.bean.PlanBean;
import com.wonders.recyclerView.DiyItemDecoration;
import com.wonders.util.ParameterizedTypeImpl;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 日常检查 和 回访任务
 */
public class DbsxFragment extends Fragment implements DbsxAdapter.OnclickListener{
    private static final String TAG = DbsxFragment.class.getName();
    private AppData appData = AppData.getInstance();
    private LinearLayout bottomLayout;
    private RecyclerView rv;

    private String type;
    private TextView tvTitle;
    //所有计划
    private ArrayList<PlanBean> allPlans = new ArrayList<>();
    //未分配计划
    private ArrayList<PlanBean> notSupervisionPlans = new ArrayList<PlanBean>();
    //已分配计划
    private ArrayList<PlanBean> supervisionPlans = new ArrayList<PlanBean>();
    // 已分配要回访待办计划
    private ArrayList<PlanBean> revisitPlans = new ArrayList<PlanBean>();
    // 未分配要回访的待办计划
    private ArrayList<PlanBean> notRevisitPlans = new ArrayList<PlanBean>();

    private Button allSelectBtn;
    private Button cancelBtn;
    private Button dispatchBottomBtn;
    private Button downloadBottomBtn;

    private ArrayList<GroupMemberBean> memberList = new ArrayList<GroupMemberBean>();

    private ArrayList<Db_message> dbList = new ArrayList<Db_message>();

    //true 拥有已经分派的待办
    boolean isNoSelect = false;
    //true 拥有已经分派的待办
    boolean isHaveDispath = false;
    //true 拥有未分派的待办
    boolean isHaveNotDispath = false;

    private DbsxAdapter dbsxAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        type = getArguments().getString("type") != null? getArguments().getString("type"): Constants.RCJC;
        Log.e(TAG, "类型：" + type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");

//        type = getArguments().getString("type") != null? getArguments().getString("type"): Constants.RCJC;
//        Log.e(TAG, "类型：" + type);

        View view = inflater.inflate(R.layout.fragment_dbsx, null);

        dbsxAdapter = new DbsxAdapter(allPlans, getActivity());
        dbsxAdapter.setOnclickListener(this);

        findView(view);

        //如果是单机模式 把下方layout给屏蔽掉
        if (!appData.isNetWork()) {
            bottomLayout.setVisibility(View.GONE);
        }
        getData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG, "onResume");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult");
        Log.e(TAG, "requestCode：" + requestCode);
        Log.e(TAG, "resultCode：" + resultCode);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            getData();
        }
    }

    /**
     * 刷新列表数据
     */
    private void getData() {
        LoadingDialog.show(getActivity());
        if (!appData.isNetWork()) {
            //单机版的数据
            DbHelper dbhelper = new DbHelper(getActivity(), DbConstants.TABLENAME, null, 1);

            if (Constants.TYPE.equals("")) {
                dbList = dbhelper.queryDbMessageAll("0", appData.getLoginBean().getUserId());
            } else {
                dbList = dbhelper.queryDbMessageAll("1", appData.getLoginBean().getUserId());
            }

            for (int i = 0; i < dbList.size(); i++) {
                PlanBean planbean = new PlanBean();

                planbean.setAddress(dbList.get(i).getAddress());
                planbean.setAllUserName(dbList.get(i).getAllUserName());
                planbean.setPlanMonth(dbList.get(i).getPlanMonth());
                planbean.setEtpsName(dbList.get(i).getEtpsName());
                planbean.setEtpsId(dbList.get(i).getEtpsId());
                planbean.setType(Integer.valueOf(dbList.get(i).getType()));
                planbean.setPlanId(dbList.get(i).getPlanId());
                planbean.setShowType(0);

                switch (planbean.getType()) {
                    case 0:
                        notSupervisionPlans.add(planbean);
                        break;
                    case 2:
                        //日常
                        supervisionPlans.add(planbean);
                        break;
                    case 4:
                        // 回访任务
                        revisitPlans.add(planbean);
                        break;
                    case 5:
                        notRevisitPlans.add(planbean);
                        break;
                }
            }
            chooseResult();
            LoadingDialog.dismiss();
        } else {
            String url = "";
            if ("".equals(Constants.TYPE))
                url = Retrofit2Service.QUERY_TODO;
            else
                url = Retrofit2Service.LT_QUERY_TODO;

            Call<Result<DbsxBean>> call = Retrofit2Helper.getInstance().queryToDo(url);
            call.enqueue(new Callback<Result<DbsxBean>>() {
                @Override
                public void onResponse(Call<Result<DbsxBean>> call, Response<Result<DbsxBean>> response) {
                    if (response.body() != null && response.body().getObject() !=null){
                        DbsxBean dbsxBean = response.body().getObject();
                        if (dbsxBean.getNotSupervisionPlans() != null){
                            for (PlanBean planBean: dbsxBean.getNotSupervisionPlans()){
                                planBean.setType(0);
                                planBean.setShowType(1);
                            }
                            notSupervisionPlans.clear();
                            notSupervisionPlans.addAll(dbsxBean.getNotSupervisionPlans());
                        }

                        if (dbsxBean.getSupervisionPlans() != null){
                            for (PlanBean planBean: dbsxBean.getSupervisionPlans()){
                                planBean.setType(2);
                                planBean.setShowType(1);
                            }
                            supervisionPlans.clear();
                            supervisionPlans.addAll(dbsxBean.getSupervisionPlans());
                        }

                        if (dbsxBean.getRevisitPlans() != null){
                            for (PlanBean planBean: dbsxBean.getRevisitPlans()){
                                planBean.setType(4);
                                planBean.setShowType(1);
                            }
                            revisitPlans.clear();
                            revisitPlans.addAll(dbsxBean.getRevisitPlans());
                        }

                        if (dbsxBean.getNotRevisitPlans() != null){
                            for (PlanBean planBean: dbsxBean.getNotRevisitPlans()){
                                planBean.setType(5);
                                planBean.setShowType(1);
                            }
                            notRevisitPlans.clear();
                            notRevisitPlans.addAll(dbsxBean.getNotRevisitPlans());
                        }
                        chooseResult();
                        LoadingDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Result<DbsxBean>> call, Throwable t) {
                    LoadingDialog.dismiss();
                }
            });
    }}

    private void chooseResult() {
        allPlans.clear();
        if (Constants.RCJC.equals(type)) {
            allPlans.addAll(notSupervisionPlans);
            allPlans.addAll(supervisionPlans);
        } else if (Constants.HFRW.equals(type)) {
            allPlans.addAll(revisitPlans);
            allPlans.addAll(notRevisitPlans);
        }
        dbsxAdapter.notifyDataSetChanged();
    }

    private void findView(View view) {
        tvTitle = getActivity().findViewById(R.id.tv_title);
        tvTitle.setText(type);

        bottomLayout = view.findViewById(R.id.bottom_layout);
        //全选
        allSelectBtn = view.findViewById(R.id.all_select_btn);
        allSelectBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < allPlans.size(); i++) {
                    allPlans.get(i).setSelect(true);
                }

                dbsxAdapter.notifyDataSetChanged();
            }
        });
        //取消全选
        cancelBtn = (Button) view.findViewById(R.id.cancel_select_btn);
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < allPlans.size(); i++) {
                    allPlans.get(i).setSelect(false);
                    dbsxAdapter.notifyDataSetChanged();
                }
            }
        });
        //分配
        dispatchBottomBtn = (Button) view.findViewById(R.id.btn_dispatch_bottom);
        dispatchBottomBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看是否是组员
                AppData appData = (AppData) getActivity().getApplication();
                if (!appData.getLoginBean().isManager()) {
                    ToastUtil.show("组员无法分配");

                    return;
                }

                if (!isNoSelect) {
                    ToastUtil.show("未选中待办");

                    return;
                }

                if (!isHaveNotDispath) {
                    ToastUtil.show("没有可以分派的待办");

                    return;
                }

                if (isHaveDispath && isHaveNotDispath) {
                    ToastUtil.show("选中的待办中有已分配的待办，请重新选择。");

                    return;
                }

                //有没有分配的人物
                boolean canDispatch = false;
                boolean isHaveDispatch = false;

                ArrayList<PlanBean> selectPlans = new ArrayList<PlanBean>();

                for (int j = 0; j < allPlans.size(); j++) {
                    if (allPlans.get(j).isSelect()) {
                        selectPlans.add(allPlans.get(j));
                    }
                }

                for (int i = 0; i < selectPlans.size(); i++) {
                    if ((selectPlans.get(i).getType() == 0
                            || selectPlans.get(i).getType() == 1
                            || selectPlans.get(i).getType() == 5) && selectPlans.get(i).isSelect()) {
                        canDispatch = true;
                    } else {
                        isHaveDispatch = true;
                    }
                }

                if (isHaveDispatch) {
                    ToastUtil.show("已分配的任务不再分配");
                }

                if (canDispatch == false) {
                    ToastUtil.show("当前没有可分配的任务");

                    return;
                }

                // 已经选择了可以分配的任务
                boolean haveSelect = false;
                for (int i = 0; i < allPlans.size(); i++) {
                    if (allPlans.get(i).isSelect()) {
                        haveSelect = true;
                        break;
                    }
                }

                if (haveSelect) {
                    if (memberList.size() == 0) {
                        getMemberData(getActivity(), memberList);
                    } else {
                        createMemberDialog();
                    }
                }
            }
        });

        //下载
        downloadBottomBtn = (Button) view.findViewById(R.id.btn_bottom_download);
        downloadBottomBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNoSelect) {
                    ToastUtil.show("未选中待办");

                    return;
                }

                if (!isHaveDispath) {
                    ToastUtil.show("未分配的待办不能下载");

                    return;
                }

                if (isHaveDispath && isHaveNotDispath) {
                    ToastUtil.show("选中的待办中有未分配的待办，请重新选择。");

                    return;
                }



                for (int i = 0; i < allPlans.size(); i++) {


                    //如果这一项已经被选择
                    if (allPlans.get(i).isSelect() && !(allPlans.get(i).getType() == 0
                            || allPlans.get(i).getType() == 1
                            || allPlans.get(i).getType() == 5)) {


                        //检查是不是有本地数据
                        DbHelper dbhelper = new DbHelper(getActivity(), DbConstants.TABLENAME, null, 1);

                        Db_message message = dbhelper.query_Db_message(allPlans.get(i).getPlanId());

                        //没有本地数据访问网络
                        if (message.getPlanId() == null) {

                            downloadOneItem(allPlans.get(i));
                        } else {
//
                        }
                    }
                }
            }
        });

        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv.addItemDecoration(new DiyItemDecoration(getActivity(),DiyItemDecoration.VERTICAL_LIST));
//        rv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(dbsxAdapter);
//        rv.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * @param position
     * recyclerView item的点击事件(分派人员或跳转下一页面)
     */
    @Override
    public void click0(int position) {
        if (allPlans.get(position).getType() == 0
                || allPlans.get(position).getType() == 1
                || allPlans.get(position).getType() == 5) {

            allPlans.get(position).setSelect(true);

            if (memberList.size() == 0) {
                getMemberData(getActivity(), memberList);
            } else {
                createMemberDialog();
            }
        } else {
            if (!AppData.getInstance().isNetWork()) {
                //把单机数据转化成全局变量
                AppData.getInstance().setDb_message(dbList.get(position));
            }
            HashMap params = new HashMap();
            params.put(Constants.TASK_TYPE,0);
            params.put(Constants.ETPS_ID,allPlans.get(position).getEtpsId());
            params.put(Constants.ETPS_NAME,allPlans.get(position).getEtpsName());
            params.put(Constants.PLAN_ID,allPlans.get(position).getPlanId());
            params.put(Constants.ALL_USER_NAME,allPlans.get(position).getAllUserName());
            Intent intent = new Intent(getActivity(), CompanyActivity.class);
            intent.putExtra(Constants.PARAMS, params);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void click1(int position) {

        allPlans.get(position).setSelect(!allPlans.get(position).isSelect());

        isNoSelect = false;
        isHaveDispath = false;
        isHaveNotDispath = false;

        for (int i = 0; i < allPlans.size(); i++) {
            //是否有选择
            if (allPlans.get(i).isSelect()) {
                isNoSelect = true;
            }

            //拥有已分派任务
            if (allPlans.get(i).isSelect() && !(allPlans.get(i).getType() == 0
                    || allPlans.get(i).getType() == 1
                    || allPlans.get(i).getType() == 5)) {
                isHaveDispath = true;
            }

            //拥有未分派任务
            if (allPlans.get(i).isSelect() && (allPlans.get(i).getType() == 0
                    || allPlans.get(i).getType() == 1
                    || allPlans.get(i).getType() == 5)) {
                isHaveNotDispath = true;
            }
        }


        dbsxAdapter.notifyDataSetChanged();
    }

    @Override
    public void click2(int position) {
        PlanBean bean = allPlans.get(position);
        HashMap params = new HashMap();
        params.put(Constants.PLAN_ID, bean.getPlanId());
        params.put(Constants.DOC_TYPE, 1);
        params.put("gaozhi", makeGaozhiData(bean).toString());
        Intent intent = new Intent(getActivity(), PreviewScActivity.class);
        intent.putExtra(Constants.PARAMS, params);
        startActivity(intent);
    }

    /**
     * 分配人员选择dialog
     */
    private void createMemberDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setContentView(R.layout.dialog_member);
        dialog.setCancelable(false);

        ListView lv = (ListView) dialog.findViewById(R.id.lv);
        TextView manager = (TextView) dialog
                .findViewById(R.id.group_manager_tv);

        manager.setText(AppData.getInstance().getLoginBean().getUserName());

        Button confirmBtn = (Button) dialog.findViewById(R.id.btn_confirm);
        Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);

        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 取消分配 只显示数据

                dbsxAdapter.notifyDataSetChanged();
                dialog.dismiss();

                for (int i = 0; i < allPlans.size(); i++) {
                    allPlans.get(i).setSelect(false);
                }

                for (int j = 0; j < memberList.size(); j++) {
                    memberList.get(j).setSelect(false);
                }
            }
        });

        confirmBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean selectFlag = false;


                for (int i = 0; i < memberList.size(); i++) {
                    if (memberList.get(i).isSelect()) {
                        selectFlag = true;
                        break;
                    }
                }


                selectFlag = true;

                if (selectFlag) {
                    LoadingDialog.show(getActivity());

                    String url = "";
                    if ("".equals(Constants.TYPE)){
                        url = Retrofit2Service.BINDING_GROUP;
                    }else {
                        url = Retrofit2Service.LT_BINDING_GROUP;
                    }

                    HashMap<String,String> params = new HashMap<String, String>();


                    // 拼接计划
                    String planIds = "";
                    for (int i = 0; i < allPlans.size(); i++) {
                        if (allPlans.get(i).isSelect() && (allPlans.get(i).getType() == 0
                                || allPlans.get(i).getType() == 1
                                || allPlans.get(i).getType() == 5)) {
                            planIds += allPlans.get(i).getPlanId() + ";";
                        }
                    }

                    // 拼接分配组员
                    String userIds = "";
                    String userNames = "";
                    for (int i = 0; i < memberList.size(); i++) {
                        if (memberList.get(i).isSelect()) {
                            userIds += memberList.get(i).getUserId() + ";";
                            userNames += memberList.get(i).getUserName() + ";";
                        }
                    }

                    params.put("planIds", planIds);
                    params.put("userIds", userIds);
                    params.put("userNames", userNames);

                    Call<ResponseBody> call = Retrofit2Helper.getInstance().bindingGroup(url,params);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String result = "";
                            try {
                                result = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            LoadingDialog.dismiss();
                            dialog.dismiss();

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

                            try {
                                int code = json.getInt("code");
                                String message = json.getString("message");
                                if (code == 0) {
                                    ToastUtil.show(message);

                                    getData();
                                }
                            } catch (JSONException e) {
                                ToastUtil.show(getString(R.string.error_json));
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            dialog.dismiss();
                            LoadingDialog.dismiss();
                            ToastUtil.show(getResources().getString(R.string.error_server));
                        }
                    });

                } else {
                    ToastUtil.show("请选择将要分配的人员");
                }
            }
        });

        final BaseAdapter myAdapter = new BaseAdapter() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(getActivity(), R.layout.item_member_choose, null);
                TextView name = (TextView) convertView.findViewById(R.id.text);
                ImageView selectImg = (ImageView) convertView
                        .findViewById(R.id.img);
                name.setText(memberList.get(position).getUserName());

                if (memberList.get(position).isSelect()) {
                    selectImg.setImageResource(R.drawable.choose);
                } else {
                    selectImg.setImageResource(R.drawable.unchoose);
                }

                return convertView;
            }

            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public Object getItem(int position) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return memberList.size();
            }
        };
        lv.setAdapter(myAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                memberList.get(position).setSelect(
                        !memberList.get(position).isSelect());
                myAdapter.notifyDataSetChanged();
            }
        });

        dialog.show();
    }

    private void downloadOneItem(PlanBean planBean1) {
        String url = "";
        if ("".equals(Constants.TYPE)){
            url = Retrofit2Service.GET_CHECK_ITEM;
        }else {
            url = Retrofit2Service.LT_GET_CHECK_ITEM;
        }

        HashMap<String,String> params = new HashMap<String, String>();
        params.put("etpId", planBean1.getEtpsId());
        params.put("planId", planBean1.getPlanId());

        final PlanBean planBean = planBean1;

        Call<ResponseBody> call = Retrofit2Helper.getInstance().getCheckItem(url,params);
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
                JSONObject caseList = null;
                try {
                    caseList = new JSONObject(result);
                } catch (JSONException e) {

                    e.printStackTrace();
                }

                JSONArray object = null;
                try {
                    object = new JSONArray(caseList.getString("object"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (object == null) {
                    ToastUtil.show("下载数据失败");

                    return;
                }else{
                    ToastUtil.show("下载更新成功");
                }


                Db_message db_message = new Db_message();
                DbHelper dbhelper = new DbHelper(getActivity(), DbConstants.TABLENAME, null, 1);

                for (int i = 0; i < object.length(); i++) {
                    try {

                        db_message.setPlanId(planBean.getPlanId());
                        db_message.setUserId(appData.getLoginBean().getUserId());
                        db_message.setFlag(0);
                        switch (i) {
                            case 0:
                                db_message.setGet_etpCheckInfo(object.getString(0));
                                break;
                            case 1:
                                db_message.setGet_superviseRecord(object.getString(1));
                            case 2:
                                db_message.setGet_planCheckContent(object.getString(2));
                            case 3:
                                db_message.setGet_planCheckContentDetail(object.getString(3));
                            case 4:
                                db_message.setGet_fpsiEtpsInfo(object.getString(4));
                            case 5:
                                db_message.setGet_fpsiCertInfo(object.getString(5));
                            case 6:
                                db_message.setGet_fpsiInspPlan(object.getString(6));
                            default:
                                break;
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                if (Constants.TYPE.equals("")) {
                    //生产
                    db_message.setIsLt("0");
                } else {
                    db_message.setIsLt("1");
                }


                db_message.setAddress(planBean.getAddress());
                db_message.setEtpsName(planBean.getEtpsName());
                db_message.setEtpsId(planBean.getEtpsId());
                db_message.setAllUserName(planBean.getAllUserName());
                db_message.setPlanMonth(planBean.getPlanMonth());
                db_message.setType(planBean.getType() + "");
                db_message.setUserId(appData.getLoginBean().getUserId());
                db_message.setIsFinish("0");

                Db_message message = dbhelper.query_Db_message(db_message.getPlanId());
                if (message.getPlanId() == null) {
                    dbhelper.insert_Db_message(db_message);
                }

                dbsxAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoadingDialog.dismiss();
                ToastUtil.show(getResources().getString(R.string.error_server));
            }
        });
    }

    public JSONObject makeGaozhiData(PlanBean bean){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("etpsName", bean.getEtpsName() == null ? "" : bean.getEtpsName());
            jsonObject.put("address", bean.getAddress() == null ? "" : bean.getAddress());
            jsonObject.put("checkUnit", bean.getExeOrgan() == null ? "" : bean.getExeOrgan());
            jsonObject.put("personName","");
            jsonObject.put("personNo","");
            jsonObject.put("certName","");
            jsonObject.put("certNo","");
            jsonObject.put("checkYear",year);
            jsonObject.put("checkMonth",month);
            jsonObject.put("checkDay",day);
            jsonObject.put("checkAddress",bean.getAddress() == null ? "" : bean.getAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void getMemberData(Context context,final ArrayList<GroupMemberBean> memberList){
        LoadingDialog.show(context);
        String url = "";
        if ("".equals(Constants.TYPE)){
            url = Retrofit2Service.DISTRIBUTION_GROUP;
        }else {
            url = Retrofit2Service.LT_DISTRIBUTION_GROUP;
        }

        Call<ResponseBody> call = Retrofit2Helper.getInstance().distributionGroup(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = "";
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                LoadingDialog.dismiss();

                Type listType = new ParameterizedTypeImpl(List.class,new Class[]{GroupMemberBean.class});
                Type type = new ParameterizedTypeImpl(Result.class,new Type[]{listType});
                Gson gson = new Gson();
                Result<List<GroupMemberBean>> results = gson.fromJson(result,type);
                memberList.addAll(results.getObject());
                memberList.remove(0);

                createMemberDialog();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoadingDialog.dismiss();
                ToastUtil.show(getResources().getString(R.string.error_server));
            }
        });
    }
}
