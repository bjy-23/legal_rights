package com.wonders.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.legal_rights.R;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wonders.constant.Constants;
import com.wonders.http.Retrofit2Helper;
import com.wonders.bean.CheckContentDto;
import com.wonders.bean.CheckContentDtosc;
import com.wonders.bean.FpsiCertInfo;
import com.wonders.bean.FpsiEtpsInfo;
import com.wonders.bean.FpsiInspItem;
import com.wonders.bean.FpsiInspPlan;
import com.wonders.bean.FpsiLtInfoEnty;
import com.wonders.bean.FpsiLtInspItem;
import com.wonders.bean.FpsiLtInspPlan;
import com.wonders.bean.FpsiLtPlanResult;
import com.wonders.bean.FpsiPlanResult;
import com.wonders.util.ToastUtil;
import com.wonders.widget.LoadingDialog;

import okhttp3.Cookie;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YlActivity extends AppCompatActivity {
    private WebView wb;
    private String planId;
    private String sub;
    private String acc;
    private Button closeBtn;
    private Button printBtn;//打印

    private String scUrl;
    private String ltUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_yl);

        scUrl = Retrofit2Helper.BSAE_URL + "SopCheckForm.do";
        ltUrl = Retrofit2Helper.BSAE_URL + "documentPrint.do";

        File destDir = new File(Environment.getExternalStorageDirectory(),
                "fpsi");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        printBtn = (Button) findViewById(R.id.print_btn);
        printBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SplashActivity.isAvailable(YlActivity.this, Constants.PRINT_SOFTWARE_NAME)) {
                    SplashActivity.showPrintDialog(YlActivity.this);
                }else {
                    LoadingDialog.show(YlActivity.this);
                    HashMap<String,String> params = new HashMap<String, String>();
                    params.put("planId", planId);
                    params.put("isPreview", sub);
                    params.put("accompany", acc);

                    Call<ResponseBody> call = Retrofit2Helper.getInstance().ltGetPDFPrintData(params);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String result = "";
                            try {
                                result = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if ("null".equals(jsonObject.getString("object"))){
                                    ToastUtil.show(getResources().getString(R.string.error_json));
                                    LoadingDialog.dismiss();
                                    return;
                                }
                                JSONObject jsonObject1 = (JSONObject) jsonObject.get("object");
                                Iterator it = jsonObject1.keys();
                                HashMap map = new HashMap();
                                while (it.hasNext()) {
                                    String key = String.valueOf(it.next());
                                    Object value = (Object) jsonObject1.get(key);
                                    map.put(key, value);
                                }
                                createPdf(Environment.getExternalStorageDirectory() + "/Download/1228.pdf", map);
                                MessageActivity.doPrintPDF(YlActivity.this);
                                LoadingDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            LoadingDialog.dismiss();
                            ToastUtil.show(getResources().getString(R.string.error_server));
                        }
                    });
                }
            }
        });

        closeBtn = (Button) findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        planId = getIntent().getStringExtra("planId");
        sub = "1".equals(getIntent().getStringExtra("sub")) ? "1" : "0";
        acc = (getIntent().getStringExtra("acc") == null) ? "无" : getIntent().getStringExtra("acc");

        wb = (WebView) findViewById(R.id.webview);
        //
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wb.setWebChromeClient(new WebChromeClient());
        // 设置可以支持缩放
        wb.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        wb.getSettings().setBuiltInZoomControls(true);
        // 扩大比例的缩放
        wb.getSettings().setUseWideViewPort(true);
        wb.setInitialScale(10);

        wb.setWebViewClient(new WebViewClient());

        String url4load = "";
        if (Constants.TYPE.equals("")) {
            url4load = scUrl + "?planId=" + planId + "&imgFlg=0&isPreview=" + sub + "&accompany=" + acc;
        } else {
            url4load = ltUrl + "?planId=" + planId + "&imgFlg=0&isPreview=" + sub + "&accompany=" + acc;
        }
        Log.e("url4load", url4load);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        Cookie sessionCookie = com.wonders.http.CookieManager.getCookie();

        if (sessionCookie != null) {
            String cookieString = sessionCookie.name() + "="
                    + sessionCookie.value() + "; domain="
                    + sessionCookie.domain();
            cookieManager.setCookie(url4load, cookieString);
            CookieSyncManager.getInstance().sync();

            Log.i("cookieString", cookieString);
        }
        wb.loadUrl(url4load);

        super.onCreate(savedInstanceState);
    }

    public boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    private boolean isAvilible( Context context, String packageName )
    {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++ )
        {
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }


    public void createPdf(String dest, Map map){
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            if(Constants.TYPE.equals("")){
                setPDFInfo(document,map);
            }else {
                setltPDFInfo(document,map);
            }
            document.close();
            //
        } catch (FileNotFoundException e) {
            document.close();
            e.printStackTrace();
        } catch (DocumentException e) {
            document.close();
            e.printStackTrace();
        } catch (IOException e) {
            document.close();
            e.printStackTrace();
        }

    }

    protected void setPDFInfo(com.itextpdf.text.Document document, Map map) throws DocumentException,
            IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList<CheckContentDtosc>  itemList2 = new ArrayList<CheckContentDtosc>();
        JSONArray jsonArray1 = (JSONArray) map.get("itemList2");
        for(int i=0;i<jsonArray1.length();i++){
            try {
                CheckContentDtosc checkContentDtosc =new CheckContentDtosc();
                JSONObject jsonObject1 = (JSONObject) jsonArray1.get(i);
                checkContentDtosc.setItemCode((String) jsonObject1.get("itemCode"));
                checkContentDtosc.setParentCode((String) jsonObject1.get("parentCode"));
                checkContentDtosc.setCheckContent((String) jsonObject1.get("checkContent"));
                checkContentDtosc.setPlanId((String) jsonObject1.get("planId"));
                checkContentDtosc.setIfAdded((String) jsonObject1.get("ifAdded"));
                checkContentDtosc.setIfCustom((String) jsonObject1.get("ifCustom"));
                checkContentDtosc.setAttFlag((String) jsonObject1.get("attFlag"));
                checkContentDtosc.setRemarkFlag((String) jsonObject1.get("remarkFlag"));
                checkContentDtosc.setResult((String) jsonObject1.get("result"));
                checkContentDtosc.setId((String) jsonObject1.get("id"));
                checkContentDtosc.setRemark((String) jsonObject1.get("remark"));
                itemList2.add(checkContentDtosc);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayList<FpsiInspItem> itemList =  new ArrayList<FpsiInspItem>();
        JSONArray jsonArray2 = (JSONArray) map.get("itemList");
        for(int i=0;i<jsonArray2.length();i++){
            try {
                FpsiInspItem fpsiInspItem = new FpsiInspItem();
                JSONObject jsonObject2 = (JSONObject)jsonArray2.get(i);
                fpsiInspItem.setUuid(jsonObject2.getString("uuid"));
                fpsiInspItem.setItemCode(jsonObject2.getString("itemCode"));
                fpsiInspItem.setParentCode(jsonObject2.getString("parentCode"));
                fpsiInspItem.setCheckContent(jsonObject2.getString("checkContent"));
                fpsiInspItem.setPlanId(jsonObject2.getString("planId"));
                fpsiInspItem.setIfAdded(jsonObject2.getString("ifAdded"));
                fpsiInspItem.setIfCustom(jsonObject2.getString("ifCustom"));
                fpsiInspItem.setRemarkFlag(jsonObject2.getString("remarkFlag"));
                if(!jsonObject2.get("fpsiPlanResult").equals(null)){
                    FpsiPlanResult fpsiPlanResult = new FpsiPlanResult();
                    JSONObject jsonObject = (JSONObject)jsonObject2.get("fpsiPlanResult");
                    fpsiPlanResult.setResult(jsonObject.getString("result"));
                    fpsiPlanResult.setRemark(jsonObject.getString("remark"));
                    fpsiInspItem.setFpsiPlanResult(fpsiPlanResult);//bjy0
                }
                itemList.add(fpsiInspItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String isPreview=(String)map.get("isPreview");
        String accompany=(String)map.get("accompany");
        HashMap certTypeMap = new HashMap();
        JSONObject jsonObject2 = (JSONObject)map.get("certTypeMap");
        Iterator it = jsonObject2.keys();
        while (it.hasNext())
        {
            String key = String.valueOf(it.next());
            Object value = null;
            try {
                value = (Object) jsonObject2.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            certTypeMap.put(key, value);

        }
//        FpsiInspPlan fpsiInspPlan=(FpsiInspPlan)map.get("fpsiInspPlan");
        FpsiInspPlan fpsiInspPlan = new FpsiInspPlan();
        JSONObject jsonObject = (JSONObject)map.get("fpsiInspPlan");
        try {
            fpsiInspPlan.setPlanId(jsonObject.getString("planId"));
            fpsiInspPlan.setTypeId(jsonObject.getString("typeId"));
            fpsiInspPlan.setEtpsId(jsonObject.getString("etpsId"));
            fpsiInspPlan.setPlanYear((Integer) jsonObject.get("planYear"));
            fpsiInspPlan.setPlanMonth((Integer) jsonObject.get("planMonth"));
            Date startdate = new Date();
            Date enddate = new Date();
            Date createDate = new Date();
            try {
                if(!jsonObject.getString("startDate").equals("null")){
                    startdate = sdf.parse((String) jsonObject.get("startDate"));
                }else {
                    startdate = null;
                }
                if(!jsonObject.getString("endDate").equals("null")){
                    enddate = sdf.parse((String) jsonObject.get("endDate"));
                }else {
                    enddate = null;
                }
                if(!jsonObject.getString("createDate").equals("null")){
                    createDate =sdf.parse((String) jsonObject.get("createDate"));
                }else{
                    createDate = null;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            fpsiInspPlan.setStartDate(startdate);
            fpsiInspPlan.setEndDate(enddate);
            fpsiInspPlan.setExeOrgan(jsonObject.getString("exeOrgan"));
            fpsiInspPlan.setExeDept(jsonObject.getString("exeDept"));
            fpsiInspPlan.setInspDept(jsonObject.getString("inspDept"));
            fpsiInspPlan.setResult(jsonObject.getString("result"));
            fpsiInspPlan.setRemark(jsonObject.getString("remark"));
            fpsiInspPlan.setStatus(jsonObject.getString("status"));
            fpsiInspPlan.setNeedRevisit(jsonObject.getString("needRevisit"));
            fpsiInspPlan.setNeedSample(jsonObject.getString("needSample"));
            fpsiInspPlan.setParentPlanId(jsonObject.getString("parentPlanId"));
            fpsiInspPlan.setIfSuo(jsonObject.getString("ifSuo"));
            fpsiInspPlan.setAccompaniedPeople(jsonObject.getString("accompaniedPeople"));
            fpsiInspPlan.setCreateUser(jsonObject.getString("createUser"));
            fpsiInspPlan.setCreateDate(createDate);
            fpsiInspPlan.setDiyId(jsonObject.getString("diyId"));
            fpsiInspPlan.setExcutorUser(jsonObject.getString("excutorUser"));
            fpsiInspPlan.setNotes(jsonObject.getString("notes"));
//            fpsiInspPlan.setFpsiPlanExcutor();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        FpsiEtpsInfoBean fpsiEtpsInfo=(FpsiEtpsInfoBean)map.get("fpsiEtpsInfo");
        FpsiEtpsInfo fpsiEtpsInfo = new FpsiEtpsInfo();
        JSONObject jsonObject1 = (JSONObject)map.get("fpsiEtpsInfo");
        try {
            fpsiEtpsInfo.setEtpsId(jsonObject1.getString("etpsId"));
            fpsiEtpsInfo.setTypeId(jsonObject1.getString("typeId"));
            fpsiEtpsInfo.setEtpsName(jsonObject1.getString("etpsName"));
            fpsiEtpsInfo.setAddress(jsonObject1.getString("address"));
            fpsiEtpsInfo.setProduct(jsonObject1.getString("product"));
            fpsiEtpsInfo.setPersonName(jsonObject1.getString("personName"));
            fpsiEtpsInfo.setTelephone(jsonObject1.getString("telephone"));
            fpsiEtpsInfo.setOrganId(jsonObject1.getString("organId"));
            fpsiEtpsInfo.setZjCode(jsonObject1.getString("zjCode"));
            fpsiEtpsInfo.setPostCode(jsonObject1.getString("postCode"));
            fpsiEtpsInfo.setFax(jsonObject1.getString("fax"));
            fpsiEtpsInfo.setMail(jsonObject1.getString("mail"));
            fpsiEtpsInfo.setGrade(jsonObject1.getString("grade"));
            fpsiEtpsInfo.setFrequency(jsonObject1.getString("frequency"));
//            fpsiEtpsInfo.setFpsiCertInfoList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        List<FpsiCertInfo> fpsiCertInfoList =(List<FpsiCertInfo>)map.get("fpsiCertInfoList");
        ArrayList<FpsiCertInfo> fpsiCertInfoList = new ArrayList<FpsiCertInfo>();
        JSONArray jsonArray3 = (JSONArray) map.get("fpsiCertInfoList");
        for(int i=0;i<jsonArray3.length();i++){
            try {
                FpsiCertInfo fpsiCertInfo = new FpsiCertInfo();
                JSONObject jsonObject3 = (JSONObject)jsonArray2.get(i);
                fpsiCertInfo.setUuid(jsonObject3.getString("uuid"));
                fpsiCertInfo.setCertType(jsonObject3.getString("certType"));
                fpsiCertInfo.setCertNo(jsonObject3.getString("certNo"));
                fpsiCertInfo.setEtpsId(jsonObject3.getString("etpsId"));
                fpsiCertInfo.setEtpsName(jsonObject3.getString("etpsName"));
                fpsiCertInfo.setAddr(jsonObject3.getString("addr"));
                fpsiCertInfo.setFactoryAddr(jsonObject3.getString("factoryAddr"));
                fpsiCertInfo.setCheckType(jsonObject3.getString("checkType"));
                fpsiCertInfo.setProductName(jsonObject3.getString("productName"));
                fpsiCertInfo.setDetail(jsonObject3.getString("detail"));
                Date issueDate = new Date();
                Date expireDate = new Date();
                try {
                    if(!jsonObject3.getString("issueDate").equals("null")){
                        issueDate = sdf.parse((String) jsonObject3.get("issueDate"));
                    }else{
                        issueDate = null;
                    }
                    if(!jsonObject3.getString("expireDate").equals("null")){
                        expireDate = sdf.parse((String) jsonObject3.get("expireDate"));
                    }else {
                        expireDate = null;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fpsiCertInfo.setIssueDate(issueDate);
                fpsiCertInfo.setExpireDate(expireDate);
                fpsiCertInfo.setMemo(jsonObject3.getString("memo"));
                fpsiCertInfo.setCertPrintNo(jsonObject3.getString("certPrintNo"));
                fpsiCertInfo.setCertStatus(jsonObject3.getString("certStatus"));
                fpsiCertInfo.setProvideOrgan(jsonObject3.getString("provideOrgan"));
//                fpsiCertInfo.setFpsiPlanResult();
//                fpsiInspItem.setFpsiPlanProcess();
//                fpsiInspItem.setFpsiItemAtt();
                fpsiCertInfoList.add(fpsiCertInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Font font = null;
        final Font baseFont = new Font(BaseFont.createFont("assets/MSYYY.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
        Font fontB = new Font(baseFont);
        Font fontN = new Font(baseFont);
        font = new Font(baseFont);
        font.setSize(18);
        font.setStyle(Font.BOLD);
        fontB.setSize(12);
        fontB.setStyle(Font.BOLD);
        fontN.setSize(12);
        fontN.setStyle(Font.NORMAL);
        Paragraph pg = new Paragraph(
                String.format("食品生产监督通用检查标准规程（SOP）现场核查表格"), font);
        pg.setAlignment(1);
        document.add(pg);
        font.setSize(8);
        pg = new Paragraph(String.format(" "), font);
        document.add(pg);
        font.setSize(18);
        font.setStyle(Font.NORMAL);
        // ///////////////////////////////////////////////////////////////////////////////////////////////
        pg = new Paragraph(String.format(fpsiInspPlan.getExeOrgan()), font);


        //pg = new Paragraph(String.format("上海市食品药品监督管理局"), font);
        pg.setAlignment(1);
        document.add(pg);

        font.setSize(5);
        pg = new Paragraph(String.format(" "), font);
        pg.setAlignment(1);
        document.add(pg);
        font.setSize(6);
        font.setStyle(Font.NORMAL);
        pg = new Paragraph(
                String.format("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"),
                font);
        pg.setAlignment(1);
        document.add(pg);
        font.setSize(18);
        pg = new Paragraph(String.format("监督检查笔录"), font);
        pg.setAlignment(1);
        document.add(pg);
        font.setSize(8);
        pg = new Paragraph(String.format(" "), font);
        pg.setAlignment(1);
        document.add(pg);

        // 加入表格布局
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(95);
        Phrase phrase = new Phrase();
        Chunk boldtext = new Chunk("被检查单位名称（姓名）：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        Chunk underline = new Chunk(fpsiEtpsInfo.getEtpsName());
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase();
        boldtext = new Chunk("地址：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        underline = new Chunk(fpsiEtpsInfo.getAddress());
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);

        // ----------------------------------循环-----------------------------------
        for(int i=0;i<fpsiCertInfoList.size();i++){
            phrase = new Phrase();
            boldtext = new Chunk("许可证号：");
            boldtext.setFont(fontB);
            phrase.add(boldtext);
            // ////////////////////////////////////////////////////////////////
            font.setStyle(Font.NORMAL);
            underline = new Chunk(fpsiCertInfoList.get(i).getCertNo());
            underline.setFont(fontN);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            cell = new PdfPCell(phrase);
            cell.setBorder(0);
            table.addCell(cell);

            phrase = new Phrase();
            boldtext = new Chunk("许可类别：");
            boldtext.setFont(fontB);
            phrase.add(boldtext);
            // ////////////////////////////////////////////////////////////////
            underline = new Chunk((String)certTypeMap.get((String)(fpsiCertInfoList.get(i).getCheckType())));
            underline.setFont(fontN);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            cell = new PdfPCell(phrase);
            cell.setBorder(0);
            table.addCell(cell);
        }

        // -----------------------循环结束------------------------------------
        phrase = new Phrase();
        boldtext = new Chunk("法定代表人：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(null==fpsiEtpsInfo.getPersonName()||fpsiEtpsInfo.getPersonName().length()<1){
            underline = new Chunk("　　　　　　　");
        }else{
            underline = new Chunk(fpsiEtpsInfo.getPersonName());
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase();
        boldtext = new Chunk("有效证件号：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        underline = new Chunk("　　　　　　　");
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase();
        boldtext = new Chunk("陪同检查人：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(null==accompany||accompany.length()<1){
            underline = new Chunk("　　　　　　　");
        }else{
            underline = new Chunk(accompany);
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("职务：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        underline = new Chunk("　　　　　　　");
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("有效证件号：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        underline = new Chunk("　　　　　　　");
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        cell.setColspan(2);
        table.addCell(cell);
        // 日期
        phrase = new Phrase();
        boldtext = new Chunk("检查时间：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(fpsiInspPlan.getStartDate()==null){
            underline = new Chunk("　"+"　");
        }else{
            underline = new Chunk("　"+(fpsiInspPlan.getStartDate().getYear()+1900)+"　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("年");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(fpsiInspPlan.getStartDate()==null){
            underline = new Chunk("　"+"　");
        }else{
            underline = new Chunk("　"+(fpsiInspPlan.getStartDate().getMonth()+1)+"　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("月");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(fpsiInspPlan.getStartDate()==null){
            underline = new Chunk("　"+"　");
        }else {
            underline = new Chunk("　"+(fpsiInspPlan.getStartDate().getDate())+"　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("日");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(fpsiInspPlan.getStartDate()==null){
            underline = new Chunk("　"+"　");
        }else {
            underline = new Chunk("　"+(fpsiInspPlan.getStartDate().getHours())+"　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("时");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(fpsiInspPlan.getStartDate()==null){
            underline = new Chunk("　"+"　");
        }else {
            underline = new Chunk("　"+(fpsiInspPlan.getStartDate().getMinutes())+"　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("分至");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(fpsiInspPlan.getEndDate()==null){
            underline = new Chunk("　");
        }else{
            underline = new Chunk("　"+(fpsiInspPlan.getEndDate().getHours())+"　");
        }

        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("时");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(fpsiInspPlan.getEndDate()==null){
            underline = new Chunk("　");
        }else{
            underline = new Chunk("　"+(fpsiInspPlan.getEndDate().getMinutes())+"　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("分");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        cell.setColspan(2);
        table.addCell(cell);
        phrase = new Phrase();
        boldtext = new Chunk("本次检查内容：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        cell = new PdfPCell(phrase);
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);
        // table.setLockedWidth(true);

        // 第二段文字
        String str1 = "          我们是";
        String orgName = fpsiInspPlan.getExeOrgan();
        String str2 = "行政执法人员，现出示执法证件，依法对你单位进行现场检查，请予配合。依照规定，执法人员少于两人或者所出示的执法证件与其身份不符的，你单位有权拒绝检查；对于检查人员，有下列情形之一的，你单位有权申请检查人员回避：（1）系当事人或当事人的近亲属；（2）与本人或本人近亲属有利害关系；（3）与当事人有其他关系，可能影响公正执法的。";
        phrase = new Phrase(str1 + orgName + str2, fontN);
        cell = new PdfPCell(phrase);
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);
        document.add(table);
        // SOP检查项表
        pg = new Paragraph(String.format(" "), font);
        document.add(pg);
        table = new PdfPTable(new float[] { 3, 9, 33, 2, 2 });
        table.setWidthPercentage(95);
        phrase = new Phrase("序号", fontB);
        cell = new PdfPCell(phrase);
        cell.setMinimumHeight(22f);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);
        phrase = new Phrase("核查项目", fontB);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);
        phrase = new Phrase("核查内容", fontB);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);
        phrase = new Phrase("是否发现问题", fontB);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setColspan(2);
        table.addCell(cell);

        // PdfPTable table1=new PdfPTable(2);
        phrase = new Phrase("是", fontB);
        cell = new PdfPCell(phrase);
        cell.setMinimumHeight(22f);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setColspan(1);
        table.addCell(cell);
        phrase = new Phrase("否", fontB);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setColspan(1);
        table.addCell(cell);
        boolean hasProblem=false;
        if(isPreview.equals("1")){
            for (int i = 0; i < itemList.size(); i++) {
                phrase = new Phrase((i + 1) + "", fontN);
                cell = new PdfPCell(phrase);
                cell.setMinimumHeight(22f);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 编号
                phrase = new Phrase(itemList.get(i).getItemCode(), fontN);
                cell = new PdfPCell(phrase);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 文字
                phrase = new Phrase(itemList.get(i).getCheckContent(), fontN);
                cell = new PdfPCell(phrase);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 是否发现问题
                //bjy0
                if(itemList.get(i).getFpsiPlanResult()!=null){
                    if (itemList.get(i).getFpsiPlanResult().getResult().equals("0")) {
                        hasProblem=true;
                        phrase = new Phrase("√", fontB);
                        cell = new PdfPCell(phrase);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                        table.addCell(cell);
                        phrase = new Phrase(" ", fontB);
                        cell = new PdfPCell(phrase);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                        table.addCell(cell);
                    } else {
                        phrase = new Phrase(" ", fontB);
                        cell = new PdfPCell(phrase);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                        table.addCell(cell);
                        phrase = new Phrase("√", fontB);
                        cell = new PdfPCell(phrase);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                        table.addCell(cell);
                    }
                }else {
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                }

                }

        }else{
            for (int i = 0; i < itemList2.size(); i++) {
                phrase = new Phrase((i + 1) + "", fontN);
                cell = new PdfPCell(phrase);
                cell.setMinimumHeight(22f);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 编号
                phrase = new Phrase(itemList2.get(i).getItemCode(), fontN);
                cell = new PdfPCell(phrase);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 文字
                phrase = new Phrase(itemList2.get(i).getCheckContent(), fontN);
                cell = new PdfPCell(phrase);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 是否发现问题
                if (itemList2.get(i).getResult().equals("0")) {
                    hasProblem=true;
                    phrase = new Phrase("√", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                } else {
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                    phrase = new Phrase("√", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
        }
        document.add(table);
        pg = new Paragraph(String.format(" "), font);
        document.add(pg);
        // 不通过项单独列出
        if (hasProblem) {
            table = new PdfPTable(new float[] { 1, 15 });
            table.setWidthPercentage(95);
            phrase = new Phrase("序号", fontB);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(22f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            table.addCell(cell);
            phrase = new Phrase("监督检查问题项目具体内容（可加附页）", fontB);
            cell = new PdfPCell(phrase);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            table.addCell(cell);
            // 循环不通过项
            if(isPreview.equals("1")){
                int num=0;
                for (int i = 0; i < itemList.size(); i++) {
                    if(itemList.get(i).getFpsiPlanResult().getResult().equals("0")){
                        phrase = new Phrase((++num) + "", fontN);
                        cell = new PdfPCell(phrase);
                        cell.setMinimumHeight(22f);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                        table.addCell(cell);
                        // 文字
                        phrase = new Phrase("检查项目："+itemList.get(i).getItemCode()+"\u3000\u3000\u3000检查内容："+itemList.get(i).getCheckContent() + "\n 具体问题详细记录：" +itemList.get(i).getFpsiPlanResult().getRemark(), fontN);
                        cell = new PdfPCell(phrase);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_LEFT);
                        table.addCell(cell);
                    }
                }
            }else{
                int num=0;
                for (int i = 0; i < itemList2.size(); i++) {
                    if(itemList2.get(i).getResult().equals("0")){
                        phrase = new Phrase((++num) + "", fontN);
                        cell = new PdfPCell(phrase);
                        cell.setMinimumHeight(22f);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                        table.addCell(cell);
                        // 文字
                        phrase = new Phrase("检查项目："+itemList2.get(i).getItemCode()+"\u3000\u3000\u3000检查内容："+itemList2.get(i).getCheckContent() + " \n 具体问题详细记录："+itemList2.get(i).getRemark(), fontN);
                        cell = new PdfPCell(phrase);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_LEFT);
                        table.addCell(cell);
                    }
                }
            }
            document.add(table);
            pg = new Paragraph(String.format(" "), font);
            document.add(pg);
        }
        //备注
        table = new PdfPTable(new float[] { 1, 7 });
        table.setWidthPercentage(95);
        phrase = new Phrase("备注 ：",fontN);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        cell.setMinimumHeight(22f);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_LEFT);
        table.addCell(cell);
        if(fpsiInspPlan.getNotes()!=null){
            phrase = new Phrase(fpsiInspPlan.getNotes(),fontN);
        }else {
            phrase = new Phrase(" ",fontN);
        }
        cell =new PdfPCell(phrase);
        cell.setBorder(0);
        cell.setMinimumHeight(22f);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_LEFT);
        table.addCell(cell);
        document.add(table);
        //下方签名部分
        table = new PdfPTable(new float[] { 1, 1 });
        table.setWidthPercentage(95);
        phrase = new Phrase("检查人员现场向企业（当事人）反馈了本次检查情况。", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setMinimumHeight(22f);
        table.addCell(cell);
        phrase = new Phrase("被检查人（法定代表人或授权人）意见：", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setMinimumHeight(16f);
        table.addCell(cell);
        phrase = new Phrase(" ", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setColspan(2);
        table.addCell(cell);

        phrase = new Phrase("签          名：", fontN);
        underline=new Chunk("\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000");
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        table.addCell(cell);


        phrase = new Phrase("检 查 人 员 签 名：", fontN);
        underline=new Chunk("\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000");
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        table.addCell(cell);
        phrase = new Phrase(" ", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setColspan(2);
        table.addCell(cell);

        phrase = new Phrase("　　　年　　　月　　　日", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        table.addCell(cell);
        phrase = new Phrase("　　　年　　　月　　　日", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        table.addCell(cell);
        document.add(table);

    }

    protected void setltPDFInfo(com.itextpdf.text.Document document, Map map)
            throws DocumentException, IOException {
        String exeOrganName = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        List<CheckContentDto> itemList2 = (List<CheckContentDto>) map
//                .get("itemList2");
        ArrayList<CheckContentDto> itemList2 = new ArrayList<CheckContentDto>();
        JSONArray jsonArray1 = (JSONArray) map.get("itemList2");
        if(jsonArray1!=null) {
            for (int i = 0; i < jsonArray1.length(); i++) {
                try {
                    CheckContentDto checkContentDto = new CheckContentDto();
                    JSONObject jsonObject1 = (JSONObject) jsonArray1.get(i);
                    checkContentDto.setItemCode((String) jsonObject1.get("itemCode"));
                    checkContentDto.setParentCode((String) jsonObject1.get("parentCode"));
                    checkContentDto.setCheckContent((String) jsonObject1.get("checkContent"));
                    checkContentDto.setPlanId((String) jsonObject1.get("planId"));
                    checkContentDto.setIfAdded((String) jsonObject1.get("ifAdded"));
                    checkContentDto.setIfCustom((String) jsonObject1.get("ifCustom"));
                    checkContentDto.setAttFlag((String) jsonObject1.get("attFlag"));
                    checkContentDto.setRemarkFlag((String) jsonObject1.get("remarkFlag"));
                    checkContentDto.setResult((String) jsonObject1.get("result"));
                    checkContentDto.setId((String) jsonObject1.get("id"));
//                checkContentDtosc.setPictureUrl();
                    checkContentDto.setRemark((String) jsonObject1.get("remark"));
                    itemList2.add(checkContentDto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
//        List<FpsiLtInspItem> itemList = (List<FpsiLtInspItem>) map.get("itemList");
        ArrayList<FpsiLtInspItem> itemList = new ArrayList<FpsiLtInspItem>();
        JSONArray jsonArray2 = (JSONArray) map.get("itemList");
        for(int i=0;i<jsonArray2.length();i++){
            try {
                FpsiLtInspItem fpsiLtInspItem = new FpsiLtInspItem();
                JSONObject jsonObject2 = (JSONObject)jsonArray2.get(i);
                fpsiLtInspItem.setUuid(jsonObject2.getString("uuid"));
                fpsiLtInspItem.setItemCode(jsonObject2.getString("itemCode"));
                fpsiLtInspItem.setParentCode(jsonObject2.getString("parentCode"));
                fpsiLtInspItem.setCheckContent(jsonObject2.getString("checkContent"));
                fpsiLtInspItem.setPlanId(jsonObject2.getString("planId"));
                fpsiLtInspItem.setIfAdded(jsonObject2.getString("ifAdded"));
                fpsiLtInspItem.setIfCustom(jsonObject2.getString("ifCustom"));
//                fpsiInspItem.setAttFlag(jsonObject2.getString("attFlag"));
                fpsiLtInspItem.setRemarkFlag(jsonObject2.getString("remarkFlag"));
                fpsiLtInspItem.setScheId(jsonObject2.getString("scheId"));
                fpsiLtInspItem.setExeOrgan(jsonObject2.getString("exeOrgan"));
                fpsiLtInspItem.setExeDept(jsonObject2.getString("exeDept"));
                fpsiLtInspItem.setParentFlag(jsonObject2.getString("parentFlag"));
                //bjy0
                if(jsonObject2.get("fpsiPlanResult")!=null){
                    FpsiLtPlanResult fpsiLtPlanResult = new FpsiLtPlanResult();
                    JSONObject jsonObject = (JSONObject)jsonObject2.get("fpsiPlanResult");
                    fpsiLtPlanResult.setResult(jsonObject.getString("result"));
                    fpsiLtPlanResult.setRemark(jsonObject.getString("remark"));
                    fpsiLtInspItem.setFpsiPlanResult(fpsiLtPlanResult);//bjy0
                }
//                fpsiInspItem.setFpsiPlanProcess();
//                fpsiInspItem.setFpsiItemAtt();
                itemList.add(fpsiLtInspItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String isPreview = (String) map.get("isPreview");
        String accompany = (String) map.get("accompany");
//        FpsiLtInspPlan fpsiInspPlan = (FpsiLtInspPlan) map.get("fpsiInspPlan");
        FpsiLtInspPlan fpsiLtInspPlan = new FpsiLtInspPlan();
        JSONObject jsonObject = (JSONObject)map.get("fpsiInspPlan");
        try {
            fpsiLtInspPlan.setScheId(jsonObject.getString("scheId"));
            fpsiLtInspPlan.setPlanId(jsonObject.getString("planId"));
            fpsiLtInspPlan.setTypeId(jsonObject.getString("typeId"));
            fpsiLtInspPlan.setEtpsId(jsonObject.getString("etpsId"));
            fpsiLtInspPlan.setPlanYear((Integer) jsonObject.get("planYear"));
            fpsiLtInspPlan.setPlanMonth((Integer) jsonObject.get("planMonth"));
            Date startdate = new Date();
            Date enddate = new Date();
            Date createDate = new Date();
            try {
                if(!jsonObject.getString("startDate").equals("null")){
                    startdate = sdf.parse((String) jsonObject.get("startDate"));
                }else {
                    startdate = null;
                }
                if(!jsonObject.getString("endDate").equals("null")){
                    enddate = sdf.parse((String) jsonObject.get("endDate"));
                }else {
                    enddate = null;
                }
                if(!jsonObject.getString("createDate").equals("null")){
                    createDate =sdf.parse((String) jsonObject.get("createDate"));
                }else{
                    createDate = null;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            fpsiLtInspPlan.setStartDate(startdate);
            fpsiLtInspPlan.setEndDate(enddate);
            fpsiLtInspPlan.setExeOrgan(jsonObject.getString("exeOrgan"));
            fpsiLtInspPlan.setExeDept(jsonObject.getString("exeDept"));
            fpsiLtInspPlan.setInspDept(jsonObject.getString("inspDept"));
            fpsiLtInspPlan.setResult(jsonObject.getString("result"));
            fpsiLtInspPlan.setRemark(jsonObject.getString("remark"));
            fpsiLtInspPlan.setStatus(jsonObject.getString("status"));
            fpsiLtInspPlan.setNeedRevisit(jsonObject.getString("needRevisit"));
            fpsiLtInspPlan.setNeedSample(jsonObject.getString("needSample"));
            fpsiLtInspPlan.setParentPlanId(jsonObject.getString("parentPlanId"));
            fpsiLtInspPlan.setIfSuo(jsonObject.getString("ifSuo"));
            fpsiLtInspPlan.setAccompaniedPeople(jsonObject.getString("accompaniedPeople"));
            fpsiLtInspPlan.setCreateUser(jsonObject.getString("createUser"));
            fpsiLtInspPlan.setCreateDate(createDate);
            fpsiLtInspPlan.setDiyId(jsonObject.getString("diyId"));
            fpsiLtInspPlan.setExcutorUser(jsonObject.getString("excutorUser"));
            fpsiLtInspPlan.setNotes(jsonObject.getString("notes"));
//            fpsiInspPlan.setFpsiPlanExcutor();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        FpsiLtInfoEnty fpsiEtpsInfo = (FpsiLtInfoEnty) map.get("fpsiEtpsInfo");
        FpsiLtInfoEnty fpsiEtpsInfo = new FpsiLtInfoEnty();
        String adress = (String)map.get("address");
        JSONObject jsonObject1  = (JSONObject)map.get("fpsiEtpsInfo");
        try {
            fpsiEtpsInfo.setFdName(jsonObject1.getString("fdName"));
//            FpsiLtAddressEnty fpsiLtAddressEnty = new FpsiLtAddressEnty();
//            if(jsonObject1.get("fpsiLtAddressEnty")!=null){
//                JSONObject jsonObject2 = (JSONObject)jsonObject1.get("fpsiLtAddressEnty");
//                fpsiLtAddressEnty.setAddress(jsonObject2.getString("address"));
//                fpsiEtpsInfo.setFpsiLtAddressEnty(fpsiLtAddressEnty);
//            }

            fpsiEtpsInfo.setLicNo(jsonObject1.getString("licNo"));
            fpsiEtpsInfo.setPePerson(jsonObject1.getString("pePerson"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<FpsiCertInfo> fpsiCertInfoList = new ArrayList<FpsiCertInfo>();

        Font font = null;
        final Font baseFont = new Font(BaseFont.createFont("assets/MSYYY.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));

        Font fontB = new Font(baseFont);
        Font fontN = new Font(baseFont);
        font = new Font(baseFont);
        font.setSize(18);
        font.setStyle(Font.BOLD);
        fontB.setSize(12);
        fontB.setStyle(Font.BOLD);
        fontN.setSize(12);
        fontN.setStyle(Font.NORMAL);
        Paragraph pg = new Paragraph(
                String.format("食品流通监督通用检查标准规程（SOP）现场核查表格"), font);
        pg.setAlignment(1);
        document.add(pg);
        font.setSize(8);
        pg = new Paragraph(String.format(" "), font);
        document.add(pg);
        font.setSize(18);
        font.setStyle(Font.NORMAL);
        // ///////////////////////////////////////////////////////////////////////////////////////////////

                pg = new Paragraph(String.format(fpsiLtInspPlan.getExeOrgan()), font);


        // pg = new Paragraph(String.format("上海市食品药品监督管理局"), font);
        pg.setAlignment(1);
        document.add(pg);

        font.setSize(5);
        pg = new Paragraph(String.format(" "), font);
        pg.setAlignment(1);
        document.add(pg);
        font.setSize(6);
        font.setStyle(Font.NORMAL);
        pg = new Paragraph(
                String.format("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"),
                font);
        pg.setAlignment(1);
        document.add(pg);
        font.setSize(18);
        pg = new Paragraph(String.format("监督检查笔录"), font);
        pg.setAlignment(1);
        document.add(pg);
        font.setSize(8);
        pg = new Paragraph(String.format(" "), font);
        pg.setAlignment(1);
        document.add(pg);

        // 加入表格布局
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(95);
        Phrase phrase = new Phrase();
        Chunk boldtext = new Chunk("被检查单位名称（姓名）：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        Chunk underline = new Chunk(fpsiEtpsInfo.getFdName());
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase();
        boldtext = new Chunk("地址：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        underline = new Chunk(adress);
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);

        // ----------------------------------循环-----------------------------------
		//for (int i = 0; i < fpsiCertInfoList.size(); i++) {
			phrase = new Phrase();
			boldtext = new Chunk("许可证号：");
			boldtext.setFont(fontB);
			phrase.add(boldtext);
			// ////////////////////////////////////////////////////////////////
			font.setStyle(Font.NORMAL);
			underline = new Chunk(fpsiEtpsInfo.getLicNo());
			underline.setFont(fontN);
			underline.setUnderline(-1.0f, -1.5f);
			phrase.add(underline);
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			table.addCell(cell);

			phrase = new Phrase();
			boldtext = new Chunk("许可类别：");
			boldtext.setFont(fontB);
			phrase.add(boldtext);
			// ////////////////////////////////////////////////////////////////
			underline = new Chunk("食品流通");
			underline.setFont(fontN);
			underline.setUnderline(-1.0f, -1.5f);
			phrase.add(underline);
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			table.addCell(cell);
		//}
        phrase = new Phrase();
        boldtext = new Chunk("许可证号：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        font.setStyle(Font.NORMAL);
        underline = new Chunk(fpsiEtpsInfo.getLicNo());
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase();
        boldtext = new Chunk("许可类别：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        underline = new Chunk("食品流通");
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

        // -----------------------循环结束------------------------------------
        phrase = new Phrase();
        boldtext = new Chunk("法定代表人：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if (null == fpsiEtpsInfo.getPePerson()
                || fpsiEtpsInfo.getPePerson().length() < 1) {
            underline = new Chunk("______________");
        } else {
            underline = new Chunk(fpsiEtpsInfo.getPePerson());
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase();
        boldtext = new Chunk("有效证件号：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        underline = new Chunk("　　　　　　　");
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase();
        boldtext = new Chunk("陪同检查人：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if (null == accompany || accompany.length() < 1) {
            underline = new Chunk("　　　　　　　");
        } else {
            underline = new Chunk(accompany);
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("职务：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        underline = new Chunk("　　　　　　　");
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("有效证件号：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        underline = new Chunk("\u3000\u3000\u3000\u3000\u3000\u3000\u3000");
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        cell.setColspan(2);
        table.addCell(cell);
        // 日期
        phrase = new Phrase();
        boldtext = new Chunk("检查时间：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(fpsiLtInspPlan.getStartDate()==null){
            underline = new Chunk("　" + "　");
        }else {
            underline = new Chunk("　"
                    + (fpsiLtInspPlan.getStartDate().getYear() + 1900) + "　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("年");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(fpsiLtInspPlan.getStartDate()==null){
            underline = new Chunk("　" + "　");
        }else {
            underline = new Chunk("　"
                    + (fpsiLtInspPlan.getStartDate().getMonth() + 1) + "　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("月");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(fpsiLtInspPlan.getStartDate()==null){
            underline = new Chunk("　" + "　");
        }else {
            underline = new Chunk("　" + (fpsiLtInspPlan.getStartDate().getDate())
                    + "　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("日");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if(fpsiLtInspPlan.getStartDate()==null){
            underline = new Chunk("　" + "　");
        }else {
            underline = new Chunk("　" + (fpsiLtInspPlan.getStartDate().getHours())
                    + "　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("时");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if (fpsiLtInspPlan.getStartDate()==null){
            underline = new Chunk("　" + "　");
        }else {
            underline = new Chunk("　" + (fpsiLtInspPlan.getStartDate().getMinutes())
                    + "　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("分至");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if (fpsiLtInspPlan.getEndDate() == null) {
            underline = new Chunk("　");
        } else {
            underline = new Chunk("　" + (fpsiLtInspPlan.getEndDate().getHours())
                    + "　");
        }

        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("时");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        // ////////////////////////////////////////////////////////////////
        if (fpsiLtInspPlan.getEndDate() == null) {
            underline = new Chunk("　");
        } else {
            underline = new Chunk("　"
                    + (fpsiLtInspPlan.getEndDate().getMinutes()) + "　");
        }
        underline.setFont(fontN);
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        boldtext = new Chunk("分");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        cell.setColspan(2);
        table.addCell(cell);
        phrase = new Phrase();
        boldtext = new Chunk("本次检查内容：");
        boldtext.setFont(fontB);
        phrase.add(boldtext);
        cell = new PdfPCell(phrase);
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);
        // table.setLockedWidth(true);

        // 第二段文字
        String str1 = "          我们是";
        String orgName = fpsiLtInspPlan.getExeOrgan();
        String str2 = "行政执法人员，现出示执法证件，依法对你单位进行现场检查，请予配合。依照规定，执法人员少于两人或者所出示的执法证件与其身份不符的，你单位有权拒绝检查；对于检查人员，有下列情形之一的，你单位有权申请检查人员回避：（1）系当事人或当事人的近亲属；（2）与本人或本人近亲属有利害关系；（3）与当事人有其他关系，可能影响公正执法的。";
        phrase = new Phrase(str1 + orgName + str2, fontN);
        cell = new PdfPCell(phrase);
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);
        document.add(table);
        // SOP检查项表
        pg = new Paragraph(String.format(" "), font);
        document.add(pg);
        table = new PdfPTable(new float[] { 3, 9, 33, 2, 2 });
        table.setWidthPercentage(95);
        phrase = new Phrase("序号", fontB);
        cell = new PdfPCell(phrase);
        cell.setMinimumHeight(22f);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);
        phrase = new Phrase("核查项目", fontB);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);
        phrase = new Phrase("核查内容", fontB);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);
        phrase = new Phrase("是否发现问题", fontB);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setColspan(2);
        table.addCell(cell);

        // PdfPTable table1=new PdfPTable(2);
        phrase = new Phrase("是", fontB);
        cell = new PdfPCell(phrase);
        cell.setMinimumHeight(22f);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setColspan(1);
        table.addCell(cell);
        phrase = new Phrase("否", fontB);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        cell.setColspan(1);
        table.addCell(cell);
        boolean hasProblem = false;
        if (isPreview.equals("1")) {
            for (int i = 0; i < itemList.size(); i++) {
                phrase = new Phrase((i + 1) + "", fontN);
                cell = new PdfPCell(phrase);
                cell.setMinimumHeight(22f);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 编号
                phrase = new Phrase(itemList.get(i).getItemCode(), fontN);
                cell = new PdfPCell(phrase);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 文字
                phrase = new Phrase(itemList.get(i).getCheckContent(), fontN);
                cell = new PdfPCell(phrase);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 是否发现问题
                if (itemList.get(i).getFpsiPlanResult().getResult()!=null&&itemList.get(i).getFpsiPlanResult().getResult().equals("0")) {
                    hasProblem = true;
                    phrase = new Phrase("√", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                } else if(itemList.get(i).getFpsiPlanResult().getResult()!=null){
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                    phrase = new Phrase("√", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                }else{
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
        } else {
            for (int i = 0; i < itemList2.size(); i++) {
                phrase = new Phrase((i + 1) + "", fontN);
                cell = new PdfPCell(phrase);
                cell.setMinimumHeight(22f);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 编号
                phrase = new Phrase(itemList2.get(i).getItemCode(), fontN);
                cell = new PdfPCell(phrase);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 文字
                phrase = new Phrase(itemList2.get(i).getCheckContent(), fontN);
                cell = new PdfPCell(phrase);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                table.addCell(cell);
                // 是否发现问题
                if (itemList2.get(i).getResult()!=null&&itemList2.get(i).getResult().equals("0")) {
                    hasProblem = true;
                    phrase = new Phrase("√", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                } else if(itemList2.get(i).getResult()!=null) {
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                    phrase = new Phrase("√", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                }else{
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                    phrase = new Phrase(" ", fontB);
                    cell = new PdfPCell(phrase);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
        }
        document.add(table);
        pg = new Paragraph(String.format(" "), font);
        document.add(pg);
        // 不通过项单独列出
        if (hasProblem) {
            table = new PdfPTable(new float[] { 1, 15 });
            table.setWidthPercentage(95);
            phrase = new Phrase("序号", fontB);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(22f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            table.addCell(cell);
            phrase = new Phrase("监督检查问题项目具体内容（可加附页）", fontB);
            cell = new PdfPCell(phrase);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            table.addCell(cell);
            // 循环不通过项
            if (isPreview.equals("1")) {
                int num = 0;
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).getFpsiPlanResult().getResult()
                            .equals("0")) {
                        phrase = new Phrase((++num) + "", fontN);
                        cell = new PdfPCell(phrase);
                        cell.setMinimumHeight(22f);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                        table.addCell(cell);
                        // 文字
                        phrase = new Phrase("检查项目："+itemList.get(i).getItemCode()+"\u3000\u3000检查内容："+itemList.get(i).getCheckContent() +"\n 具体问题详细记录：" +itemList.get(i).getFpsiPlanResult().getRemark(),
                                fontN);
                        cell = new PdfPCell(phrase);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_LEFT);
                        table.addCell(cell);
                    }
                }
            } else {
                int num = 0;
                for (int i = 0; i < itemList2.size(); i++) {
                    if (itemList2.get(i).getResult().equals("0")) {
                        phrase = new Phrase((++num) + "", fontN);
                        cell = new PdfPCell(phrase);
                        cell.setMinimumHeight(22f);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                        table.addCell(cell);
                        // 文字
                        phrase = new Phrase("检查项目："+itemList2.get(i).getItemCode()+"\u3000\u3000检查内容："+itemList2.get(i).getCheckContent() + "\n 具体问题详细记录："+itemList2.get(i).getRemark(),
                                fontN);
                        cell = new PdfPCell(phrase);
                        cell.setUseAscender(true);
                        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(cell.ALIGN_LEFT);
                        table.addCell(cell);
                    }
                }
            }
            document.add(table);
            pg = new Paragraph(String.format(" "), font);
            document.add(pg);
        }
        //备注
        table = new PdfPTable(new float[] { 1, 7 });
        table.setWidthPercentage(95);
        phrase = new Phrase("备注 ：",fontN);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        cell.setMinimumHeight(22f);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_LEFT);
        table.addCell(cell);
        if(fpsiLtInspPlan.getNotes()!=null){
            phrase = new Phrase(fpsiLtInspPlan.getNotes(),fontN);
        }else {
            phrase = new Phrase(" ",fontN);
        }
        cell =new PdfPCell(phrase);
        cell.setBorder(0);
        cell.setMinimumHeight(22f);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_LEFT);
        table.addCell(cell);
        document.add(table);
        // 下方签名部分
        table = new PdfPTable(new float[] { 1, 1 });
        table.setWidthPercentage(95);
        phrase = new Phrase("检查人员现场向企业（当事人）反馈了本次检查情况。", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setMinimumHeight(22f);
        table.addCell(cell);
        phrase = new Phrase("被检查人（法定代表人或授权人）意见：", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setMinimumHeight(16f);
        table.addCell(cell);
        phrase = new Phrase(" ", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setColspan(2);
        table.addCell(cell);

        phrase = new Phrase("签          名：", fontN);
        underline=new Chunk("\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000");
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        table.addCell(cell);


        phrase = new Phrase("检 查 人 员 签 名：", fontN);
        underline=new Chunk("\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000");
        underline.setUnderline(-1.0f, -1.5f);
        phrase.add(underline);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        table.addCell(cell);
        phrase = new Phrase(" ", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setColspan(2);
        table.addCell(cell);

        phrase = new Phrase("　　　年　　　月　　　日", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        table.addCell(cell);
        phrase = new Phrase("　　　年　　　月　　　日", fontN);
        cell = new PdfPCell(phrase);
        cell.setUseAscender(true);
        cell.setBorder(0);
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        table.addCell(cell);
        document.add(table);

    }
}
