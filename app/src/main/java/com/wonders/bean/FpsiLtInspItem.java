package com.wonders.bean;

import java.util.List;



/**
 * 流通检查项
 * @author wonders
 *
 */

public class FpsiLtInspItem  {

	private static final long serialVersionUID = -8057020086221025126L;

	/** 主键 */
	private String uuid;

	/** 检查项编号 */
	private String itemCode;

	/** 检查项目父编号 */
	private String parentCode;

	/** 检查内容 */
	private String checkContent;

	/** 大计划标识 */
	private String scheId;

	/** 计划执行机关 */
	private String exeOrgan;

	/** 计划执行部门 */
	private String exeDept;

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

    /** 是否大计划检查项，0：否；1：是 */
    private String parentFlag;

	private FpsiLtPlanResult fpsiPlanResult;

	private FpsiPlanProcess fpsiPlanProcess;

	private List<FpsiItemAtt> fpsiItemAtt;
	
	private List<FpsiLtItemAtt> fpsiLtItemAtt;

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

	public String getScheId() {
		return scheId;
	}

	public void setScheId(String scheId) {
		this.scheId = scheId;
	}

	public String getExeOrgan() {
		return exeOrgan;
	}

	public void setExeOrgan(String exeOrgan) {
		this.exeOrgan = exeOrgan;
	}

	public String getExeDept() {
		return exeDept;
	}

	public void setExeDept(String exeDept) {
		this.exeDept = exeDept;
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

	public String getParentFlag() {
		return parentFlag;
	}

	public void setParentFlag(String parentFlag) {
		this.parentFlag = parentFlag;
	}

	public FpsiLtPlanResult getFpsiPlanResult() {
		return fpsiPlanResult;
	}

	public void setFpsiPlanResult(FpsiLtPlanResult fpsiPlanResult) {
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

	public List<FpsiLtItemAtt> getFpsiLtItemAtt() {
		return fpsiLtItemAtt;
	}

	public void setFpsiLtItemAtt(List<FpsiLtItemAtt> fpsiLtItemAtt) {
		this.fpsiLtItemAtt = fpsiLtItemAtt;
	}
}
