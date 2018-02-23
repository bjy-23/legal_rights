package com.wonders.bean;

import java.util.List;


/**
 * 计划检查项目
 * @author wonders
 *
 */

public class FpsiInspItem {

	private static final long serialVersionUID = -4281165936786828480L;
	
	/** 主键 */
	private String uuid;
	
	/** 检查项编号 */
	private String itemCode;
	
	/** 检查项目父编号 */
	private String parentCode;
	
	/** 检查内容 */
	private String checkContent;
	
	/** 计划标识 */
	private String planId;
	
	/** 是否属于新增SOP，0：否；1：是 */
	private String ifAdded;
	
	/** 是否属于自定义SOP，0：否；1：是 */
	private String ifCustom;
	
	/** 是否已上传附件，0：否；1：是 */
	private String attFlag;
	
	/** 是否已备注，0：否；1：是 */
	private String remarkFlag;
	
	private FpsiPlanResult fpsiPlanResult;
	
	private FpsiPlanProcess fpsiPlanProcess; 
	
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

	public FpsiPlanResult getFpsiPlanResult() {
		return fpsiPlanResult;
	}

	public void setFpsiPlanResult(FpsiPlanResult fpsiPlanResult) {
		this.fpsiPlanResult = fpsiPlanResult;
	}

	public FpsiPlanProcess getFpsiPlanProcess() {
		return fpsiPlanProcess;
	}

	public void setFpsiPlanProcess(FpsiPlanProcess fpsiPlanProcess) {
		this.fpsiPlanProcess = fpsiPlanProcess;
	}

	public List<FpsiItemAtt> getFpsiItemAtt() {
		return fpsiItemAtt;
	}

	public void setFpsiItemAtt(List<FpsiItemAtt> fpsiItemAtt) {
		this.fpsiItemAtt = fpsiItemAtt;
	}
}
