package com.wonders.bean;


import java.util.ArrayList;

public class ZtBaseInfoBean {
	private String etpsName;
	private String legalPerson;
	private String factoryAddr;
	private String phoneNo;
	private String exportDate;
	private ArrayList<ZtXkzBean> certificateInfos;
	private String sopType;
	private String etpsId;

	public String getEtpsName() {
		return etpsName;
	}

	public void setEtpsName(String etpsName) {
		this.etpsName = etpsName;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getFactoryAddr() {
		return factoryAddr;
	}

	public void setFactoryAddr(String factoryAddr) {
		this.factoryAddr = factoryAddr;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getExportDate() {
		return exportDate;
	}

	public void setExportDate(String exportDate) {
		this.exportDate = exportDate;
	}

	public ArrayList<ZtXkzBean> getCertificateInfos() {
		return certificateInfos;
	}

	public void setCertificateInfos(ArrayList<ZtXkzBean> certificateInfos) {
		this.certificateInfos = certificateInfos;
	}

	public String getSopType() {
		return sopType;
	}

	public void setSopType(String sopType) {
		this.sopType = sopType;
	}

	public String getEtpsId() {
		return etpsId;
	}

	public void setEtpsId(String etpsId) {
		this.etpsId = etpsId;
	}
}
