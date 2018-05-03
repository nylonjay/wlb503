package com.bankscene.bes.welllinkbank.Util;

import android.util.Log;

/**
 * Created by tianwei on 2016/10/25.
 */
public class Trace {

    public static boolean isDebug = true;

    public final static void e(String tag, String msg) {

        if (isDebug) {
            for (int i = 0; i <= msg.length() / 2000; i++) {
                int max = 2000 * (i + 1);
                if (max >= msg.length()) {
                    Log.e(tag, msg.substring(2000 * i));
                } else {
                    Log.e(tag, msg.substring(2000 * i, max));
                }
            }
        }
    }

    public final static void key(String tag, String msg) {
        Log.e(tag, "***************************************");
        Log.e(tag, msg);
        Log.e(tag, "***************************************");
    }

}
