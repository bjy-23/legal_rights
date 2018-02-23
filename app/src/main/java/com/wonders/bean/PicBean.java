package com.wonders.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PicBean implements Parcelable{
	private String picName;
	private int picNum;//第一张图片位置：0;
	private String picPath;//图片路径
	private String planId;
	private String userId;
	private String itemCode;
	private String checkContent;
	private String picSource;//网络图片的资源
	private int type;//0,网络图片；1，本地图片。
    private int model;//0,计划图片，1，计划项图片

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
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

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public int getPicNum() {
		return picNum;
	}

	public void setPicNum(int picNum) {
		this.picNum = picNum;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getCheckContent() {
		return checkContent;
	}

	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}

    public String getPicSource() {
        return picSource;
    }

    public void setPicSource(String picSource) {
        this.picSource = picSource;
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
        dest.writeString(picName);
        dest.writeString(picPath);
        dest.writeString(planId);
        dest.writeString(userId);
        dest.writeString(itemCode);
        dest.writeString(checkContent);
        dest.writeString(picSource);
        dest.writeInt(picNum);
        dest.writeInt(type);
        dest.writeInt(model);
    }

    public static final Parcelable.Creator<PicBean> CREATOR = new Creator<PicBean>() {
        @Override
        public PicBean createFromParcel(Parcel source) {
            return new PicBean(source);
        }

        @Override
        public PicBean[] newArray(int size) {
            return new PicBean[size];
        }
    };

    private PicBean(Parcel in){
        picName = in.readString();
        picPath = in.readString();
        planId = in.readString();
        userId = in.readString();
        itemCode = in.readString();
        checkContent = in.readString();
        picSource = in.readString();
        picNum = in.readInt();
        type = in.readInt();
        model = in.readInt();
    }

    public PicBean() {
    }
}
