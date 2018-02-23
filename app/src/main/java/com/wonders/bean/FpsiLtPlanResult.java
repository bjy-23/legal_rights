package com.wonders.bean;


/**
 * 流通计划检查项目结果信息
 * @author wonders
 *
 */

public class FpsiLtPlanResult  {

	private static final long serialVersionUID = -2512545202850950344L;

	/** 主键 */
	private String uuid;
	
	/** 计划ID */
	private String planId;
	
	/** 检查项编号 */
	private String itemCode;
	
	/** 二级检查项编号  只用于显示*/
	private String secondCode;
	
	/** 二级内容   只用于显示*/
	private String secondContent;
	
	/** 备注 */
	private String remark;
	
	/** 检查结果，0：不通过；1：通过 */
	private String result;
    
    /** 检查备注 */
    private String checkContent;
    
	/** 地址*/
	private String address;
	
	/** 经纬度*/
	private String longitudeLatitude;
	
//	/** 检查项uuid*/
//	private String fpsiInspItemUuid;


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public String getSecondCode() {
		return secondCode;
	}

	public void setSecondCode(String secondCode) {
		this.secondCode = secondCode;
	}

	public String getSecondContent() {
		return secondContent;
	}

	public void setSecondContent(String secondContent) {
		this.secondContent = secondContent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCheckContent() {
		return checkContent;
	}

	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitudeLatitude() {
		return longitudeLatitude;
	}

	public void setLongitudeLatitude(String longitudeLatitude) {
		this.longitudeLatitude = longitudeLatitude;
	}
}
