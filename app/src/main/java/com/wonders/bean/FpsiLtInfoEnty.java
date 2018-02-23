package com.wonders.bean;


import java.util.Date;
import java.util.List;

public class FpsiLtInfoEnty  {

	private static final long serialVersionUID = -4329719028204923936L;

	private String adress;

	private String fdId;

	/**
	 * 许可证号(G)
	 */
	private String licNo;

	/**
	 * 
	 */
	private String nameId;

	/**
	 * 食品经营者名称(G)
	 */
	private String fdName;

	/**
	 * 实体类型(G)（1企业，5个体，8农民）(S)
	 */
	private String fdThreadId;

	/**
	 * 主体类型(G)
	 */
	private String fdTypeId;

	/**
	 * 
	 */
	private String sonFlag;

	/**
	 * 是否属于市场(S)
	 */
	private String ifMarket;

	/**
	 * 市场企业标识(S)
	 */
	private String marketId;

	/**
	 * 市场名称(S)
	 */
	private String marketName;

	/**
	 * 经营面积(G)
	 */
	private Double tradeArea;

	/**
	 * 邮政编码(G)
	 */
	private String postcode;

	/**
	 * 固定电话(G)
	 */
	private String telephone;

	/**
	 * 移动电话(G)
	 */
	private String mobilePhone;

	/**
	 * E-mail(G)
	 */
	private String email;

	/**
	 * 所在经济小区(S)
	 */
	private String zoneId;

	/**
	 * 属地监管工商所(S)
	 */
	private String areaOrganId;

	/**
	 * 副本数(G)
	 */
	private Integer licCopyNum;

	/**
	 * 有效期限自(G)
	 */
	private Date startDate;

	/**
	 * 有效期限至(G)
	 */
	private Date endDate;

	/**
	 * 发证机关(S)
	 */
	private String provideOrgan;

	/**
	 * 发证机关(G)
	 */
	private String provideOrganGb;

	/**
	 * 发证日期(G)
	 */
	private Date provideDate;

	/**
	 * 新设日期(S)
	 */
	private Date establishDate;

	/**
	 * 吊销日期
	 */
	private Date dxDate;

	/**
	 * 吊销后注销日期
	 */
	private Date dxZxDate;

	/**
	 * 注销日期
	 */
	private Date zxDate;

	/**
	 * 撤销日期
	 */
	private Date cxDate;

	/**
	 * 撤销后注销日期
	 */
	private Date cxZxDate;

	/**
	 * 受理机关(S)
	 */
	private String acceptOrgan;

	/**
	 * 许可证状态
	 */
	private String licStatus;

	/**
	 * 许可证状态(G)
	 */
	private String licStatusGb;

	/**
	 * 审查方式(S)
	 */
	private String checkType;

	/**
	 * 实体标识
	 */
	private String entityId;

	/**
	 * 迁移至机关
	 */
	private String moveToOrgan;

	/**
	 * 备注(S)
	 */
	private String remark;

	/**
	 * 法定代表人
	 */
	private String pePerson;

	/**
	 * 
	 */
	private String foodId;

	/**
	 * 
	 */
	private String isMilk;

	/**
	 * 
	 */
	private String fdNameGb;

	/**
	 * 
	 */
	private String bankName;

	/**
	 * 
	 */
	private String bankNo;

	/**
	 * 
	 */
	private String bankAccountName;

	/**
	 * 
	 */
	private String acceptOrganOld;

	/**
	 * 
	 */
	private String moveToOrganOld;

	/**
	 * 
	 */
	private String ifSimply;

	/**
	 * 
	 */
	private String appTypeId;

	/**
	 * 
	 */
	private String oldProvideOrgan;
	
	/** 评价等级 */
	private String grade;
	
	/** 检查频次 */
	private String frequency;

    private FpsiLtAddressEnty fpsiLtAddressEnty;

    private FpsiLtScopeEnty fpsiLtScopeEnty;

    private List<FpsiLtInspPlan> fpsiLtInspPlans;

	// Constructors

	/** default constructor */
	public FpsiLtInfoEnty() {
	}

	/** minimal constructor */
	public FpsiLtInfoEnty(String fdId) {
		this.fdId = fdId;
	}


	public FpsiLtInfoEnty(String fdId, String licNo, String nameId, String fdName, String fdThreadId, String fdTypeId, String sonFlag, String ifMarket, String marketId, String marketName, Double tradeArea, String postcode, String telephone, String mobilePhone, String email, String zoneId, String areaOrganId, Integer licCopyNum, Date startDate, Date endDate, String provideOrgan, String provideOrganGb, Date provideDate, Date establishDate, Date dxDate, Date dxZxDate, Date zxDate, Date cxDate, Date cxZxDate, String acceptOrgan, String licStatus, String licStatusGb, String checkType, String entityId, String moveToOrgan, String remark, String pePerson, String foodId, String isMilk, String fdNameGb, String bankName, String bankNo, String bankAccountName, String acceptOrganOld, String moveToOrganOld, String ifSimply, String appTypeId, String oldProvideOrgan, String grade, String frequency, FpsiLtAddressEnty fpsiLtAddressEnty, FpsiLtScopeEnty fpsiLtScopeEnty, List<FpsiLtInspPlan> fpsiLtInspPlans) {
		this.fdId = fdId;
		this.licNo = licNo;
		this.nameId = nameId;
		this.fdName = fdName;
		this.fdThreadId = fdThreadId;
		this.fdTypeId = fdTypeId;
		this.sonFlag = sonFlag;
		this.ifMarket = ifMarket;
		this.marketId = marketId;
		this.marketName = marketName;
		this.tradeArea = tradeArea;
		this.postcode = postcode;
		this.telephone = telephone;
		this.mobilePhone = mobilePhone;
		this.email = email;
		this.zoneId = zoneId;
		this.areaOrganId = areaOrganId;
		this.licCopyNum = licCopyNum;
		this.startDate = startDate;
		this.endDate = endDate;
		this.provideOrgan = provideOrgan;
		this.provideOrganGb = provideOrganGb;
		this.provideDate = provideDate;
		this.establishDate = establishDate;
		this.dxDate = dxDate;
		this.dxZxDate = dxZxDate;
		this.zxDate = zxDate;
		this.cxDate = cxDate;
		this.cxZxDate = cxZxDate;
		this.acceptOrgan = acceptOrgan;
		this.licStatus = licStatus;
		this.licStatusGb = licStatusGb;
		this.checkType = checkType;
		this.entityId = entityId;
		this.moveToOrgan = moveToOrgan;
		this.remark = remark;
		this.pePerson = pePerson;
		this.foodId = foodId;
		this.isMilk = isMilk;
		this.fdNameGb = fdNameGb;
		this.bankName = bankName;
		this.bankNo = bankNo;
		this.bankAccountName = bankAccountName;
		this.acceptOrganOld = acceptOrganOld;
		this.moveToOrganOld = moveToOrganOld;
		this.ifSimply = ifSimply;
		this.appTypeId = appTypeId;
		this.oldProvideOrgan = oldProvideOrgan;
		this.grade = grade;
		this.frequency = frequency;
		this.fpsiLtAddressEnty = fpsiLtAddressEnty;
		this.fpsiLtScopeEnty = fpsiLtScopeEnty;
		this.fpsiLtInspPlans = fpsiLtInspPlans;
	}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFdId() {
        return fdId;
    }


	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public void setFdId(String fdId) {
        this.fdId = fdId;
    }

    public String getLicNo() {
        return licNo;
    }

    public void setLicNo(String licNo) {
        this.licNo = licNo;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getFdName() {
        return fdName;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public String getFdThreadId() {
        return fdThreadId;
    }

    public void setFdThreadId(String fdThreadId) {
        this.fdThreadId = fdThreadId;
    }

    public String getFdTypeId() {
        return fdTypeId;
    }

    public void setFdTypeId(String fdTypeId) {
        this.fdTypeId = fdTypeId;
    }

    public String getSonFlag() {
        return sonFlag;
    }

    public void setSonFlag(String sonFlag) {
        this.sonFlag = sonFlag;
    }

    public String getIfMarket() {
        return ifMarket;
    }

    public void setIfMarket(String ifMarket) {
        this.ifMarket = ifMarket;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public Double getTradeArea() {
        return tradeArea;
    }

    public void setTradeArea(Double tradeArea) {
        this.tradeArea = tradeArea;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getAreaOrganId() {
        return areaOrganId;
    }

    public void setAreaOrganId(String areaOrganId) {
        this.areaOrganId = areaOrganId;
    }

    public Integer getLicCopyNum() {
        return licCopyNum;
    }

    public void setLicCopyNum(Integer licCopyNum) {
        this.licCopyNum = licCopyNum;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getProvideOrgan() {
        return provideOrgan;
    }

    public void setProvideOrgan(String provideOrgan) {
        this.provideOrgan = provideOrgan;
    }

    public String getProvideOrganGb() {
        return provideOrganGb;
    }

    public void setProvideOrganGb(String provideOrganGb) {
        this.provideOrganGb = provideOrganGb;
    }

    public Date getProvideDate() {
        return provideDate;
    }

    public void setProvideDate(Date provideDate) {
        this.provideDate = provideDate;
    }

    public Date getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(Date establishDate) {
        this.establishDate = establishDate;
    }

    public Date getDxDate() {
        return dxDate;
    }

    public void setDxDate(Date dxDate) {
        this.dxDate = dxDate;
    }

    public Date getDxZxDate() {
        return dxZxDate;
    }

    public void setDxZxDate(Date dxZxDate) {
        this.dxZxDate = dxZxDate;
    }

    public Date getZxDate() {
        return zxDate;
    }

    public void setZxDate(Date zxDate) {
        this.zxDate = zxDate;
    }

    public Date getCxDate() {
        return cxDate;
    }

    public void setCxDate(Date cxDate) {
        this.cxDate = cxDate;
    }

    public Date getCxZxDate() {
        return cxZxDate;
    }

    public void setCxZxDate(Date cxZxDate) {
        this.cxZxDate = cxZxDate;
    }

    public String getAcceptOrgan() {
        return acceptOrgan;
    }

    public void setAcceptOrgan(String acceptOrgan) {
        this.acceptOrgan = acceptOrgan;
    }

    public String getLicStatus() {
        return licStatus;
    }

    public void setLicStatus(String licStatus) {
        this.licStatus = licStatus;
    }

    public String getLicStatusGb() {
        return licStatusGb;
    }

    public void setLicStatusGb(String licStatusGb) {
        this.licStatusGb = licStatusGb;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getMoveToOrgan() {
        return moveToOrgan;
    }

    public void setMoveToOrgan(String moveToOrgan) {
        this.moveToOrgan = moveToOrgan;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPePerson() {
        return pePerson;
    }

    public void setPePerson(String pePerson) {
        this.pePerson = pePerson;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getIsMilk() {
        return isMilk;
    }

    public void setIsMilk(String isMilk) {
        this.isMilk = isMilk;
    }

    public String getFdNameGb() {
        return fdNameGb;
    }

    public void setFdNameGb(String fdNameGb) {
        this.fdNameGb = fdNameGb;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getAcceptOrganOld() {
        return acceptOrganOld;
    }

    public void setAcceptOrganOld(String acceptOrganOld) {
        this.acceptOrganOld = acceptOrganOld;
    }

    public String getMoveToOrganOld() {
        return moveToOrganOld;
    }

    public void setMoveToOrganOld(String moveToOrganOld) {
        this.moveToOrganOld = moveToOrganOld;
    }

    public String getIfSimply() {
        return ifSimply;
    }

    public void setIfSimply(String ifSimply) {
        this.ifSimply = ifSimply;
    }

    public String getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(String appTypeId) {
        this.appTypeId = appTypeId;
    }

    public String getOldProvideOrgan() {
        return oldProvideOrgan;
    }

    public void setOldProvideOrgan(String oldProvideOrgan) {
        this.oldProvideOrgan = oldProvideOrgan;
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

    public FpsiLtAddressEnty getFpsiLtAddressEnty() {
        return fpsiLtAddressEnty;
    }

    public void setFpsiLtAddressEnty(FpsiLtAddressEnty fpsiLtAddressEnty) {
        this.fpsiLtAddressEnty = fpsiLtAddressEnty;
    }

    public FpsiLtScopeEnty getFpsiLtScopeEnty() {
        return fpsiLtScopeEnty;
    }

    public void setFpsiLtScopeEnty(FpsiLtScopeEnty fpsiLtScopeEnty) {
        this.fpsiLtScopeEnty = fpsiLtScopeEnty;
    }

    public List<FpsiLtInspPlan> getFpsiLtInspPlans() {
        return fpsiLtInspPlans;
    }

    public void setFpsiLtInspPlans(List<FpsiLtInspPlan> fpsiLtInspPlans) {
        this.fpsiLtInspPlans = fpsiLtInspPlans;
    }
}
