package com.wonders.bean;

import java.util.List;

/**
 * Created by BJY on 2018/2/9.
 */

public class DbsxBean {
    private String loginType;
    private List<PlanBean> supervisionPlans;
    private List<PlanBean> notSupervisionPlans;
    private List<PlanBean> revisitPlans;
    private List<PlanBean> notRevisitPlans;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public List<PlanBean> getNotRevisitPlans() {
        return notRevisitPlans;
    }

    public void setNotRevisitPlans(List<PlanBean> notRevisitPlans) {
        this.notRevisitPlans = notRevisitPlans;
    }

    public List<PlanBean> getNotSupervisionPlans() {
        return notSupervisionPlans;
    }

    public void setNotSupervisionPlans(List<PlanBean> notSupervisionPlans) {
        this.notSupervisionPlans = notSupervisionPlans;
    }

    public List<PlanBean> getSupervisionPlans() {
        return supervisionPlans;
    }

    public void setSupervisionPlans(List<PlanBean> supervisionPlans) {
        this.supervisionPlans = supervisionPlans;
    }

    public List<PlanBean> getRevisitPlans() {
        return revisitPlans;
    }

    public void setRevisitPlans(List<PlanBean> revisitPlans) {
        this.revisitPlans = revisitPlans;
    }
}
