package com.bankscene.bes.welllinkbank.core;

/**
 * Created by tianwei on 2017/4/25.
 */

public class State {

    public static boolean isLogin = false;
    public static boolean isLoginTemporary = false;
    public static boolean loginCalled = false;


    public static boolean verifyRealName = false;
    public static boolean hasBankCard = false;
    public static boolean hasPayPassword = false;

    public static boolean gestureLoginOpen = true;
    public static boolean showTrail=true;
    public static boolean isFirstLogin=false;
    public static boolean isGestureLogin=false;
}
