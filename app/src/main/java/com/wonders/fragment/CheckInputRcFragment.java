package com.wonders.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.legal_rights.BuildConfig;
import com.example.legal_rights.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wonders.activity.InputActivity;
import com.wonders.activity.PreviewScActivity;
import com.wonders.activity.NotesActivity;
import com.wonders.activity.PreviewLtActivity;
import com.wonders.adapter.ImageGridViewAdapter;
import com.wonders.adapter.MyExpandableListAdapter;
import com.wonders.application.AppData;
import com.wonders.bean.RecordBean;
import com.wonders.bean.Result;
import com.wonders.bean.SavePlanBean;
import com.wonders.bean.SopItemBean;
import com.wonders.constant.Constants;
import com.wonders.thread.FastDealExecutor;
import com.wonders.util.DbHelper;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.bean.Db_message;
import com.wonders.bean.HzBean;
import com.wonders.bean.NoteBean;
import com.wonders.bean.PicBean;
import com.wonders.bean.SopBean;
import com.wonders.bean.SopItemModel;
import com.wonders.bean.SopListViewBean;
import com.wonders.util.DateUtil;
import com.wonders.util.PicUtil;
import com.wonders.util.ToastUtil;
import com.wonders.widget.HeightExpandableGridView;
import com.wonders.widget.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 巡查录入
 */

@SuppressLint("NewApi")
public class CheckInputRcFragment extends CheckInputFragment implements MyExpandableListAdapter.DeleteListener, PicUtil.PicListener {
    private static final String TAG = CheckInputRcFragment.class.getName();
    private Db_message dbMessage;
    private AppData appData;
    // 根级菜单 组级菜单 单元级菜单
    private final static int TYPE_ROOT = 0;
    private final static int TYPE_GROUP = 1;
    private final static int TYPE_CELL = 2;

    // 计划检查项 新增检查项 其他
    private final static int KIND_PLAN = 3;
    private final static int KIND_ADDATION = 4;
    private final static int KIND_DIY = 5;

    private String planId, etpsId, userId;
    private ArrayList<SopListViewBean> hzSopList = new ArrayList<SopListViewBean>();
    private ArrayList<SopListViewBean> hzPlanList = new ArrayList<SopListViewBean>();
    private ArrayList<SopListViewBean> hzAddList = new ArrayList<SopListViewBean>();
    private ArrayList<SopListViewBean> hzDiyList = new ArrayList<SopListViewBean>();

    private ArrayList<HzBean> hzList = new ArrayList<HzBean>();
    private ArrayList<SopBean> sopList = new ArrayList<>();

    private Button hzBtn, ylBtn, qxBtn;
    private Button updataBtn;//提交
    private Button noProblemBtn;//批量通过
    private Button uncheckedBtn;//未检查提交
    private boolean isChecked = true;//true:检查；false：未检查
    private String notes = "";
    private TextView notes_tv;
    private LinearLayout managerLayoutOne;
    private LinearLayout managerLayoutTwo;
    private LinearLayout beizhu_layout;
    private LinearLayout tjLayout;
    private LinearLayout tjMonthLayout;
    private LinearLayout commitLayout, layoutYl;

    private ImageView sfxyhfIv;
    private ImageView sfxycyIv;
    private TextView sfxyhfTv;
    private TextView sfxycyTv;
    private TextView mouthTv;

    private boolean sfxyhfFlag = false;
    private boolean sfxycjFlag = false;
    private int checkMonth = 0;
    private int checkyear = 0;

    private EditText ptrEt;

    // 是否需要回访
    private String ifRevisit;

    private DbHelper dbHelper;
    private String allUserName;//用来判断检查人员个数

    //用于ExpandableListview
    private ArrayList<SopItemBean> items;
    private ArrayList<SopListViewBean> groupArray = new ArrayList<SopListViewBean>();
    private ArrayList<ArrayList<SopListViewBean>> childArray = new ArrayList<ArrayList<SopListViewBean>>();
    private ArrayList<SopListViewBean> groupArray2 = new ArrayList<SopListViewBean>();
    private ArrayList<ArrayList<SopListViewBean>> childArray2 = new ArrayList<ArrayList<SopListViewBean>>();
    private ArrayList<PicBean> imgArray;

    private ExpandableListView elv;
    private ExpandableListView elv2;
    private HeightExpandableGridView igv;

    private MyExpandableListAdapter myAdapter1;
    private MyExpandableListAdapter myAdapter2;
    private ImageGridViewAdapter imgAdapter;

    private ArrayList<SopListViewBean> childList;//保存新增的数据

    private List<Boolean> groupStatues = new ArrayList<Boolean>();//计划检查项的group状态值,true代表展开；false代表关闭
    private List<Boolean> groupStatues2 = new ArrayList<Boolean>();//新增SOP项group状态值,true代表展开；false代表关闭
    private Bundle bundleStatues2 = new Bundle();

    private String picPath = "";
    private String timeMark = "";

    private TextView text_title1;
    private TextView text_title2;
    private TextView text_title3;

    private ImageView img_addSop2;
    private boolean isOncreateView, isLoaded;
    private ArrayList<SopListViewBean> uploadDataList;
    private Call<ResponseBody> call;
    private RecordBean recordBean;
    private HashMap params;

    public MyHandler handler = new MyHandler(this);

    class MyHandler extends Handler {
        private WeakReference<Fragment> fragmentWeakReference;

        public MyHandler(Fragment fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showMid("暂时没有需要上传的待办数据");
                    LoadingDialog.dismiss();
                    break;
                case 1:
                    break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //初始化一些参数
        params = (getArguments().getSerializable(Constants.PARAMS) != null) ? (HashMap) getArguments().getSerializable(Constants.PARAMS) : new HashMap();
        planId = (params.get(Constants.PLAN_ID) != null) ? (String) params.get(Constants.PLAN_ID) : "";
        etpsId = (params.get(Constants.ETPS_ID) != null) ? (String) params.get(Constants.ETPS_ID) : "";
        allUserName = (params.get(Constants.ALL_USER_NAME) != null) ? (String) params.get(Constants.ALL_USER_NAME) : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isOncreateView = true;

        dbHelper = DbHelper.getInstance();
        appData = AppData.getInstance();
        userId = appData.getLoginBean().getUserId();
        myAdapter1 = new MyExpandableListAdapter(getActivity(), groupArray, childArray);
        myAdapter2 = new MyExpandableListAdapter(getActivity(), groupArray2, childArray2);
        myAdapter2.setDeleteListener(this);

//            读取本地图片
        imgArray = new ArrayList<>();
        imgAdapter = new ImageGridViewAdapter(getActivity(), imgArray);
        ImageGridViewAdapter.type = PicUtil.PIC_SHOW;

        View view = View.inflate(getActivity(), R.layout.fragment_check_input,
                null);

        initView(view);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isOncreateView && isVisibleToUser && !isLoaded) {
            getData();
        } else if (!isVisibleToUser && call != null && !isLoaded) {
            call.cancel();
        }
    }

    @Override
    public void deleteData(final SopListViewBean bean) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);
        builder.setTitle("提示"); // 设置标题
        builder.setMessage("是否确认删除?"); // 设置内容

        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() { // 设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() { // 设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // 关闭dialog

                        String itemCode = bean.getItemCode();
                        String content = bean.getContent();

                        //删除本地的数据
                        FastDealExecutor.run(new Runnable() {
                            @Override
                            public void run() {
                                dbHelper.deleteSop(userId, planId, itemCode, content);
                            }
                        });

                        boolean isHave = false;
                        for (int i = 0; i < childArray2.size(); i++) {
                            ArrayList<SopListViewBean> list = childArray2.get(i);
                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getItemCode().equals(itemCode)) {
                                    isHave = true;
                                    list.remove(j);
                                    if (list.size() == 0) {
                                        groupArray2.remove(i);
                                        childArray2.remove(i);
                                    } else {
                                        childArray2.set(i, list);
                                    }
                                    break;
                                }
                            }
                            if (isHave)
                                break;
                        }

                        if (groupStatues2.size() != groupArray2.size()) {//大项有减少，状态相应删除
                            groupStatues2.clear();
                            for (int i = 0; i < groupArray2.size(); i++) {
                                groupStatues2.add(bundleStatues2.getBoolean(groupArray2.get(i).getContent()));
                            }
                            bundleStatues2.remove(bean.getParentCode());
                        }

                        elv.setAdapter(myAdapter1);
                        elv2.setAdapter(myAdapter2);

                        for (int x = 0; x < myAdapter1.getGroupCount(); x++) {
                            if (groupStatues.get(x) == true) {
                                elv.expandGroup(x);
                            } else {
                                elv.collapseGroup(x);
                            }
                        }
                        for (int x = 0; x < myAdapter2.getGroupCount(); x++) {
                            if (groupStatues2.get(x) == true) {
                                elv2.expandGroup(x);
                            } else {
                                elv.collapseGroup(x);
                            }
                        }

                        if (appData.isNetWork()) {
                            if (planId != null
                                    && itemCode != null) {

                                LoadingDialog.show(getActivity());
                                String url = "";
                                if ("".equals(Constants.TYPE)) {
                                    url = Retrofit2Service.DELETE_ADD_SOP_INFO;
                                } else {
                                    url = Retrofit2Service.LT_DELETE_ADD_SOP_INFO;
                                }
                                Call<ResponseBody> call = Retrofit2Helper.getInstance().deleteAddSopInfo(url, planId, itemCode);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        String result = "";
                                        try {
                                            result = response.body().string();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        ToastUtil.show("删除成功");
                                        LoadingDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        LoadingDialog.dismiss();
                                        ToastUtil.show(getResources().getString(R.string.error_server));
                                    }
                                });
                            }
                        } else {
                            ToastUtil.show("删除成功");
                            LoadingDialog.dismiss();
                        }
                    }
                });
        builder.create().show();
    }

    private void initView(View view) {
        text_title1 = view.findViewById(R.id.item_title1).findViewById(R.id.text_title);
        text_title1.setText(getResources().getString(R.string.item_title1));

        text_title2 = view.findViewById(R.id.item_title2).findViewById(R.id.text_title);
        text_title2.setText(getResources().getString(R.string.item_title2));

        img_addSop2 = view.findViewById(R.id.item_title2).findViewById(R.id.img_add);
        img_addSop2.setVisibility(View.VISIBLE);
        img_addSop2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupArray.size() != 0) {
                    getSopList();
                    String type = groupArray.get(0).getContent().substring(0, 1);
                    createRootChooseDialog(getActivity(), type, sopList);
                }
            }
        });

        elv = view.findViewById(R.id.elv1);
        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (elv.isGroupExpanded(groupPosition)) {
                    elv.collapseGroup(groupPosition);
                    groupStatues.remove(groupPosition);
                    groupStatues.add(groupPosition, false);
                } else {
                    elv.expandGroup(groupPosition);
                    groupStatues.remove(groupPosition);
                    groupStatues.add(groupPosition, true);
                }
                return true;
            }
        });
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), InputActivity.class);

                if (childArray.get(groupPosition).get(childPosition).getKind() == KIND_DIY) {
                    intent.putExtra("isDiy", true);
                }
                intent.putExtra("planId", planId);
                intent.putExtra("etpsId", etpsId);
                intent.putExtra("planType", 0);
                intent.putExtra("groupPosition", groupPosition);
                intent.putExtra("childPosition", childPosition);
                intent.putExtra("sopBean", childArray.get(groupPosition).get(childPosition));
                ;
                startActivityForResult(intent, ItemWriteFragment.REFRESH_REQUEST_CODE1);

                return true;
            }
        });
        elv2 = view.findViewById(R.id.elv2);
        elv2.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (elv2.isGroupExpanded(groupPosition)) {
                    elv2.collapseGroup(groupPosition);
                    groupStatues2.remove(groupPosition);
                    groupStatues2.add(groupPosition, false);
                    bundleStatues2.remove(groupArray2.get(groupPosition).getContent());
                    bundleStatues2.putBoolean(groupArray2.get(groupPosition).getContent(), false);
                } else {
                    elv2.expandGroup(groupPosition);
                    groupStatues2.remove(groupPosition);
                    groupStatues2.add(groupPosition, true);
                    bundleStatues2.remove(groupArray2.get(groupPosition).getContent());
                    bundleStatues2.putBoolean(groupArray2.get(groupPosition).getContent(), true);
                }

                return true;
            }
        });
        elv2.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), InputActivity.class);

                if (childArray2.get(groupPosition).get(childPosition).getKind() == KIND_DIY) {
                    intent.putExtra("isDiy", true);
                }
                intent.putExtra("planId", planId);
                intent.putExtra("etpsId", etpsId);
                intent.putExtra("planType", 0);
                intent.putExtra("groupPosition", groupPosition);
                intent.putExtra("childPosition", childPosition);
                intent.putExtra("sopBean", childArray2.get(groupPosition).get(childPosition));
                startActivityForResult(intent, ItemWriteFragment.REFRESH_REQUEST_CODE2);

                return true;
            }
        });

        igv = view.findViewById(R.id.igv);
        igv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        igv.setAdapter(imgAdapter);
        igv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PicUtil picUtil = new PicUtil(getActivity());
                picUtil.setPicListener(CheckInputRcFragment.this);
                PicBean picBean = imgArray.get(position);
                picBean.setModel(0);
                picUtil.imgDialogShow(position, imgArray.size(), picBean);
            }
        });

        commitLayout = view.findViewById(R.id.commit_layout);

        layoutYl = view.findViewById(R.id.layout_yl);

        noProblemBtn = view.findViewById(R.id.no_problem_btn);
        noProblemBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                noProOption(childArray, etpsId, planId, 0);
                noProOption(childArray2, etpsId, planId, 0);

                refreshListView();

            }
        });

        updataBtn = view.findViewById(R.id.updata_btn);
        if (appData.isNetWork()) {
            updataBtn.setText("提交");
        } else {
            updataBtn.setText("预览");
        }
        updataBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("预览".equals(updataBtn.getText())) {
                    makeRecordDataOffLine();
                    actionYlSCOff();
                } else {
                    queryUploadData();

                    if (appData.getLoginBean().isManager() && !allUserName.contains(",")) {
                        showNoteDialog();

                        hzBtn.setVisibility(View.VISIBLE);
                        hzBtn.setText("办结");
                        qxBtn.setVisibility(View.VISIBLE);
                        ylBtn.setVisibility(View.VISIBLE);
                        ptrEt.setVisibility(View.VISIBLE);
                        tjLayout.setVisibility(View.VISIBLE);
                        tjMonthLayout.setVisibility(View.VISIBLE);
                        uncheckedBtn.setVisibility(View.GONE);
                    }
                }
            }
        });

        uncheckedBtn = view.findViewById(R.id.unchecked_btn);
        if (appData.getLoginBean().isManager() && appData.isNetWork()) {
            uncheckedBtn.setVisibility(View.VISIBLE);
        } else {
            uncheckedBtn.setVisibility(View.GONE);
        }
        uncheckedBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uncheckedBtn.getText().equals("未检查提交")) {
                    isChecked = false;
                }

                showNoteDialog();
            }
        });
        //备注
        beizhu_layout = (LinearLayout) view.findViewById(R.id.beizhu_layout);
        notes_tv = (TextView) view.findViewById(R.id.beizhu);
        notes_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoteDialog();
            }
        });


        tjLayout = (LinearLayout) view.findViewById(R.id.manager_check_box);
        tjMonthLayout = (LinearLayout) view.findViewById(R.id.manager_mouth);

        managerLayoutOne = (LinearLayout) view
                .findViewById(R.id.manager_check_box);
        managerLayoutTwo = (LinearLayout) view.findViewById(R.id.manager_mouth);

        if (!appData.getLoginBean().isManager()
                || !appData.isNetWork()) {
            managerLayoutOne.setVisibility(View.GONE);
            managerLayoutTwo.setVisibility(View.GONE);
        }

        sfxyhfIv = view.findViewById(R.id.sfxyhf_iv);
        sfxyhfIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSopHavePro()) {
                    ToastUtil.show("沒有发现问题的项，不能回访");
                    return;
                }

                sfxyhfFlag = !sfxyhfFlag;

                if (sfxyhfFlag) {
                    sfxyhfIv.setBackgroundResource(R.drawable.check);
                } else {
                    sfxyhfIv.setBackgroundResource(R.drawable.check_blank);
                }

            }
        });

        sfxycyIv = view.findViewById(R.id.sfxycy_iv);
        sfxycyIv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sfxycjFlag = !sfxycjFlag;

                if (sfxycjFlag) {
                    sfxycyIv.setBackgroundResource(R.drawable.check);
                } else {
                    sfxycyIv.setBackgroundResource(R.drawable.check_blank);
                }
            }
        });

        sfxyhfTv = view.findViewById(R.id.sfxyhf_tv);
        sfxyhfTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sfxyhfFlag = !sfxyhfFlag;

                if (sfxyhfFlag) {
                    sfxyhfIv.setBackgroundResource(R.drawable.check);
                } else {
                    sfxyhfIv.setBackgroundResource(R.drawable.check_blank);
                }

            }
        });

        sfxycyTv = view.findViewById(R.id.sfxycy_tv);
        sfxycyTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sfxycjFlag = !sfxycjFlag;

                if (sfxycjFlag) {
                    sfxycyIv.setBackgroundResource(R.drawable.check);
                } else {
                    sfxycyIv.setBackgroundResource(R.drawable.check_blank);
                }
            }
        });

        mouthTv = view.findViewById(R.id.mouth);
        mouthTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!sfxyhfFlag) {
                    ToastUtil.showMid("请先选择需要回访");

                    return;
                }

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_date_picker, null, false);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
                View view1 = ((ViewGroup) (((ViewGroup) datePicker.getChildAt(0)).getChildAt(0))).getChildAt(2);
                if (view1 != null)
                    view1.setVisibility(View.GONE);
//                ((ViewGroup) (((ViewGroup) datePicker.getChildAt(0)).getChildAt(0))).getChildAt(2).setVisibility(View.GONE);

                final Calendar curCalendar = Calendar.getInstance();

                datePicker.init(curCalendar.get(Calendar.YEAR),
                        curCalendar.get(Calendar.MONTH) + 1,
                        curCalendar.get(Calendar.DAY_OF_MONTH), null);


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);
                builder.setView(view);
                builder.setTitle("请选择日期");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkMonth = datePicker.getMonth() + 1;
                        checkyear = datePicker.getYear();
                        mouthTv.setText(checkyear + "-" + checkMonth);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });

        hzBtn = view.findViewById(R.id.hz_btn);
        hzBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.show(getActivity(), false);
                if (hzBtn.getText().toString().equals(Constants.HZ)) {
                    DbHelper dbhelper = DbHelper.getInstance();
                    //拿到需要做的列表
                    ArrayList<SopListViewBean> list = dbhelper.querySops(appData.getLoginBean().getUserId(), planId);
                    if (list.size() != 0) {
                        ToastUtil.show("您还有待提交的项目");
                        return;
                    }
                    saveData();
                    hzBtn.setText(Constants.BJ);
                    qxBtn.setVisibility(View.VISIBLE);
                    ylBtn.setVisibility(View.VISIBLE);
                    uncheckedBtn.setVisibility(View.GONE);
                    tjMonthLayout.setVisibility(View.VISIBLE);
                    ptrEt.setVisibility(View.VISIBLE);
                    tjLayout.setVisibility(View.VISIBLE);
                } else if (hzBtn.getText().equals(Constants.BJ)) {
                    DbHelper dbhelper = DbHelper.getInstance();
                    //拿到需要做的列表
                    ArrayList<SopListViewBean> list = dbhelper.querySops(appData.getLoginBean().getUserId(), planId);
                    if (!isChecked) {
                        list.clear();
                    } else {
                        if (list.size() != 0) {
                            ToastUtil.showMid("有未上传的项目，请先提交");
                            return;
                        }

                        if (!isSopDone()) {
                            ToastUtil.showMid("您还有待检查的项目");
                            return;
                        }

                        if (sfxyhfFlag) {
                            if (checkMonth == 0) {
                                ToastUtil.showMid("请选择回访月份");
                                return;
                            }
                        }
                    }

                    HashMap<String, String> params = new HashMap();
                    params.put("planId", planId);
                    params.put("accompaniedPeople", ptrEt.getText().toString());
                    params.put("month", checkMonth + "");
                    params.put("year", checkyear + "");
                    params.put("notes", notes);
                    if (sfxyhfFlag) {
                        params.put("hfflag", "1");
                    } else {
                        params.put("hfflag", "0");
                    }

                    if (sfxycjFlag) {
                        params.put("cjflag", "1");
                    } else {
                        params.put("cjflag", "0");
                    }

                    if (isChecked) {
                        params.put("isChecked", "1");
                    } else {
                        params.put("isChecked", "0");
                    }
                    String url = "";
                    if ("".equals(Constants.TYPE)) {
                        url = Retrofit2Service.SAVE_GATHER_PLAN_CHECK_CONTENT;
                    } else {
                        url = Retrofit2Service.LT_SAVE_GATHER_PLAN_CHECK_CONTENT;
                    }
                    Call<Result> call = Retrofit2Helper.getInstance().saveGatherPlanCheckContent(url, params);
                    call.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            LoadingDialog.dismiss();
                            if (response.body() != null
                                    && response.body().getCode() == 0) {
                                ToastUtil.show("该计划已办结");
                                getActivity().setResult(Activity.RESULT_OK);

                                if ("".equals(Constants.TYPE)) {
                                    SopListViewBean bean = groupArray.get(0);
                                    String content = bean.getContent();

                                    HashMap params = new HashMap();
                                    params.put(Constants.PLAN_TYPE, content.substring(0, 1));
                                    params.put(Constants.PLAN_ID, planId);
                                    params.put(Constants.DOC_TYPE, 2);
                                    PreviewScActivity.groupJSONArray = getAllGroup();
                                    PreviewScActivity.childJSONArray = getAllChild();
                                    PreviewScActivity.notesJSONArray = getNotes();
                                    Intent intent = new Intent(getActivity(), PreviewScActivity.class);
                                    intent.putExtra(Constants.PARAMS, params);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getActivity(), PreviewLtActivity.class);
                                    intent.putExtra("planId", planId);
                                    intent.putExtra("sub", "1");
                                    intent.putExtra("acc", ptrEt.getText().toString());

                                    startActivity(intent);
                                }
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            LoadingDialog.dismiss();
                            ToastUtil.show(getResources().getString(R.string.error_server));
                        }
                    });
                }
            }
        });

        ylBtn = view.findViewById(R.id.yl_btn);
        ylBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ylBtn.getText().equals("预览")) {
                    if ("".equals(Constants.TYPE)) {
                        actionYlSC();
                    } else {
                        Intent intent = new Intent(getActivity(), PreviewLtActivity.class);
                        intent.putExtra("planId", planId);
                        intent.putExtra("acc", ptrEt.getText().toString());
                        startActivity(intent);
                    }
                } else {
                    showNoteDialog();
                }

            }
        });

        qxBtn = view.findViewById(R.id.qx_btn);
        qxBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appData.getLoginBean().isManager()) {
                    if (allUserName.contains(",")) {
                        hzBtn.setVisibility(View.VISIBLE);
                        hzBtn.setText("汇总");
                    } else {
                        hzBtn.setVisibility(View.INVISIBLE);
                    }
                    uncheckedBtn.setVisibility(View.VISIBLE);
                    uncheckedBtn.setText("未检查提交");
                    notes = "";
                    beizhu_layout.setVisibility(View.INVISIBLE);

                    ylBtn.setVisibility(View.GONE);
                    v.setVisibility(View.GONE);

                    hzSopList.clear();
                    hzAddList.clear();
                    hzDiyList.clear();
                    hzPlanList.clear();
                    hzList.clear();
                    ptrEt.setVisibility(View.GONE);
                    tjLayout.setVisibility(View.GONE);
                    tjMonthLayout.setVisibility(View.GONE);
                }
            }
        });

        if (appData.isNetWork()) {
            if (appData.getLoginBean().isManager() && allUserName.contains(",")) {
                hzBtn.setVisibility(View.VISIBLE);
                ylBtn.setVisibility(View.INVISIBLE);
            } else {
                hzBtn.setVisibility(View.GONE);
            }
        } else {
            layoutYl.setVisibility(View.GONE);
        }
        ptrEt = view.findViewById(R.id.ptjcr_et);
    }

    /**
     * 获取数据
     **/
    public void getData() {
        Log.e("时间2", System.currentTimeMillis()+ "");
        LoadingDialog.show(getActivity());

        groupArray.clear();
        childArray.clear();
        groupArray2.clear();
        childArray2.clear();

        if (!appData.isNetWork()) {
            //单机版的操作
            dbMessage = AppData.getInstance().getDb_message();

            Gson gson = new Gson();
            items = gson.fromJson(dbMessage.getGet_planCheckContent(), new TypeToken<ArrayList<SopItemBean>>() {
            }.getType());

            makeSopItems(items);

            //和本地存储数据整合
            manageLocalData();

            //配置检查依据
            addCheckRules();

            setAdapter();

            LoadingDialog.dismiss();
            isLoaded = true;
        } else {
            String url = "".equals(Constants.TYPE)? url = Retrofit2Service.GET_PLAN_CHECK_CONTENT: Retrofit2Service.LT_GET_PLAN_CHECK_CONTENT;

            call = Retrofit2Helper.getInstance().getPlanCheckContent(url, planId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String result = "";
                    try {
                        result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    isLoaded = true;

                    ArrayList<PicBean> arrayList = new ArrayList<PicBean>();
                    Gson gson = new Gson();
                    try {
                        JSONObject json = new JSONObject(result);
                        if ("".equals(Constants.TYPE)) {
                            JSONObject object = json.getJSONObject("object");
                            items = gson.fromJson(object.getString("items"), new TypeToken<ArrayList<SopItemBean>>() {
                            }.getType());
//                            arrayList = gson.fromJson(json.getString("planPic"),new TypeToken<ArrayList<PicBean>>(){}.getType());
                        } else {
                            items = gson.fromJson(json.getString("object"), new TypeToken<ArrayList<SopItemBean>>() {
                            }.getType());
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (arrayList.size() != 0) {
//                        showPlanPic(arrayList,imgArray,planId);
                        imgAdapter.notifyDataSetChanged();
                    }

                    if (items != null)
                        makeSopItems(items);

                    //和本地存储数据整合
                    manageLocalData();

                    //配置检查依据
                    addCheckRules();

                    setAdapter();

                    getNotification();

                    LoadingDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    LoadingDialog.dismiss();
                }
            });
        }
    }

    public void makeSopItems(ArrayList<SopItemBean> items) {
        for (SopItemBean itemBean : items) {
            //封装二级列表的首项
            SopListViewBean groupBean = new SopListViewBean();
            groupBean.setContent(itemBean.getParentCode());

            //封装二级列表的子项
            SopListViewBean childBean = new SopListViewBean();

            sopBeanTransForm(childBean, itemBean);

            boolean isHave = false;
            int index = 0;
            switch (itemBean.getIfAdded()) {
                case "0":
                    childBean.setKind(KIND_PLAN);
                    for (int i = 0; i < groupArray.size(); i++) {
                        if (groupArray.get(i).getContent().equals(itemBean.getParentCode())) {
                            isHave = true;
                            index = i;
                            break;
                        }
                    }
                    if (isHave) {
                        childArray.get(index).add(childBean);
                    } else {
                        groupArray.add(groupBean);
                        ArrayList<SopListViewBean> list = new ArrayList<SopListViewBean>();
                        list.add(childBean);
                        childArray.add(list);
                    }
                    break;
                case "1":
                    if ("1".equals(itemBean.getIfCustom())) {
                        childBean.setKind(KIND_DIY);
                        childBean.setCheckCode("其他");
                    } else
                        childBean.setKind(KIND_ADDATION);
                    for (int i = 0; i < groupArray2.size(); i++) {
                        if (groupArray2.get(i).getContent().equals(itemBean.getParentCode())) {
                            isHave = true;
                            index = i;
                            break;
                        }
                    }
                    if (isHave) {
                        childArray2.get(index).add(childBean);
                    } else {
                        groupArray2.add(groupBean);
                        ArrayList<SopListViewBean> list = new ArrayList<SopListViewBean>();
                        list.add(childBean);
                        childArray2.add(list);
                    }
            }
        }
    }

    public void sopBeanTransForm(SopListViewBean childBean, SopItemBean itemBean) {
        switch (itemBean.getResult()) {
            case "":
                childBean.setIsPass("");
                childBean.setIsEdit("0");
                break;
            case "0":
                childBean.setIsPass("0");
                childBean.setIsEdit("1");
                break;
            case "1":
                childBean.setIsPass("1");
                childBean.setIsEdit("1");
                break;
        }
        childBean.setParentCode(itemBean.getParentCode());
        childBean.setItemCode(itemBean.getItemCode());
        childBean.setId(itemBean.getId());
        childBean.setContent(itemBean.getCheckContent());
        childBean.setRemark(itemBean.getRemark());
        if (itemBean.getPicInfo().size() == 0) {
            childBean.setIsHavePic("0");
            childBean.setPic(new ArrayList<PicBean>());
        } else {
            childBean.setIsHavePic("1");
            ArrayList<PicBean> pic = itemBean.getPicInfo();
            for (int i = 0; i < pic.size(); i++) {
                pic.get(i).setType(0);
            }
            childBean.setPic(pic);
        }

        if ("".equals(Constants.TYPE)) {
            childBean.setCheckCode(itemBean.getCheckCode());
            childBean.setIsKey(itemBean.getIsKey());
        } else {
            childBean.setCheckCode("");
            childBean.setIsKey("0");
        }
    }

    public void makeSopList(JSONArray array) throws JSONException {
        Gson gson = new Gson();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            //封装二级列表的首项
            SopListViewBean groupBean = new SopListViewBean();
            groupBean.setContent(jsonObject.getString("parentCode"));

            //封装二级列表的子项
            SopListViewBean childBean = new SopListViewBean();
            if ("1".equals(jsonObject.getString("result"))) {
                childBean.setIsPass("1");
                childBean.setIsEdit("1");
            } else if ("0".equals(jsonObject.getString("result"))) {
                childBean.setIsPass("0");
                childBean.setIsEdit("1");
            } else if ("".equals(jsonObject.getString("result"))) {
                childBean.setIsPass("");
                childBean.setIsEdit("0");
            }
            String pic = jsonObject.getString("picInfo");
            ArrayList<PicBean> picArray = gson.fromJson(pic, new TypeToken<ArrayList<PicBean>>() {
            }.getType());
            if (picArray.size() == 0) {
                childBean.setIsHavePic("0");
            } else {
                childBean.setIsHavePic("1");
            }
            childBean.setPic(picArray);
            //流通的字典表还未修改,这两个字段还没有
            if ("".equals(Constants.TYPE)) {
                childBean.setCheckCode(jsonObject.getString("checkCode"));
                childBean.setIsKey(jsonObject.getString("isKey"));
            }
            childBean.setParentCode(jsonObject.getString("parentCode"));
            childBean.setItemCode(jsonObject.getString("itemCode"));
            childBean.setId(jsonObject.getString("id"));
            childBean.setContent(jsonObject.getString("checkContent"));
            childBean.setRemark(jsonObject.getString("remark"));
            boolean isHave = false;
            int index = 0;
            if ("0".equals(jsonObject.getString("ifAdded"))) {
                childBean.setKind(KIND_PLAN);

                for (int j = 0; j < groupArray.size(); j++) {
                    if (jsonObject.getString("parentCode").equals(groupArray.get(j).getContent())) {
                        isHave = true;
                        index = j;
                        break;
                    }
                }
                if (isHave) {
                    childArray.get(index).add(childBean);
                } else {
                    groupArray.add(groupBean);
                    ArrayList<SopListViewBean> list = new ArrayList<SopListViewBean>();
                    list.add(childBean);
                    childArray.add(list);
                }

            } else {
                if ("1".equals(jsonObject.getString("ifCustom"))) {
                    childBean.setKind(KIND_DIY);
                    childBean.setCheckCode("其他");
                } else
                    childBean.setKind(KIND_ADDATION);

                for (int j = 0; j < groupArray2.size(); j++) {
                    if (jsonObject.getString("parentCode").equals(groupArray2.get(j).getContent())) {
                        isHave = true;
                        index = j;
                        break;
                    }
                }
                if (isHave) {
                    childArray2.get(index).add(childBean);
                } else {
                    groupArray2.add(groupBean);
                    ArrayList<SopListViewBean> list = new ArrayList<SopListViewBean>();
                    list.add(childBean);
                    childArray2.add(list);
                }
            }
        }
    }

    public void manageLocalData() {
        ArrayList<SopListViewBean> tempList = dbHelper.querySops(appData.getLoginBean().getUserId(), planId);

        for (int i = 0; i < tempList.size(); i++) {
            SopListViewBean childBean = tempList.get(i);
            if (childBean.getKind() == KIND_PLAN) {
                for (int j = 0; j < childArray.size(); j++) {
                    ArrayList<SopListViewBean> list = childArray.get(j);
                    for (int k = 0; k < list.size(); k++) {
                        SopListViewBean bean = list.get(k);
                        if (childBean.getItemCode().equals(bean.getItemCode())) {
                            childArray.get(j).set(k, childBean);
                        }
                    }
                }
            } else {
                //封装二级列表的首项
                SopListViewBean groupBean = new SopListViewBean();
                groupBean.setContent(childBean.getParentCode());

                boolean isGroupHave = false;
                if (groupArray2.size() != 0) {
                    for (int j = 0; j < groupArray2.size(); j++) {
                        if (groupBean.getContent().equals(groupArray2.get(j).getContent())) {
                            isGroupHave = true;
                            boolean isChildHave = false;
                            for (int k = 0; k < childArray2.size(); k++) {
                                ArrayList<SopListViewBean> list = childArray2.get(k);
                                for (int l = 0; l < list.size(); l++) {
                                    if (childBean.getItemCode().equals(list.get(l).getItemCode())) {
                                        isChildHave = true;
                                        childArray2.get(k).set(l, childBean);
                                        break;
                                    }
                                }
                                if (isChildHave)
                                    break;
                            }
                            if (!isChildHave) {
                                childArray2.get(j).add(childBean);
                            }
                            if (isGroupHave)
                                break;
                        }
                    }
                    if (!isGroupHave) {
                        groupArray2.add(groupBean);

                        ArrayList<SopListViewBean> list = new ArrayList<SopListViewBean>();
                        list.add(childBean);
                        childArray2.add(list);
                    }
                } else {
                    groupArray2.add(groupBean);

                    ArrayList<SopListViewBean> list = new ArrayList<SopListViewBean>();
                    list.add(childBean);
                    childArray2.add(list);
                }
            }
        }
    }

    public void addCheckRules() {
        for (int i = 0; i < childArray.size(); i++) {
            ArrayList<SopListViewBean> list = childArray.get(i);
            for (int j = 0; j < list.size(); j++) {
                SopListViewBean bean = list.get(j);
                for (int k = 0; k < sopList.size(); k++) {
                    ArrayList<SopItemModel> items = sopList.get(k).getItems();
                    for (int l = 0; l < items.size(); l++) {
                        if (bean.getItemCode().equals(items.get(l).getItemCode())) {
                            bean.setFaq(items.get(l).getFaq());
                            bean.setCheckBasis(items.get(l).getCheckBasis());
                            bean.setCheckRule(items.get(l).getCheckRule());
                            bean.setFoucsNotes(items.get(l).getFoucsNotes());
                            list.set(j, bean);
                            childArray.set(i, list);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < childArray2.size(); i++) {
            ArrayList<SopListViewBean> list = childArray2.get(i);
            for (int j = 0; j < list.size(); j++) {
                SopListViewBean bean = list.get(j);
                for (int k = 0; k < sopList.size(); k++) {
                    ArrayList<SopItemModel> items = sopList.get(k).getItems();
                    for (int l = 0; l < items.size(); l++) {
                        if (bean.getItemCode().equals(items.get(l).getItemCode())) {
                            bean.setFaq(items.get(l).getFaq());
                            bean.setCheckBasis(items.get(l).getCheckBasis());
                            bean.setCheckRule(items.get(l).getCheckRule());
                            bean.setFoucsNotes(items.get(l).getFoucsNotes());
                            list.set(j, bean);
                            childArray2.set(i, list);
                        }
                    }
                }
            }
        }
    }

    public void setAdapter() {
        elv.setAdapter(myAdapter1);
        elv2.setAdapter(myAdapter2);

        elv.setGroupIndicator(null);
        elv2.setGroupIndicator(null);
        elv.setDivider(null);
        elv2.setDivider(null);

        for (int i = 0; i < groupArray.size(); i++) {
            elv.expandGroup(i);
            groupStatues.add(true);
        }
        for (int i = 0; i < groupArray2.size(); i++) {
            elv2.expandGroup(i);
            groupStatues2.add(true);
            bundleStatues2.putBoolean(groupArray2.get(i).getContent(), true);
        }
    }

    private void saveData() {

        HashMap<String, String> params = new HashMap<>();
        params.put("planId", planId);
        params.put("etpsId", etpsId);

        String url = "";
        if ("".equals(Constants.TYPE)) {
            url = Retrofit2Service.GATHER_PLAN_CHECK_CONTENT;
        } else {
            url = Retrofit2Service.LT_GATHER_PLAN_CHECK_CONTENT;
        }
        Call<ResponseBody> call = Retrofit2Helper.getInstance().gatherPlanCheckContent(url, params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = "";
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                groupArray.clear();
                groupArray2.clear();
                childArray.clear();
                childArray2.clear();

                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (json == null) {
                    ToastUtil.showMid("服务器数据错误");

                    return;
                }

                JSONObject root = null;
                try {
                    root = new JSONObject(json.getString("object"));
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                if (root == null) {
                    ToastUtil.showMid("服务器数据出错");

                    return;
                }

                try {
                    ifRevisit = root.getString("ifRevisit");
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                if (ifRevisit != null && ifRevisit.equals("0")) {
                    tjMonthLayout.setVisibility(View.VISIBLE);
                } else {
                    tjMonthLayout.setVisibility(View.GONE);
                }

                JSONArray array = null;
                try {
                    array = new JSONArray(root.getString("gatherList"));
                    makeSopList(array);
                    JSONArray array2 = root.getJSONArray("countList");
                    makeRecordData(array2);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                //配置检查依据
                addCheckRules();

                setAdapter();

                showNoteDialog();

                LoadingDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoadingDialog.dismiss();
                ToastUtil.show(getResources().getString(R.string.error_server));
            }
        });
    }

    /**
     * 查询要上传的数据-异步
     */
    private void queryUploadData() {
        LoadingDialog.show(getActivity());

        if (BuildConfig.DEBUG) {
            io.reactivex.Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    Log.e(TAG, "当前线程：" + Thread.currentThread().getName());
                    uploadDataList = dbHelper.querySops(userId, planId);
                    ArrayList<PicBean> picList = dbHelper.selectPic(planId, appData.getLoginBean().getUserId());
                    if (uploadDataList.size() == 0 && picList.size() == 0) {
                        e.onNext(1);
                    } else {
                        uploadData();
                    }
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer integer) {
                            Log.e(TAG, "当前线程：" + Thread.currentThread().getName());
                            if (integer == 1) {
                                ToastUtil.showMid("暂时没有需要上传的待办数据");
                                LoadingDialog.dismiss();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            FastDealExecutor.run(new Runnable() {
                @Override
                public void run() {
                    uploadDataList = dbHelper.querySops(userId, planId);
                    ArrayList<PicBean> picList = dbHelper.selectPic(planId, appData.getLoginBean().getUserId());
                    if (uploadDataList.size() == 0 && picList.size() == 0) {
                        handler.sendEmptyMessage(0);
                    } else {
                        uploadData();
                    }
                }
            });
        }

    }

    /**
     * 上传数据
     */
    private void uploadData() {
        for (int i = 0; i < uploadDataList.size(); i++) {
            SopListViewBean sopListViewBean = uploadDataList.get(i);
            sopListViewBean.setDataType("online");
        }

        Gson gson = new Gson();
        String url = "";
        String uploadData = "";
        if ("".equals(Constants.TYPE)) {
            url = Retrofit2Service.SAVE_PLAN_CHECK_CONTENT;

            SavePlanBean savePlanBean = new SavePlanBean();
            savePlanBean.setItems(uploadDataList);
            savePlanBean.setPlanPic(imgArray);
            uploadData = gson.toJson(savePlanBean);
        } else {
            url = Retrofit2Service.LT_SAVE_PLAN_CHECK_CONTENT;
            uploadData = gson.toJson(uploadDataList);
        }

        byte[] uploadDataBuffer = null;
        try {
            uploadDataBuffer = uploadData.getBytes("gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), uploadDataBuffer);
        Call<Result> call = Retrofit2Helper.getInstance().savePlanCheckContent(url, requestBody);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null && response.body().getCode() == 0) {

                    deleteUploadedData();

                    try {
                        JSONArray array = new JSONArray(response.body().getMessage());
                        makeRecordData(array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //图片上传
                    ArrayList<PicBean> picList = dbHelper.selectPic(planId, userId);
                    if (picList.size() != 0)
                        uploadPics(picList, new PicUploadListener() {
                            @Override
                            public void success() {
                                getData();
                            }
                        });
                    else {
                        LoadingDialog.dismiss();
                        ToastUtil.show("提交数据成功");
                        getData();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                LoadingDialog.dismiss();
                ToastUtil.show(getResources().getString(R.string.error_server));
            }
        });
    }

    /**
     * 删除已上传数据
     */
    private void deleteUploadedData() {
        for (int i = 0; i < uploadDataList.size(); i++) {
            dbHelper.deleteSop(uploadDataList.get(i).getUserId(), uploadDataList.get(i).getPlanId(), uploadDataList.get(i).getItemCode(), uploadDataList.get(i).getContent());
        }
    }

    private void createRootChooseDialog(final Context context, String type, ArrayList<SopBean> sopList) {
        final ArrayList<SopBean> addSopList = new ArrayList<>();
        ArrayList<String> itemsList = new ArrayList<String>();
        for (int i = 0; i < sopList.size(); i++) {
            SopBean sopBean = sopList.get(i);
            String s = sopBean.getId().substring(0, 1);
            if (type.equals(s)) {
                itemsList.add(sopBean.getName());
                addSopList.add(sopBean);
            }
        }

        AlertDialog.Builder bulder = new AlertDialog.Builder(context, R.style.alertDialog);
        bulder.setSingleChoiceItems(itemsList.toArray(new String[itemsList.size()]), -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //多选框
                        createGroupDialog(context, which, addSopList);
                    }
                });
        bulder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        bulder.create().show();
    }

    private void createGroupDialog(Context context, int index, final ArrayList<SopBean> addSopList) {
        final int myindex = index;
        final SopBean sop = addSopList.get(index);

        ArrayList<CharSequence> itemsList = new ArrayList();
        final ArrayList<String> itemsList2 = new ArrayList();
        //取出页面上未展示的item项
        for (int i = 0; i < sop.getItems().size(); i++) {
            boolean isHave = false;
            for (int j = 0; j < childArray.size(); j++) {
                ArrayList<SopListViewBean> list = childArray.get(j);
                for (int k = 0; k < list.size(); k++) {
                    SopListViewBean bean = list.get(k);
                    if (sop.getItems().get(i).getCheckContent().equals(bean.getContent())) {
                        isHave = true;
                        break;
                    }
                }
            }

            if (!isHave) {
                for (int j = 0; j < childArray2.size(); j++) {
                    ArrayList<SopListViewBean> list = childArray2.get(j);
                    for (int k = 0; k < list.size(); k++) {
                        SopListViewBean bean = list.get(k);
                        if (sop.getItems().get(i).getCheckContent().equals(bean.getContent())) {
                            isHave = true;
                            break;
                        }
                    }
                }
            }

            if (!isHave) {
                if ("".equals(Constants.TYPE)) {
                    String text = sop.getItems().get(i).getCheckCode() + sop.getItems().get(i).getCheckContent();

                    if ("0".equals(sop.getItems().get(i).getIsKey()))
                        itemsList.add(text);
                    else
                        itemsList.add(Html.fromHtml("<font color=red>" + text + "</font>"));

                    itemsList2.add(text);
                } else {
                    String text = sop.getItems().get(i).getCheckContent();
                    itemsList.add(text);
                    itemsList2.add(text);
                }
            }
        }
        itemsList.add("其他");
        itemsList2.add("其他");
        final boolean checkedItems[] = new boolean[itemsList.size()];

        AlertDialog.Builder bulder = new AlertDialog.Builder(context, R.style.alertDialog);
        bulder.setMultiChoiceItems(itemsList.toArray(new CharSequence[itemsList.size()]), checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                    }
                }
        );
        bulder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                childList = new ArrayList<SopListViewBean>();
                boolean isChoose = false;
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i] == true) {
                        isChoose = true;
                        if (itemsList2.get(i) == "其他") {
                            dialog.dismiss();
                            createDiyDialog(myindex, getActivity(), addSopList);
                            return;
                        } else {
                            SopListViewBean sopBean = new SopListViewBean();
                            makeSopBean(sopBean, makeItemBean(i, addSopList.get(myindex).getItems(), itemsList2), addSopList);

                            childList.add(sopBean);
                        }
                    }
                }

                if (!isChoose) {
                    dialog.dismiss();
                } else {
                    makeAddData(childList, groupArray2, childArray2, groupStatues2, bundleStatues2);
                    refreshListView();
                }

            }

        });
        bulder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        bulder.show();

    }

    public static SopItemModel makeItemBean(int i, ArrayList<SopItemModel> items, ArrayList<String> itemsList2) {
        SopItemModel itemBean = new SopItemModel();
        for (int j = 0; j < items.size(); j++) {
            if ("".equals(Constants.TYPE)) {
                if ((items.get(j).getCheckCode() + items.get(j).getCheckContent())
                        .equals(itemsList2.get(i))) {
                    itemBean = items.get(j);
                    break;
                }
            } else {
                if (items.get(j).getCheckContent().equals(itemsList2.get(i))) {
                    itemBean = items.get(j);
                    break;
                }
            }
        }
        return itemBean;
    }

    public static void makeSopBean(SopListViewBean sopBean, SopItemModel itemBean, ArrayList<SopBean> addSopList) {
        sopBean.setId("");
        sopBean.setIsEdit("0");
        sopBean.setIsPass("0");
        sopBean.setIsHavePic("0");
        sopBean.setPic(new ArrayList<PicBean>());
        sopBean.setRemark("");
        sopBean.setType(TYPE_CELL);
        sopBean.setParentCode(itemBean.getCheckType());
        sopBean.setItemCode(itemBean.getItemCode());
        sopBean.setContent(itemBean.getCheckContent());
        sopBean.setKind(KIND_ADDATION);
        if ("".equals(Constants.TYPE)) {
            sopBean.setIsKey(itemBean.getIsKey());
            sopBean.setCheckCode(itemBean.getCheckCode());
        } else {
            sopBean.setIsKey("0");
            sopBean.setCheckCode("");
        }

        for (int j = 0; j < addSopList.size(); j++) {
            ArrayList<SopItemModel> itemList = addSopList.get(j).getItems();
            for (int k = 0; k < itemList.size(); k++) {
                if (sopBean.getItemCode().equals(itemList.get(k).getItemCode())) {
                    sopBean.setFaq(itemList.get(k).getFaq());
                    sopBean.setCheckBasis(itemList.get(k).getCheckBasis());
                    sopBean.setCheckRule(itemList.get(k).getCheckRule());
                    sopBean.setFoucsNotes(itemList.get(k).getFoucsNotes());
                }
            }
        }
    }

    private void createDiyDialog(final int index, final Context context, final ArrayList<SopBean> addSopList) {
        final int myindex = index;
        AlertDialog.Builder bulder = new AlertDialog.Builder(context, R.style.alertDialog);

        bulder.setTitle("请输入检查项");
        final EditText editText = new EditText(context);
        bulder.setView(editText);
        bulder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = editText.getText().toString();
                //不能输入为空
                if ("".equals(content)) {
                    ToastUtil.show("输入不能为空！");

                    return;
                }

                //判断输入是否重复
                if (isInput(content)) {
                    ToastUtil.show("请不要输入重复项！");

                    return;
                }

                SopListViewBean sopBean = new SopListViewBean();
                makeDiySopBean(sopBean);
                sopBean.setParentCode(addSopList.get(myindex).getId() + addSopList.get(myindex).getName());
                sopBean.setContent(content);
                childList.add(sopBean);

                makeAddData(childList, groupArray2, childArray2, groupStatues2, bundleStatues2);

                refreshListView();

            }
        });

        bulder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        bulder.create().show();

    }

    public static void makeDiySopBean(SopListViewBean sopBean) {
        sopBean.setId("");
        sopBean.setIsPass("0");
        sopBean.setIsEdit("0");
        sopBean.setIsHavePic("0");
        sopBean.setPic(new ArrayList<PicBean>());
        sopBean.setRemark("");
        sopBean.setType(TYPE_CELL);
        sopBean.setKind(KIND_DIY);
        sopBean.setItemCode(String.valueOf(System.currentTimeMillis()));
        if ("".equals(Constants.TYPE)) {
            sopBean.setCheckCode("其他 ");
            sopBean.setIsKey("0");
        } else {
            sopBean.setCheckCode("");
            sopBean.setIsKey("0");
        }
    }

    public boolean isInput(String content) {
        for (int i = 0; i < childArray.size(); i++) {
            ArrayList<SopListViewBean> list = childArray.get(i);
            for (int j = 0; j < list.size(); j++) {
                SopListViewBean bean = list.get(j);
                if (bean.getContent().equals(content))
                    return true;
            }
        }

        for (int i = 0; i < childArray2.size(); i++) {
            ArrayList<SopListViewBean> list = childArray2.get(i);
            for (int j = 0; j < list.size(); j++) {
                SopListViewBean bean = list.get(j);
                if (bean.getContent().equals(content))
                    return true;
            }
        }
        return false;
    }

    public void showNoteDialog() {
        View beizhu = LayoutInflater.from(getActivity()).inflate(R.layout.beizhu, null);
        final EditText et = (EditText) beizhu.findViewById(R.id.et);
        et.setText(notes);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);
        builder.setTitle("备注");
        builder.setView(beizhu);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (!isChecked) {
                    hzBtn.setVisibility(View.VISIBLE);
                    hzBtn.setText("办结");
                    commitLayout.setVisibility(View.GONE);
                    ylBtn.setVisibility(View.INVISIBLE);
                }
                notes = et.getText().toString();
                beizhu_layout.setVisibility(View.VISIBLE);
                notes_tv.setText(notes);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .create();
        Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                AppData.getInputMethodManger().showSoftInput(et, 0);
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public static void makeAddData(ArrayList<SopListViewBean> list, ArrayList<SopListViewBean> groupArray
            , ArrayList<ArrayList<SopListViewBean>> childArray, List<Boolean> groupStatues
            , Bundle bundleStatues) {
        SopListViewBean bean = list.get(0);
        if (groupArray.size() != 0) {
            boolean isHave = false;
            int index = 0;
            for (int i = 0; i < groupArray.size(); i++) {
                if (groupArray.get(i).getContent().equals(bean.getParentCode())) {
                    isHave = true;
                    index = i;
                    break;
                }
            }
            if (!isHave) {
                SopListViewBean sopListViewBean = new SopListViewBean();
                sopListViewBean.setContent(bean.getParentCode());
                sopListViewBean.setType(TYPE_GROUP);
                groupArray.add(sopListViewBean);
                childArray.add(list);

                groupStatues.add(true);
                bundleStatues.putBoolean(bean.getParentCode(), true);
            } else {
                ArrayList<SopListViewBean> list1 = childArray.get(index);
                for (int i = 0; i < list.size(); i++) {
                    list1.add(list.get(i));
                }
                childArray.set(index, list1);

                groupStatues.remove(index);
                groupStatues.add(index, true);
                bundleStatues.remove(bean.getParentCode());
                bundleStatues.putBoolean(bean.getParentCode(), true);
            }
        } else {
            SopListViewBean sopListViewBean = new SopListViewBean();
            sopListViewBean.setContent(bean.getParentCode());
            sopListViewBean.setType(TYPE_GROUP);
            groupArray.add(sopListViewBean);
            childArray.add(list);

            groupStatues.add(true);
            bundleStatues.putBoolean(bean.getParentCode(), true);
        }
    }

    public static void noProOption(ArrayList<ArrayList<SopListViewBean>> array, String etpsId, String planId, int planType) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        DbHelper dbHelper = DbHelper.getInstance();
        for (int i = 0; i < array.size(); i++) {
            ArrayList<SopListViewBean> list = array.get(i);
            for (int j = 0; j < list.size(); j++) {
                SopListViewBean bean = list.get(j);

                if ("0".equals(bean.getIsEdit())) {
                    bean.setIsEdit("1");
                    bean.setIsPass("1");
                    bean.setFirstDate(DateUtil.format(System.currentTimeMillis()));
                    bean.setPlanType(planType);
                    bean.setPlanId(planId);
                    bean.setEtpsId(etpsId);
                    bean.setUserId(AppData.getInstance().getLoginBean().getUserId());
                    bean.setYear(year + "");
                    bean.setMonth(month + 1 + "");
                    bean.setSecondDate("");
                    bean.setAddress("");
                    bean.setLongitude("");
                    bean.setLatitude("");
                    //修改childArray状态
                    list.set(j, bean);
                    array.set(i, list);

                    dbHelper.insertSopListViewBean(bean);

                }
            }
        }
    }

    public void refreshListView() {
        myAdapter1.notifyDataSetChanged();
        myAdapter2.notifyDataSetChanged();

        for (int x = 0; x < myAdapter1.getGroupCount(); x++) {
            if (groupStatues.get(x) == true) {
                elv.expandGroup(x);
            } else {
                elv.collapseGroup(x);
            }
        }
        for (int x = 0; x < myAdapter2.getGroupCount(); x++) {
            if (groupStatues2.get(x) == true) {
                elv2.expandGroup(x);
            } else {
                elv.collapseGroup(x);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0) {
            return;
        }

        switch (requestCode) {
            case ItemWriteFragment.REFRESH_REQUEST_CODE1:
                if (resultCode == ItemWriteFragment.REFERESH_RESULT_CODE_YES) {
                    SopListViewBean bean = data.getParcelableExtra("sopBean");
                    int groupPosition = data.getIntExtra("groupPosition", 0);
                    int childPosition = data.getIntExtra("childPosition", 0);
                    childArray.get(groupPosition).set(childPosition, bean);
                    refreshListView();
                }
                return;
            case ItemWriteFragment.REFRESH_REQUEST_CODE2:
                if (resultCode == ItemWriteFragment.REFERESH_RESULT_CODE_YES) {
                    SopListViewBean bean = data.getParcelableExtra("sopBean");
                    int groupPosition = data.getIntExtra("groupPosition", 0);
                    int childPosition = data.getIntExtra("childPosition", 0);
                    childArray2.get(groupPosition).set(childPosition, bean);
                    refreshListView();
                }
                return;
        }

        if (requestCode > 99 && resultCode == -1) {
            picPath = getPicPath(data);
        }

        int position = requestCode;
        if (requestCode > 10)
            position = requestCode - 100;
        else {
            ItemWriteFragment.waterMark(picPath, timeMark);
        }

        final PicBean picBean = new PicBean();
        picBean.setPicName(System.currentTimeMillis() + "");
        picBean.setPicPath(picPath);
        picBean.setPlanId(planId);
        picBean.setUserId(userId);
        picBean.setPicNum(position);
        picBean.setType(1);

        if (!"".equals(imgArray.get(position)) && (imgArray.size() - position) != 1) {
            //替换图片

            imgArray.set(position, picBean);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    dbHelper.updatePic(picBean);
                }
            }).start();

        } else {
            //添加图片
            imgArray.remove(imgArray.size() - 1);
            imgArray.add(picBean);
            if (imgArray.size() < PicUtil.PIC_MAX_SC) {
                PicBean picBean1 = new PicBean();
                picBean1.setPicPath("");
                imgArray.add(picBean1);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    dbHelper.savePic(picBean);
                }
            }).start();

        }

        imgAdapter.notifyDataSetChanged();

    }

    public String getPicPath(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor c = getActivity().getContentResolver().query(selectedImage,
                filePathColumns, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePathColumns[0]);
        String picturePath = c.getString(columnIndex);
        c.close();

        return picturePath;
    }

    public JSONArray getAllGroup() {
        JSONArray arrayList = new JSONArray();
        makeGroupData(arrayList, groupArray);
        makeGroupData(arrayList, groupArray2);

        return arrayList;
    }

    public static void makeGroupData(JSONArray arrayList, ArrayList<SopListViewBean> array) {
        for (SopListViewBean bean : array) {
            String content = bean.getContent();
            String s = getGroupName(content);

            arrayList.put(s);
        }
    }

    public static String getGroupName(String content) {
        int length = content.length();
        int i = 0;
        for (; i < length; i++) {
            String s = content.substring(i, i + 1);
            if (s.matches("[\u4e00-\u9fa5]")) {
                break;
            }
        }
        String s0 = content.substring(i, length);
        String s1 = "";
        if (i == 2) {
            s1 = content.substring(1, 2);
        } else if (i == 3) {
            s1 = content.substring(1, 3);
        }
        String s = s1 + "." + s0;

        return s;
    }

    public JSONArray getAllChild() {
        JSONArray arrayList = new JSONArray();
        makeChildData(arrayList, childArray);
        makeChildData(arrayList, childArray2);

        return arrayList;
    }

    public static void makeChildData(JSONArray arrayList, ArrayList<ArrayList<SopListViewBean>> childArray) {
        for (ArrayList<SopListViewBean> array : childArray) {
            JSONArray arrayList1 = new JSONArray();
            for (SopListViewBean bean : array) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("content", bean.getContent());
                    if (bean.getRemark() == null)
                        jsonObject.put("remark", "");
                    else
                        jsonObject.put("remark", bean.getRemark());
                    jsonObject.put("isPass", bean.getIsPass());
                    jsonObject.put("isEdit", bean.getIsEdit());
                    String num = getItemNum(bean.getItemCode());
                    if ("9".equals(bean.getItemCode().substring(bean.getItemCode().length() - 1, bean.getItemCode().length())))
                        num = "其他";
                    jsonObject.put("itemCode", num);
                    arrayList1.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            arrayList.put(arrayList1);
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
            String checkContent = getItemNum(itemCode);
            if ("9".equals(itemCode.substring(itemCode.length() - 1, itemCode.length())))
                checkContent = "其他";
            String note = "";
            bean.setCheckContent(checkContent);
            bean.setNote(note);
            getRemark(childArray, itemCode, bean);
            getRemark(childArray2, itemCode, bean);

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

    public void getRemark(ArrayList<ArrayList<SopListViewBean>> arrayLists, String itemCode, NoteBean noteBean) {
        for (ArrayList<SopListViewBean> arrayList : arrayLists) {
            for (SopListViewBean bean : arrayList) {
                if (bean.getItemCode().equals(itemCode)) {
                    noteBean.setNote(bean.getRemark() == null ? "" : bean.getRemark());
                    String s = noteBean.getCheckContent();
                    noteBean.setCheckContent(s);
                    break;
                }
            }
        }
    }

    public void getSopList() {
        if (Constants.TYPE.equals("")) {
            sopList = AppData.getInstance().getSopList();
        } else {
            if (AppData.getInstance().getSopType().equals("0a")) {
                sopList = AppData.getInstance().getSopList_lt().get(0);
            } else if (AppData.getInstance().getSopType().equals("0b")) {
                sopList = AppData.getInstance().getSopList_lt().get(1);
            } else if (AppData.getInstance().getSopType().equals("0c")) {
                sopList = AppData.getInstance().getSopList_lt().get(2);
            } else if (AppData.getInstance().getSopType().equals("0d")) {
                sopList = AppData.getInstance().getSopList_lt().get(3);
            } else if (AppData.getInstance().getSopType().equals("0e")) {
                sopList = AppData.getInstance().getSopList_lt().get(4);
            } else if (AppData.getInstance().getSopType().equals("0f")) {
                sopList = AppData.getInstance().getSopList_lt().get(5);
            } else if (AppData.getInstance().getSopType().equals("0g")) {
                sopList = AppData.getInstance().getSopList_lt().get(6);
            }
        }
    }

    public static void makeRecordData(JSONArray array) {
        PreviewScActivity.highItems.clear();
        PreviewScActivity.lowItems.clear();
        PreviewScActivity.highPros.clear();
        PreviewScActivity.lowPros.clear();
        try {
            PreviewScActivity.count1 = array.get(0).toString();
            PreviewScActivity.count2 = array.get(1).toString();
            PreviewScActivity.count3 = array.get(2).toString();
            JSONArray jsonArray4 = array.getJSONArray(3);
            if (jsonArray4.length() != 0) {
                String s4 = "";
                for (int i = 0; i < jsonArray4.length(); i++) {
                    PreviewScActivity.highItems.add(jsonArray4.get(i).toString());
                    String itemCode = jsonArray4.get(i).toString();
                    int length = itemCode.length();
                    String s = "";
                    if ("9".equals(itemCode.substring(length - 1, length)))
                        s = "其他";
                    else
                        s = getItemNum(itemCode);
                    s4 += s.substring(1, s.length()) + "、";
                }
                PreviewScActivity.count4 = s4.substring(0, s4.length() - 1);
            } else {
                PreviewScActivity.count4 = "";
            }
            PreviewScActivity.count5 = array.get(4).toString();
            JSONArray jsonArray6 = array.getJSONArray(5);
            if (jsonArray6.length() != 0) {
                String s6 = "";
                for (int i = 0; i < jsonArray6.length(); i++) {
                    PreviewScActivity.lowItems.add(jsonArray6.get(i).toString());
                    String itemCode = jsonArray6.get(i).toString();
                    int length = itemCode.length();
                    String s = "";
                    if ("9".equals(itemCode.substring(length - 1, length)))
                        s = "其他";
                    else
                        s = getItemNum(itemCode);
                    s6 += s + "、";
                }
                PreviewScActivity.count6 = s6.substring(0, s6.length() - 1);
            } else {
                PreviewScActivity.count6 = "";
            }
            PreviewScActivity.count7 = array.get(6).toString();
            JSONArray jsonArray8 = array.getJSONArray(7);
            if (jsonArray8.length() != 0) {
                String s8 = "";
                for (int i = 0; i < jsonArray8.length(); i++) {
                    PreviewScActivity.highPros.add(jsonArray8.get(i).toString());
                    String itemCode = jsonArray8.get(i).toString();
                    int length = itemCode.length();
                    String s = "";
                    if ("9".equals(itemCode.substring(length - 1, length)))
                        s = "其他";
                    else
                        s = getItemNum(itemCode);
                    s8 += s.substring(1, s.length()) + "、";
                }
                PreviewScActivity.count8 = s8.substring(0, s8.length() - 1);
            } else {
                PreviewScActivity.count8 = "";
            }
            PreviewScActivity.count9 = array.get(8).toString();
            JSONArray jsonArray10 = array.getJSONArray(9);
            if (jsonArray10.length() != 0) {
                String s10 = "";
                for (int i = 0; i < jsonArray10.length(); i++) {
                    PreviewScActivity.lowPros.add(jsonArray10.get(i).toString());
                    String itemCode = jsonArray10.get(i).toString();
                    int length = itemCode.length();
                    String s = "";
                    if ("9".equals(itemCode.substring(length - 1, length)))
                        s = "其他";
                    else
                        s = getItemNum(itemCode);
                    s10 += s + "、";
                }
                PreviewScActivity.count10 = s10.substring(0, s10.length() - 1);
            } else {
                PreviewScActivity.count10 = "";
            }
            PreviewScActivity.checkResult = array.get(10).toString();
            String startDate = array.get(11).toString().substring(0, 10);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try {
                date = sdf.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            PreviewScActivity.startDate = (date.getYear() + 1900) + "年" + (date.getMonth() + 1) + "月" + date.getDate() + "日";


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getItemNum(String itemCode) {
        String s0 = "";
        String s1 = "";
        String s2 = "";
        String s3 = "";
        switch (itemCode.length()) {
            case 5:
                s1 = itemCode.substring(1, 2);
                s2 = itemCode.substring(2, 3);
                if ("0".equals(s2))
                    s3 = itemCode.substring(3, 4);
                else
                    s3 = itemCode.substring(2, 4);
                break;
            case 6:
                s1 = itemCode.substring(1, 3);
                s2 = itemCode.substring(3, 4);
                if ("0".equals(s2))
                    s3 = itemCode.substring(4, 5);
                else
                    s3 = itemCode.substring(3, 5);
                break;
        }
        s0 = s1 + "." + s3;

        boolean isHave = false;
        for (String item : PreviewScActivity.highItems) {
            if (item.equals(itemCode)) {
                isHave = true;
                break;
            }
        }

        if (isHave)
            s0 = "*" + s0;

        return s0;
    }

    @Override
    public void deletePic(int position) {
        final ArrayList<PicBean> picList = new ArrayList<>();
        int max = imgArray.size();
        if (!(!"".equals(imgArray.get(imgArray.size() - 1).getPicPath()) && imgArray.size() == PicUtil.PIC_MAX_SC))
            max--;
        for (int i = position; i < max; i++) {
            picList.add(imgArray.get(i));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                dbHelper.deletePic(picList);
            }
        }).start();

        imgArray.remove(position);

        if (!"".equals(imgArray.get(imgArray.size() - 1).getPicPath())) {
            PicBean picBean = new PicBean();
            picBean.setPicPath("");
            picBean.setType(1);
        }

        imgAdapter.notifyDataSetChanged();
    }

    @Override
    public void paiZhao(int position) {
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        timeMark = DateUtil.format(System.currentTimeMillis());
        picPath = Environment.getExternalStorageDirectory()
                + "/DCIM/Camera/" + timeMark + ".jpg";
        File out = new File(picPath);
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, position);
    }

    @Override
    public void xuanQu(int position) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, position);
    }


    public boolean isSopDone() {
        for (ArrayList<SopListViewBean> arrayList : childArray) {
            for (SopListViewBean bean : arrayList) {
                if ("0".equals(bean.getIsEdit())) {
                    return false;
                }
            }
        }

        for (ArrayList<SopListViewBean> arrayList : childArray2) {
            for (SopListViewBean bean : arrayList) {
                if ("0".equals(bean.getIsEdit())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSopHavePro() {
        for (ArrayList<SopListViewBean> arrayList : childArray) {
            for (SopListViewBean bean : arrayList) {
                if ("1".equals(bean.getIsEdit())) {
                    if ("0".equals(bean.getIsPass()))
                        return true;
                }
            }
        }

        for (ArrayList<SopListViewBean> arrayList : childArray2) {
            for (SopListViewBean bean : arrayList) {
                if ("1".equals(bean.getIsEdit())) {
                    if ("0".equals(bean.getIsPass()))
                        return true;
                }
            }
        }
        return false;
    }

    public boolean isFirstDo(ArrayList<ArrayList<SopListViewBean>> array) {
        for (ArrayList<SopListViewBean> arrayList : array) {
            for (SopListViewBean bean : arrayList) {
                if ("1".equals(bean.getIsEdit()))
                    return false;
            }
        }
        return true;
    }

    public void getNotification() {
        if ("".equals(Constants.TYPE)) {
            if (AppData.getInstance().isFirstDo) {
                if (isFirstDo(childArray) && isFirstDo(childArray2)) {
                    AppData.getInstance().isFirstDo = false;
                    Intent intent = new Intent(getActivity(), NotesActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    /*
    * 生产预览
    * */
    public void actionYlSC() {
        SopListViewBean bean = groupArray.get(0);
        String content = bean.getContent();
        HashMap params = new HashMap();
        params.put(Constants.PLAN_TYPE, content.substring(0, 1));
        params.put(Constants.PLAN_ID, planId);
        params.put(Constants.DOC_TYPE, 2);
        PreviewScActivity.groupJSONArray = getAllGroup();
        PreviewScActivity.childJSONArray = getAllChild();
        PreviewScActivity.notesJSONArray = getNotes();
        Intent intent = new Intent(getActivity(), PreviewScActivity.class);
        intent.putExtra(Constants.PARAMS, params);
        startActivity(intent);
    }

    /*
    * 生产预览
    * */
    public void actionYlSCOff() {
        SopListViewBean bean = groupArray.get(0);
        String content = bean.getContent();
        HashMap params = new HashMap();
        params.put(Constants.PLAN_TYPE, content.substring(0, 1));
        params.put(Constants.PLAN_ID, planId);
        params.put("recordBean", recordBean);
        params.put(Constants.DOC_TYPE, 2);
        PreviewScActivity.groupJSONArray = getAllGroup();
        PreviewScActivity.childJSONArray = getAllChild();
        PreviewScActivity.notesJSONArray = getNotes();

        Intent intent = new Intent(getActivity(), PreviewScActivity.class);
        intent.putExtra(Constants.PARAMS, params);
        startActivity(intent);
    }

    public void makeRecordDataOffLine() {
        recordBean = new RecordBean();
        recordBean.setCheckNum("\u3000");
        recordBean.setCheckDate(DateUtil.formate2(System.currentTimeMillis()));

        makeCounts(childArray);
        makeCounts(childArray2);
    }

    public void makeCounts(ArrayList<ArrayList<SopListViewBean>> childArray) {
        for (ArrayList<SopListViewBean> arrayList : childArray) {
            for (SopListViewBean sopListViewBean : arrayList) {
                recordBean.setItemCounts(recordBean.getItemCounts() + 1);
                if ("1".equals(sopListViewBean.getIsKey())) {
                    recordBean.setHighCounts(recordBean.getHighCounts() + 1);
                    recordBean.setHighContents(recordBean.getHighContents() == null ? "" : recordBean.getHighContents() + sopListViewBean.getCheckCode() + ",");
                    if ("0".equals(sopListViewBean.getIsPass())) {
                        recordBean.setHighProCounts(recordBean.getHighProCounts() + 1);
                        recordBean.setHighProContents(((recordBean.getHighProContents() == null) ? "" : recordBean.getHighProContents())
                                + sopListViewBean.getCheckCode() + ",");
                    }
                } else {
                    recordBean.setLowCounts(recordBean.getLowCounts() + 1);
                    recordBean.setLowContents(recordBean.getLowContents() == null ? "" : recordBean.getLowContents() + sopListViewBean.getCheckCode() + ",");
                    if ("0".equals(sopListViewBean.getIsPass())) {
                        recordBean.setLowProCounts(recordBean.getLowProCounts() + 1);
                        recordBean.setLowProContents(((recordBean.getLowProContents() == null) ? "" : recordBean.getLowProContents())
                                + sopListViewBean.getCheckCode() + ",");
                    }
                }
            }
        }
    }
}
