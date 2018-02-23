package com.wonders.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class SopListViewBean implements Parcelable {
    private String id;
    private String planId;
    private String etpsId;
    private String etpsName;
    private String userId;
    private String year;
    private String month;
    private String firstDate;
    private String secondDate;
    private String itemCode;
    private String parentCode;
    private String checkCode;
    private String isKey;//0：一般项；1：重点项
    private String content;
    private String remark;
    private String isEdit;//1:编辑过；0：没编辑过
    private String isPass;//1:没有问题；0:有问题;"":还未操作
    private String isHavePic;//1,有图，0，无图
    private String longitude;
    private String latitude;
    private String dataType;
    private String address;
    private String checkBasis;// 检查依据
    private String checkRule;// 检查规程
    private String foucsNotes;// 重点注视
    private String faq;// 常见问题
    private String isNetWork;
    private int type;
    private int planType;
    private int kind;//!!需要保留
    private ArrayList<PicBean> pic ;//图片信息
    private String tradeType;

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getIsNetWork() {
        return isNetWork;
    }

    public void setIsNetWork(String isNetWork) {
        this.isNetWork = isNetWork;
    }

    public String getEtpsName() {
        return etpsName;
    }

    public void setEtpsName(String etpsName) {
        this.etpsName = etpsName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getEtpsId() {
        return etpsId;
    }

    public void setEtpsId(String etpsId) {
        this.etpsId = etpsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getSecondDate() {
        return secondDate;
    }

    public void setSecondDate(String secondDate) {
        this.secondDate = secondDate;
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

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getIsKey() {
        return isKey;
    }

    public void setIsKey(String isKey) {
        this.isKey = isKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    public String getIsHavePic() {
        return isHavePic;
    }

    public void setIsHavePic(String isHavePic) {
        this.isHavePic = isHavePic;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public ArrayList<PicBean> getPic() {
        return pic;
    }

    public void setPic(ArrayList<PicBean> pic) {
        this.pic = pic;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPlanType() {
        return planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }

    public String getCheckBasis() {
        return checkBasis;
    }

    public void setCheckBasis(String checkBasis) {
        this.checkBasis = checkBasis;
    }

    public String getCheckRule() {
        return checkRule;
    }

    public void setCheckRule(String checkRule) {
        this.checkRule = checkRule;
    }

    public String getFoucsNotes() {
        return foucsNotes;
    }

    public void setFoucsNotes(String foucsNotes) {
        this.foucsNotes = foucsNotes;
    }

    public String getFaq() {
        return faq;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(planId);
        dest.writeString(etpsId);
        dest.writeString(etpsName);
        dest.writeString(userId);
        dest.writeString(year);
        dest.writeString(month);
        dest.writeString(firstDate);
        dest.writeString(secondDate);
        dest.writeString(itemCode);
        dest.writeString(parentCode);
        dest.writeString(checkCode);
        dest.writeString(isKey);
        dest.writeString(content);
        dest.writeString(remark);
        dest.writeString(isEdit);
        dest.writeString(isPass);
        dest.writeString(isHavePic);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(dataType);
        dest.writeString(address);
        dest.writeString(checkBasis);
        dest.writeString(checkRule);
        dest.writeString(foucsNotes);
        dest.writeString(faq);
        dest.writeString(isNetWork);
        dest.writeInt(type);
        dest.writeInt(planType);
        dest.writeInt(kind);
        dest.writeList(pic);
    }

    public static final Creator<SopListViewBean> CREATOR = new Creator<SopListViewBean>() {
        @Override
        public SopListViewBean createFromParcel(Parcel source) {
            return new SopListViewBean(source);
        }

        @Override
        public SopListViewBean[] newArray(int size) {
            return new SopListViewBean[size];
        }
    };

    public SopListViewBean(Parcel in){
        id = in.readString();
        planId = in.readString();
        etpsId = in.readString();
        etpsName = in.readString();
        userId = in.readString();
        year = in.readString();
        month = in.readString();
        firstDate = in.readString();
        secondDate = in.readString();
        itemCode = in.readString();
        parentCode = in.readString();
        checkCode = in.readString();
        isKey = in.readString();
        content = in.readString();
        remark = in.readString();
        isEdit = in.readString();
        isPass = in.readString();
        isHavePic = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        dataType = in.readString();
        address = in.readString();
        checkBasis = in.readString();
        checkRule = in.readString();
        foucsNotes = in.readString();
        faq = in.readString();
        isNetWork = in.readString();
        type = in.readInt();
        planType = in.readInt();
        kind = in.readInt();
        pic = in.readArrayList(Thread.currentThread().getContextClassLoader());
    }

    public SopListViewBean(){

    }
}
