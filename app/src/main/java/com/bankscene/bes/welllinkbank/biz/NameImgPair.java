package com.bankscene.bes.welllinkbank.biz;

/**
 * Created by Nylon on 2018/4/9.20:27
 */

public class NameImgPair {
    private static final long serialVersionUID = 22L;
    String loginId;
    String imagePath;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
