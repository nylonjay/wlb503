package com.bankscene.bes.welllinkbank.view.droplistview;

/**
 * Created by Administrator on 2015/5/28.
 */
public class DropItemObject {

    private int id;
    private String text;
    private String value;

    public DropItemObject(String text, int id, String value) {
        this.text = text;
        this.id = id;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getText(){
        return text;
    }

    public int getId() {
        return id;
    }
}
