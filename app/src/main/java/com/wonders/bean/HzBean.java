package com.wonders.bean;

import java.util.ArrayList;

public class HzBean {
	private String itemCode;
	private String parentCode;
	private String checkContent;
	private String planId;
	private String ifAdded;//1，表示属于新增SOP检查项
	private String ifCustom;//1,表示属于自定义
	private String attFlag;
	private String remarkFlag;
	private String result;//1,没有问题，0表示有问题
	private String remark;
	private String id;
	private ArrayList<String> picList = new ArrayList<String>();

	public String getItemCode() {
		return itemCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getCheckContent() {
		return checkContent;
	}

	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
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

	public String getAttFlag() {
		return attFlag;
	}

	public void setAttFlag(String attFlag) {
		this.attFlag = attFlag;
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

	public ArrayList<String> getPicList() {
		return picList;
	}

	public void setPicList(ArrayList<String> picList) {
		this.picList = picList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
