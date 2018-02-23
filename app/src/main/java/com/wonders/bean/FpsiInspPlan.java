package com.wonders.bean;

import java.util.Date;
import java.util.List;


/**
 * 监督计划
 * @author wonders
 *
 */

public class FpsiInspPlan  {

	private static final long serialVersionUID = -2667488018088753886L;
	
	/** 计划标识 */
	private String planId;
	
	/** 计划类型 */
	private String typeId;
	
	/** 企业标识 */
	private String etpsId;
	
	/** 计划年份 */
	private int planYear;
	
	/** 计划月份 */
	private int planMonth;
	
	/** 计划开始时间 */
	private Date startDate;
	
	/** 计划结束时间 */
	private Date endDate;
	
	/** 计划执行机关 */
	private String exeOrgan;
	
    /** 计划执行部门 */
    private String exeDept;
    
    /** 制定部门*/
    private String inspDept;
	
	/** 检查结果 */
	private String result;
	
	/** 备注 */
	private String remark;
	
	/** 计划状态 */
	private String status;
	
	/** 需回访标识，0：无需回访；1：需回访 */
	private String needRevisit;
	
	/** 需抽样标识，0：无需抽样，1：需抽样 */
	private String needSample;
	
	/** 需要回访时关联的父计划Id*/
	private String parentPlanId;
	
	/**1是市局 2分局 3所制定的计划*/
	private String ifSuo;
	
	/**陪同检查人*/
	private String accompaniedPeople;
	
	/**创建人*/
	private String createUser;
	
	/**创建时间*/
	private Date createDate;
	
	private String diyId;
	
	private List<FpsiPlanExcutor> fpsiPlanExcutor;
	
	/**
	 * 计划执行负责人
	 */
	private String excutorUser;

	private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}


	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}


	public String getEtpsId() {
		return etpsId;
	}

	public void setEtpsId(String etpsId) {
		this.etpsId = etpsId;
	}


	public int getPlanYear() {
		return planYear;
	}

	public void setPlanYear(int planYear) {
		this.planYear = planYear;
	}


	public int getPlanMonth() {
		return planMonth;
	}

	public void setPlanMonth(int planMonth) {
		this.planMonth = planMonth;
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


	public String getExeOrgan() {
		return exeOrgan;
	}

	public void setExeOrgan(String exeOrgan) {
		this.exeOrgan = exeOrgan;
	}
	

	public String getExeDept()
    {
        return exeDept;
    }

    public void setExeDept(String exeDept)
    {
        this.exeDept = exeDept;
    }


	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

	public String getNeedRevisit() {
		return needRevisit;
	}

	public void setNeedRevisit(String needRevisit) {
		this.needRevisit = needRevisit;
	}


	public String getNeedSample() {
		return needSample;
	}

	public void setNeedSample(String needSample) {
		this.needSample = needSample;
	}


	public List<FpsiPlanExcutor> getFpsiPlanExcutor() {
		return fpsiPlanExcutor;
	}

	public void setFpsiPlanExcutor(List<FpsiPlanExcutor> fpsiPlanExcutor) {
		this.fpsiPlanExcutor = fpsiPlanExcutor;
	}


	public String getParentPlanId() {
		return parentPlanId;
	}

	public void setParentPlanId(String parentPlanId) {
		this.parentPlanId = parentPlanId;
	}


	public String getIfSuo() {
		return ifSuo;
	}

	public void setIfSuo(String ifSuo) {
		this.ifSuo = ifSuo;
	}


	public String getInspDept() {
		return inspDept;
	}

	public void setInspDept(String inspDept) {
		this.inspDept = inspDept;
	}


	public String getAccompaniedPeople() {
		return accompaniedPeople;
	}

	public void setAccompaniedPeople(String accompaniedPeople) {
		this.accompaniedPeople = accompaniedPeople;
	}
	

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	

	public String getDiyId() {
		return diyId;
	}

	public void setDiyId(String diyId) {
		this.diyId = diyId;
	}


	public Object getPrimaryKey() {
		return this.planId;
	}

	public String getExcutorUser() {
		return excutorUser;
	}

	public void setExcutorUser(String excutorUser) {
		this.excutorUser = excutorUser;
	}

}
