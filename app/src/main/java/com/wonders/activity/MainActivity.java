package com.wonders.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.legal_rights.R;
import com.wonders.bean.DeptBean;
import com.wonders.bean.LoginBean;
import com.wonders.bean.Result;
import com.wonders.bean.UserBean;
import com.wonders.bean.UserInfoBean;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.constant.DbConstants;
import com.wonders.fragment.ChecksQueryFragmentLt;
import com.wonders.fragment.HomepageFragment;
import com.wonders.fragment.QyjgcxFragment;
import com.wonders.fragment.QyxxGlFragment;
import com.wonders.fragment.SettingFragment;
import com.wonders.fragment.DocQueryFragment;
import com.wonders.util.DbHelper;
import com.wonders.util.FragmentUtil;
import com.wonders.http.Retrofit2Helper;
import com.wonders.thread.MainAsyncTask;
import com.wonders.util.ToastUtil;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 总控制器 控制fragment 跳转
 *
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private RelativeLayout layoutHome;
    private RelativeLayout layoutBack;
    private TextView tvTitle;
    private Context mContext;

    private ListView menuLv;
    private BaseAdapter adapter;
    private DrawerLayout drawerLayout;
    private FragmentManager fm;

    //个人信息的展示
    private TextView informationTv;
    private TextView identity;
    private TextView nameTv;
    private TextView hjTv;
    private TextView moodTv;

    private LoginBean loginBean;

    private final static String[] menuName = {"监督管理", "企业信息查询", "监管记录查询",
            "文书打印", "设置"};
    private final static int[] menuPic = {R.drawable.menu_nav,
            R.drawable.menu_nav, R.drawable.menu_nav, R.drawable.menu_nav,
            R.drawable.menu_nav, R.drawable.menu_nav};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        findView();

        mContext = this;
        final AppData appData = (AppData) getApplication();
        if (appData.isNetWork()) {
            getLogin();
        }

        MainAsyncTask mainAsyncTask = new MainAsyncTask(mContext);
        mainAsyncTask.execute();

        //当activity被系统强制收回的时候，保存数据
        if (savedInstanceState != null) {

            LoginBean loginBean = new LoginBean();
            loginBean.setDeptName(savedInstanceState.getString("deptname"));
            loginBean.setTodoCount(savedInstanceState.getString("todocount"));
            loginBean.setUserId(savedInstanceState.getString("userid"));
            loginBean.setUserName(savedInstanceState.getString("username"));

            appData.setLoginBean(loginBean);
        }

        hjTv = (TextView) findViewById(R.id.hj_tv);
        moodTv = (TextView) findViewById(R.id.mood_tv);

        changeText();

        //得到userbean
        loginBean = AppData.getInstance().getLoginBean();
        //左边菜单栏的用户信息
        initPersonInformation();

        fm = getSupportFragmentManager();
        //回退栈添加监听，修改图标变化
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.e(TAG, "Fragment回退栈长度：  " + fm.getBackStackEntryCount());
                if (fm.getBackStackEntryCount() == 1){
                    layoutHome.setVisibility(View.VISIBLE);
                    layoutBack.setVisibility(View.GONE);
                    tvTitle.setText(fm.getBackStackEntryAt(0).getName());
                }else {
                    layoutHome.setVisibility(View.GONE);
                    layoutBack.setVisibility(View.VISIBLE);
                }
            }
        });

        //加载第一个Fragment
        Fragment f = new HomepageFragment();
        FragmentUtil.addStack(fm, f, R.id.fragment, Constants.JDGL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("deptname", loginBean.getDeptName());
        outState.putString("todocount", loginBean.getTodoCount());
        outState.putString("userid", loginBean.getUserId());
        outState.putString("username", loginBean.getUserName());

        super.onSaveInstanceState(outState);
    }

    private void initPersonInformation() {
        nameTv = (TextView) findViewById(R.id.name);
        informationTv = (TextView) findViewById(R.id.person_imformation);
        identity = (TextView) findViewById(R.id.identity);

        nameTv.setText(loginBean.getUserName());
        informationTv.setText(loginBean.getDeptName());
        identity.setText(loginBean.isManager() == true ? "组长" : "组员");
    }

    private void findView() {
        tvTitle = findViewById(R.id.tv_title);
        layoutHome = findViewById(R.id.layout_home);
        layoutHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        layoutBack = findViewById(R.id.layout_back);
        layoutBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    doBack();
                return true;
            }
        });
//        layoutBack.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().popBackStack();
//            }
//        });
        menuLv = (ListView) findViewById(R.id.menu_lv);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        adapter = new MenuAdapter();
        menuLv.setAdapter(adapter);
        menuLv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        FragmentUtil.clearAllFragments(fm);

                        Fragment homepageFragment = new HomepageFragment();
                        FragmentUtil.addStack(fm, homepageFragment, R.id.fragment, Constants.JDGL);

                        break;
                    case 1://企业信息查询
                        FragmentUtil.clearAllFragments(fm);

                        QyxxGlFragment qyxxGlFragment = new QyxxGlFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", 1);
                        qyxxGlFragment.setArguments(bundle);
                        FragmentUtil.addStack(fm, qyxxGlFragment, R.id.fragment, Constants.QYXXCX);

                        break;
                    case 2://监管记录查询
                        if (!AppData.getInstance().isNetWork()) {
                            ToastUtil.showMid("请在联网模式下查询");

                            return;
                        }
                        FragmentUtil.clearAllFragments(fm);

                        if ("".equals(Constants.TYPE)) {
                            QyjgcxFragment fqy1 = new QyjgcxFragment();
                            FragmentUtil.addStack(fm, fqy1, R.id.fragment, Constants.JGJLCX);
                        } else {
                            ChecksQueryFragmentLt fqy1 = new ChecksQueryFragmentLt();
                            FragmentUtil.addStack(fm, fqy1, R.id.fragment, Constants.JGJLCX);
                        }

                        break;
                    case 3://文书打印
                        FragmentUtil.clearAllFragments(fm);
                        DocQueryFragment docQueryFragment = new DocQueryFragment();
                        FragmentUtil.addStack(fm, docQueryFragment, R.id.fragment, Constants.WSDY);

                        break;
                    case 4:
                        SettingFragment settingFragment = new SettingFragment();
                        FragmentUtil.clearAllFragments(fm);
                        FragmentUtil.addStack(fm, settingFragment, R.id.fragment, Constants.SZ);

                        break;
                }

                drawerLayout.closeDrawers();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            doBack();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //切换环节和模式
    public void change(){
        changeText();

        Fragment fragment = new HomepageFragment();
        FragmentUtil.clearAllFragments(fm);
        FragmentUtil.addStack(fm, fragment, R.id.fragment, Constants.JDGL);
    }

    //
    public void changeText(){
        if (AppData.getInstance().isNetWork()) {
            moodTv.setText("联网模式");
        } else {
            moodTv.setText("单机模式");
        }

        if (Constants.TYPE.equals("")) {
            hjTv.setText("生产环节");
        } else {
            hjTv.setText("流通环节");
        }
    }

    //处理回退按钮和系统回退键
    public void doBack(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            doFinish();
        }else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void doFinish(){
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.dialog_exitapp);

        TextView exit = (TextView) dialog.findViewById(R.id.exit_tv);
        exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        TextView cancel = (TextView) dialog.findViewById(R.id.cancel_tv);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return menuName.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this,
                        R.layout.item_left_menu, null);

                holder = new ViewHolder();
                holder.leftImg = (ImageView) convertView.findViewById(R.id.img);
                holder.menuTv = (TextView) convertView.findViewById(R.id.tv);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.leftImg.setImageResource(menuPic[position]);
            holder.menuTv.setText(menuName[position]);

            return convertView;
        }

    }

    class ViewHolder {
        ImageView leftImg;
        TextView menuTv;
    }

    private void getLogin() {
        Call<Result<ArrayList<UserInfoBean>>> call = Retrofit2Helper.getInstance().getAccount("");
        call.enqueue(new Callback<Result<ArrayList<UserInfoBean>>>() {
            @Override
            public void onResponse(Call<Result<ArrayList<UserInfoBean>>> call, Response<Result<ArrayList<UserInfoBean>>> response) {

                ArrayList<UserInfoBean> userInfoList = response.body().getObject();

                DbHelper dbHelper = new DbHelper(AppData.getInstance(), DbConstants.TABLENAME, null, 1);

                if (userInfoList.size() != 0)
                    dbHelper.deleteUserInfo();

                for (UserInfoBean userInfoBean : userInfoList) {
                    DeptBean deptBean = userInfoBean.getDept();
                    UserBean userBean = userInfoBean.getUser();
                    userBean.setDeptName(deptBean.getDeptName());
                    dbHelper.insertUserInfo(userBean);
                }
            }

            @Override
            public void onFailure(Call<Result<ArrayList<UserInfoBean>>> call, Throwable t) {

            }
        });
    }
}
