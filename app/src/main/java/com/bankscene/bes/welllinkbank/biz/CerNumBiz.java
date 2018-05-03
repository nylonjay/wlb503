package com.bankscene.bes.welllinkbank.biz;

import java.io.Serializable;

/**
 * Created by Nylon on 2018/3/5.19:58
 */

public class CerNumBiz implements Serializable{
    private static final long serialVersionUID = 3L;
    String currency;
    String certiNum;
    String out_date;
    String state;
    String certi_type;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCertiNum() {
        return certiNum;
    }

    public void setCertiNum(String certiNum) {
        this.certiNum = certiNum;
    }

    public String getOut_date() {
        return out_date;
    }

    public void setOut_date(String out_date) {
        this.out_date = out_date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCerti_type() {
        return certi_type;
    }

    public void setCerti_type(String certi_type) {
        this.certi_type = certi_type;
    }
}
