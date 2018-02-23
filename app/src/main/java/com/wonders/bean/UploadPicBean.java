package com.wonders.bean;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 1229 on 2016/3/15.
 */
public class UploadPicBean {
    private File file;
    private String picName;
    private String picNum;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicNum() {
        return picNum;
    }

    public void setPicNum(String picNum) {
        this.picNum = picNum;
    }
}
