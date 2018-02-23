package com.wonders.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

public class PlanBean implements Parcelable{
	private String address;
	private String etpsName;
	private String planMonth;
	private String allUserName;
	private String planId;
	private String etpsId;
	private String exeOrgan;//检查机关
	private String legalPerson;//联系人
	private String phoneNo;//电话
	private String certNo;//许可证
	private boolean isSelect = false;
    private JSONArray countList;
    private String checkNo;//记录
    private String startDate;
    private String remark;
    private String opinion;
    private int showType;//0，离线模式；1，在线模式

	// 0 未分配日常监督检查待办计划 1 未分配专项整治待办计划 2 已分配日常监督检查待办计划 3 已分配专项整治待办计划 4 非组长成员的待办计划
	private int type;

    private String personNameGaozhi = "　　　　　　　　　";
    private String personNoGaozhi = "　　　　　　　　　";
    private String certNameGaozhi = "　　　　　　　　　";
    private String certNoGaozhi = "　　　　　　　　　";
    private String checkDateGaozhi = "   年   月   日";
    private String checkAddressGaozhi = "";

    public PlanBean(){

    }

    protected PlanBean(Parcel in) {
        address = in.readString();
        etpsName = in.readString();
        planMonth = in.readString();
        allUserName = in.readString();
        planId = in.readString();
        etpsId = in.readString();
        exeOrgan = in.readString();
        legalPerson = in.readString();
        phoneNo = in.readString();
        certNo = in.readString();
        isSelect = in.readByte() != 0;
        checkNo = in.readString();
        startDate = in.readString();
        remark = in.readString();
        opinion = in.readString();
        type = in.readInt();
        personNameGaozhi = in.readString();
        personNoGaozhi = in.readString();
        certNameGaozhi = in.readString();
        certNoGaozhi = in.readString();
        checkDateGaozhi = in.readString();
        checkAddressGaozhi = in.readString();
        showType = in.readInt();
    }

    public static final Creator<PlanBean> CREATOR = new Creator<PlanBean>() {
        @Override
        public PlanBean createFromParcel(Parcel in) {
            return new PlanBean(in);
        }

        @Override
        public PlanBean[] newArray(int size) {
            return new PlanBean[size];
        }
    };

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getPersonNameGaozhi() {
        return personNameGaozhi;
    }

    public void setPersonNameGaozhi(String personNameGaozhi) {
        this.personNameGaozhi = personNameGaozhi;
    }

    public String getPersonNoGaozhi() {
        return personNoGaozhi;
    }

    public void setPersonNoGaozhi(String personNoGaozhi) {
        this.personNoGaozhi = personNoGaozhi;
    }

    public String getCertNameGaozhi() {
        return certNameGaozhi;
    }

    public void setCertNameGaozhi(String certNameGaozhi) {
        this.certNameGaozhi = certNameGaozhi;
    }

    public String getCertNoGaozhi() {
        return certNoGaozhi;
    }

    public void setCertNoGaozhi(String certNoGaozhi) {
        this.certNoGaozhi = certNoGaozhi;
    }

    public String getCheckDateGaozhi() {
        return checkDateGaozhi;
    }

    public void setCheckDateGaozhi(String checkDateGaozhi) {
        this.checkDateGaozhi = checkDateGaozhi;
    }

    public String getCheckAddressGaozhi() {
        return checkAddressGaozhi;
    }

    public void setCheckAddressGaozhi(String checkAddressGaozhi) {
        this.checkAddressGaozhi = checkAddressGaozhi;
    }

    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEtpsName() {
		return etpsName;
	}

	public void setEtpsName(String etpsName) {
		this.etpsName = etpsName;
	}

	public String getPlanMonth() {
		return planMonth;
	}

	public void setPlanMonth(String planMonth) {
		this.planMonth = planMonth;
	}

	public String getAllUserName() {
		return allUserName;
	}

	public void setAllUserName(String allUserName) {
		this.allUserName = allUserName;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getEtpsId() {
		return etpsId;
	}

	public void setEtpsId(String etpsId) {
		this.etpsId = etpsId;
	}

	public String getExeOrgan() {
		return exeOrgan;
	}

	public void setExeOrgan(String exeOrgan) {
		this.exeOrgan = exeOrgan;
	}

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public JSONArray getCountList() {
        return countList;
    }

    public void setCountList(JSONArray countList) {
        this.countList = countList;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(etpsName);
        dest.writeString(planMonth);
        dest.writeString(allUserName);
        dest.writeString(planId);
        dest.writeString(etpsId);
        dest.writeString(exeOrgan);
        dest.writeString(legalPerson);
        dest.writeString(phoneNo);
        dest.writeString(certNo);
        dest.writeByte((byte) (isSelect ? 1 : 0));
        dest.writeString(checkNo);
        dest.writeString(startDate);
        dest.writeString(remark);
        dest.writeString(opinion);
        dest.writeInt(type);
        dest.writeString(personNameGaozhi);
        dest.writeString(personNoGaozhi);
        dest.writeString(certNameGaozhi);
        dest.writeString(certNoGaozhi);
        dest.writeString(checkDateGaozhi);
        dest.writeString(checkAddressGaozhi);
        dest.writeInt(showType);
    }
}
