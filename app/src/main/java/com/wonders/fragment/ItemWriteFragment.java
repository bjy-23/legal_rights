package com.wonders.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.legal_rights.R;
import com.wonders.adapter.ImageGridViewAdapter;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.constant.DbConstants;
import com.wonders.util.DbHelper;
import com.wonders.bean.PicBean;
import com.wonders.bean.SopListViewBean;
import com.wonders.util.DateUtil;
import com.wonders.util.PicUtil;
import com.wonders.widget.HeightExpandableGridView;
import com.wonders.widget.LoadingDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by bjy on 2017/2/23.
 *
 * 检查项录入
 */

public class ItemWriteFragment extends Fragment implements AMapLocationListener,PicUtil.PicListener {
    private TextView tvContent,tvAddress;
    private ImageView imgOk,imgNo;
    private LinearLayout layoutOk,layoutNo;
    private EditText etRemark;
    private Button btnQuestion,btnSave;
    private HeightExpandableGridView igv;
    private View view;
    private ImageGridViewAdapter imgAdapter;
    private ArrayList<PicBean> imgArray;
    private PicBean[] addImages;
    private SopListViewBean sopBean;

    // 用于判断定位超时
    private AMapLocation aMapLocation;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption;
    private AMapLocationClient mlocationClient ;

    private int planType;
    private int passFlag = 2;
    private int groupPosition,childPosition;
    private boolean isDiy = false;
    private String timeMark = "";
    private String picPath ="";
    private String planId,etpsId;
    //是否刷新列表
    public final static int REFRESH_REQUEST_CODE1 = 10;
    public final static int REFRESH_REQUEST_CODE2 = 11;
    public final static int REFERESH_RESULT_CODE_YES = 12;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_write,container,false);

        igv = (HeightExpandableGridView)view.findViewById(R.id.igv);
        layoutOk = (LinearLayout) view.findViewById(R.id.layout_ok);
        layoutNo = (LinearLayout) view.findViewById(R.id.layout_no);
        imgOk = (ImageView) view.findViewById(R.id.img_ok);
        imgNo = (ImageView) view.findViewById(R.id.img_no);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnQuestion = (Button) view.findViewById(R.id.btn_question);

        etRemark = (EditText) view.findViewById(R.id.et_remark);
        etRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etRemark.setFocusable(true);
                etRemark.setFocusableInTouchMode(true);
                etRemark.requestFocus();
                etRemark.requestFocusFromTouch();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etRemark,0);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sopBean = getArguments().getParcelable("sopBean");
        planId = getArguments().getString("planId");
        etpsId = getArguments().getString("etpsId");
        groupPosition = getArguments().getInt("groupPosition");
        childPosition = getArguments().getInt("childPosition");

        if("".equals(Constants.TYPE))
            addImages = new PicBean[PicUtil.PIC_MAX];
        else
            addImages = new PicBean[PicUtil.PIC_MAX_LT];

        //设置图片
        imgArray = new ArrayList<>();
        if (!"".equals(Constants.TYPE)){
            for (int i=0;i<PicUtil.PIC_MAX_LT;i++){
                PicBean picBean = new PicBean();
                picBean.setType(1);
                picBean.setPicPath("");
                imgArray.add(picBean);
            }
        }
        setImage(sopBean,imgArray);
        if("".equals(Constants.TYPE)&&imgArray.size()<PicUtil.PIC_MAX){
            PicBean picBean = new PicBean();
            picBean.setType(1);
            picBean.setPicPath("");
            imgArray.add(picBean);
        }

        imgAdapter = new ImageGridViewAdapter(getActivity(),imgArray);
        ImageGridViewAdapter.type = PicUtil.PIC_SHOW;

        igv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        igv.setAdapter(imgAdapter);
        igv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PicUtil picUtil = new PicUtil(getActivity());
                picUtil.setPicListener(ItemWriteFragment.this);
                PicBean picBean = imgArray.get(position);
                picBean.setPlanId(planId);
                picBean.setItemCode(sopBean.getItemCode());
                picBean.setModel(1);
                picUtil.imgDialogShow(position,imgArray.size(),picBean);
            }
        });

        layoutOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseOk();
            }
        });

        layoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseNo();
            }
        });


        if ("1".equals(sopBean.getIsEdit())) {
            etRemark.setText(sopBean.getRemark());

            if ("1".equals(sopBean.getIsPass())) {
               chooseOk();
            } else {
               chooseNo();
            }
        }

        tvContent.setText(sopBean.getContent());

        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passFlag == 0) {
                    createRootChooseDialog();
                } else {
                    Toast.makeText(getActivity(), "请先选择发现问题",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passFlag == 2) {
                    Toast.makeText(getActivity(), "检查结果必须选择",
                            Toast.LENGTH_SHORT).show();

                    return;
                }
                LoadingDialog.show(getActivity());
                new Thread() {
                    @Override
                    public void run() {
                        saveData(sopBean,imgArray,addImages,planType);
                    }
                }.start();
            }
        });


        setLocation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            return;
        }
        if(requestCode>99&&resultCode==-1){
            picPath = getPicPath(data);
        }

        int position = requestCode;
        if(requestCode>10)
            position = requestCode-100;
        else{
            //给图片添加水印
            waterMark(picPath,timeMark);
        }
        PicBean picBean = new PicBean();
        picBean.setPicName(System.currentTimeMillis()+"");
        picBean.setPicNum(position);
        picBean.setPicPath(picPath);
        picBean.setPlanId(planId);
        picBean.setUserId(AppData.getInstance().getLoginBean().getUserId());
        picBean.setItemCode(sopBean.getItemCode());
        picBean.setCheckContent(sopBean.getContent());
        picBean.setType(1);

        addImages[position] = picBean;
        if ("".equals(Constants.TYPE)){
            if(!"".equals(imgArray.get(position).getPicPath())&&(imgArray.size()-position)!=1){
                //替换图片
                imgArray.set(position,picBean);
            }else{
                //添加图片
                imgArray.add(imgArray.size()-1,picBean);
                if (imgArray.size()>PicUtil.PIC_MAX){
                    imgArray.remove(imgArray.size()-1);
                }
            }
        }else {
            imgArray.set(position,picBean);
        }

        imgAdapter.notifyDataSetChanged();
    }

    private void setImage(SopListViewBean sopBean, ArrayList<PicBean> imgArray) {
        ArrayList<PicBean> pic = sopBean.getPic();
        for (PicBean picBean : pic){
            if (picBean.getType()==0){
                saveNetImages(picBean,imgArray);
            }else {
                if ("".equals(Constants.TYPE)){
                    imgArray.add(picBean);
                }else {
                    imgArray.set(picBean.getPicNum(),picBean);
                }
            }
        }
    }

    public  void saveNetImages(PicBean picBean, ArrayList<PicBean> imgArray) {
        if ("".equals(Constants.TYPE)){
            imgArray.add(picBean);
        }else {
            imgArray.set(picBean.getPicNum(),picBean);
        }
    }

    public void setLocation(){
        mlocationClient = new AMapLocationClient(getActivity());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);

        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            this.aMapLocation = location;// 判断超时机制
            Double geoLat = location.getLatitude();
            Double geoLng = location.getLongitude();
            String desc = "";
            Bundle locBundle = location.getExtras();
            if (locBundle != null) {
                desc = locBundle.getString("desc");
            }

            tvAddress.setText(desc);

        }
    }

    @Override
    public void deletePic(int position) {
        //图片还未保存，删除暂存的数据
        addImages[position] = null;
        //图片已经保存，删除本地的存储
        DbHelper dbHelper = new DbHelper(getActivity(),DbConstants.TABLENAME,null,1);
        dbHelper.deletePicOnPosition(imgArray.get(position));

        if ("".equals(Constants.TYPE)){
            imgArray.remove(position);
            if(!"".equals(imgArray.get(imgArray.size()-1).getPicPath())){
                PicBean picBean = new PicBean();
                picBean.setPicPath("");
                imgArray.add(picBean);
            }
        }else {
            PicBean picBean = new PicBean();
            picBean.setPicPath("");
            imgArray.set(position,picBean);
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
        startActivityForResult(intent,position);
    }

    @Override
    public void xuanQu(int position) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,position);
    }

    public void chooseOk(){
        passFlag = 1;

        imgOk.setBackgroundResource(R.drawable.select);
        imgNo.setBackgroundResource(R.drawable.unselect);
    }

    public void chooseNo(){
        passFlag = 0;

        imgOk.setBackgroundResource(R.drawable.unselect);
        imgNo.setBackgroundResource(R.drawable.select);
    }

    private void createRootChooseDialog() {
        AlertDialog.Builder bulder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);

        if (sopBean.getFaq() == null || sopBean.getFaq().trim().equals("")) {
            Toast.makeText(getActivity(), "没有常见问题", Toast.LENGTH_SHORT).show();

            return;
        }

        String faq = sopBean.getFaq();
        String faq2 =faq.replace(";","；");
        String[] faqList = faq2.split("；");

        final ArrayList<String> itemsList = new ArrayList<String>();
        if (faqList.length <= 1) {
            itemsList.add(faq);
        } else {
            for (int i = 0; i < faqList.length; i++) {
                itemsList.add(faqList[i]);
            }

        }

        final boolean[] checkedItems = {false, false, false, false, false,
                false, false, false, false, false};

        bulder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String remarkString = "";

                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        String item = getChinese(itemsList.get(i));
                        remarkString += "."+item+"\n";
                    }
                }
                if(!"".equals(remarkString)){
                    int length = remarkString.length();
                    String content = remarkString.substring(0,length-2);
                    etRemark.setText(content);
                }

            }
        });

        bulder.setMultiChoiceItems(itemsList.toArray(new String[0]),
                checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        checkedItems[which] = isChecked;

                    }
                });

        bulder.create().show();

    }

    public static String getChinese(String input){
        int length = input.length();
        int i = 0;
        for(;i<length;i++){
            String s = input.substring(i,i+1);
            if(s.matches("[\u4e00-\u9fa5]")){
                break;
            }
        }
        String output = input.substring(i,length);

        return output;
    }

    private void saveData(SopListViewBean sopBean,ArrayList<PicBean> imgArray,PicBean[] addImages,int planType) {
        DbHelper dbHelper = new DbHelper(AppData.getInstance(), DbConstants.TABLENAME,null,1);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        //存进数据库

        sopBean.setIsEdit("1");
        if (passFlag == 1) {
            sopBean.setIsPass("1");
        } else {
            sopBean.setIsPass("0");
        }
        sopBean.setRemark(etRemark.getText().toString());
        sopBean.setPlanType(planType);
        sopBean.setUserId(AppData.getInstance().getLoginBean().getUserId());
        sopBean.setPlanId(planId);
        sopBean.setEtpsId(etpsId);
        sopBean.setYear(year+"");
        sopBean.setMonth(month+1+"");
        if(sopBean.getFirstDate()==null){
            sopBean.setFirstDate(DateUtil.format(System.currentTimeMillis()));
            sopBean.setSecondDate("");
        }else {
            sopBean.setFirstDate(sopBean.getFirstDate());
            sopBean.setSecondDate(DateUtil.format(System.currentTimeMillis()));
        }
        sopBean.setAddress("");
        sopBean.setLongitude("");
        sopBean.setLatitude("");
        //保存新增或修改的图片
        for(int i=0;i<addImages.length;i++){
            if(addImages[i]!=null){
                dbHelper.savePic(addImages[i]);
            }
        }

        //保存图片信息
        sopBean.setIsHavePic("0");
        ArrayList<PicBean> pic = new ArrayList<>();
        for (int i = 0; i < imgArray.size(); i++) {
            PicBean picBean = imgArray.get(i);
            if (!"".equals(picBean.getPicPath())) {
                sopBean.setIsHavePic("1");
                pic.add(picBean);
            }
        }
        sopBean.setPic(pic);

        // 通过content和itemCode 找到记录 如果没有记录创建一条新的纪录 如果有记录 对这条记录进行修改
        SopListViewBean sopListViewBean = dbHelper.querySop(sopBean.getItemCode(), sopBean.getContent(),planId, AppData.getInstance().getLoginBean().getUserId());

        if (sopListViewBean.getPlanId() == null) {
            dbHelper.insertSopListViewBean(sopBean);
        } else {
            dbHelper.updateSop(sopBean.getItemCode(),sopBean.getContent(), planId, AppData.getInstance().getLoginBean().getUserId(), sopBean);
        }

        Intent intent = new Intent();
        intent.putExtra("sopBean",sopBean);
        intent.putExtra("groupPosition",groupPosition);
        intent.putExtra("childPosition",childPosition);
        getActivity().setResult(REFERESH_RESULT_CODE_YES,intent);

        LoadingDialog.dismiss();
        getActivity().finish();
    }

    public String getPicPath(Intent data){
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

    public static void waterMark(String picPath,String timeMark){
        Bitmap bitmap = BitmapFactory.decodeFile(picPath).copy(Bitmap.Config.ARGB_8888,true);

        Canvas cv = new Canvas(bitmap);
        Paint p = new Paint();
        Typeface font = Typeface
                .create("宋体", Typeface.BOLD);
        p.setColor(Color.BLUE);
        p.setTypeface(font);
        p.setTextSize(72);
        cv.drawBitmap(bitmap, 0, 0, p);
        cv.drawText(timeMark, 40, cv.getHeight() - 100, p);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();

        saveBitmapToJPG(bitmap,picPath);
    }

    public static void saveBitmapToJPG(Bitmap bitmap,String picPath){
        File file = new File(picPath);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
