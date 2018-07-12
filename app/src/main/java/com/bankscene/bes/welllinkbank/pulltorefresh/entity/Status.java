package com.bankscene.bes.welllinkbank.pulltorefresh.entity;

import java.io.Serializable;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class Status implements Serializable{
    private String Mkey;
    private String Mtitle;
    private String Mimg;
    private String Mimgurl;
    private String Mcontent;
    private String Mengtitle;
    private String Mengcontent;

    public String getMengtitle() {
        return Mengtitle;
    }

    public void setMengtitle(String mengtitle) {
        Mengtitle = mengtitle;
    }

    public String getMengcontent() {
        return Mengcontent;
    }

    public void setMengcontent(String mengcontent) {
        Mengcontent = mengcontent;
    }

    private static final long serialVersionUID = 12L;

    public String getMimgurl() {
        return Mimgurl;
    }

    public void setMimgurl(String mimgurl) {
        Mimgurl = mimgurl;
    }

    public String getMkey() {
        return Mkey;
    }

    public void setMkey(String mkey) {
        Mkey = mkey;
    }

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
    //    @Override
//    public String toString() {
//        return "Status{" +
//                "isRetweet=" + isRetweet +
//                ", text='" + text + '\'' +
//                ", userName='" + userName + '\'' +
//                ", userAvatar='" + userAvatar + '\'' +
//                ", createdAt='" + createdAt + '\'' +
//                '}';
//    }
}
