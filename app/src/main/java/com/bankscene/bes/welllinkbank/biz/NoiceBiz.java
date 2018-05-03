package com.bankscene.bes.welllinkbank.biz;

import java.io.Serializable;

/**
 * Created by Nylon on 2018/4/2.10:14
 */

public class NoiceBiz implements Serializable{
    private static final long serialVersionUID = 2L;
    String Mtitle;//标题
    String Mimg;//图片base64
    String Mcontent;//内容

    public String getMtitle() {
        return Mtitle;
    }

    public void setMtitle(String mtitle) {
        Mtitle = mtitle;
    }

    public String getMimg() {
        return Mimg;
    }

    public void setMimg(String mimg) {
        Mimg = mimg;
    }

    public String getMcontent() {
        return Mcontent;
    }

    public void setMcontent(String mcontent) {
        Mcontent = mcontent;
    }

    public String getMkey() {
        return Mkey;
    }

    public void setMkey(String mkey) {
        Mkey = mkey;
    }

    String Mkey;//公告id
}
