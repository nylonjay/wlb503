package com.bankscene.bes.welllinkbank.biz;

/**
 * Created by Nylon on 2018/4/3.14:14
 */

public class ClientInfoBiz {
    private static final long serialVersionUID = 123L;
    String CHN_NAME;
    String TEL_NO;
    String ENG_NAME;
    String image;

    public String getCHN_NAME() {
        return CHN_NAME;
    }

    public void setCHN_NAME(String CHN_NAME) {
        this.CHN_NAME = CHN_NAME;
    }

    public String getTEL_NO() {
        return TEL_NO;
    }

    public void setTEL_NO(String TEL_NO) {
        this.TEL_NO = TEL_NO;
    }

    public String getENG_NAME() {
        return ENG_NAME;
    }

    public void setENG_NAME(String ENG_NAME) {
        this.ENG_NAME = ENG_NAME;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
