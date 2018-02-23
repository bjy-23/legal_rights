package com.wonders.bean;
import java.util.Date;
import java.util.List;

/**
 * 计划检查项目过信息
 * @author wonders
 *
 */

public class FpsiPlanProcess  {

	private static final long serialVersionUID = -5186969100934295724L;
	
	/** 主键 */
	private String uuid;
	
	/** 计划ID */
	private String planId;
	
	/** 检查项编号 */
	private String itemCode;
	
	/** 任务执行人  */
	private String userId;
	
	/** 备注 */
	private String remark;
	
	/** 检查结果，0：不通过；1：通过 */
	private String result;
	
	/** 有效性，0：无效；1：有效 */
	private String valid;
	
	/** 创建时间 */
	private Date createDate;
	
	/** 用户名   只用于显示*/
	private String userName;
	
	/** 地址*/
	private String address;
	
	/** 经纬度*/
	private String longitudeLatitude;
	
	/** 检查项uuid*/
	private String fpsiInspItemUuid;
	
	/** 图片*/
	private List<FpsiItemAtt> fpsiItemAtt;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getFpsiInspItemUuid() {
		return fpsiInspItemUuid;
	}

	public void setFpsiInspItemUuid(String fpsiInspItemUuid) {
		this.fpsiInspItemUuid = fpsiInspItemUuid;
	}

	public List<FpsiItemAtt> getFpsiItemAtt() {
		return fpsiItemAtt;
	}

	public void setFpsiItemAtt(List<FpsiItemAtt> fpsiItemAtt) {
		this.fpsiItemAtt = fpsiItemAtt;
	}
}
