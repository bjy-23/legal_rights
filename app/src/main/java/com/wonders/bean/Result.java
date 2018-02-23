package com.wonders.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bjy on 2016/11/7.
 */
public class Result<T> {
    @SerializedName(value = "code")
    private int code;
    @SerializedName(value = "message")
    private String message;
    @SerializedName("object")
    private T object;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
