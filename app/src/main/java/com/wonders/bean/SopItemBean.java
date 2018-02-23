package com.wonders.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjy on 2016/12/8.
 */

public class SopItemBean implements Serializable{
    private String attFlag;
    private String checkContent;
    private String id;
    private String ifAdded;
    private String ifCustom;
    private String itemCode;
    private String parentCode;
    private List<String> pictureUrl;
    private String planId;
    private String remark;
    private String remarkFlag;
    private String result;//"0":发现问题；"1":未发现问题

    //以下字段目前为生产环节所有
    private String checkCode;
    private String isKey;
    private ArrayList<PicBean> picInfo;

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getIsKey() {
        return isKey;
    }

    public void setIsKey(String isKey) {
        this.isKey = isKey;
    }

    public ArrayList<PicBean> getPicInfo() {
        return picInfo;
    }

    public void setPicInfo(ArrayList<PicBean> picInfo) {
        this.picInfo = picInfo;
    }

    public String getAttFlag() {
        return attFlag;
    }

    public void setAttFlag(String attFlag) {
        this.attFlag = attFlag;
    }

    public String getCheckContent() {
        return checkContent;
    }

    public void setCheckContent(String checkContent) {
        this.checkContent = checkContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIfAdded() {
        return ifAdded;
    }

    public void setIfAdded(String ifAdded) {
        this.ifAdded = ifAdded;
    }

    public String getIfCustom() {
        return ifCustom;
    }

    public void setIfCustom(String ifCustom) {
        this.ifCustom = ifCustom;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public List<String> getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(List<String> pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkFlag() {
        return remarkFlag;
    }

    public void setRemarkFlag(String remarkFlag) {
        this.remarkFlag = remarkFlag;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
