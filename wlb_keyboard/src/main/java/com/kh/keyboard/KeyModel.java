package com.kh.keyboard;

/**
 * Created by Nylon on 2018/7/24.10:43
 */

public class KeyModel {
    String lable;
    int code;

    public KeyModel(int code, String lable) {
        this.lable = lable;
        this.code = code;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
