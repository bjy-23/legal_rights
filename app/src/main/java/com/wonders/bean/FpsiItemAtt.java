package com.wonders.bean;

import java.util.Date;


/**
 * 检查项目附件信息
 * @author wonders
 *
 */

public class FpsiItemAtt  {

	private static final long serialVersionUID = 3705874997968284592L;
	
	/** 主键 */
	private String id;
	
	/** FpsiPlanResult uuid */
	private String resultId;
	
	/** processID */
	private String procId;
	
	/** 计划ID */
	private String planId;
	
	/** 检查项编号 */
	private String itemCode;
	
	/** 附件创建人 */
	private String userId;
	
	/** 附件名称 */
	private String origianlName;
	
	/** 保存路径 */
	private String savePath;
	
	/** 创建时间 */
	private Date createTime;
	
	/** 图片序号 */
	private String picNum;
	
	/** 是否是文书图片 1是 0否*/
	private String ifDocumentPicture;

	/** 有效性，0：无效；1：无效 */
	private String valid;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public String getProcId() {
		return procId;
	}

	public void setProcId(String procId) {
		this.procId = procId;
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

	public String getOrigianlName() {
		return origianlName;
	}

	public void setOrigianlName(String origianlName) {
		this.origianlName = origianlName;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPicNum() {
		return picNum;
	}

	public void setPicNum(String picNum) {
		this.picNum = picNum;
	}

	public String getIfDocumentPicture() {
		return ifDocumentPicture;
	}

	public void setIfDocumentPicture(String ifDocumentPicture) {
		this.ifDocumentPicture = ifDocumentPicture;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}
}
