package com.wonders.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bjy on 2017/2/23.
 */

public class RecordBean implements Parcelable {
    private String checkNum;
    private String checkDate;
    private int itemCounts;
    private int highCounts;//重点项个数
    private String highContents;
    private int highProCounts;//重点项的问题项个数
    private String highProContents;
    private int lowCounts;//一般项个数
    private String lowContents;
    private int lowProCounts;//一般项的问题项个数
    private String lowProContents;

    public String getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public int getItemCounts() {
        return itemCounts;
    }

    public void setItemCounts(int itemCounts) {
        this.itemCounts = itemCounts;
    }

    public int getHighCounts() {
        return highCounts;
    }

    public void setHighCounts(int highCounts) {
        this.highCounts = highCounts;
    }

    public String getHighContents() {
        return highContents;
    }

    public void setHighContents(String highContents) {
        this.highContents = highContents;
    }

    public int getHighProCounts() {
        return highProCounts;
    }

    public void setHighProCounts(int highProCounts) {
        this.highProCounts = highProCounts;
    }

    public String getHighProContents() {
        return highProContents;
    }

    public void setHighProContents(String highProContents) {
        this.highProContents = highProContents;
    }

    public int getLowCounts() {
        return lowCounts;
    }

    public void setLowCounts(int lowCounts) {
        this.lowCounts = lowCounts;
    }

    public String getLowContents() {
        return lowContents;
    }

    public void setLowContents(String lowContents) {
        this.lowContents = lowContents;
    }

    public int getLowProCounts() {
        return lowProCounts;
    }

    public void setLowProCounts(int lowProCounts) {
        this.lowProCounts = lowProCounts;
    }

    public String getLowProContents() {
        return lowProContents;
    }

    public void setLowProContents(String lowProContents) {
        this.lowProContents = lowProContents;
    }

    protected RecordBean(Parcel in) {
        checkNum = in.readString();
        checkDate = in.readString();
        itemCounts = in.readInt();
        highCounts = in.readInt();
        highContents = in.readString();
        highProCounts = in.readInt();
        highProContents = in.readString();
        lowCounts = in.readInt();
        lowContents = in.readString();
        lowProCounts = in.readInt();
        lowProContents = in.readString();
    }

    public static final Creator<RecordBean> CREATOR = new Creator<RecordBean>() {
        @Override
        public RecordBean createFromParcel(Parcel in) {
            return new RecordBean(in);
        }

        @Override
        public RecordBean[] newArray(int size) {
            return new RecordBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(checkNum);
        dest.writeString(checkDate);
        dest.writeInt(itemCounts);
        dest.writeInt(highCounts);
        dest.writeString(highContents);
        dest.writeInt(highProCounts);
        dest.writeString(highProContents);
        dest.writeInt(lowCounts);
        dest.writeString(lowContents);
        dest.writeInt(lowProCounts);
        dest.writeString(lowProContents);
    }

    public RecordBean() {
    }
}
