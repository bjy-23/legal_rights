package com.wonders.application;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.legal_rights.BuildConfig;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;
import com.wonders.bean.LoginBean;
import com.wonders.bean.Db_message;
import com.wonders.bean.SopBean;
import com.wonders.bean.SopListViewBean;
import com.wonders.util.CrashHandler;

public class AppData extends MultiDexApplication {
	private final static String TAG = AppData.class.getName();
	private static AppData instance;
	private LoginBean loginBean;
	private int widthPixels,heightPixels;
	private ArrayList<SopBean> sopList = new ArrayList<SopBean>();

	private ArrayList<SopBean> sopList_a = new ArrayList<SopBean>();
	private ArrayList<SopBean> sopList_b = new ArrayList<SopBean>();
	private ArrayList<SopBean> sopList_c = new ArrayList<SopBean>();
	private ArrayList<SopBean> sopList_d = new ArrayList<SopBean>();
	private ArrayList<SopBean> sopList_e = new ArrayList<SopBean>();
	private ArrayList<SopBean> sopList_f = new ArrayList<SopBean>();
	private ArrayList<SopBean> sopList_g = new ArrayList<SopBean>();

	private ArrayList<ArrayList<SopBean>> sopList_lt = new ArrayList<ArrayList<SopBean>>();

	private String phoneNum;

	private SopListViewBean sopTemp;
	private String sopType = "0a";

	//true    网络版
	//false   单机版
	private boolean isNetWork;

	//单机版数据
	private Db_message db_message;

	public int x = 0;
	public static boolean isFirstDo = true;

    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		instance = this;

		//捕获全局变量,只用在发布版
		if (!BuildConfig.DEBUG){
			CrashHandler crashHandler = CrashHandler.getInstance();
			crashHandler.init(this);
		}


		//获取手机屏幕宽高
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        widthPixels = dm.widthPixels;
        heightPixels = dm.heightPixels;

		Hawk.init(this)
				.setEncryption(new NoEncryption())
				.build();

		sopList_lt.add(sopList_a);
		sopList_lt.add(sopList_b);
		sopList_lt.add(sopList_c);
		sopList_lt.add(sopList_d);
		sopList_lt.add(sopList_e);
		sopList_lt.add(sopList_f);
		sopList_lt.add(sopList_g);
	}

	public static String getAppVersion() {
		PackageManager packageManager = instance.getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(instance.getPackageName(),
					0);
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e(TAG, "当前版本：" + packageInfo.versionName);
		return packageInfo.versionName;
	}

	public static InputMethodManager getInputManger(){
    	return (InputMethodManager) instance.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	public int getHeightPixels() {
        return heightPixels;
    }

    public int getWidthPixels() {
        return widthPixels;
    }

    public static synchronized AppData getInstance(){
		return instance;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public ArrayList<SopBean> getSopList() {
		return sopList;
	}

	public void setSopList(ArrayList<SopBean> sopList) {
		this.sopList = sopList;
	}

	public SopListViewBean getSopTemp() {
		return sopTemp;
	}

	public void setSopTemp(SopListViewBean sopTemp) {
		this.sopTemp = sopTemp;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void clear() {
		sopList.clear();
		// sopList_lt.clear();

		sopList_a.clear();
		sopList_b.clear();
		sopList_c.clear();
		sopList_d.clear();
		sopList_e.clear();
		sopList_f.clear();
		sopList_g.clear();
	}

	public ArrayList<SopBean> getSopList_a() {
		return sopList_a;
	}

	public void setSopList_a(ArrayList<SopBean> sopList_a) {
		this.sopList_a = sopList_a;
	}

	public ArrayList<SopBean> getSopList_b() {
		return sopList_b;
	}

	public void setSopList_b(ArrayList<SopBean> sopList_b) {
		this.sopList_b = sopList_b;
	}

	public ArrayList<SopBean> getSopList_c() {
		return sopList_c;
	}

	public void setSopList_c(ArrayList<SopBean> sopList_c) {
		this.sopList_c = sopList_c;
	}

	public ArrayList<SopBean> getSopList_d() {
		return sopList_d;
	}

	public void setSopList_d(ArrayList<SopBean> sopList_d) {
		this.sopList_d = sopList_d;
	}

	public ArrayList<SopBean> getSopList_e() {
		return sopList_e;
	}

	public void setSopList_e(ArrayList<SopBean> sopList_e) {
		this.sopList_e = sopList_e;
	}

	public ArrayList<SopBean> getSopList_f() {
		return sopList_f;
	}

	public void setSopList_f(ArrayList<SopBean> sopList_f) {
		this.sopList_f = sopList_f;
	}

	public ArrayList<SopBean> getSopList_g() {
		return sopList_g;
	}

	public void setSopList_g(ArrayList<SopBean> sopList_g) {
		this.sopList_g = sopList_g;
	}

	public ArrayList<ArrayList<SopBean>> getSopList_lt() {
		return sopList_lt;
	}

	public void setSopList_lt(ArrayList<ArrayList<SopBean>> sopList_lt) {
		this.sopList_lt = sopList_lt;
	}

	public String getSopType() {
		return sopType;
	}

	public void setSopType(String sopType) {
		this.sopType = sopType;
	}

	public boolean isNetWork() {
		return isNetWork;
	}

	public void setIsNetWork(boolean isNetWork) {
		this.isNetWork = isNetWork;
	}

	public Db_message getDb_message() {
		return db_message;
	}

	public void setDb_message(Db_message db_message) {
		this.db_message = db_message;
	}

}
