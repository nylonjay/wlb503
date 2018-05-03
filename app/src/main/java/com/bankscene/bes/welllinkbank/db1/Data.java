package com.bankscene.bes.welllinkbank.db1;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by tianwei on 2017/9/20.
 */
@Entity
public class Data {

    @Id
    private long id;
    private String data;
    @Generated(hash = 1149445826)
    public Data(long id, String data) {
        this.id = id;
        this.data = data;
    }
    @Generated(hash = 2135787902)
    public Data() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }

}