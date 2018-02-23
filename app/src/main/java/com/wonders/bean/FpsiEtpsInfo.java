package com.wonders.bean;

import java.util.List;



/**
 * 企业信息
 * @author wonders
 *
 */

public class FpsiEtpsInfo {
	
	private static final long serialVersionUID = 6214032876810749881L;
	
	/** 企业标识 */
	private String etpsId;
	
	/** 企业类型 */
	private String typeId;
	
	/** 企业名称 */
	private String etpsName;
	
	/** 企业地址 */
	private String address;
	
	/** 产品类型 */
	private String product;
	
	/** 法定代表人 */
	private String personName;
	
	/** 联系电话 */
	private String telephone;
	
	/** 所属机构 */
	private String organId;
	
	/** 组织机构代码 */
	private String zjCode;
	
	/** 邮政编码 */
	private String postCode;
	
	/** 传真 */
	private String fax;
	
	/** 邮箱 */
	private String mail;
	
	/** 评价等级 */
	private String grade;
	
	/** 检查频次 */
	private String frequency;

	private List<FpsiCertInfo> fpsiCertInfoList;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getEtpsId() {
		return etpsId;
	}

	public void setEtpsId(String etpsId) {
		this.etpsId = etpsId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getEtpsName() {
		return etpsName;
	}

	public void setEtpsName(String etpsName) {
		this.etpsName = etpsName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getZjCode() {
		return zjCode;
	}

	public void setZjCode(String zjCode) {
		this.zjCode = zjCode;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public List<FpsiCertInfo> getFpsiCertInfoList() {
		return fpsiCertInfoList;
	}

	public void setFpsiCertInfoList(List<FpsiCertInfo> fpsiCertInfoList) {
		this.fpsiCertInfoList = fpsiCertInfoList;
	}
}
