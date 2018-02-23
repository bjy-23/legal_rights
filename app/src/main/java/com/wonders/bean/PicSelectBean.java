package com.wonders.bean;

import java.util.ArrayList;

/**
 * Created by bjy on 2016/10/19.
 */
public class PicSelectBean {
    private String userId;
    private String planId;
    private String itemCode;
    private String checkContent;
    private ArrayList<PicBean> picInfo;
    private int model;//0,计划图片，1，item项图片。

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public ArrayList<PicBean> getPicInfo() {
        return picInfo;
    }

    public void setPicInfo(ArrayList<PicBean> picInfo) {
        this.picInfo = picInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getCheckContent() {
        return checkContent;
    }

    public void setCheckContent(String checkContent) {
        this.checkContent = checkContent;
    }

}
