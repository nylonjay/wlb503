package com.bankscene.bes.welllinkbank.common;

/**
 * Created by Nylon on 2018/3/5.17:13
 */

public class Currency {
    public static String[] getCurrs(){
        return new String[]{USD,GBP,JPY,AUD,NZD,CAD,CHF,EUR,SGD,DKK,SEK,PHP,THB,TWD};
    }

    public static final String USD="USD";//美元
    public static final String GBP="GBP";//英鎊
    public static final String JPY="JPY";//日元
    public static final String AUD="AUD";//澳元
    public static final String NZD="NZD";//新西蘭元
    public static final String CAD="CAD";//加元
    public static final String CHF="CHF";//瑞士法郎
    public static final String EUR="EUR";//歐元
    public static final String SGD="SGD";//新加坡元
    public static final String DKK="DKK";//丹麥克朗
    public static final String SEK="SEK";//瑞典克朗
    public static final String PHP="PHP";//菲律賓比索
    public static final String THB="THB";//泰銖
    public static final String TWD="TWD";//新台幣

}
