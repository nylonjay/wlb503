package com.bankscene.bes.welllinkbank.biz;

import java.io.Serializable;

/**
 * Created by Nylon on 2018/3/5.16:47
 */

public class QuoteBiz implements Serializable {
    private static final long serialVersionUID = 2L;
    private String currency;
    private String tel_in_price;
    private String tel_out_price;
    private String oof_in_price;
    private String oof_out_price;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTel_in_price() {
        return tel_in_price;
    }

    public void setTel_in_price(String tel_in_price) {
        this.tel_in_price = tel_in_price;
    }

    public String getTel_out_price() {
        return tel_out_price;
    }

    public void setTel_out_price(String tel_out_price) {
        this.tel_out_price = tel_out_price;
    }

    public String getOof_in_price() {
        return oof_in_price;
    }

    public void setOof_in_price(String oof_in_price) {
        this.oof_in_price = oof_in_price;
    }

    public String getOof_out_price() {
        return oof_out_price;
    }

    public void setOof_out_price(String oof_out_price) {
        this.oof_out_price = oof_out_price;
    }
}
