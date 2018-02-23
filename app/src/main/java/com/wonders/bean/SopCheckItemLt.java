package com.wonders.bean;

import java.util.ArrayList;

/**
 * Created by bjy on 2017/6/14.
 */

public class SopCheckItemLt {
    private String dicId;
    private String dicName;
    private ArrayList<SopBean> dicLtcheckTypes;

    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public ArrayList<SopBean> getDicLtcheckTypes() {
        return dicLtcheckTypes;
    }

    public void setDicLtcheckTypes(ArrayList<SopBean> dicLtcheckTypes) {
        this.dicLtcheckTypes = dicLtcheckTypes;
    }
}
