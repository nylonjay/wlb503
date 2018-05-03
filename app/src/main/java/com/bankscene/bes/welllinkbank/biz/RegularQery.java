package com.bankscene.bes.welllinkbank.biz;

/**
 * Created by Nylon on 2018/3/3.15:51
 */

public class RegularQery {
    String date;
    String buss_type;
    String currency_type;
    String trade_amount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBuss_type() {
        return buss_type;
    }

    public void setBuss_type(String buss_type) {
        this.buss_type = buss_type;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getTrade_amount() {
        return trade_amount;
    }

    public void setTrade_amount(String trade_amount) {
        this.trade_amount = trade_amount;
    }
}
