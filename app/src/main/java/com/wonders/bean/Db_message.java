package com.wonders.bean;


//代办信息
public class Db_message {
    private String planId; //代办id
    private String userId; //用户id
    private int flag; //标志位，判断代办信息是否完成
    private String get_etpCheckInfo; //检查信息
    private String get_superviseRecord; //监管记录
    private String get_planCheckContent;//巡查录入
    private String get_planCheckContentDetail;//监管记录详情
    private String isLt;//0 不是流通环节  1 是流通环节
    private String address;
    private String etpsName;
    private String planMonth;
    private String allUserName;
    private String etpsId;
    private String type;
    private String isFinish;
    private String get_fpsiEtpsInfo;//企业信息

    public String getGet_fpsiCertInfo() {
        return get_fpsiCertInfo;
    }

    public void setGet_fpsiCertInfo(String get_fpsiCertInfo) {
        this.get_fpsiCertInfo = get_fpsiCertInfo;
    }

    public String getGet_fpsiEtpsInfo() {
        return get_fpsiEtpsInfo;
    }

    public void setGet_fpsiEtpsInfo(String get_fpsiEtpsInfo) {
        this.get_fpsiEtpsInfo = get_fpsiEtpsInfo;
    }

    public String getGet_fpsiInspPlan() {
        return get_fpsiInspPlan;
    }

    public void setGet_fpsiInspPlan(String get_fpsiInspPlan) {
        this.get_fpsiInspPlan = get_fpsiInspPlan;
    }

    private String get_fpsiCertInfo;//证书信息
    private String get_fpsiInspPlan;//监督计划

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getGet_etpCheckInfo() {
        return get_etpCheckInfo;
    }

    public void setGet_etpCheckInfo(String get_etpCheckInfo) {
        this.get_etpCheckInfo = get_etpCheckInfo;
    }

    public String getGet_superviseRecord() {
        return get_superviseRecord;
    }

    public void setGet_superviseRecord(String get_superviseRecord) {
        this.get_superviseRecord = get_superviseRecord;
    }

    public String getGet_planCheckContent() {
        return get_planCheckContent;
    }

    public void setGet_planCheckContent(String get_planCheckContent) {
        this.get_planCheckContent = get_planCheckContent;
    }

    public String getGet_planCheckContentDetail() {
        return get_planCheckContentDetail;
    }

    public void setGet_planCheckContentDetail(String get_planCheckContentDetail) {
        this.get_planCheckContentDetail = get_planCheckContentDetail;
    }

    public String getIsLt() {
        return isLt;
    }

    public void setIsLt(String isLt) {
        this.isLt = isLt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEtpsName() {
        return etpsName;
    }

    public void setEtpsName(String etpsName) {
        this.etpsName = etpsName;
    }

    public String getPlanMonth() {
        return planMonth;
    }

    public void setPlanMonth(String planMonth) {
        this.planMonth = planMonth;
    }

    public String getEtpsId() {
        return etpsId;
    }

    public void setEtpsId(String etpsId) {
        this.etpsId = etpsId;
    }

    public String getAllUserName() {
        return allUserName;
    }

    public void setAllUserName(String allUserName) {
        this.allUserName = allUserName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}