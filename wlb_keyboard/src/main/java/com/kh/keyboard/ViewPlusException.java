package com.kh.keyboard;


import utils.Err;

/**
 * Created by Nylon on 2018/3/16.17:46
 */

class ViewPlusException extends Exception{

    private String code;

    public String getCode() {
        return code;
    }

    public ViewPlusException() {
    }

    public ViewPlusException(String message) {
        super(message);
    }

    public ViewPlusException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViewPlusException(Throwable cause) {
        super(cause);
    }

    public ViewPlusException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ViewPlusException(Err.CodeAndErrMsg codeAndErrMsg) {
        this(codeAndErrMsg.getMsg(), codeAndErrMsg.getCode());
    }

    // TODO
//    @TargetApi(Build.VERSION_CODES.N)
//    public ViewPlusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
//        super(message, cause, enableSuppression, writableStackTrace);
//    }


    public static void illegalArgument(String msg, Object... params)
    {
        throw new IllegalArgumentException(String.format(msg, params));
    }
}
