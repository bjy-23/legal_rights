package com.wonders.bean;

import java.io.Serializable;



/**
 * 计划执行人
 * @author wonders
 *
 */

public class FpsiPlanExcutor  {

	private static final long serialVersionUID = 7344609451532770155L;
	
	/** 主键 */
	private String uuid;
	
	/** 计划ID */
	private String planId;
	
	/** 任务执行人ID */
	private String userId;
	
	/** 任务执行人名称 */
	private String userName;
	
	/** 是否组长 */
	private String ifLeader;
	
	/** 是否已提交 */
	private String ifCommited;


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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIfLeader() {
		return ifLeader;
	}

	public void setIfLeader(String ifLeader) {
		this.ifLeader = ifLeader;
	}

	public String getIfCommited() {
		return ifCommited;
	}

	public void setIfCommited(String ifCommited) {
		this.ifCommited = ifCommited;
	}
}
