package com.bankscene.bes.welllinkbank.exception;

/**
 * Created by Nylon on 2018/5/11.10:34
 */

public class WLBException extends Exception {
    public WLBException() {
    }

    public WLBException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WLBException(String detailMessage) {
        super(detailMessage);
    }

    public WLBException(Throwable throwable) {
        super(throwable);
    }
}
