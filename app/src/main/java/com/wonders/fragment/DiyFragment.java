package com.wonders.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.legal_rights.R;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.wonders.activity.InputActivity;
import com.wonders.activity.MessageActivity;
import com.wonders.activity.YlActivity;
import com.wonders.adapter.MyExpandableListAdapter;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.bean.NoteBean;
import com.wonders.bean.PicBean;
import com.wonders.bean.SopBean;
import com.wonders.bean.SopItemModel;
import com.wonders.bean.SopListViewBean;
import com.wonders.util.DateUtil;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wonders.fragment.ItemWriteFragment.REFERESH_RESULT_CODE_YES;


/**
 * 查看相关事项的页面
 *
 * @author Administrator
 */

@SuppressLint("NewApi")
public class DiyFragment extends Fragment implements MyExpandableListAdapter.DeleteListener {

    public static int type = 0;

    private DbHelper dbHelper;
    private AppData appData;
    private final static int KIND_DIY = 5;
    private ArrayList<SopListViewBean> childList;//保存新增的数据
    private String planId = "", etpsId = "";
    private ArrayList<SopBean> sopList;
    private ArrayList<SopBean> addSopList = new ArrayList<>();

    private Button ylBtn;
    private Button updataBtn;
    private Button noProblemBtn;
    private ExpandableListView elv;
    private MyExpandableListAdapter elvAdapter;

    private String addType = "H";

    private ArrayList<SopListViewBean> groupArray = new ArrayList<SopListViewBean>();
    private ArrayList<ArrayList<SopListViewBean>> childArray = new ArrayList<ArrayList<SopListViewBean>>();

    private List<Boolean> groupStatues = new ArrayList<Boolean>();//新增SOP项group状态值,true代表展开；false代表关闭
    private Bundle bundleStatues = new Bundle();

    private TextView text_title2;
    private TextView text_title3;
    private ImageView img_addSop;
    private LinearLayout layout_item;
    private LinearLayout layout_img;
    private ArrayList<SopListViewBean> uploadDataList;
    private String tradeType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        HashMap params = (HashMap) getArguments().getSerializable(Constants.PARAMS);
        if (params != null){
            planId = (String) params.get(Constants.PLAN_ID);
            etpsId = (String) params.get(Constants.ETPS_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dbHelper = new DbHelper(getActivity(), DbConstants.TABLENAME, null, 1);
        appData = AppData.getInstance();

        View view = View.inflate(getActivity(), R.layout.fragment_diy, null);

        findView(view);

        getData();

        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        elv.setAdapter(elvAdapter);
        for (int i = 0; i < elvAdapter.getGroupCount(); i++) {
            elv.expandGroup(i);
        }

        if (Constants.TYPE.equals("")) {
            sopList = appData.getSopList();
        } else {
            sopList = Hawk.get(Constants.SOP_LT_ITEM_LIST);
            tradeType = sopList != null? sopList.get(0).getTradeType(): "";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ItemWriteFragment.REFRESH_REQUEST_CODE1:
                if (resultCode == REFERESH_RESULT_CODE_YES) {
                    SopListViewBean bean = data.getParcelableExtra("sopBean");
                    int groupPosition = data.getIntExtra("groupPosition", 0);
                    int childPosition = data.getIntExtra("childPosition", 0);
                    childArray.get(groupPosition).set(childPosition, bean);
                    refreshListView();
                }
                return;
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
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        dialog.dismiss();

                    }
                });

        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() { // 设置确定按钮
                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        dialog.dismiss();

                        String itemCode = bean.getItemCode();
                        String content = bean.getContent();
                        dbHelper.deleteSop(AppData.getInstance().getLoginBean().getUserId(),planId, itemCode, content);

                        for (int i = 0; i < childArray.size(); i++) {
                            ArrayList<SopListViewBean> list = childArray.get(i);
                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getItemCode().equals(itemCode)) {
                                    list.remove(j);
                                    if (list.size() == 0) {
                                        childArray.remove(i);
                                        groupArray.remove(i);
                                    } else {
                                        childArray.set(i, list);
                                    }
                                }
                            }
                        }

                        if (groupStatues.size() != groupArray.size()) {//大项有减少，状态相应删除
                            for (int i = 0; i < groupArray.size(); i++) {
                                groupStatues.clear();
                                groupStatues.add(bundleStatues.getBoolean(groupArray.get(i).getContent()));
                            }
                            bundleStatues.remove(bean.getParentCode());
                        } else {
                            for (int j = 0; j < groupArray.size(); j++) {//大项有修改，状态默认展开
                                if (groupArray.get(j).getContent().equals(bean.getParentCode())) {
                                    groupStatues.remove(j);
                                    groupStatues.add(j, true);
                                    bundleStatues.remove(bean.getParentCode());
                                    bundleStatues.putBoolean(bean.getParentCode(), true);
                                    break;
                                }
                            }
                        }

                        //refreshListView();
                        elv.setAdapter(elvAdapter);
                        for (int x = 0; x < elvAdapter.getGroupCount(); x++) {
                            if (groupStatues.get(x) == true) {
                                elv.expandGroup(x);
                            } else {
                                elv.collapseGroup(x);
                            }
                        }
                    }
                });
        builder.create().show();
    }


    public void findView(View view) {
        text_title2 = (TextView) view.findViewById(R.id.item_title2).findViewById(R.id.text_title);
        text_title2.setText(getResources().getString(R.string.item_title2));

        img_addSop = (ImageView) view.findViewById(R.id.item_title2).findViewById(R.id.img_add);
        img_addSop.setVisibility(View.VISIBLE);
        img_addSop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(Constants.TYPE)){
                    if (groupArray.size() == 0) {
                        createTypeDialog();
                        return;
                    } else {
                        createRootChooseDialog(addType);
                    }
                }else {
                    addType = "L";
                    createRootChooseDialog(addType);
                }
            }
        });

        //fragment点击穿透
        layout_item = (LinearLayout)view.findViewById(R.id.item_title2);
        layout_item.setClickable(true);

        layout_img = (LinearLayout) view.findViewById(R.id.layout_img);
        text_title3 = (TextView) view.findViewById(R.id.item_title3).findViewById(R.id.text_title);
        text_title3.setText(getResources().getString(R.string.item_title3));

        elv = (ExpandableListView) view.findViewById(R.id.elv);
        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (elv.isGroupExpanded(groupPosition)) {
                    elv.collapseGroup(groupPosition);
                    groupStatues.remove(groupPosition);
                    groupStatues.add(groupPosition, false);
                    bundleStatues.remove(groupArray.get(groupPosition).getContent());
                    bundleStatues.putBoolean(groupArray.get(groupPosition).getContent(), false);
                } else {
                    elv.expandGroup(groupPosition);
                    groupStatues.remove(groupPosition);
                    groupStatues.add(groupPosition, true);
                    bundleStatues.remove(groupArray.get(groupPosition).getContent());
                    bundleStatues.putBoolean(groupArray.get(groupPosition).getContent(), true);
                }


                return true;
            }
        });

        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), InputActivity.class);
                appData.setSopTemp(childArray.get(groupPosition).get(childPosition));

                if (childArray.get(groupPosition).get(childPosition).getKind() == KIND_DIY) {
                    intent.putExtra("isDiy", true);
                }

                intent.putExtra("planId", planId);
                intent.putExtra("etpsId", etpsId);
                intent.putExtra("planType",1);
                intent.putExtra("groupPosition",groupPosition);
                intent.putExtra("childPosition",childPosition);
                intent.putExtra("sopBean",childArray.get(groupPosition).get(childPosition));
                startActivityForResult(intent, ItemWriteFragment.REFRESH_REQUEST_CODE1);

                return true;
            }
        });


        noProblemBtn = (Button) view.findViewById(R.id.no_problem_btn);
        noProblemBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //批量选择未发现问题
                CheckTypeInFragment.noProOption(childArray,etpsId,planId,1);

                refreshListView();
            }
        });

        updataBtn = (Button) view.findViewById(R.id.updata_btn);

        ylBtn = (Button) view.findViewById(R.id.yl_btn);
        ylBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("".equals(Constants.TYPE)){
                    SopListViewBean bean = groupArray.get(0);
                    String content = bean.getContent();
                    HashMap params = new HashMap();
                    params.put(Constants.PLAN_TYPE, content.substring(0, 1));
                    params.put(Constants.DOC_TYPE, 2);
                    JSONArray arrayList1 = new JSONArray();
                    CheckTypeInFragment.makeGroupData(arrayList1,groupArray);
                    MessageActivity.groupJSONArray = arrayList1;
                    JSONArray arrayList2 = new JSONArray();
                    CheckTypeInFragment.makeChildData(arrayList2,childArray);
                    MessageActivity.childJSONArray = arrayList2;
                    MessageActivity.notesJSONArray = getNotes();

                    Intent intent = new Intent(getActivity(), MessageActivity.class);
                    intent.putExtra(Constants.PARAMS, params);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), YlActivity.class);
                    intent.putExtra("planId", planId);
                    startActivity(intent);
                }

            }
        });


        if (appData.isNetWork()) {
            updataBtn.setText("提交");
        } else {
            updataBtn.setText("预览");
        }

        updataBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updataBtn.getText().toString().equals("预览")) {

                } else {
                    if (appData.isNetWork()) {
                        //上传此条待办的全部内容
                        updateOnClick();

                        ylBtn.setVisibility(View.VISIBLE);
                    } else {
                        //预览
                        Intent intent = new Intent(getActivity(), YlActivity.class);
                        intent.putExtra(Constants.PLAN_ID, planId);
                        intent.putExtra("acc", "");

                        startActivity(intent);
                    }
                }
            }
        });

    }

    public void getData() {
        ArrayList<SopListViewBean> tempList = dbHelper.querySops(appData.getLoginBean().getUserId(), planId);
        for (int i = 0; i < tempList.size(); i++) {
            SopListViewBean childBean = tempList.get(i);
            
            //封装二级列表的首项和子项
            SopListViewBean groupBean = new SopListViewBean();
            groupBean.setContent(childBean.getParentCode());


            boolean isGroupHave = false;
            if (groupArray.size() != 0) {
                for (int j = 0; j < groupArray.size(); j++) {
                    if (groupBean.getContent().equals(groupArray.get(j).getContent())) {
                        isGroupHave = true;
                        childArray.get(j).add(childBean);
                        break;
                    }
                }
                if (!isGroupHave) {
                    groupArray.add(groupBean);

                    ArrayList<SopListViewBean> list = new ArrayList<SopListViewBean>();
                    list.add(childBean);
                    childArray.add(list);
                }
            } else {
                groupArray.add(groupBean);

                ArrayList<SopListViewBean> list = new ArrayList<SopListViewBean>();
                list.add(childBean);
                childArray.add(list);
            }
        }

        elvAdapter = new MyExpandableListAdapter(getActivity(), groupArray, childArray);
        elvAdapter.setDeleteListener(this);

        //配置检查依据
        addCheckRules();

        elv.setAdapter(elvAdapter);
        elv.setGroupIndicator(null);
        elv.setDivider(null);

        for (int i = 0; i < groupArray.size(); i++) {
            elv.expandGroup(i);
            groupStatues.add(true);
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
    }

    private void updateOnClick() {
        LoadingDialog.show(getActivity());

        //拿到需要做的列表
        uploadDataList = dbHelper.querySops(appData.getLoginBean().getUserId(), planId);

        if (uploadDataList.size() == 0) {
            ToastUtil.showMid("暂时没有需要上传的待办数据");
            return;
        }
        for (SopListViewBean sopListViewBean : uploadDataList){
            sopListViewBean.setTradeType(tradeType);
        }
        Gson gson = new Gson();
        String url = "";
        if ("".equals(Constants.TYPE)){
            url = Retrofit2Service.SAVE_SOP;
        }else {
            url = Retrofit2Service.LT_SAVE_SOP;
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),gson.toJson(uploadDataList));
        Call<ResponseBody> call = Retrofit2Helper.getInstance().saveSop(url,requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    handleResult(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoadingDialog.dismiss();
                ToastUtil.show(getResources().getString(R.string.error_server));
            }
        });

        noProblemBtn.setVisibility(View.GONE);
        updataBtn.setVisibility(View.GONE);
    }

    public void handleResult(String result){
        //删除已上传的文字数据
        for (int i = 0; i < uploadDataList.size(); i++) {
            dbHelper.deleteSop(uploadDataList.get(i).getUserId(),uploadDataList.get(i).getPlanId(), uploadDataList.get(i).getItemCode(), uploadDataList.get(i).getContent());
        }
        JSONObject jb = null;

        ArrayList<PicBean> picList = new ArrayList<>();
        picList = dbHelper.selectPic(planId,appData.getLoginBean().getUserId());

        try {
            jb = new JSONObject(result);
        } catch (JSONException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        if (jb == null) {
            ToastUtil.show("服务器连接失败");
            return;
        } else {
            if ("".equals(Constants.TYPE)){
                try {
                    JSONArray array = new JSONArray(jb.getString("message"));
                    makeRecordData(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    planId = jb.getString("object");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        //图片上传
        if (picList.size() != 0)
            CheckTypeInFragment.UploadPics(picList);
        else {
            ToastUtil.show("提交数据成功");
        }

        LoadingDialog.dismiss();
    }

    public void createTypeDialog() {
        AlertDialog.Builder bulder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);

        String[] typeArray = {"食品生产日常检查", "保健食品日常生产检查"};
        bulder.setSingleChoiceItems(typeArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("which", which + "");
                switch (which) {
                    case 0:
                        addType = "F";
                        break;
                    case 1:
                        addType = "H";
                        break;
                }
                dialog.dismiss();
                createRootChooseDialog(addType);
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

    private void createRootChooseDialog(String type) {
        AlertDialog.Builder bulder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);
        ArrayList<String> itemsList = new ArrayList<String>();
        for (int i = 0; i < sopList.size(); i++) {
            SopBean sopBean = sopList.get(i);
            if (type.equals(sopBean.getId().substring(0, 1))) {
                itemsList.add(sopBean.getName());
                addSopList.add(sopBean);
            }
        }

        bulder.setSingleChoiceItems(itemsList.toArray(new String[itemsList.size()]), -1,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        createGroupDialog(which);
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

    private void createGroupDialog(int index) {
        final int myindex = index;
        final SopBean sop = addSopList.get(index);

        ArrayList<CharSequence> itemsList = new ArrayList<CharSequence>();
        final ArrayList<String> itemsList2 = new ArrayList<String>();
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
                if (isHave)
                    break;
            }

            if (!isHave) {
                if ("".equals(Constants.TYPE)){
                    String text = sop.getItems().get(i).getCheckCode()+sop.getItems().get(i).getCheckContent();

                    if ("0".equals(sop.getItems().get(i).getIsKey()))
                        itemsList.add(text);
                    else
                        itemsList.add(Html.fromHtml("<font color=red>"+text+"</font>"));

                    itemsList2.add(text);
                }else {
                    String text = sop.getItems().get(i).getCheckContent();
                    itemsList.add(text);
                    itemsList2.add(text);
                }
            }
        }
        itemsList.add("其他");
        itemsList2.add("其他");

        AlertDialog.Builder bulder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);
        bulder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        final boolean checkedItems[] = new boolean[itemsList.size()];

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
                            createDiyDialog(myindex);
                            return;
                        } else {
                            SopListViewBean sopBean = new SopListViewBean();
                            CheckTypeInFragment.makeSopBean(sopBean, CheckTypeInFragment.makeItemBean(i,addSopList.get(myindex).getItems(),itemsList2),addSopList);

                            childList.add(sopBean);
                        }
                    }
                }

                if (!isChoose) {
                    dialog.dismiss();
                } else {
                    CheckTypeInFragment.makeAddData(childList,groupArray,childArray,groupStatues,bundleStatues);
                    refreshListView();
                }
            }
        });
        bulder.show();
    }

    private void createDiyDialog(int index) {
        final int myindex = index;
        AlertDialog.Builder bulder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);

        bulder.setTitle("请输入检查项");
        final EditText editText = new EditText(getActivity());
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
                CheckTypeInFragment.makeDiySopBean(sopBean);
                sopBean.setParentCode(addSopList.get(myindex).getId() + addSopList.get(myindex).getName());
                sopBean.setContent(content);
                childList.add(sopBean);

                CheckTypeInFragment.makeAddData(childList,groupArray,childArray,groupStatues,bundleStatues);

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

    public boolean isInput(String content) {
        for (int i = 0; i < childArray.size(); i++) {
            ArrayList<SopListViewBean> list = childArray.get(i);
            for (int j = 0; j < list.size(); j++) {
                SopListViewBean bean = list.get(j);
                if (bean.getContent().equals(content))
                    return true;
            }
        }
        return false;
    }

    public void refreshListView() {

        elvAdapter.notifyDataSetChanged();

        for (int x = 0; x < elvAdapter.getGroupCount(); x++) {
            if (groupStatues.get(x) == true) {
                elv.expandGroup(x);
            } else {
                elv.collapseGroup(x);
            }
        }
    }

    public void makeRecordData(JSONArray array){
        MessageActivity.highItems.clear();
        MessageActivity.lowItems.clear();
        MessageActivity.highPros.clear();
        MessageActivity.lowPros.clear();
        try {
            MessageActivity.count1 = array.get(0).toString();
            MessageActivity.count2 = array.get(1).toString();
            MessageActivity.count3 = array.get(2).toString();
            JSONArray jsonArray4 = array.getJSONArray(3);
            if(jsonArray4.length()!=0){
                String s4 = "";
                for(int i=0;i<jsonArray4.length();i++){
                    MessageActivity.highItems.add(jsonArray4.get(i).toString());
                    String s = CheckTypeInFragment.getItemNum(jsonArray4.get(i).toString());
                    s4+=s.substring(1,s.length())+"、";
                }
                MessageActivity.count4 = s4.substring(0,s4.length()-1);
            }else {
                MessageActivity.count4 = "";
            }
            MessageActivity.count5 = array.get(4).toString();
            JSONArray jsonArray6 = array.getJSONArray(5);
            if(jsonArray6.length()!=0){
                String s6 = "";
                for(int i=0;i<jsonArray6.length();i++){
                    MessageActivity.lowItems.add(jsonArray6.get(i).toString());
                    String s = CheckTypeInFragment.getItemNum(jsonArray6.get(i).toString());
                    s6+=s+"、";
                }
                MessageActivity.count6 = s6.substring(0,s6.length()-1);
            }else {
                MessageActivity.count6 = "";
            }
            MessageActivity.count7 = array.get(6).toString();
            JSONArray jsonArray8 = array.getJSONArray(7);
            if(jsonArray8.length()!=0){
                String s8 = "";
                for(int i=0;i<jsonArray8.length();i++){
                    MessageActivity.highPros.add(jsonArray8.get(i).toString());
                    String s= CheckTypeInFragment.getItemNum(jsonArray8.get(i).toString());
                    s8+=s.substring(1,s.length())+"、";
                }
                MessageActivity.count8 = s8.substring(0,s8.length()-1);
            }else {
                MessageActivity.count8 = "";
            }
            MessageActivity.count9 = array.get(8).toString();
            JSONArray jsonArray10 = array.getJSONArray(9);
            if(jsonArray10.length()!=0){
                String s10 = "";
                for(int i=0;i<jsonArray10.length();i++){
                    MessageActivity.lowPros.add(jsonArray10.get(i).toString());
                    String s = CheckTypeInFragment.getItemNum(jsonArray10.get(i).toString());
                    s10+=s+"、";
                }
                MessageActivity.count10 = s10.substring(0,s10.length()-1);
            }else {
                MessageActivity.count10 = "";
            }
            MessageActivity.checkResult = array.get(10).toString();
            planId = array.get(12).toString();

            MessageActivity.startDate = DateUtil.formate2(System.currentTimeMillis());


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public JSONArray getNotes(){
        JSONArray jsonArray = new JSONArray();
        makeNoteData(jsonArray, MessageActivity.highPros);
        makeNoteData(jsonArray, MessageActivity.lowPros);

        return jsonArray;
    }

    public void makeNoteData(JSONArray jsonArray,ArrayList<String> arrayList){
        for(String itemCode :arrayList){
            NoteBean bean  = new NoteBean();
            String checkContent = CheckTypeInFragment.getItemNum(itemCode);
            String note = "";
            bean.setCheckContent(checkContent);
            bean.setNote(note);
            getRemark(childArray,itemCode,bean);

            JSONObject jsonObject= new JSONObject();
            try {
                jsonObject.put("checkContent",bean.getCheckContent());
                jsonObject.put("note",bean.getNote());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
    }

    public void getRemark(ArrayList<ArrayList<SopListViewBean>> arrayLists, String itemCode, NoteBean noteBean){
        for (ArrayList<SopListViewBean> arrayList:arrayLists){
            for(SopListViewBean bean: arrayList){
                if (bean.getItemCode().equals(itemCode)){
                    noteBean.setNote(bean.getRemark()==null?"":bean.getRemark());
                    String s = noteBean.getCheckContent();
                    noteBean.setCheckContent(s);
                    break;
                }
            }
        }
    }
}
