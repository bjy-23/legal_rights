package com.wonders.bean;

import java.util.Date;

public class FpsiCertInfo  {

	private static final long serialVersionUID = -2358447553890298610L;
	
	/** 主键 */
	private String uuid;
	
	/** 证书类型 */
	private String certType;
	
	/** 证书编号 */
	private String certNo;
	
	/** 企业标识 */
	private String etpsId;
	
	/** 企业名称 */
	private String etpsName;
	
	/** 住所 */
	private String addr;
	
	/** 生产地址 */
	private String factoryAddr;
	
	/** 检验方式 */
	private String checkType;
	
	/** 产品名称 */
	private String productName;
	
	/** 产品明细 */
	private String detail;
	
	/** 发证日期 */
	private Date issueDate;
	
	/** 有效期 */
	private Date expireDate;
	
	/** 备注 */
	private String memo;
	
	/** 许可证打印编号 */
	private String certPrintNo;
	
	/** 许可证状态 */
	private String certStatus;
	
	/** 发证机关 */
	private String provideOrgan;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getEtpsId() {
		return etpsId;
	}

	public void setEtpsId(String etpsId) {
		this.etpsId = etpsId;
	}

	public String getEtpsName() {
		return etpsName;
	}

	public void setEtpsName(String etpsName) {
		this.etpsName = etpsName;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getFactoryAddr() {
		return factoryAddr;
	}

	public void setFactoryAddr(String factoryAddr) {
		this.factoryAddr = factoryAddr;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCertPrintNo() {
		return certPrintNo;
	}

	public void setCertPrintNo(String certPrintNo) {
		this.certPrintNo = certPrintNo;
	}

	public String getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(String certStatus) {
		this.certStatus = certStatus;
	}

	public String getProvideOrgan() {
		return provideOrgan;
	}

	public void setProvideOrgan(String provideOrgan) {
		this.provideOrgan = provideOrgan;
	}
}
