package com.bankscene.bes.welllinkbank.common;


public class CommDictAction {

    public static final String imageCode = Config.TRANS_IP_PORT + "GenTokenImg.do";//登录图像验证码
    public static final String loginNormal = Config.TRANS_IP_PORT + "login.do";//登录
    public static final String logout = Config.TRANS_IP_PORT + "logout.do";//退出登录
    //首頁樣式參數
    public static final int gridRow = 3;
    //    public static final int gridRow = 30;
    public static final int gridColumn = 3;
    public static final int gridItemCount = gridRow * gridColumn;
    public static final String appFileName="appdebug.apk";
    public static final String AppDownloadPath=Config.HOST_ADDRESS+"/static/androidApk/";

//    // produce key
//    public static final String SecurityPubKey = "8EC0A4B77DD65720A3FC566B2245E0985ADCC8180D9E5E25C318F610D9ABE6380D8DD59CB624246C87BCA2673036E4F8DEE3E5625FD7A0A265E05878ED142F387BD94B98A45FA76067D8F58813C438211A35FC922533E517ADE7D61C22C3A1C45FE031C50F0D71D6FCB6D69286946F93B591E73C642514E56566811367C3C289";
//    //test
    public static final String SecurityPubKey = "3197772c3b8598bb8f436f2d7ad4f7d48bacb58fc4c6ab9539831c654b1189e69d4f93bdadaa5ee79ad0ebf2a9574bd9c60352cc2a3a0ef3bbd13c5c1702f637a16a35bcf87448c4daa45a60b68c84b60b6f0048b3ce31e89e3f0054e7d0c65c5cbfa9ed9789bb96f90b1f30551238398dd4517918cf8757a224bc28a3aa952f9f95faa68443ec7b300e6fc5fc5703e5";
//    public static final String SecurityPubKey = "87e9a9c4468837f1c486f6d74af49ec0887f47d4db11ab24535ee15bf9bf664756f7bf21b7cfee00b358666d0b4bb0aff32ad8c1eb0f5939414addad0d881205e3ada0dc431bc798be75c4995e424b25ed142a8a9fd73267b117787d5bdb7dc0609a6381a0562e5a684eed561f2e01e16e2545717d8690e44abf998a913cb2258bd243c610ff0c2b0ddc84f83fc6ecbd";
//    public static final String Secrity3DESKey=  "d9ac0154de73fc798ea8b2165a7751a1c6ce5452c1cff939122c162fc02c244a8d1cd7723af2c2942829f288f1275b84f8017c1abbf456d2729154cec13a43cb486d8fa5d90fa7f1cc4fc511dec55098725798cdf52a9b996ac993bf9433271b853a69988d6385eb058fabc35408010d1dca076dde8df45a5410703504b9b787";
    public static final String QueryLoginState=Config.TRANS_IP_PORT+"QryLoginState.do";
    public static final String QueryNoticeList=Config.TRANS_IP_PORT+"MadvmsgQry.do";
    public static final String MadvmsgDetailedQry=Config.TRANS_IP_PORT+"MadvmsgDetailedQry.do";
    public static final String QryCifInfo=Config.TRANS_IP_PORT+"QryCifInfo.do";
    public static final String PictureModify=Config.TRANS_IP_PORT+"PictureModify.do";
    public static final String SaveGestureCode=Config.TRANS_IP_PORT+"UserPatternLockPasswordSetting.do";
    public static final String UpdatePassword=Config.TRANS_IP_PORT+"UpdatePassword.do";
    public static final String GetAdverTiseMent=Config.TRANS_IP_PORT+"AdvertListQry.do";
    public static final String ReSendSMSOTP=Config.TRANS_IP_PORT+"ReSendSMSOTP.do";
    public static final String RefreshTimeStamp=Config.TRANS_IP_PORT+"GetTimestamp.do";
    public static final String QryPublicKey=Config.TRANS_IP_PORT+"QryPublicKey.do";
    public static final String GetVersionInfo=Config.HOST_ADDRESS+"/static/androidApk/androidApkUpdInfo.json";
}
