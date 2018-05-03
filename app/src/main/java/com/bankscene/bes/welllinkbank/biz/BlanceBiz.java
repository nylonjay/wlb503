package com.bankscene.bes.welllinkbank.biz;

import java.io.Serializable;

/**
 * Created by Nylon on 2018/3/5.10:50
 */

public class BlanceBiz implements Serializable{
    private static final long serialVersionUID = 1L;

    String acc_type;
    String acc_num;
    String current_balance;
    String usable_balance;

    public String getAcc_type() {
        return acc_type;
    }

    public void setAcc_type(String acc_type) {
        this.acc_type = acc_type;
    }

    public String getAcc_num() {
        return acc_num;
    }

    public void setAcc_num(String acc_num) {
        this.acc_num = acc_num;
    }

    public String getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(String current_balance) {
        this.current_balance = current_balance;
    }

    public String getUsable_balance() {
        return usable_balance;
    }

    public void setUsable_balance(String usable_balance) {
        this.usable_balance = usable_balance;
    }
}
