package com.wonders.bean;

/**
 * Created by bjy on 2017/3/10.
 */

public class PlanTitle {
    private String name;
    private int type;//0,计划检查项；1,新增SOP检查项

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
