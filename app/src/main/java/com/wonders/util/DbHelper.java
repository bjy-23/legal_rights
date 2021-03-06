package com.wonders.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wonders.bean.UserBean;
import com.wonders.application.AppData;
import com.wonders.constant.DbConstants;
import com.wonders.bean.DbLogin;
import com.wonders.bean.Db_message;
import com.wonders.bean.EnterpriseBean;
import com.wonders.bean.PicBean;
import com.wonders.bean.PicSelectBean;
import com.wonders.bean.SopListViewBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getName();
    private int invokeNum;
    private Context mcontext;
    private AppData appData;
    private String organId;
    private static DbHelper dbHelper;

    public DbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mcontext = context;
        appData = AppData.getInstance();

        SharedPreferences sp = mcontext.getSharedPreferences("config", Context.MODE_PRIVATE);
        if (appData.getLoginBean() != null) {
            organId = sp.getString(appData.getLoginBean().getUserName(), "123");
        }
    }

    public static DbHelper getInstance() {
        if (dbHelper == null) {
            synchronized (DbHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DbHelper(AppData.getInstance(), DbConstants.TABLENAME, null, 1);
                }
            }
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //图片缓存传输列表
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(DbConstants.CREAT_TABLE);
        strBuf.append("contact(");
        strBuf.append(DbConstants.PRIMARY_KEY + ", ");
        strBuf.append("path VARCHAR(200) NOT NULL, ");
        strBuf.append("picName VARCHAR(200) NOT NULL, ");
        strBuf.append("picNum " + DbConstants.INTEGER + ",");
        strBuf.append("isProduct INTEGER, ");
        strBuf.append("isFinish INTEGER, ");
        strBuf.append("picState VARCHAR(20) ");
        strBuf.append(");");

        db.execSQL(strBuf.toString());

        //用户名密码 列表
        StringBuffer strBuf2 = new StringBuffer();
        strBuf2.append(DbConstants.CREAT_TABLE);
        strBuf2.append("userInfoTable(");
        strBuf2.append(DbConstants.PRIMARY_KEY);
        strBuf2.append(",loginName VARCHAR(1000)");
        strBuf2.append(",password VARCHAR(1000)");
        strBuf2.append(",userName VARCHAR(1000)");
        strBuf2.append(",userId VARCHAR(1000)");
        strBuf2.append(",deptName VARCHAR(1000)");
        strBuf2.append(");");

        db.execSQL(strBuf2.toString());


        //代办事项列表
        StringBuffer strBuf3 = new StringBuffer();
        strBuf3.append(DbConstants.CREAT_TABLE);
        strBuf3.append("Db_message_table(");
        strBuf3.append("planId VARCHAR(100) PRIMARY KEY,");
        strBuf3.append("userId VARCHAR(100),");
        strBuf3.append("flag INTEGER,");
        strBuf3.append("get_etpCheckInfo VARCHAR(10000),");
        strBuf3.append("get_superviseRecord VARCHAR(10000),");
        strBuf3.append("get_planCheckContent VARCHAR(10000),");
        strBuf3.append("get_planCheckContentDetail VARCHAR(10000),");
        strBuf3.append("isLt VARCHAR(20),");
        strBuf3.append("address VARCHAR(2000),");
        strBuf3.append("etpsName VARCHAR(2000),");
        strBuf3.append("planMonth VARCHAR(2000),");
        strBuf3.append("allUserName VARCHAR(2000),");
        strBuf3.append("etpsId VARCHAR(2000),");
        strBuf3.append("type VARCHAR(20),");
        strBuf3.append("isFinish VARCHAR(20),");
        strBuf3.append("get_fpsiCertInfo VARCHAR(10000),");
        strBuf3.append("get_fpsiEtpsInfo VARCHAR(10000),");
        strBuf3.append("get_fpsiInspPlan VARCHAR(10000)");
        strBuf3.append(");");

        db.execSQL(strBuf3.toString());


        StringBuffer strBuf4 = new StringBuffer();
        strBuf4.append(DbConstants.CREAT_TABLE);
        strBuf4.append("SopListViewBeanTable(");
        strBuf4.append(DbConstants.PRIMARY_KEY2);
        strBuf4.append(",id VARCHAR(1000)");
        strBuf4.append(",planId VARCHAR(1000)");
        strBuf4.append(",etpsId VARCHAR(1000)");
        strBuf4.append(",etpsName VARCHAR(1000)");
        strBuf4.append(",userId VARCHAR(100)");
        strBuf4.append(",year VARCHAR(1000)");
        strBuf4.append(",month VARCHAR(1000)");
        strBuf4.append(",firstDate VARCHAR(100)");
        strBuf4.append(",secondDate VARCHAR(100)");
        strBuf4.append(",itemCode VARCHAR(1000)");
        strBuf4.append(",parentCode VARCHAR(1000)");
        strBuf4.append(",checkCode VARCHAR(1000)");
        strBuf4.append(",isKey VARCHAR(1000)");
        strBuf4.append(",content VARCHAR(1000)");
        strBuf4.append(",remark VARCHAR(1000)");
        strBuf4.append(",isEdit VARCHAR(1000)");
        strBuf4.append(",isPass VARCHAR(1000)");
        strBuf4.append(",isHavePic VARCHAR(100)");
        strBuf4.append(",kind INTEGER");
        strBuf4.append(",pic VARCHAR(10000)");
        strBuf4.append(",longitude INTEGER");
        strBuf4.append(",latitude INTEGER");
        strBuf4.append(",address INTEGER");
        strBuf4.append(",planType INTEGER");
        strBuf4.append(");");
        db.execSQL(strBuf4.toString());

        StringBuffer strBuf5 = new StringBuffer();
        strBuf5.append(DbConstants.CREAT_TABLE);
        strBuf5.append("EnterpriseBean_table(");
        strBuf5.append(DbConstants.PRIMARY_KEY + ",");
        strBuf5.append("address VARCHAR(100),");
        strBuf5.append("etpsId VARCHAR(100),");
        strBuf5.append("etpsType VARCHAR(100),");
        strBuf5.append("etpsName VARCHAR(100),");
        strBuf5.append("exeOrgan VARCHAR(100),");
        strBuf5.append("frequency VARCHAR(100),");
        strBuf5.append("etpsInfo VARCHAR(10000),");
        strBuf5.append("recordInfo VARCHAR(1000),");
        strBuf5.append("licNo VARCHAR(1000),");
        strBuf5.append("grade VARCHAR(100)");
        strBuf5.append(");");
        db.execSQL(strBuf5.toString());

        StringBuffer strBuf6 = new StringBuffer();
        strBuf6.append(DbConstants.CREAT_TABLE);
        strBuf6.append("picTable(");
        strBuf6.append(DbConstants.PRIMARY_KEY);
        strBuf6.append(",picName VARCHAR(1000)");
        strBuf6.append(",picPath VARCHAR(1000)");
        strBuf6.append(",picNum " + DbConstants.INTEGER);
        strBuf6.append(",planId VARCHAR(1000)");
        strBuf6.append(",userId VARCHAR(1000)");
        strBuf6.append(",itemCode VARCHAR(1000)");
        strBuf6.append(",checkContent VARCHAR(1000)");
        strBuf6.append(",picSource VARCHAR(10000)");
        strBuf6.append(",type " + DbConstants.INTEGER);//0,网络图片，1，本地图片
        strBuf6.append(");");
        db.execSQL(strBuf6.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
    * 重写以下三个函数，帮助在多线程环境下关闭数据库
    * */
    @Override
    public SQLiteDatabase getReadableDatabase() {
        invokeNum++;
        return super.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        invokeNum++;
        return super.getWritableDatabase();
    }

    @Override
    public synchronized void close() {
        if (invokeNum-- == 0) {
            Log.e(TAG, "database:  close!!!");
            super.close();
        }
    }

    public void insertUserInfo(UserBean userBean) {
        SQLiteDatabase sdb = getWritableDatabase();
        String sql = "insert into userInfoTable(loginName,password,userName,userId,deptName) values(?,?,?,?,?)";
        sdb.execSQL(sql, new Object[]{
                userBean.getLoginName(), userBean.getPassword(), userBean.getUserName(), userBean.getUserId(), userBean.getDeptName()
        });
        close();
    }

    public UserBean queryUserInfo(String loginName) {
        SQLiteDatabase sdb = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = sdb.rawQuery("select * from userInfoTable where loginName = ?", new String[]{loginName});
            UserBean userBean = new UserBean();
            while (cursor.moveToNext()) {
                userBean.setDeptName(cursor.getString(cursor.getColumnIndex("deptName")));
                userBean.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                userBean.setLoginName(cursor.getString(cursor.getColumnIndex("loginName")));
                userBean.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
                userBean.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            }
            close();
            return userBean;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void deleteUserInfo() {
        SQLiteDatabase sdb = getWritableDatabase();
        String sql = "delete from userInfoTable";
        sdb.execSQL(sql);
        close();
    }


    public void insert_Db_message(Db_message db_message) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("insert into Db_message_table (planId,userId,flag,get_etpCheckInfo,get_superviseRecord,get_planCheckContent," +
                "get_planCheckContentDetail,isLt,address,etpsName,planMonth,allUserName,etpsId,type,isFinish,get_fpsiCertInfo,get_fpsiEtpsInfo,get_fpsiInspPlan) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
        db.execSQL(
                strBuf.toString(),
                new Object[]{db_message.getPlanId(), db_message.getUserId(), db_message.getFlag(), db_message.getGet_etpCheckInfo(),
                        db_message.getGet_superviseRecord(), db_message.getGet_planCheckContent(), db_message.getGet_planCheckContentDetail(), db_message.getIsLt(), db_message.getAddress(), db_message.getEtpsName(), db_message.getPlanMonth(), db_message.getAllUserName(), db_message.getEtpsId(), db_message.getType(), db_message.getIsFinish(), db_message.getGet_fpsiCertInfo(), db_message.getGet_fpsiEtpsInfo(), db_message.getGet_fpsiInspPlan()
                });
        close();
    }

    public Db_message query_Db_message(String planId) {
        Db_message db_message = new Db_message();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(
                    "select * from Db_message_table where planId = ? ", new String[]{planId + ""});
            while (cursor.moveToNext()) {
                db_message.setPlanId(cursor.getString(0));
                db_message.setUserId(cursor.getString(1));
                db_message.setFlag(cursor.getInt(2));
                db_message.setGet_etpCheckInfo(cursor.getString(3));
                db_message.setGet_superviseRecord(cursor.getString(4));
                db_message.setGet_planCheckContent(cursor.getString(5));
                db_message.setGet_planCheckContentDetail(cursor.getString(6));
                db_message.setIsLt(cursor.getString(7));
                db_message.setAddress(cursor.getString(8));
                db_message.setEtpsName(cursor.getString(9));
                db_message.setPlanMonth(cursor.getString(10));
                db_message.setAllUserName(cursor.getString(11));
                db_message.setEtpsId(cursor.getString(12));
                db_message.setType(cursor.getString(13));
                db_message.setIsFinish(cursor.getString(14));
                db_message.setGet_fpsiCertInfo(cursor.getString(15));
                db_message.setGet_fpsiEtpsInfo(cursor.getString(16));
                db_message.setGet_fpsiInspPlan(cursor.getString(17));
            }
            close();
            return db_message;
        } finally {
            if (cursor != null)
                cursor.close();
        }

    }

    /**
     * 取出列表
     *
     * @return
     */
    public ArrayList<Db_message> queryDbMessageAll(String lt, String userId) {
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery(
                    "select * from Db_message_table where isLt = ? and userId = ? and isFinish = ?", new String[]{lt, userId, "0"});

            ArrayList<Db_message> list = new ArrayList<Db_message>();

            while (cursor.moveToNext()) {
                Db_message db_message = new Db_message();

                db_message.setPlanId(cursor.getString(0));
                db_message.setUserId(cursor.getString(1));
                db_message.setFlag(cursor.getInt(2));
                db_message.setGet_etpCheckInfo(cursor.getString(3));
                db_message.setGet_superviseRecord(cursor.getString(4));
                db_message.setGet_planCheckContent(cursor.getString(5));
                db_message.setGet_planCheckContentDetail(cursor.getString(6));
                db_message.setIsLt(cursor.getString(7));
                db_message.setAddress(cursor.getString(8));
                db_message.setEtpsName(cursor.getString(9));
                db_message.setPlanMonth(cursor.getString(10));
                db_message.setAllUserName(cursor.getString(11));
                db_message.setEtpsId(cursor.getString(12));
                db_message.setType(cursor.getString(13));
                db_message.setIsFinish(cursor.getString(14));
                db_message.setGet_fpsiCertInfo(cursor.getString(15));
                db_message.setGet_fpsiEtpsInfo(cursor.getString(16));
                db_message.setGet_fpsiInspPlan(cursor.getString(17));

                list.add(db_message);
            }
            close();
            return list;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void insertSopListViewBean(SopListViewBean bean) {
        Gson gson = new Gson();
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("insert into SopListViewBeanTable (id,planId,etpsId,userId,year,month" +
                ",firstDate,secondDate,itemCode,parentCode,checkCode,isKey,content,remark" +
                ",isEdit,isPass,isHavePic,kind,pic,longitude,latitude,address,planType,etpsName)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
        db.execSQL(
                strBuf.toString(),
                new Object[]{bean.getId(), bean.getPlanId(), bean.getEtpsId(), bean.getUserId()
                        , bean.getYear(), bean.getMonth(), bean.getFirstDate(), bean.getSecondDate()
                        , bean.getItemCode(), bean.getParentCode(), bean.getCheckCode(), bean.getIsKey()
                        , bean.getContent(), bean.getRemark(), bean.getIsEdit(), bean.getIsPass()
                        , bean.getIsHavePic(), bean.getKind(), gson.toJson(bean.getPic()), bean.getLongitude()
                        , bean.getLatitude(), bean.getAddress(), bean.getPlanType(), bean.getEtpsName()});
        close();
    }

    /**
     * 完成了某条代办
     *
     * @param planId
     * @param itemCode
     */
    public void deleteSop(String userId, String planId, String itemCode, String content) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from SopListViewBeanTable where userId =? and planId=? and itemCode = ? and content =?", new Object[]{
                userId, planId, itemCode, content});
        close();
    }

    /**
     * 根据 itemcode 和 content 找到某条代办
     */
    public SopListViewBean querySop(String itemCode, String content, String planId, String userId) {
        Gson gson = new Gson();
        SopListViewBean sopListViewBean = new SopListViewBean();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(
                    "select * from SopListViewBeanTable where itemCode = ? and content =? and planId = ? and userId = ? ", new String[]{itemCode, content, planId, userId});
            while (cursor.moveToNext()) {
                sopListViewBean.setId(cursor.getString(cursor.getColumnIndex("id")));
                sopListViewBean.setPlanId(cursor.getString(cursor.getColumnIndex("planId")));
                sopListViewBean.setEtpsId(cursor.getString(cursor.getColumnIndex("etpsId")));
                sopListViewBean.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                sopListViewBean.setYear(cursor.getString(cursor.getColumnIndex("year")));
                sopListViewBean.setMonth(cursor.getString(cursor.getColumnIndex("month")));
                sopListViewBean.setFirstDate(cursor.getString(cursor.getColumnIndex("firstDate")));
                sopListViewBean.setSecondDate(cursor.getString(cursor.getColumnIndex("secondDate")));
                sopListViewBean.setItemCode(cursor.getString(cursor.getColumnIndex("itemCode")));
                sopListViewBean.setParentCode(cursor.getString(cursor.getColumnIndex("parentCode")));
                sopListViewBean.setCheckCode(cursor.getString(cursor.getColumnIndex("checkCode")));
                sopListViewBean.setIsKey(cursor.getString(cursor.getColumnIndex("isKey")));
                sopListViewBean.setContent(cursor.getString(cursor.getColumnIndex("content")));
                sopListViewBean.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                sopListViewBean.setIsEdit(cursor.getString(cursor.getColumnIndex("isEdit")));
                sopListViewBean.setIsPass(cursor.getString(cursor.getColumnIndex("isPass")));
                sopListViewBean.setIsHavePic(cursor.getString(cursor.getColumnIndex("isHavePic")));
                sopListViewBean.setKind(cursor.getInt(cursor.getColumnIndex("kind")));
                sopListViewBean.setPic((ArrayList<PicBean>) gson.fromJson(cursor.getString(cursor.getColumnIndex("pic")), new TypeToken<ArrayList<PicBean>>() {
                }.getType()));
                sopListViewBean.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                sopListViewBean.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                sopListViewBean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                sopListViewBean.setPlanType(cursor.getInt(cursor.getColumnIndex("planType")));
            }
            close();
            return sopListViewBean;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public ArrayList<SopListViewBean> querySops(String userId, String planId) {
        Gson gson = new Gson();
        ArrayList<SopListViewBean> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "select * from SopListViewBeanTable where userId = ? and planId = ?", new String[]{userId, planId});
            while (cursor.moveToNext()) {
                SopListViewBean sopListViewBean = new SopListViewBean();
                sopListViewBean.setId(cursor.getString(cursor.getColumnIndex("id")));
                sopListViewBean.setPlanId(cursor.getString(cursor.getColumnIndex("planId")));
                sopListViewBean.setEtpsId(cursor.getString(cursor.getColumnIndex("etpsId")));
                sopListViewBean.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                sopListViewBean.setYear(cursor.getString(cursor.getColumnIndex("year")));
                sopListViewBean.setMonth(cursor.getString(cursor.getColumnIndex("month")));
                sopListViewBean.setFirstDate(cursor.getString(cursor.getColumnIndex("firstDate")));
                sopListViewBean.setSecondDate(cursor.getString(cursor.getColumnIndex("secondDate")));
                sopListViewBean.setItemCode(cursor.getString(cursor.getColumnIndex("itemCode")));
                sopListViewBean.setParentCode(cursor.getString(cursor.getColumnIndex("parentCode")));
                sopListViewBean.setCheckCode(cursor.getString(cursor.getColumnIndex("checkCode")));
                sopListViewBean.setIsKey(cursor.getString(cursor.getColumnIndex("isKey")));
                sopListViewBean.setContent(cursor.getString(cursor.getColumnIndex("content")));
                sopListViewBean.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                sopListViewBean.setIsEdit(cursor.getString(cursor.getColumnIndex("isEdit")));
                sopListViewBean.setIsPass(cursor.getString(cursor.getColumnIndex("isPass")));
                sopListViewBean.setIsHavePic(cursor.getString(cursor.getColumnIndex("isHavePic")));
                sopListViewBean.setKind(cursor.getInt(cursor.getColumnIndex("kind")));
                sopListViewBean.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                sopListViewBean.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                sopListViewBean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                sopListViewBean.setPlanType(cursor.getInt(cursor.getColumnIndex("planType")));
                sopListViewBean.setPic((ArrayList<PicBean>) gson.fromJson(cursor.getString(cursor.getColumnIndex("pic")), new TypeToken<ArrayList<PicBean>>() {
                }.getType()));
                ;
                list.add(sopListViewBean);
            }
            close();
            return list;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public ArrayList<SopListViewBean> querySops(String userId) {
        ArrayList<SopListViewBean> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "select * from SopListViewBeanTable where userId = ? ", new String[]{userId});
            while (cursor.moveToNext()) {
                SopListViewBean sopListViewBean = new SopListViewBean();

                sopListViewBean.setId(cursor.getString(cursor.getColumnIndex("id")));
                sopListViewBean.setPlanId(cursor.getString(cursor.getColumnIndex("planId")));
                sopListViewBean.setEtpsId(cursor.getString(cursor.getColumnIndex("etpsId")));
                sopListViewBean.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                sopListViewBean.setYear(cursor.getString(cursor.getColumnIndex("year")));
                sopListViewBean.setMonth(cursor.getString(cursor.getColumnIndex("month")));
                sopListViewBean.setFirstDate(cursor.getString(cursor.getColumnIndex("firstDate")));
                sopListViewBean.setSecondDate(cursor.getString(cursor.getColumnIndex("secondDate")));
                sopListViewBean.setItemCode(cursor.getString(cursor.getColumnIndex("itemCode")));
                sopListViewBean.setParentCode(cursor.getString(cursor.getColumnIndex("parentCode")));
                sopListViewBean.setCheckCode(cursor.getString(cursor.getColumnIndex("checkCode")));
                sopListViewBean.setIsKey(cursor.getString(cursor.getColumnIndex("isKey")));
                sopListViewBean.setContent(cursor.getString(cursor.getColumnIndex("content")));
                sopListViewBean.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                sopListViewBean.setIsEdit(cursor.getString(cursor.getColumnIndex("isEdit")));
                sopListViewBean.setIsPass(cursor.getString(cursor.getColumnIndex("isPass")));
                sopListViewBean.setIsHavePic(cursor.getString(cursor.getColumnIndex("isHavePic")));
                sopListViewBean.setKind(cursor.getInt(cursor.getColumnIndex("kind")));
//            String s = cursor.getString(cursor.getColumnIndex("pic"));
//            ArrayList<PicBean> pics = (ArrayList<PicBean>) gson.fromJson(cursor.getString(cursor.getColumnIndex("pic")),new TypeToken<ArrayList<PicBean>>(){}.getType());
//            sopListViewBean.setPic((ArrayList<PicBean>) gson.fromJson(cursor.getString(cursor.getColumnIndex("pic")),new TypeToken<ArrayList<PicBean>>(){}.getType()));
                sopListViewBean.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                sopListViewBean.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                sopListViewBean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                sopListViewBean.setPlanType(cursor.getInt(cursor.getColumnIndex("planType")));
                list.add(sopListViewBean);
            }
            close();
            return list;
        } finally {
            if (cursor != null)
                cursor.close();
        }

    }

    /**
     * 修改某条代办
     */
    public void updateSop(String itemCode, String content, String planId, String userId, SopListViewBean bean) {
        Gson gson = new Gson();
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("update SopListViewBeanTable set isPass=?,secondDate=?,remark=?,pic=? where planId=? and userId = ? and itemCode = ? and content = ?", new Object[]{
                bean.getIsPass(), bean.getSecondDate(), bean.getRemark(), gson.toJson(bean.getPic()), planId, userId, itemCode, content});

        close();
    }

    public void insertEnterpriseBean(EnterpriseBean enterpriseBean) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("insert into EnterpriseBean_table (address,etpsId,etpsType,etpsName,frequency" +
                ",grade,etpsInfo,recordInfo,licNo,exeOrgan) values(?,?,?,?,?,?,?,?,?,?);");
        db.execSQL(
                strBuf.toString(),
                new Object[]{enterpriseBean.getAddress(), enterpriseBean.getEtpsId(), enterpriseBean.getEtpsType(),
                        enterpriseBean.getEtpsName(), enterpriseBean.getFrequency(), enterpriseBean.getGrade(),
                        enterpriseBean.getEtpsInfo(), enterpriseBean.getRecordInfo(), enterpriseBean.getLicNo(),
                        enterpriseBean.getExeOrgan()});
        close();
    }

    public ArrayList<EnterpriseBean> queryEnterpriseBean(EnterpriseBean bean) {
        ArrayList<EnterpriseBean> list = new ArrayList<EnterpriseBean>();
        Cursor cursor = null;
        try {
            if (bean.getEtpsType().equals("sc")) {
                SQLiteDatabase db = getReadableDatabase();
                if (bean.getEtpsName().equals("") && bean.getAddress().equals("") & bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from EnterpriseBean_table where etpsType = ?", new String[]{bean.getEtpsType()});
                } else if (!bean.getEtpsName().equals("") && bean.getAddress().equals("") & bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from EnterpriseBean_table where etpsName like ? and etpsType = ?", new String[]{"%" + bean.getEtpsName() + "%", bean.getEtpsType()});
                } else if (bean.getEtpsName().equals("") && !bean.getAddress().equals("") & bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from EnterpriseBean_table where address like ? and etpsType = ?", new String[]{"%" + bean.getAddress() + "%", bean.getEtpsType()});
                } else if (bean.getEtpsName().equals("") && bean.getAddress().equals("") & !bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from EnterpriseBean_table where grade = ?and etpsType = ?", new String[]{bean.getGrade(), bean.getEtpsType()});
                } else if (!bean.getEtpsName().equals("") && !bean.getAddress().equals("") & bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from EnterpriseBean_table where etpsName like ? and address like? and etpsType = ?", new String[]{"%" + bean.getEtpsName() + "%", "%" + bean.getAddress() + "%", bean.getEtpsType()});
                } else if (!bean.getEtpsName().equals("") && bean.getAddress().equals("") & !bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from EnterpriseBean_table where etpsName like ? and grade = ? and etpsType = ?", new String[]{"%" + bean.getEtpsName() + "%", bean.getGrade(), bean.getEtpsType()});
                } else if (bean.getEtpsName().equals("") && !bean.getAddress().equals("") & !bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from EnterpriseBean_table where address like ? and grade = ? and etpsType = ?", new String[]{"%" + bean.getAddress() + "%", bean.getGrade(), bean.getEtpsType()});
                } else {
                    cursor = db.rawQuery("select * from EnterpriseBean_table where etpsName like ? and address like ? and grade = ? and etpsType = ?", new String[]{"%" + bean.getEtpsName() + "%", "%" + bean.getAddress() + "%", bean.getGrade(), bean.getEtpsType()});
                }

                while (cursor.moveToNext()) {
                    EnterpriseBean enterpriseBean = new EnterpriseBean();

                    enterpriseBean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    enterpriseBean.setEtpsId(cursor.getString(cursor.getColumnIndex("etpsId")));
                    enterpriseBean.setEtpsName(cursor.getString(cursor.getColumnIndex("etpsName")));
                    enterpriseBean.setFrequency(cursor.getString(cursor.getColumnIndex("frequency")));
                    enterpriseBean.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
                    enterpriseBean.setEtpsInfo(cursor.getString(cursor.getColumnIndex("etpsInfo")));
                    enterpriseBean.setRecordInfo(cursor.getString(cursor.getColumnIndex("recordInfo")));
                    enterpriseBean.setEtpsType(cursor.getString(cursor.getColumnIndex("etpsType")));
                    enterpriseBean.setLicNo(cursor.getString(cursor.getColumnIndex("licNo")));
                    enterpriseBean.setExeOrgan(cursor.getString(cursor.getColumnIndex("exeOrgan")));

                    list.add(enterpriseBean);
                }
                close();
            } else {
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().
                        getAbsolutePath() + "/生产流通移动执法/" + organId + ".db", null);
                if (bean.getLicNo().equals("") && !bean.getEtpsName().equals("") && bean.getAddress().equals("") && bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where etpsName like ?  ", new String[]{"%" + bean.getEtpsName() + "%"});
                } else if (bean.getLicNo().equals("") && !bean.getEtpsName().equals("") && !bean.getAddress().equals("") && bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where etpsName like ? and address like? ", new String[]{"%" + bean.getEtpsName() + "%", "%" + bean.getAddress() + "%"});
                } else if (bean.getLicNo().equals("") && !bean.getEtpsName().equals("") && bean.getAddress().equals("") && !bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where etpsName like ? and grade = ? ", new String[]{"%" + bean.getEtpsName() + "%", bean.getGrade()});
                } else if (bean.getLicNo().equals("") && !bean.getEtpsName().equals("") && !bean.getAddress().equals("") && !bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where etpsName like ? and address like ? and grade = ? ", new String[]{"%" + bean.getEtpsName() + "%", "%" + bean.getAddress() + "%", bean.getGrade()});
                } else if (!bean.getLicNo().equals("") && bean.getEtpsName().equals("") && bean.getAddress().equals("") && bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where licNo like ? ", new String[]{"%" + bean.getLicNo() + "%"});
                } else if (!bean.getLicNo().equals("") && bean.getEtpsName().equals("") && !bean.getAddress().equals("") && bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where licNo like ? and address like ? ", new String[]{"%" + bean.getLicNo() + "%", "%" + bean.getAddress() + "%"});
                } else if (!bean.getLicNo().equals("") && bean.getEtpsName().equals("") && bean.getAddress().equals("") && !bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where licNo like ? and grade = ? ", new String[]{"%" + bean.getLicNo() + "%", bean.getGrade()});
                } else if (!bean.getLicNo().equals("") && bean.getEtpsName().equals("") && !bean.getAddress().equals("") && !bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where licNo like ? and address like ? and grade = ? ", new String[]{"%" + bean.getLicNo() + "%", "%" + bean.getAddress() + "%", bean.getGrade()});
                } else if (!bean.getLicNo().equals("") && !bean.getEtpsName().equals("") && bean.getAddress().equals("") && bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where licNo like ? and etpsName like ?  ", new String[]{"%" + bean.getLicNo() + "%", "%" + bean.getEtpsName() + "%"});
                } else if (!bean.getLicNo().equals("") && !bean.getEtpsName().equals("") && !bean.getAddress().equals("") && bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where licNo like ? and etpsName like ? and address like ? ", new String[]{"%" + bean.getLicNo() + "%", "%" + bean.getEtpsName() + "%", "%" + bean.getAddress() + "%"});
                } else if (!bean.getLicNo().equals("") && !bean.getEtpsName().equals("") && bean.getAddress().equals("") && !bean.getGrade().equals("")) {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where licNo like ? and etpsName like ? and grade = ? ", new String[]{"%" + bean.getLicNo() + "%", "%" + bean.getEtpsName() + "%", bean.getGrade()});
                } else {
                    cursor = db.rawQuery("select * from lt_dataInfo_offline where licNo like ? and etpsName like ?and address like ? and grade = ? ", new String[]{"%" + bean.getLicNo() + "%", "%" + bean.getEtpsName() + "%", "%" + bean.getAddress() + "%", bean.getGrade()});
                }

                while (cursor.moveToNext()) {
                    EnterpriseBean enterpriseBean = new EnterpriseBean();

                    enterpriseBean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    enterpriseBean.setEtpsId(cursor.getString(cursor.getColumnIndex("etpsId")));
                    enterpriseBean.setEtpsName(cursor.getString(cursor.getColumnIndex("etpsName")));
                    if (cursor.getString(cursor.getColumnIndex("frequency")).equals("null")) {
                        enterpriseBean.setFrequency("");
                    } else {
                        enterpriseBean.setFrequency(cursor.getString(cursor.getColumnIndex("frequency")));
                    }
                    if (cursor.getString(cursor.getColumnIndex("grade")).equals("null")) {
                        enterpriseBean.setGrade("");
                    } else {
                        enterpriseBean.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
                    }
//            enterpriseBean.setEtpsInfo(cursor.getString(cursor.getColumnIndex("etpsInfo")));
//            enterpriseBean.setRecordInfo(cursor.getString(cursor.getColumnIndex("recordInfo")));
//            enterpriseBean.setEtpsType(cursor.getString(cursor.getColumnIndex("etpsType")));
                    enterpriseBean.setLicNo(cursor.getString(cursor.getColumnIndex("licNo")));
//            enterpriseBean.setExeOrgan(cursor.getString(cursor.getColumnIndex("exeOrgan")));
                    close();
                    list.add(enterpriseBean);
                }
            }
            return list;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public EnterpriseBean queryQyxxSc(String etpsId) {
        SQLiteDatabase db = getReadableDatabase();
        EnterpriseBean enterpriseBean = new EnterpriseBean();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "select * from EnterpriseBean_table where etpsId = ?", new String[]{etpsId + ""});
            while (cursor.moveToNext()) {
                enterpriseBean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                enterpriseBean.setEtpsId(cursor.getString(cursor.getColumnIndex("etpsId")));
                enterpriseBean.setEtpsName(cursor.getString(cursor.getColumnIndex("etpsName")));
                enterpriseBean.setFrequency(cursor.getString(cursor.getColumnIndex("frequency")));
                enterpriseBean.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
                enterpriseBean.setEtpsInfo(cursor.getString(cursor.getColumnIndex("etpsInfo")));
                enterpriseBean.setRecordInfo(cursor.getString(cursor.getColumnIndex("recordInfo")));
                enterpriseBean.setEtpsType(cursor.getString(cursor.getColumnIndex("etpsId")));
                enterpriseBean.setLicNo(cursor.getString(cursor.getColumnIndex("licNo")));
                enterpriseBean.setExeOrgan(cursor.getString(cursor.getColumnIndex("exeOrgan")));
            }
            close();
            return enterpriseBean;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public EnterpriseBean queryQyxxLt(String etpsId) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/生产流通移动执法/" + organId + ".db", null);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from lt_dataInfo_offline where etpsId = ?", new String[]{etpsId + ""});
            EnterpriseBean bean = new EnterpriseBean();
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            while (cursor.moveToNext()) {
                try {
                    bean.setEtpsName(cursor.getString(cursor.getColumnIndex("etpsName")));
                    bean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    bean.setExeOrgan(cursor.getString(cursor.getColumnIndex("exeOrgan")));
                    jsonObject.put("etpsName", cursor.getString(cursor.getColumnIndex("etpsName")));
                    jsonObject.put("factoryAddr", cursor.getString(cursor.getColumnIndex("address")));
                    jsonObject.put("legalPerson", cursor.getString(cursor.getColumnIndex("legalPerson")));
                    jsonObject.put("phoneNo", cursor.getString(cursor.getColumnIndex("telephone")));

                    jsonObject1.put("address", cursor.getString(cursor.getColumnIndex("address")));
                    jsonObject1.put("certNo", cursor.getString(cursor.getColumnIndex("licNo")));
                    jsonObject1.put("certType", "食品流通许可证");
                    jsonObject1.put("endDate", cursor.getString(cursor.getColumnIndex("endDate")));
                    jsonObject1.put("startDate", cursor.getString(cursor.getColumnIndex("startDate")));
                    jsonObject1.put("etpsName", cursor.getString(cursor.getColumnIndex("etpsName")));
                    jsonObject1.put("factoryAddr", cursor.getString(cursor.getColumnIndex("address")));
                    jsonObject1.put("fdTypeId", cursor.getString(cursor.getColumnIndex("fdTypeId")));
                    jsonObject1.put("fzOrgan", cursor.getString(cursor.getColumnIndex("fzOrgan")));
                    jsonObject1.put("pePerson", cursor.getString(cursor.getColumnIndex("legalPerson")));
                    jsonObject1.put("tradeScope", cursor.getString(cursor.getColumnIndex("tradeScope")));
                    jsonObject1.put("provideDate", cursor.getString(cursor.getColumnIndex("provideDate")));

                    jsonArray.put(jsonObject1);
                    jsonObject.put("certificateInfos", jsonArray);

                    bean.setEtpsInfo(jsonObject.toString());

                    if (cursor.getString(cursor.getColumnIndex("superviseRecord")).equals("null")) {
                        bean.setRecordInfo("[]");
                    } else {
                        JSONObject jsonObject2 = new JSONObject(cursor.getString(cursor.getColumnIndex("superviseRecord")));
                        JSONObject jsonObject3 = new JSONObject();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = new Date(jsonObject2.getString("checkDate"));
                        Date date2 = new Date(jsonObject2.getString("submitDate"));
                        jsonObject3.put("checkDate", dateFormat.format(date1));
                        jsonObject3.put("submitDate", dateFormat.format(date2));
                        if (jsonObject2.getString("checkResult").equals("0")) {
                            jsonObject3.put("checkResult", "未发现问题");
                        } else {
                            jsonObject3.put("checkResult", "发现问题");
                        }
                        jsonObject3.put("planId", jsonObject2.getString("planId"));
                        jsonObject3.put("checkPerson", jsonObject2.getString("checkPerson"));
                        JSONArray jsonArray1 = new JSONArray();
                        jsonArray1.put(jsonObject3);
                        bean.setRecordInfo(jsonArray1.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            close();
            return bean;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void savePic(PicBean picBean) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("insert into picTable(picName,picPath,picNum,planId,userId,itemCode,checkContent,type) values(?,?,?,?,?,?,?,?);");
        db.execSQL(
                strBuf.toString(),
                new Object[]{picBean.getPicName(), picBean.getPicPath(), picBean.getPicNum(), picBean.getPlanId(),
                        picBean.getUserId(), picBean.getItemCode(), picBean.getCheckContent(), picBean.getType()});
        close();
    }

    public void deletePic(ArrayList<PicBean> picBeanList) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer strBuf = new StringBuffer();
        PicBean picBean = picBeanList.get(0);
        //删除当前位置的图片
        strBuf.append("delete from picTable where picNum = ? and planId = ? and userId = ? and itemCode is null");
        db.execSQL(
                strBuf.toString(),
                new Object[]{picBean.getPicNum(), picBean.getPlanId(), picBean.getUserId()});
        //修改后续图片的位置减1
        picBeanList.remove(0);
        for (PicBean picBean1 : picBeanList) {
            db.execSQL("update picTable set picNum = ? where planId =? and userId = ?",
                    new Object[]{picBean1.getPicNum() - 1, picBean1.getPlanId(), picBean1.getUserId()});
        }
        close();
    }

    public void deletePicOnPosition(PicBean picBean) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "delete from picTable where userId = ? and planId = ? and itemCode = ? " +
                "and checkContent = ? and picNum = ?";
        database.execSQL(sql, new Object[]{picBean.getUserId(), picBean.getPlanId(), picBean.getItemCode(), picBean.getCheckContent(), picBean.getPicNum()});
        close();
    }

    public void deletePicAfterUpload(PicBean picBean) {
        SQLiteDatabase db = getWritableDatabase();
        //删除当前位置的图片
        String sql = "";
        if (picBean.getItemCode() == null) {
            sql = "delete from picTable where userId = ? and planId = ? and itemCode is null";
            db.execSQL(sql, new Object[]{picBean.getUserId(), picBean.getPlanId()});
        } else {
            sql = "delete from picTable where userId = ? and planId = ? and itemCode = ? and checkContent = ?";
            db.execSQL(sql, new Object[]{picBean.getUserId(), picBean.getPlanId(), picBean.getItemCode()
                    , picBean.getCheckContent()});
        }
        close();
    }

    public void updatePic(PicBean picBean) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("update picTable set picPath = ? where planId = ? and userId = ? and picNum = ?");
        db.execSQL(
                strBuf.toString(),
                new Object[]{picBean.getPicPath(), picBean.getPlanId(),
                        picBean.getUserId(), picBean.getPicNum()});
        close();
    }

    public ArrayList<PicBean> selectPic(String planId, String userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from picTable where planId = ? and userId = ?", new String[]{planId, userId});
            ArrayList<PicBean> list = new ArrayList<PicBean>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    PicBean pic = new PicBean();
                    pic.setPicPath(cursor.getString(cursor.getColumnIndex("picPath")));
                    pic.setPicName(cursor.getString(cursor.getColumnIndex("picName")));
                    pic.setPicNum(cursor.getInt(cursor.getColumnIndex("picNum")));
                    pic.setItemCode(cursor.getString(cursor.getColumnIndex("itemCode")));
                    pic.setCheckContent(cursor.getString(cursor.getColumnIndex("checkContent")));
                    pic.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                    pic.setPlanId(cursor.getString(cursor.getColumnIndex("planId")));
                    list.add(pic);
                }
            }
            close();
            return list;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }
}
