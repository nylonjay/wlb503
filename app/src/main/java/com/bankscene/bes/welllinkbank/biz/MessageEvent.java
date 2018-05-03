package com.bankscene.bes.welllinkbank.biz;

/**
 * Created by Nylon on 2018/2/6.16:20
 */

public class MessageEvent {
    private String message;
    private int what;
    private Object o;

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public MessageEvent(String message){
        this.message=message;
    }
    public MessageEvent(int what){
        this.what=what;
    }
    public MessageEvent(Object o,int what){
        this.o=0;
        this.what=what;
    }
    public MessageEvent(Object o,int what,String message){
        this.o=0;
        this.what=what;
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
