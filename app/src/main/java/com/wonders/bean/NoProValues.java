package com.wonders.bean;

import com.wonders.application.AppData;
import com.wonders.util.DbHelper;

/**
 * Created by bjy on 2016/9/26.
 */
public class NoProValues {
    private String planId;
    private String etpsId;
    private DbHelper dbHelper;
    private AppData appData;

    public AppData getAppData() {
        return appData;
    }

    public void setAppData(AppData appData) {
        this.appData = appData;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getEtpsId() {
        return etpsId;
    }

    public void setEtpsId(String etpsId) {
        this.etpsId = etpsId;
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
}
