package com.wonders.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.fragment.CheckInfoFragment;
import com.wonders.http.Retrofit2Helper;
import com.wonders.bean.PlanBean;
import com.wonders.bean.ZtXkzBean;
import com.wonders.thread.FastDealExecutor;
import com.wonders.util.DateUtil;
import com.wonders.widget.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 1229 on 2016/8/29.
 * 生产-告知页、预览页、办结页
 */
public class PreviewScActivity extends PreviewActivity {
    private PlanBean bean;
    private JSONObject jsonObject;
    private JSONObject checksJson;
    private String planId;
    private String id;

    public static ArrayList<String> highItems = new ArrayList<>();//重点项
    public static ArrayList<String> lowItems = new ArrayList<>();//一般项
    public static ArrayList<String> highPros = new ArrayList<>();//重点项的问题项
    public static ArrayList<String> lowPros = new ArrayList<>();//一般项的问题项
    public static JSONArray groupJSONArray;
    public static JSONArray childJSONArray;
    public static JSONArray notesJSONArray;
    public static String count1 = "";
    public static String count2 = "";
    public static String count3 = "";
    public static String count4 = "";//重点项
    public static String count5 = "";
    public static String count6 = "";//一般项
    public static String count7 = "";
    public static String count8 = "";//重点项的问题项
    public static String count9 = "";
    public static String count10 = "";//一般项的问题项
    public static String checkResult = "0";
    public static String startDate = "   年   月   日";
    public static String checkUnit = "   ";

    private int docType;
    private String planType;
    private String checkName = "";

    private String certNo = "";
    private String etpsName = "";
    private String address = "";
    private String personNameGaozhi = "　　　　　　　　　";
    private String personNoGaozhi = "　　　　　　　　　";
    private String certNameGaozhi = "　　　　　　　　　";
    private String certNoGaozhi = "　　　　　　　　　";
    private String checkDateGaozhi = "   年   月   日";
    private String checkAddressGaozhi = "";

    private String legalPersonRecord = "";
    private String phoneNoRecord = "";
    private String NoRecord = "";
    private String illustration = "";
    private String suggestion = "";

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HashMap params = getIntent().getSerializableExtra(Constants.PARAMS) != null ? (HashMap) getIntent().getSerializableExtra(Constants.PARAMS) : new HashMap();
        planType = params.get(Constants.PLAN_TYPE) != null ? (String) params.get(Constants.PLAN_TYPE) : "F";
        planId = params.get(Constants.PLAN_ID) != null ? (String) params.get(Constants.PLAN_ID) : "";
        docType = params.get(Constants.DOC_TYPE) != null ? (int) params.get(Constants.DOC_TYPE) : 2;
        id = getIntent().getStringExtra("id");
        certNo = handleCertNo();

        switch (docType) {
            case 1:
                //组装数据
                String gaozhi = params.get("gaozhi") != null ? (String) params.get("gaozhi") : "";
                try {
                    jsonObject = new JSONObject(gaozhi);
                    etpsName = jsonObject.getString("etpsName");
                    address = jsonObject.getString("address");
                    checkUnit = jsonObject.getString("checkUnit");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                checksJson = new JSONObject();
                try {
                    checksJson.put("group", groupJSONArray);
                    checksJson.put("child", childJSONArray);
                    checksJson.put("notes", notesJSONArray);
                    if ("H".equals(planType))
                        checkName = "保健";
                    checksJson.put("checkName", checkName);
                    checksJson.put("count2", count2);
                    checksJson.put("count3", count3);
                    checksJson.put("count5", count5);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("etpsName", CheckInfoFragment.ztInfo.getEtpsName() == null ? "" : CheckInfoFragment.ztInfo.getEtpsName());
                    etpsName = CheckInfoFragment.ztInfo.getEtpsName() == null ? "" : CheckInfoFragment.ztInfo.getEtpsName();
                    jsonObject.put("address", CheckInfoFragment.ztInfo.getFactoryAddr() == null ? "" : CheckInfoFragment.ztInfo.getFactoryAddr());
                    address = CheckInfoFragment.ztInfo.getFactoryAddr() == null ? "" : CheckInfoFragment.ztInfo.getFactoryAddr();
                    jsonObject.put("legalPerson", CheckInfoFragment.ztInfo.getLegalPerson() == null ? "" : CheckInfoFragment.ztInfo.getLegalPerson());
                    jsonObject.put("phoneNo", CheckInfoFragment.ztInfo.getPhoneNo() == null ? "" : CheckInfoFragment.ztInfo.getPhoneNo());
                    jsonObject.put("certNo", certNo);
                    jsonObject.put("count1", count1);
                    jsonObject.put("count2", count2);
                    jsonObject.put("count3", count3);
                    jsonObject.put("count4", count4);
                    jsonObject.put("count5", count5);
                    jsonObject.put("count6", count6);
                    jsonObject.put("count7", count7);
                    jsonObject.put("count8", count8);
                    jsonObject.put("count9", count9);
                    jsonObject.put("count10", count10);
                    jsonObject.put("checkUnit", checkUnit);
                    if (checkUnit.substring(0, 2).equals("上海"))
                        jsonObject.put("topTitle", checkUnit);
                    else
                        jsonObject.put("topTitle", "上海市" + checkUnit);
                    jsonObject.put("checkResult", checkResult);
                    jsonObject.put("startDate", startDate);
                    jsonObject.put("notes", notesJSONArray);
                    jsonObject.put("suggestion", "");
                    jsonObject.put("type", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                jsonObject = new JSONObject();
                bean = (PlanBean) params.get("bean");
                NoRecord = bean.getCheckNo();
                legalPersonRecord = bean.getLegalPerson();
                phoneNoRecord = bean.getPhoneNo();
                etpsName = bean.getEtpsName();
                address = bean.getAddress();
                checkUnit = bean.getExeOrgan();
                String[] remarkArray = !TextUtils.isEmpty(bean.getRemark()) ? bean.getRemark().split("\n") : new String[0];
                suggestion = bean.getOpinion();
                try {
                    jsonObject.put("checkNo", bean.getCheckNo() == null ? "" : bean.getCheckNo());
                    jsonObject.put("etpsName", bean.getEtpsName() == null ? "" : bean.getEtpsName());
                    jsonObject.put("address", bean.getAddress() == null ? "" : bean.getAddress());
                    jsonObject.put("legalPerson", bean.getLegalPerson() == null ? "" : bean.getLegalPerson());
                    jsonObject.put("phoneNo", bean.getPhoneNo() == null ? "" : bean.getPhoneNo());
                    jsonObject.put("certNo", bean.getCertNo() == null ? "" : bean.getCertNo());
                    certNo = bean.getCertNo() == null ? "" : bean.getCertNo();
                    jsonObject.put("count1", count1);
                    jsonObject.put("count2", count2);
                    jsonObject.put("count3", count3);
                    jsonObject.put("count4", count4);
                    jsonObject.put("count5", count5);
                    jsonObject.put("count6", count6);
                    jsonObject.put("count7", count7);
                    jsonObject.put("count8", count8);
                    jsonObject.put("count9", count9);
                    jsonObject.put("count10", count10);
                    jsonObject.put("checkUnit", bean.getExeOrgan());
                    if (bean.getExeOrgan().substring(0, 2).equals("上海"))
                        jsonObject.put("topTitle", bean.getExeOrgan());
                    else
                        jsonObject.put("topTitle", "上海市" + bean.getExeOrgan());
                    jsonObject.put("checkResult", checkResult);
                    jsonObject.put("startDate", startDate);
                    JSONArray remarkJSONArray = new JSONArray();
                    for (String s : remarkArray) {
                        remarkJSONArray.put(s);
                    }
                    jsonObject.put("remark", remarkJSONArray);
                    jsonObject.put("suggestion", suggestion);
                    jsonObject.put("type", 2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case 5:
                checksJson = new JSONObject();
                try {
                    checksJson.put("group", groupJSONArray);
                    checksJson.put("child", childJSONArray);
                    if ("H".equals(planType))
                        checkName = "保健";
                    checksJson.put("checkName", checkName);
                    checksJson.put("count2", count2);
                    checksJson.put("count3", count3);
                    checksJson.put("count5", count5);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        int resultCode = getIntent().getIntExtra("position", 0);
        setResult(resultCode);

        switch (docType) {
            case 1:
                webView.loadUrl("file:///android_asset/gaozhi.html");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        // TODO Auto-generated method stub
                        super.onPageFinished(view, url);
                        String call = "javascript:sendData(\'" + jsonObject.toString().replaceAll("\r|\n", "") + "\')";
                        view.loadUrl(call);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        return false;
                    }
                });
                webView.loadUrl("javascript:getIllustrate()");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("javascript:getValue()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }

                break;
            case 2:
            case 4:
                Log.d("sendData", jsonObject.toString());
                webView.loadUrl("file:///android_asset/record.html");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        // TODO Auto-generated method stub
                        super.onPageFinished(view, url);
                        String call = "javascript:sendData(\'" + jsonObject.toString().replaceAll("\r|\n", "") + "\')";
                        view.loadUrl(call);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        return false;
                    }
                });
                break;
            case 5:
                webView.loadUrl("file:///android_asset/checks.html");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        // TODO Auto-generated method stub
                        super.onPageFinished(view, url);
                        Log.e("checksJson", checksJson.toString());
                        String call = "javascript:createTable(\'" + checksJson.toString().replaceAll("\r|\n", "") + "\')";
                        view.loadUrl(call);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        return false;
                    }
                });
                break;
        }
    }

    @Override
    protected boolean createPDF() {
        switch (docType) {
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("javascript:getValue()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            try {
                                JSONArray arr = new JSONArray(value);
                                personNameGaozhi = TextUtils.isEmpty(arr.getString(0)) ? "　　　　　　　　　" : arr.getString(0);
                                personNoGaozhi = TextUtils.isEmpty(arr.getString(1)) ? "　　　　　　　　　" : arr.getString(1);
                                certNameGaozhi = TextUtils.isEmpty(arr.getString(2)) ? "　　　　　　　　　" : arr.getString(2);
                                certNoGaozhi = TextUtils.isEmpty(arr.getString(3)) ? "　　　　　　　　　" : arr.getString(3);
                                checkDateGaozhi = (TextUtils.isEmpty(arr.getString(4)) ? "　　　　　　　　　" : arr.getString(4)) + "年" + (TextUtils.isEmpty(arr.getString(5)) ? "　　　　　　　　　" : arr.getString(5)) + "月" + ((TextUtils.isEmpty(arr.getString(6)) ? "　　　　　　　　　" : arr.getString(6))) + "日";
                                checkAddressGaozhi = TextUtils.isEmpty(arr.getString(7)) ? "　　　　　　　　　" : arr.getString(7);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                FastDealExecutor.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        createGaoZhiPDF();
                                    }
                                });
                                if (AppData.getInstance().isNetWork())
                                    uploadGaoZhiData();
                            }
                        }
                    });
                }
                break;
            case 2:
            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("javascript:getValue()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            try {
                                JSONArray arr = new JSONArray(value);
                                legalPersonRecord = arr.getString(0);
                                phoneNoRecord = arr.getString(1);
                                NoRecord = arr.getString(2);
                                illustration = arr.getString(3) + ((TextUtils.isEmpty(arr.getString(4)) || arr.getString(4).equals("null")) ? "" : arr.getString(4));
                                suggestion = TextUtils.isEmpty(arr.getString(5)) || arr.getString(5).equals("null") ? "" : arr.getString(5);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                FastDealExecutor.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        createRecordPDF();
                                    }
                                });
                                if (AppData.getInstance().isNetWork())
                                    uploadRecordData();
                            }
                        }
                    });
                }

                break;
            case 3:
            case 5:
                FastDealExecutor.run(new Runnable() {
                    @Override
                    public void run() {
                        createChecksPDF();
                    }
                });
                break;

        }

        return false;
    }

    @Override
    protected void findView() {
        super.findView();

        btn_change.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setOnclick() {
        super.setOnclick();

        switch (docType) {
            case 1:
            case 4:
            case 5:
                btn_change.setVisibility(View.GONE);
                break;
            case 2:
            case 3:
                btn_change.setVisibility(View.VISIBLE);
                break;
        }
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((String) btn_change.getText()) {
                    case "下一页":
                        docType = 3;
                        webView.loadUrl("file:///android_asset/checks.html");
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                // TODO Auto-generated method stub
                                super.onPageFinished(view, url);
                                Log.e("checksJson", checksJson.toString());
                                String call = "javascript:createTable(\'" + checksJson.toString().replaceAll("\r|\n", "") + "\')";
                                view.loadUrl(call);
                            }

                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                                return false;
                            }
                        });
                        btn_change.setText("上一页");
                        break;
                    case "上一页":
                        docType = 2;
                        Log.d("sendData", jsonObject.toString());
                        webView.loadUrl("file:///android_asset/record.html");
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                // TODO Auto-generated method stub
                                super.onPageFinished(view, url);
                                String call = "javascript:sendData(\'" + jsonObject.toString().replaceAll("\r|\n", "") + "\')";
                                view.loadUrl(call);
                            }

                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                                return false;
                            }
                        });
                        btn_change.setText("下一页");
                        break;
                }
            }
        });

    }

    public void createGaoZhiPDF() {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
//        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4.rotate());
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF_PATH));
            document.open();
            float llx = 45;
            float lly = 50;
            float urx = 550;
            float ury = 740;
            PdfContentByte canvas = writer.getDirectContent();
            Rectangle rect = new Rectangle(llx, lly, urx, ury);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(1);
            canvas.rectangle(rect);
            Font baseFont = new Font(BaseFont.createFont("assets/MSYYY.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
            Font font = new Font(baseFont);
            font.setSize(20);
            font.setStyle(Font.BOLD);
            Font fontLab = new Font(baseFont);
            fontLab.setSize(12);
            fontLab.setStyle(Font.BOLD);
            Font fontContent = new Font(baseFont);
            fontContent.setSize(12);
            fontContent.setStyle(Font.NORMAL);

            Paragraph pg = new Paragraph(
                    "食品生产日常监督检查要点表", font);
            pg.setAlignment(1);
            document.add(pg);
            font.setSize(18);
            font.setStyle(Font.NORMAL);
            pg = new Paragraph("告知页", font);
            pg.setAlignment(1);
            document.add(pg);

            pg = new Paragraph(" ");
            pg.setAlignment(1);
            document.add(pg);
            // 加入表格布局
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(95);
//            table.setTableEvent(new BorderEvent());
//            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Phrase phrase = new Phrase();
            Chunk boldtext = new Chunk("被检查单位：");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            // ////////////////////////////////////////////////////////////////
            Chunk underline = new Chunk(etpsName);
            underline.setFont(fontContent);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            PdfPCell cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setPaddingBottom(8);
            cell.setBorder(0);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase();
            boldtext = new Chunk("地址：");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            // ////////////////////////////////////////////////////////////////
            underline = new Chunk(address);
            underline.setFont(fontContent);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setBorder(0);
            cell.setPaddingBottom(8);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase();
            phrase.setLeading(24);
            boldtext = new Chunk("检查人员及执法证件名称、编号：1、");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            underline = new Chunk(personNameGaozhi);
            underline.setFont(fontContent);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            boldtext = new Chunk(" ");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            underline = new Chunk(personNoGaozhi + "\n");
            underline.setFont(fontContent);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            boldtext = new Chunk("                              2、");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            underline = new Chunk(certNameGaozhi);
            underline.setFont(fontContent);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            boldtext = new Chunk(" ");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            underline = new Chunk(certNoGaozhi);
            underline.setFont(fontContent);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setColspan(2);
            cell.setPaddingBottom(8);
            cell.setBorder(0);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase();
            boldtext = new Chunk("检查时间：");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            underline = new Chunk(checkDateGaozhi);
            underline.setUnderline(-1.0f, -1.5f);
            underline.setFont(fontContent);
            phrase.add(underline);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setColspan(2);
            cell.setPaddingBottom(8);
            cell.setBorder(0);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase();
            boldtext = new Chunk("检查地点：");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            underline = new Chunk(checkAddressGaozhi);
            underline.setFont(fontContent);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setColspan(2);
            cell.setBorder(0);
            cell.setPaddingBottom(8);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase();
            boldtext = new Chunk("告知事项：");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            // 第一段文字
            String str1 = "    我们是" + checkUnit + "监督检查人员，现出示执法证件，我们依法对你（单位）进行日常监督检查，请予配合。";
//                    String orgName = fpsiInspPlan.getExeOrgan();
            String orgName = "万达信息";
            String str2 = "行政执法人员，现出示执法证件，依法对你单位进行现场检查，请予配合。依照规定，执法人员少于两人或者所出示的执法证件与其身份不符的，你单位有权拒绝检查；对于检查人员，有下列情形之一的，你单位有权申请检查人员回避：（1）系当事人或当事人的近亲属；（2）与本人或本人近亲属有利害关系；（3）与当事人有其他关系，可能影响公正执法的。";
            phrase = new Phrase();
            phrase.setLeading(24);
            boldtext = new Chunk("    我们是");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            underline = new Chunk(checkUnit);
            underline.setFont(fontContent);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            boldtext = new Chunk("监督检查人员，现出示执法证件，我们依法对你（单位）进行日常监督检查，请予配合。");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            // 第二段文字
            phrase = new Phrase("    依照法律规定，监督检查人员少于两人或者所出示的执法证件与其身份不符的，你（单位）有权拒绝检查，对于监督检查人员有下列情形之一的，你单位有权申请检查人员回避：（1）系当事人或当事人的近亲属；（2）与本人或本人近亲属有利害关系；（3）与当事人有其他关系，可能影响公正执法的。", fontContent);
            phrase.setLeading(24);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("    问：你（单位）是否申请回避？\n", fontContent);
            phrase.setLeading(24);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("    答：\n\n\n\n\n", fontContent);
            phrase.setLeading(24);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase();
            boldtext = new Chunk("被检查单位签字：");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            // ////////////////////////////////////////////////////////////////
            underline = new Chunk("\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000");
            underline.setFont(fontContent);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setBorder(0);
            cell.setPaddingBottom(16);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase();
            boldtext = new Chunk("检查人员签字：");
            boldtext.setFont(fontContent);
            phrase.add(boldtext);
            // ////////////////////////////////////////////////////////////////
            underline = new Chunk("\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000");
            underline.setFont(fontContent);
            underline.setUnderline(-1.0f, -1.5f);
            phrase.add(underline);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setBorder(0);
            cell.setPaddingBottom(16);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("　　　年　　　月　　　日", fontContent);
            cell = new PdfPCell(phrase);
            cell.setUseAscender(true);
            cell.setBorder(0);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            table.addCell(cell);
            phrase = new Phrase("　　　年　　　月　　　日", fontContent);
            cell = new PdfPCell(phrase);
            cell.setUseAscender(true);
            cell.setBorder(0);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            table.addCell(cell);
            document.add(table);
            // ////////////////////////////////////////////////////////////////
            document.close();
            handler.sendEmptyMessage(0);
        } catch (Exception e) {
            handler.sendEmptyMessage(1);
            document.close();
            e.printStackTrace();
        }
    }

    public void createRecordPDF() {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF_PATH));
            document.open();
            Font baseFont = new Font(BaseFont.createFont("assets/MSYYY.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
            Font font = new Font(baseFont);
            font.setSize(20);
            font.setStyle(Font.BOLD);
            Font fontLab = new Font(baseFont);
            fontLab.setSize(12);
            fontLab.setStyle(Font.BOLD);
            Font fontContent = new Font(baseFont);
            fontContent.setSize(12);
            fontContent.setStyle(Font.NORMAL);
            Font fontTitle = new Font(baseFont);
            fontTitle.setSize(18);
            fontTitle.setStyle(Font.BOLD);

            String city = (checkUnit.startsWith("上海市") || checkUnit.startsWith("市")) ? "上海市食品药品监督局" : "上海市" + checkUnit;
            Paragraph pg = new Paragraph(city
                    , fontTitle);
            pg.setAlignment(1);
            document.add(pg);

            pg = new Paragraph("食品生产经营日常监督检查结果记录表\n", font);
            pg.setAlignment(1);
            document.add(pg);

            pg = new Paragraph("编号： " + NoRecord + "\n", fontContent);
            pg.setAlignment(1);
            pg.setSpacingAfter(5);
            document.add(pg);
            // 加入表格布局
            PdfPTable table = new PdfPTable(new float[]{8, 17, 8, 17});
            table.setWidthPercentage(95);
            Phrase phrase;
            PdfPCell cell;
            Chunk chunk;
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("名称", fontContent);
            cell = new PdfPCell(phrase);
            cell.setPadding(8);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase(etpsName, fontContent);
            cell = new PdfPCell(phrase);
            cell.setPadding(8);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("地址", fontContent);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase(address, fontContent);
            cell = new PdfPCell(phrase);
            cell.setPadding(8);
            cell.setMinimumHeight(22f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("联系人", fontContent);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase(legalPersonRecord, fontContent);
            cell = new PdfPCell(phrase);
            cell.setPadding(8);
            cell.setMinimumHeight(22f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("联系方式", fontContent);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase(phoneNoRecord, fontContent);
            cell = new PdfPCell(phrase);
            cell.setPadding(8);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("许可证编号", fontContent);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase(certNo, fontContent);
            cell = new PdfPCell(phrase);
            cell.setPadding(8);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("检查次数", fontContent);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase(String.format("本年度第" + count1 + "次检查"), fontContent);
            cell = new PdfPCell(phrase);
            cell.setPadding(8);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            //内容加换行
            String blank = "    ";
            String name = checkUnit;
            String after = String.format("检查人员根据《中华人民共和国食品安全法》及其实施条例、《食品生产经营日常" +
                    "监督检查管理办法》的规定，于" + startDate + "对你单位进行了监督检查，本次监督检查按照表开展，共检查了%s项内容；其中：\n", count2
            );
            phrase = new Phrase();
            phrase.setLeading(24);
            chunk = new Chunk("检查内容：\n");
            chunk.setFont(fontContent);
            phrase.add(chunk);
            chunk = new Chunk(blank);
            chunk.setFont(fontContent);
            phrase.add(chunk);
            chunk = new Chunk(name);
            chunk.setFont(fontContent);
            chunk.setUnderline(-1.0f, -1.5f);
            phrase.add(chunk);
            Paragraph paragraph = new Paragraph(after, fontContent);
            paragraph.setLeading(24);
//            chunk = new Chunk(after);
//            chunk.setFont(fontContent);
//            paragraph.setLeading(15.0f, 5.0f);
//            paragraph.setSpacingBefore(50);
//            paragraph.setSpacingAfter(50);
//            paragraph.add(chunk);
//            paragraph.add(chunk);
//            chunk = new Chunk(sb.toString());
//            chunk.setFont(fontContent);
            phrase.add(paragraph);

            String[] countArray4 = count4.split(",");
            String[] countArray6 = count6.split(",");
            String[] countArray8 = count8.split(",");
            String[] countArray10 = count10.split(",");

//            chunk = new Chunk(String.format("    重点项（" + count3 + "）项，项目序号分别是%1$s，发现问题（" + count7 + "）项，项目序号分别是%2$s；\n", "("+count4+")", "("+count8+")"));
//            chunk.setFont(fontContent);
//            phrase.add(chunk);
//            chunk = new Chunk(String.format("    一般项（" + count5 + "）项，项目序号分别是%1$s，发现问题（" + count9 + "）项，项目序号分别是%2$s。\n", "("+count6+")", "("+count10+")"));
//            chunk.setFont(fontContent);
//            phrase.add(chunk);
            chunk = new Chunk("    重点项（" + count3 + "）项，项目序号分别是（");
            chunk.setFont(fontContent);
            phrase.add(chunk);
            for (int i = 0; i < countArray4.length; i++) {
                if (i == 0)
                    chunk = new Chunk(countArray4[i]);
                else
                    chunk = new Chunk("、" + countArray4[i]);
                chunk.setFont(fontContent);
                phrase.add(chunk);
            }
            chunk = new Chunk("），发现问题（" + count7 + "）项，项目序号分别是（");
            chunk.setFont(fontContent);
            phrase.add(chunk);
            for (int i = 0; i < countArray8.length; i++) {
                if (i == 0)
                    chunk = new Chunk(countArray8[i]);
                else
                    chunk = new Chunk("、" + countArray8[i]);
                chunk.setFont(fontContent);
                phrase.add(chunk);
            }
            chunk = new Chunk("）;\n    一般项（" + count5 + "）项，项目序号分别是（");
            chunk.setFont(fontContent);
            phrase.add(chunk);
            for (int i = 0; i < countArray6.length; i++) {
                if (i == 0)
                    chunk = new Chunk(countArray6[i]);
                else
                    chunk = new Chunk("、" + countArray6[i]);
                chunk.setFont(fontContent);
                phrase.add(chunk);
            }
            chunk = new Chunk("）,发现问题（" + count9 + "）项，项目序号分别是（");
            chunk.setFont(fontContent);
            phrase.add(chunk);
            for (int i = 0; i < countArray10.length; i++) {
                if (i == 0)
                    chunk = new Chunk(countArray10[i]);
                else
                    chunk = new Chunk("、" + countArray10[i]);
                chunk.setFont(fontContent);
                phrase.add(chunk);
            }
            phrase.add(new Chunk("）"));

            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setPadding(8);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            cell.setColspan(4);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase();
            chunk = new Chunk("检查结果：\n\n");
            chunk.setFont(fontContent);
            phrase.add(chunk);
            switch (checkResult) {
                case "0":
                    chunk = new Chunk("□符合    □基本符合    □不符合\n\n");
                    chunk.setFont(fontContent);
                    phrase.add(chunk);
                    break;
                case "1":
                    chunk = new Chunk("√符合    □基本符合    □不符合\n\n");
                    chunk.setFont(fontContent);
                    phrase.add(chunk);
                    break;
                case "2":
                    chunk = new Chunk("□符合    √基本符合    □不符合\n\n");
                    chunk.setFont(fontContent);
                    phrase.add(chunk);
                    break;
                case "3":
                    chunk = new Chunk("□符合    □基本符合    √不符合\n\n");
                    chunk.setFont(fontContent);
                    phrase.add(chunk);
                    break;
            }
            chunk = new Chunk("结果处理：\n\n");
            chunk.setFont(fontContent);
            phrase.add(chunk);
            switch (checkResult) {
                case "0":
                    chunk = new Chunk("□通过    □书面限期整改    □食品生产经营者立即停止食品生产经营活动\n\n");
                    chunk.setFont(fontContent);
                    phrase.add(chunk);
                    break;
                case "1":
                    chunk = new Chunk("√通过    □书面限期整改    □食品生产经营者立即停止食品生产经营活动\n\n");
                    chunk.setFont(fontContent);
                    phrase.add(chunk);
                    break;
                case "2":
                    chunk = new Chunk("□通过    √书面限期整改    □食品生产经营者立即停止食品生产经营活动\n\n");
                    chunk.setFont(fontContent);
                    phrase.add(chunk);
                    break;
                case "3":
                    chunk = new Chunk("□通过    □书面限期整改    √食品生产经营者立即停止食品生产经营活动\n\n");
                    chunk.setFont(fontContent);
                    phrase.add(chunk);
                    break;
            }

            cell = new PdfPCell(phrase);
            cell.setPadding(8);
            cell.setPaddingBottom(0);
            cell.setBorderWidthBottom(0f);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            cell.setColspan(4);
            table.addCell(cell);
            phrase = new Paragraph();
            phrase.setLeading(24);
            chunk = new Chunk("说明（可附页）：\n");
            chunk.setFont(fontContent);
            phrase.add(chunk);

            String joinIllustration = (TextUtils.isEmpty(illustration) ? "无\n" : illustration) + "处理意见：\n" + suggestion;
            String[] arrIllustration = joinIllustration.split("\n");

            StringBuilder notes = new StringBuilder();
            int lineLength = 0;//记录行数是否超过4行
            int index = 0;//第几个item被截取
            int currentLength = 0;//截取了item的第几行
            int lineByte = 42;
            for (int i = 0; i < arrIllustration.length; i++) {
                String checkContent = "";
                String note = "";
//                try {
//                    checkContent = notesJSONArray.getJSONObject(i).getString("checkContent");
//                    note = notesJSONArray.getJSONObject(i).getString("note");
//                    String str = String.format("检查%1$s时发现:%2$s", checkContent, note);
                String str = arrIllustration[i];
                if (lineLength < 4) {
                    currentLength = 4 - lineLength;
                    lineLength += Math.ceil((str.length() / Float.valueOf(lineByte)));
                    index = i;
                }

                notes.append(str);
                if (i != arrIllustration.length) {
                    notes.append("\n");
                }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
//            if (0 == illustration.length()) {
//                notes.append("无");
//            }
//            if (index < 3 && !(0 == arrIllustration.length)) {
//                notes.append("处理意见：");
//            }
            chunk = new Chunk(notes.toString());
            chunk.setFont(fontContent);
            phrase.add(chunk);
            cell = new PdfPCell();
            cell.addElement(phrase);
            cell.setBorderWidthTop(0f);
            cell.setPadding(8);
            cell.setPaddingTop(0);
            cell.setFixedHeight(120);
            cell.setUseAscender(true);
//            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            cell.setColspan(4);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase();
            chunk = new Chunk("执法人员（签名）：\n\n\n\n\n");
            chunk.setFont(fontContent);
            phrase.add(chunk);
            cell = new PdfPCell(phrase);
            cell.setBorderWidthBottom(0);
            cell.setPadding(8);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            cell.setColspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase();
            chunk = new Chunk("被检查单位意见：\n\n\n");
            chunk.setFont(fontContent);
            phrase.add(chunk);
            chunk = new Chunk("法人或负责人：\n\n");
            chunk.setFont(fontContent);
            phrase.add(chunk);
            cell = new PdfPCell(phrase);
            cell.setBorderWidthBottom(0);
            cell.setPadding(8);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            cell.setColspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("  年  月  日", fontContent);
            cell = new PdfPCell(phrase);
            cell.setBorderWidthTop(0);
            cell.setPadding(8);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_RIGHT);
            cell.setRowspan(2);
            cell.setColspan(2);
            table.addCell(cell);

            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("  年  月  日(章)", fontContent);
            cell = new PdfPCell(phrase);
            cell.setBorderWidthTop(0);
            cell.setPadding(8);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_RIGHT);
            cell.setRowspan(2);
            cell.setColspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            document.add(table);
            if (index == 3 || index < arrIllustration.length - 1) {
                document.newPage();
                // 加入表格布局
                table = new PdfPTable(1);
                table.setWidthPercentage(95);
                String checkContent = "";
                String note = "";
                notes = new StringBuilder();
//                try {
//                    checkContent = notesJSONArray.getJSONObject(index).getString("checkContent");
//                    note = notesJSONArray.getJSONObject(index).getString("note");
//                    String str = checkContent + (TextUtils.isEmpty(note) ? note : ":" + note);
//                    String str = String.format("检查%1$s时发现:%2$s", checkContent, note);
                String str = arrIllustration[index];
                if (currentLength * lineByte < str.length()) {
                    notes.append(str.substring(currentLength * lineByte, str.length()));
                    notes.append("\n");
                }
                for (int j = index + 1; j < arrIllustration.length; j++) {
//                        checkContent = notesJSONArray.getJSONObject(j).getString("checkContent");
//                        note = notesJSONArray.getJSONObject(j).getString("note");
//                        str = checkContent + (TextUtils.isEmpty(note) ? note : ":" + note);
//                        String.format("检查%1$s时发现:%2$s", checkContent, note);
                    str = arrIllustration[j];
                    notes.append(str);
//                        if (j != notesJSONArray.length()) {
                    notes.append("\n");
//                        }
                }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                notes.append("处理意见：");
                phrase = new Phrase(String.format("说明（附页）：\n%s", notes.toString()), fontContent);
                phrase.setLeading(24);
                cell = new PdfPCell();
                cell.addElement(phrase);
                cell.setFixedHeight(720f);
                cell.setPadding(8);
//                cell.setMinimumHeight(25f);
                cell.setUseAscender(true);
                table.addCell(cell);
                document.add(table);
            }

            // ////////////////////////////////////////////////////////////////
            document.close();
            handler.sendEmptyMessage(0);
        } catch (Exception e) {
            handler.sendEmptyMessage(1);
            document.close();
            e.printStackTrace();
        }
    }

    public void createChecksPDF() {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(PDF_PATH));
            document.open();
            Font baseFont = new Font(BaseFont.createFont("assets/MSYYY.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
            Font font = new Font(baseFont);
            font.setSize(20);
            font.setStyle(Font.BOLD);
            // ////////////////////////////////////////////////////////////////
            String name = "";
            if ("H".equals(planType))
                name = "保健";
            Paragraph pg = new Paragraph(String.format("%s食品生产日常监督检查要点表", name), font);
            pg.setSpacingAfter(10);
            pg.setAlignment(1);
            document.add(pg);
            font.setSize(14);
            font.setStyle(Font.NORMAL);
            Font fontLab = new Font(baseFont);
            fontLab.setSize(12);
            fontLab.setStyle(Font.BOLD);
            Font fontContent = new Font(baseFont);
            fontContent.setSize(12);
            fontContent.setStyle(Font.NORMAL);
            pg = new Paragraph(String.format("  " + name + "食品通用检查项目：重点项( * )" + count3 + " 项，一般项 " + count5 + " 项，共 " + count2 + " 项。"), font);
            pg.setSpacingAfter(10);
            document.add(pg);
//            pg = new Paragraph(String.format("  食品添加剂通用检查项目：重点项（ * ）%1$d 项，一般项 %2$d 项，共 %3$d 项。\n\n", 19, 31, 50), font);
//            document.add(pg);
            // 加入表格布局
            PdfPTable table = new PdfPTable(new float[]{8, 5, 25, 4, 8});
            table.setWidthPercentage(95);
            Phrase phrase;
            PdfPCell cell;
            Chunk chunk;
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("检查项目", fontContent);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("项目序号", fontContent);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("检查内容", fontContent);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("评价", fontContent);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
            // ////////////////////////////////////////////////////////////////
            phrase = new Phrase("备注", fontContent);
            cell = new PdfPCell(phrase);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(cell.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);

            for (int i = 0; i < groupJSONArray.length(); i++) {
                String groupContent = "";
                JSONArray array = new JSONArray();
                try {
                    groupContent = groupJSONArray.getString(i);
                    array = childJSONArray.getJSONArray(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                phrase = new Phrase(groupContent, fontContent);
                cell = new PdfPCell(phrase);
                cell.setBorder(Rectangle.BOX);
                cell.setMinimumHeight(25f);
                cell.setUseAscender(true);
                cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                cell.setRowspan(array.length() * 2);
                table.addCell(cell);
                for (int j = 0; j < array.length(); j++) {
                    JSONObject object = new JSONObject();
                    String childContent = "";
                    String isEdit = "";
                    String isPass = "";
                    String remark = "";
                    try {
                        object = array.getJSONObject(j);
                        childContent = object.getString("content");
                        isEdit = object.getString("isEdit");
                        isPass = object.getString("isPass");
                        remark = object.getString("remark");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String itemNum = "";
                    try {
                        itemNum = object.getString("itemCode");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    phrase = new Phrase(itemNum, fontContent);
                    cell = new PdfPCell(phrase);
                    cell.setMinimumHeight(25f);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    cell.setRowspan(2);
                    table.addCell(cell);
                    phrase = new Phrase(childContent, fontContent);
                    cell = new PdfPCell(phrase);
                    cell.setPadding(8);
                    cell.setMinimumHeight(25f);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setRowspan(2);
                    table.addCell(cell);
                    if ("1".equals(isEdit)) {
                        if ("1".equals(isPass)) {
                            phrase = new Phrase("√是 □否", fontContent);
                        } else {
                            phrase = new Phrase("□是 √否", fontContent);
                        }
                    } else {
                        phrase = new Phrase("□是 □否", fontContent);
                    }
                    cell = new PdfPCell(phrase);
                    cell.setMinimumHeight(25f);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    cell.setRowspan(2);
                    table.addCell(cell);
                    phrase = new Phrase(remark, fontContent);
                    cell = new PdfPCell(phrase);
                    cell.setMinimumHeight(25f);
                    cell.setUseAscender(true);
                    cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(cell.ALIGN_CENTER);
                    cell.setRowspan(2);
                    table.addCell(cell);
                }
            }
            phrase = new Phrase("其他需要记录的问题：\n\n\n\n\n", fontContent);
            cell = new PdfPCell(phrase);
            cell.setPadding(8);
            cell.setMinimumHeight(25f);
            cell.setUseAscender(true);
            cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
            cell.setRowspan(2);
            cell.setColspan(5);
            table.addCell(cell);
            document.add(table);
            // ////////////////////////////////////////////////////////////////
//            pg = new Paragraph("说明：1.上表中打*号的为重点项，其他为一般项。\n" +
//                    "2.每次检查重点项不应少于10项。\n 3.以抽查形式检查的项目等，在备注栏中" +
//                    "要填写必要的检查记录信息，评价仅针对本次抽查内容。", font);
//            document.add(pg);
            // ////////////////////////////////////////////////////////////////
            document.close();
            handler.sendEmptyMessage(0);
        } catch (Exception e) {
            document.close();
            e.printStackTrace();
            handler.sendEmptyMessage(1);
        }
    }

    public class BorderEvent implements PdfPTableEvent {
        public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
            float width[] = widths[0];
            float x1 = width[0];
            float x2 = width[width.length - 1];
            float y1 = heights[0];
            float y2 = heights[heights.length - 1];
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.rectangle(x1, y1, x2 - x1, y2 - y1);
            cb.stroke();
            cb.resetRGBColorStroke();
        }
    }

    public void uploadGaoZhiData() {
        HashMap<String, String> params = new HashMap<>();

        params.put("planId", planId);
        params.put("id", id);
        params.put("personName", personNameGaozhi);
        params.put("certNo", personNoGaozhi);
        params.put("personNameTwo", certNameGaozhi);
        params.put("certNoTwo", certNoGaozhi);
        params.put("checkDate", checkDateGaozhi);
        params.put("checkAddress", checkAddressGaozhi);

        Call<ResponseBody> call = Retrofit2Helper.getInstance().getInformPage(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void uploadRecordData() {
        HashMap<String, String> params = new HashMap<>();

        params.put("planId", planId);
        params.put("id", id);
        params.put("legalPerson", legalPersonRecord);
        params.put("phoneNo", phoneNoRecord);
        params.put("checkNo", NoRecord);
        params.put("remark", illustration);
        params.put("opinion", suggestion);

        Call<ResponseBody> call = Retrofit2Helper.getInstance().getResultRecord(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private String handleCertNo() {
        if (CheckInfoFragment.ztInfo != null) {
            ArrayList<ZtXkzBean> arrayList = CheckInfoFragment.ztInfo.getCertificateInfos();
            if (arrayList != null) {
                if (arrayList.size() != 0) {
                    for (int i = 0; i < arrayList.size() - 1; i++) {
                        Date date1 = DateUtil.formate4(arrayList.get(i).getExpireDate());
                        Date date2 = DateUtil.formate4(arrayList.get(i + 1).getExpireDate());
                        ZtXkzBean temp = arrayList.get(i);
                        if (date1.before(date2)) {
                            arrayList.set(i, arrayList.get(i + 1));
                            arrayList.set(i + 1, temp);
                        }
                    }
                    return arrayList.get(0).getCertNo();
                }
            }
        }
        return "";
    }

}
