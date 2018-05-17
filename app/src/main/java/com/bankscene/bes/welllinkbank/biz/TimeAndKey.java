package com.bankscene.bes.welllinkbank.biz;

/**
 * Created by Nylon on 2018/5/16.09:45
 */

public class TimeAndKey {
    private String timestamp;
    private String hms;
    private String dbp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHms() {
        return hms;
    }

    public void setHms(String hms) {
        this.hms = hms;
    }

    public String getDbp() {
        return dbp;
    }

    public void setDbp(String dbp) {
        this.dbp = dbp;
    }
}
