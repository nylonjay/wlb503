package com.bankscene.bes.welllinkbank.common;

/**
 * Created by Nylon on 2018/3/14.17:58
 */

public class Constant {
    public static final String PAGE_HEAD=Config.HOST_ADDRESS+"/app";

    public static final String EVENT_REFRESH_LANGUAGE="EVENT_REFRESH_LANGUAGE";

    public static final String QUERY_ACCOUNT_LIST= PAGE_HEAD+"/account/myaccountList";//查询账户列表
    public static final String SAMENAME_TRANSFER=PAGE_HEAD+"/account/sameNameTransfer";//同名转账
    public static final String ACCOUNT_TRANS_HISTORY=PAGE_HEAD+"/account/accountTransRecord";//账户转账记录
    public static final String THIRD_TRANS=PAGE_HEAD+"/account/thirdTransferPre";//第三方转账
    public static final String REGULAR_QUERY_LIST=PAGE_HEAD+"/fixedDeposit/queryCertificateNum";//查询定期列表
    public static final String REGULAR_QUERY_DETAIL=PAGE_HEAD+"/fixedDeposit/queryFixedDetailPre";//查询定期详情
    public static final String REGULAR_OPEN=PAGE_HEAD+"/fixedDeposit/fixDepositOpen";//定期存款开单
    public static final String REGULAR_CLOSE=PAGE_HEAD+"/fixedDeposit/fixedDepositClose";//定期存款销单初始页
    public static final String Query_Rate=PAGE_HEAD+"/queryExchange/interestRateList";//定期存款销单初始页
    public static final String THIRD_TRANS_CONFIRM=PAGE_HEAD+"/account/thirdTransferConfirm";//定期存款销单初始页

    public static final String Query_prce=PAGE_HEAD+"/queryExchange/exchangePrice";//定期存款销单初始页
    public static final String APPLY_TRANSFER=PAGE_HEAD+"/ApplyTransfer/ApplyTransferUnRegisterInput";//汇款.
    public static final String testdwdw=Config.HOST_ADDRESS+"/account/myaccountList";
    public static final String MY_LOANS=PAGE_HEAD+"/loan/loanRecord";


    public static final String EVENT_RELOAD_FRAGMENTS="EVENT_RELOAD_FRAGMENTS";
    public static String SHOW_PRE="SHOW_PRE";
    public static final int DISMISS=3;
    public static final String GESTURE_SETTED="GESTURE_SETTED";
    public static final String QUERY_CLIENT_INFO="QUERY_CLIENT_INFO";
    public static final String InitClientInfo="InitClientInfo";
    public static final String ImgName="head.png";
    public static final String LOGOUT_SUCCEED="LOGOUT_SUCCEED";
    public static final String  STOCK_TRANS=PAGE_HEAD+"/account/stockTransferPre";
}
