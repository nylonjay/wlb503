package com.bankscene.bes.welllinkbank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Nylon on 2018/5/9.14:54
 */

public class WlbEditText extends EditText{
    private String timestamp;//时间戳
    private String dbp;//公钥
    private String hms;

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

    public WlbEditText(Context context, String timestamp, String dbp, String hms) {
        super(context);
        this.timestamp = timestamp;
        this.dbp = dbp;
        this.hms=hms;

    }
    public WlbEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public WlbEditText(Context context) {
        super(context);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
