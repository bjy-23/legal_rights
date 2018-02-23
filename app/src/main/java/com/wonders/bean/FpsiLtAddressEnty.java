package com.wonders.bean;


import java.util.Date;

/**
 * FdAddressEnty entity. @author MyEclipse Persistence Tools
 */

public class FpsiLtAddressEnty {

    // Fields

    /**
     *
     */
    private static final long serialVersionUID = 6808526336580161609L;

    private String fdId;
    /**
    *属地区划(S)
    */
    private String areaId;
    /**
    *街道代码(S)
    */
    private String street;
    /**
    *路（村）(S)
    */
    private String road;
    /**
    *弄(S)
    */
    private String lane;
    /**
    *门牌号起(S)
    */
    private Integer doorStartNo;
    /**
    *门牌号止(S)
    */
    private Integer doorEndNo;
    /**
    *门牌号属性(S)
    */
    private String doorType;
    /**
    *室(S)
    */
    private String room;
    /**
    *属地区划(G)
    */
    private String areaIdGb;
    /**
    *省（区、市）(G)
    */
    private String province;
    /**
    *市（地、区）(G)
    */
    private String city;
    /**
    *县(G)
    */
    private String county;
    /**
    *街道(G)
    */
    private String streetGb;
    /**
    *门牌号(G)	
    */
    private String doorplate;
    /**
    *经营场所(G)
    */
    private String address;
    /**
    *住所(G)
    */
    private String realAddress;
    /**
    *产权人(G)
    */
    private String owner;
    /**
    *房屋使用期限(G)
    */
    private Short houUseLimitGb;
    /**
    *房屋使用截止期限(G)
    */
    private Date houUseLimit;
    /**
    *房屋使用方式(G)
    */
    private String houUseType;

    private FpsiLtInfoEnty fpsiLtInfoEnty;

    // Constructors

    /** default constructor */
    public FpsiLtAddressEnty() {
    }

    /** minimal constructor */
    public FpsiLtAddressEnty(String fdId) {
        this.fdId = fdId;
    }

    /** full constructor */
    public FpsiLtAddressEnty(String fdId, String areaId, String street, String road, String lane, Integer doorStartNo,
                             Integer doorEndNo, String doorType, String room, String areaIdGb, String province, String city,
                             String county, String streetGb, String doorplate, String address, String realAddress, String owner,
                             Short houUseLimitGb, Date houUseLimit, String houUseType) {
        this.fdId = fdId;
        this.areaId = areaId;
        this.street = street;
        this.road = road;
        this.lane = lane;
        this.doorStartNo = doorStartNo;
        this.doorEndNo = doorEndNo;
        this.doorType = doorType;
        this.room = room;
        this.areaIdGb = areaIdGb;
        this.province = province;
        this.city = city;
        this.county = county;
        this.streetGb = streetGb;
        this.doorplate = doorplate;
        this.address = address;
        this.realAddress = realAddress;
        this.owner = owner;
        this.houUseLimitGb = houUseLimitGb;
        this.houUseLimit = houUseLimit;
        this.houUseType = houUseType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFdId() {
        return fdId;
    }

    public void setFdId(String fdId) {
        this.fdId = fdId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public Integer getDoorStartNo() {
        return doorStartNo;
    }

    public void setDoorStartNo(Integer doorStartNo) {
        this.doorStartNo = doorStartNo;
    }

    public Integer getDoorEndNo() {
        return doorEndNo;
    }

    public void setDoorEndNo(Integer doorEndNo) {
        this.doorEndNo = doorEndNo;
    }

    public String getDoorType() {
        return doorType;
    }

    public void setDoorType(String doorType) {
        this.doorType = doorType;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getAreaIdGb() {
        return areaIdGb;
    }

    public void setAreaIdGb(String areaIdGb) {
        this.areaIdGb = areaIdGb;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getStreetGb() {
        return streetGb;
    }

    public void setStreetGb(String streetGb) {
        this.streetGb = streetGb;
    }

    public String getDoorplate() {
        return doorplate;
    }

    public void setDoorplate(String doorplate) {
        this.doorplate = doorplate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRealAddress() {
        return realAddress;
    }

    public void setRealAddress(String realAddress) {
        this.realAddress = realAddress;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Short getHouUseLimitGb() {
        return houUseLimitGb;
    }

    public void setHouUseLimitGb(Short houUseLimitGb) {
        this.houUseLimitGb = houUseLimitGb;
    }

    public Date getHouUseLimit() {
        return houUseLimit;
    }

    public void setHouUseLimit(Date houUseLimit) {
        this.houUseLimit = houUseLimit;
    }

    public String getHouUseType() {
        return houUseType;
    }

    public void setHouUseType(String houUseType) {
        this.houUseType = houUseType;
    }

    public FpsiLtInfoEnty getFpsiLtInfoEnty() {
        return fpsiLtInfoEnty;
    }

    public void setFpsiLtInfoEnty(FpsiLtInfoEnty fpsiLtInfoEnty) {
        this.fpsiLtInfoEnty = fpsiLtInfoEnty;
    }
}