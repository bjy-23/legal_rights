package com.wonders.bean;

/**
 * Created by bjy on 2018/2/12.
 */

public class SopTitleBean {
    private String name;
    private boolean isImageAdd;

    public SopTitleBean(String name, boolean isImageAdd) {
        this.name = name;
        this.isImageAdd = isImageAdd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isImageAdd() {
        return isImageAdd;
    }

    public void setImageAdd(boolean imageAdd) {
        isImageAdd = imageAdd;
    }
}
