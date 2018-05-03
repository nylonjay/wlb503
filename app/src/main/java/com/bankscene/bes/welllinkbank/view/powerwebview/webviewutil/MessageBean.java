package com.bankscene.bes.welllinkbank.view.powerwebview.webviewutil;

import java.util.List;

/**
 * Created by skythinking on 16/3/25.
 */
public class MessageBean {
    private String method;
    private List<String> types;
    private List<String> args;

    public MessageBean(){}

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "method='" + method + '\'' +
                ", types=" + types +
                ", args=" + args +
                '}';
    }
}
