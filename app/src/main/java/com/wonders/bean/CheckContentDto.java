package com.wonders.bean;

import java.io.Serializable;
import java.util.List;



/**
 * 计划检查项目
 * @author wonders
 *
 */

public class CheckContentDto implements Serializable{

	private static final long serialVersionUID = -4281165936786828480L;
	
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
	
	/** 結果，0：否；1：是 */
	private String result; 
	
	/** 临时表数据id */
	private String id;
	
	private List<String> pictureUrl;
	
	//备注
	private String remark;

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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<String> getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(List<String> pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
