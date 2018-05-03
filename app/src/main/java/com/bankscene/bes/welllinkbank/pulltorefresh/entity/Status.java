package com.bankscene.bes.welllinkbank.pulltorefresh.entity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class Status {
    private String Mkey;
    private String Mtitle;
    private String Mimg;
    private String Mcontent;
    private static final long serialVersionUID = 12L;

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
