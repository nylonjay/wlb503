package com.bankscene.bes.welllinkbank.core;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.biz.FinanceMainBiz;
import com.bankscene.bes.welllinkbank.biz.MenuBiz;
import com.bankscene.bes.welllinkbank.biz.NameImgPair;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.exception.AppUncaughtExceptionHandler;
import com.bankscene.bes.welllinkbank.module.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.annotation.Encoding;
import com.okhttplib.cookie.PersistentCookieJar;
import com.okhttplib.cookie.cache.SetCookieCache;
import com.okhttplib.cookie.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;


public class BaseApplication extends Application {

    public static long TimeStamp = 0;//时间戳差值

    public static String deviceId = "";
    public static String deviceName = "";

    private static BaseApplication instance = null;

    private HashMap<String, Object> data;

    private Handler handler;
    private Intent intent;

    public static int HEIGHT;
    public static int WIDTH;
    public static float DENSITY;
    public static String REFRESH="3";
    public static String action_name="action";
    private User user;
    public static String USER_INDEX="user_index";
    public static String baseImagePath="";
    public static List<NameImgPair> nips=new ArrayList<>();
    public static String ver="1.0";
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        Fresco.initialize(this);
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            LeakCanary.install(this);
//        }
        data = new HashMap<>();
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        HEIGHT = dm.heightPixels;
        WIDTH = dm.widthPixels;
        DENSITY = dm.density;
        baseImagePath=Environment.getExternalStorageDirectory()+"/zip/";
        new File(baseImagePath).mkdirs();
//                +"head.png";
//        Config.isUmengSina = true;
//        UMShareAPI.get(this);
//        PlatformConfig.setWeixin("wx2c4d2e27a2573fd3", "53289c03d7943a4de0bcd0ad0611b192");
//        PlatformConfig.setSinaWeibo("2070055816", "40a93ba01cf651afbdb2682b9620f06c");
//        PlatformConfig.setQQZone("1105716349", "VEaLke02WVDa8Bo5");
        AppUncaughtExceptionHandler handler = AppUncaughtExceptionHandler.getInstance();
        handler.init(this);
        Thread.setDefaultUncaughtExceptionHandler(handler);

        //解决android 7.0调取系统相机崩溃的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        user = new User();
//        initOkGo();
//        initUserIndex();


        initOkHttp(this);
        DBHelper.getInstance().init(this);
        iniMenuList();
        DBHelper.insert(new Data(DataKey.isfirstLogin,"true"));
//        if (TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.language))){
//            //默认使用中文
//            DBHelper.insert(new Data(DataKey.language,"zh"));
//        }else{
//            changeAppLanguage();
//        }
        if (!TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.userName))){
            //如果进程在登陆的时候被手动关闭 数据库中应该还有用户名保存着  那么再启动的时候先清掉COOKIE
            DBHelper.insert(new Data(DataKey.cookie,""));
        }
        if (isZh()){
            //读取系统语言  更改APP语言
            changeAppLanguage("zh");
        }else {
            changeAppLanguage("en");
        }

        if (TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.login_type))){
            DBHelper.insert(new Data(DataKey.login_type,"false"));
        }

    }

    private boolean isZh() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }
    public void changeAppLanguage(String sta) {
//        String sta = isZh()? "en" : "zh";//这是SharedPreferences工具类，用于保存设置，代码很简单，自己实现吧
        // 本地语言设置
//        String sta=DBHelper.getDataByKey(DataKey.language);
        DBHelper.insert(new Data(DataKey.language,sta));
        Locale myLocale = new Locale(sta);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


    private void iniMenuList() {
        Trace.e("Application","iniMenuList");
        Gson gson=new Gson();
        String jsonListTest= DBHelper.getDataByKey(DataKey.user_index)+"";
        if (TextUtils.isEmpty(jsonListTest)){
            ArrayList<MenuBiz> mbs=new ArrayList<>();
            FinanceMainBiz fbz=new FinanceMainBiz(this);
            Object[] icons=fbz.getAllIcons();
            Object[] names=fbz.GetAllMenuNames();
            int menuLength=icons.length;
            for (int i=0;i<menuLength;i++){
                MenuBiz menuBiz=new MenuBiz();
                menuBiz.setIcon_Rsid((Integer) icons[i]);
                menuBiz.setMenu_Name((Integer) names[i]);
                if (i<11||menuBiz.getMenu_Name()==(R.string.custom)){
                    menuBiz.setIs_Checked(true);
                }
                else{
                    menuBiz.setIs_Checked(false);
                }
                mbs.add(menuBiz);
            }
            Type type=new TypeToken<List<MenuBiz>>(){}.getType();
            jsonListTest=gson.toJson(mbs, type);
            Trace.e("first_initMenuList",jsonListTest.toString());
            DBHelper.insert(new Data(DataKey.user_index,jsonListTest));
        }


    }


    void initOkHttp(Context context) {
        String downloadFileDir = Environment.getExternalStorageDirectory().getPath()+"/okHttp_download/";
        String cacheDir = Environment.getExternalStorageDirectory().getPath()+"/okHttp_cache";
        OkHttpClient okc= OkHttpUtil.init(context)
                .setConnectTimeout(30)//连接超时时间
                .setWriteTimeout(30)//写超时时间
                .setReadTimeout(30)//读超时时间
                .setMaxCacheSize(10 * 1024 * 1024)//缓存空间大小
                .setCacheType(CacheType.FORCE_NETWORK)//缓存类型
                .setHttpLogTAG("HttpLog")//设置请求日志标识
                .setIsGzip(false)//Gzip压缩，需要服务端支持
                .setShowHttpLog(true)//显示请求日志
                .setShowLifecycleLog(true)//显示Activity销毁日志
                .setRetryOnConnectionFailure(false)//失败后不自动重连
                .setCachedDir(new File(cacheDir))//设置缓存目录
                .setDownloadFileDir(downloadFileDir)//文件下载保存目录
                .setResponseEncoding(Encoding.UTF_8)//设置全局的服务器响应编码
                .setRequestEncoding(Encoding.UTF_8)//设置全局的请求参数编码
//                .setHttpsCertificate(getAssets().open("embs_fotinet_test.cer"))//设置全局Https证书
                .addResultInterceptor(HttpInterceptor.ResultInterceptor)//请求结果拦截器
                .addExceptionInterceptor(HttpInterceptor.ExceptionInterceptor)//请求链路异常拦截器
                .setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)))//持久化cookie
//                .setCookieJar(new CookieManager())
                .build().getDefaultClient();
//        okc=okc.newBuilder().sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();

//        new OkHttpClient.Builder().sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();
        Log.d("BaseProvider","OkHttp已初始化");
        Glide.get(this)
                .register(          //使用okhttp作为图片请求
                        GlideUrl.class
                        ,InputStream.class
                        ,new OkHttpUrlLoader.Factory(okc));

    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setData(String key, Object info) {
        if (null == data) {
            data = new HashMap<>();
        }
        data.put(key, info);
    }

    public Object getData(String key) {
        if (null == data)
            return null;
        Object info = data.get(key);
        data.remove(key);
        return info;
    }

    public void setKeepData(String key, Object info) {
        if (null == data) {
            data = new HashMap<>();
        }
        data.put(key, info);
    }

    public Object getKeepData(String key) {
        if (null == data) {
            return null;
        }
        return data.get(key);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getVersionName() {
        String versionName;
        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "1.0.0";
        }
        return versionName;
    }

//    private void initOkGo() {
//        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
//        HttpHeaders headers = new HttpHeaders();
////        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
////        headers.put("commonHeaderKey2", "commonHeaderValue2");
//        HttpParams params = new HttpParams();
//        params.put("BankId", "9999");
////        params.put("_channelId", "HTTP.MOBILE");
////        params.put("echannelType", "app");
//        params.put("Accept", "application/json");
//        params.put("Content-Type", "application/x-www-form-urlencoded");
////        params.put("Accept", "text/mobilejson");
//        //----------------------------------------------------------------------------------------//
//
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        //log相关
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
//        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
//        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
//        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
//        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
//        //builder.addInterceptor(new ChuckInterceptor(this));
//
//        //超时时间设置，默认60秒
//        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
//        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
//        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间
//
//        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
////        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
////        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失
//
//        //https相关设置，以下几种方案根据需要自己设置
//        //方法一：信任所有证书,不安全有风险
////        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
//        //方法二：自定义信任规则，校验服务端证书
////        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
//        //方法三：使用预埋证书，校验服务端证书（自签名证书）
////        HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("jxnxs.cer"));
//        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
////        builder.sslSocketFactory(sslParams3.sSLSocketFactory, sslParams3.trustManager);
//        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
////        builder.hostnameVerifier(new SafeHostnameVerifier());
//
////        HttpsUtils.SSLParams sslParams;
////        try {
////            sslParams = HttpsUtils.getSslSocketFactory(getAssets().open("jxnxs.cer"));
////        }catch (IOException e) {
////            sslParams = HttpsUtils.getSslSocketFactory();
////            e.printStackTrace();
////        }
////        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//
//        // 其他统一的配置
//        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
//        OkGo.getInstance().init(this)                           //必须调用初始化
//                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
//                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
//                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
//                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params);                       //全局公共参数
//
//    }

}
