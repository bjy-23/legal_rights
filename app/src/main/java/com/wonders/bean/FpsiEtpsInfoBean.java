package com.wonders.bean;

import java.util.List;

/**
 * Created by bjy on 2018/2/12.
 */

public class FpsiEtpsInfoBean {
    private String etpsName;
    private String address;
    private String organName;
    private String personName;
    private String telephone;
    private SuperviseRecordBean superviseRecord;
    private List<FpsiCertInfo> fpsiCertInfoList;

    public String getEtpsName() {
        return etpsName;
    }

    public void setEtpsName(String etpsName) {
        this.etpsName = etpsName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public SuperviseRecordBean getSuperviseRecord() {
        return superviseRecord;
    }

    public void setSuperviseRecord(SuperviseRecordBean superviseRecord) {
        this.superviseRecord = superviseRecord;
    }

    public List<FpsiCertInfo> getFpsiCertInfoList() {
        return fpsiCertInfoList;
    }

    public void setFpsiCertInfoList(List<FpsiCertInfo> fpsiCertInfoList) {
        this.fpsiCertInfoList = fpsiCertInfoList;
    }
}
