package com.bankscene.bes.welllinkbank.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.Util.dialog.DialogCallBack;
import com.bankscene.bes.welllinkbank.activity.LoginTablayoutActivity;
import com.bankscene.bes.welllinkbank.activity.PassWordDialogActivity;
import com.bankscene.bes.welllinkbank.activity.mine.password.CodeReset2;
import com.bankscene.bes.welllinkbank.activity.mine.password.TradeCodeReset;
import com.bankscene.bes.welllinkbank.activity.pdf.AssetOnSDActivity;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.common.Config;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.view.powerwebview.powerlib.PowerWebView;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.kh.keyboard.CSIICypher;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by tianwei on 2017/4/25.
 */

public class WebViewActivity extends HttpActivity implements View.OnClickListener, PowerWebView.Listener {

    private static int VERSION_CODES_LOLLIPOP = 21;
    public String url = "";

    private String title = "";

    ////
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.re_content)
    RelativeLayout re_content;
    private final int SHOW_KEYBOARD = 1;
    private ProgressDialog progressDialog;


    @Override
    public int setLayoutId() {
        return R.layout.activity_webview;
    }

    public void Close2Home(View v) {
        WebViewActivity.this.finish();
    }

    @Override
    public void setActionBar() {
//        actionBar.setActionBar(title, TranslucentActionBar.ICON_BACK, "",
//                TranslucentActionBar.ICON_NULL, "",
//                new ActionBarClickListener() {
//                    @Override
//                    public void onLeftClick() {
//                        onBackPressed();
//                    }
//
//                    @Override
//                    public void onRightClick() {
////                        showCashier("OMS1706212794139","0","");
//                        inputPasswordView("");
//                    }
//                });

    }

    public void IninActionBar(String title) {
        actionBar.setVisibility(View.VISIBLE);
        actionBar.setActionBar(title, R.string.wlb_arrow_l, "",
                0, "",
                new ActionBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        WebViewActivity.this.finish();
                    }

                    @Override
                    public void onRightClick() {
//                        showCashier("OMS1706212794139","0","");
                    }
                });
    }

    @Override
    protected void initData() {

    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initView() {
//        rootView = findViewById(R.id.root);
        String showActionBar = getIntent().getStringExtra("showActionBar");
        String title = getIntent().getStringExtra("title");
        String localHtml = getIntent().getStringExtra("localHtml");
        if (!TextUtils.isEmpty(showActionBar)) {
            IninActionBar(title);
        } else {
            actionBar.setStatusBarHeight();
        }

        webView.clearCache(true);
        webView.clearHistory();

        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);

        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setDatabaseEnabled(true);
// 缓存白屏
        String appCachePath = getApplicationContext().getCacheDir()
                .getAbsolutePath() + "/webcache";
// 设置 Application Caches 缓存目录
        webSettings.setAppCachePath(appCachePath);
        webSettings.setDatabasePath(appCachePath);
        if (isNetworkAvailable(WebViewActivity.this)) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            Trace.e(TAG, "onCreate: " + "有网络");
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            Trace.e(TAG, "onCreate: " + "无网络");
        }
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webView.addJavascriptInterface(new JavacriptInterface(), "android");
//        webView.setListener(this, this);
//        webView.setWebChromeClient(new CustomChromeClient("App", HostJsScope.class, progressBar, WebViewActivity.this));
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Trace.e("progress==", newProgress + "");
                if (null != progressBar) {
                    if (newProgress == 100) {
                        progressBar.setVisibility(View.GONE);
                    }
                    progressBar.setProgress(newProgress);
                }
            }


        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {

                    view.loadUrl(url);
                    webView.stopLoading();
                    return true;
                }

                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Trace.e("error","205");
                 if (error.getPrimaryError() == SslError.SSL_INVALID||error.getPrimaryError()==SslError.SSL_UNTRUSTED) {
                // 如果手动校验sha256成功就允许加载页面
                if (isSSLCertOk(error.getCertificate(), Config.CERHASHCODE)) {
                    handler.proceed();
                } else {
                    try {
                        new AlertDialog.Builder(WebViewActivity.this)
                                .setTitle(WebViewActivity.this.getResources().getString(R.string.promot))
                                .setMessage(WebViewActivity.this.getResources().getString(R.string.ssl_error))
                                .setPositiveButton(WebViewActivity.this.getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       WebViewActivity.this.finish();
                                    }
                                }).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else{
                handler.cancel();
            }

        }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
                Trace.e("error","235"+error.getDescription().toString());
//                actionBar.setVisibility(View.GONE);
                setStatusBarColor(WebViewActivity.this,R.color.error_404);
                findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
            }
        });


        if (!TextUtils.isEmpty(localHtml)){
            Trace.e("loclahtml:",localHtml);
            webView.loadUrl(localHtml);
            return;
        }
        String address=getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(address))
            url = address;
        Trace.e(TAG, "will load page: " + url);
        if (State.isLogin){
            synCookies(url, webView);
//            syncCookie4WebView(this,DBHelper.getDataByKey(DataKey.cookie),url);
        }

//        Trace.e("sdoul================",url);
//        if (!url.startsWith(Config.HOST_ADDRESS)){
//
//            dialogUtils.ShowDialogOne(getResources().getString(R.string.promot), getResources().getString(R.string.ssl_error), getResources().getString(R.string.confirm), new DialogCallBack() {
//                @Override
//                public void onPositive() {
//                    WebViewActivity.this.finish();
//                }
//
//                @Override
//                public void onNegative() {
////                                        startActivity(new Intent(CodeReset2.this, TradeCodeReset.class));
//                }
//            });
//        }else {
//        }
        webView.loadUrl(url);


    }

    public static void syncCookie4WebView(Context context, String cookie, String url) {
        removeCookie4WebView(context);
        if (Build.VERSION.SDK_INT < VERSION_CODES_LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

//        NylonLogUtil.I("oldcookie=="+cookie);
        cookieManager.setCookie(url, cookie);// 如果没有特殊需求，这里只需要将session JSESSIONID=C80ADB46B3F92AE07210E5D680C32355
        // id以"key=value"形式作为cookie即可
        String newCookie = cookieManager.getCookie(url);
//        NylonLogUtil.E("new cookie："+newCookie);
        if (Build.VERSION.SDK_INT < VERSION_CODES_LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }
    public static void removeCookie4WebView(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();// Removes all cookies.

        if (Build.VERSION.SDK_INT < VERSION_CODES_LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        } else {
            // CookieManager.getInstance().flush();
        }
    }
    final class JavacriptInterface{
        @JavascriptInterface
        public void Back2Homepage(){
            Trace.e(TAG,"BACK2HOME");
            WebViewActivity.this.finish();
        }
        @JavascriptInterface
        public void Back2Login(){
            startActivity(new Intent(WebViewActivity.this, LoginTablayoutActivity.class));
            WebViewActivity.this.finish();
        }
        @JavascriptInterface
        public void ShowKeyBoard(String timestamp){
            GetTimeStampAndKeyWithoutEditor();
            Intent in =new Intent(WebViewActivity.this, PassWordDialogActivity.class);
            in.putExtra("timestamp",timestamp);
            startActivityForResult(in,SHOW_KEYBOARD);

        }
        @JavascriptInterface
        public String langType(){
            Trace.e("langtype--","");
            return DBHelper.getDataByKey(DataKey.language);

        }
        @JavascriptInterface
        public void DownLoadPDF(String account,String date){
            Trace.e("acccc===",account+"///////////"+"date");
            downFile(account,date);
        }

    }
    void downFile(String account,String date) {
        progressDialog = new ProgressDialog(WebViewActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(getResources().getString(R.string.downloading));
        progressDialog.setMessage(getResources().getString(R.string.wait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.show();
//        updateInfoService.downLoadFile(url, progressDialog,handler1);
        HashMap params=new HashMap();
        params.put("_ChannelId","PMBS");
//        params.put()
//        account="300005979213";
        String parameters="&"+"acctNo="+account+"&"+"month="+date;
        downloadPDF(progressDialog,handler1, CommDictAction.DownLoadPDF+parameters,params);
    }

    Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2:
                    String url= (String) msg.obj;
//                    noticeUtils.showNotice(getResources().getString(R.string.download_succeed));
                    Intent in=new Intent(WebViewActivity.this, AssetOnSDActivity.class);
                    in.putExtra("pdf",url);
                    startActivity(in);
                    break;
                case 404:
                    noticeUtils.showNotice(getResources().getString(R.string.download_failed));
                    break;
            }
        }
    };
    @Override
    protected void setListener() {
    }

    @Override
    protected void onResume() {
        super.onResume();

        //        if (State.isLogin && State.loginCalled) {
//            webView.loadUrl("javascript:loginSuccess()");
//        }
        State.loginCalled = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        destroyWebView();
    }

    public void destroyWebView() {

//        re_content.removeAllViews();
        removeCookie4WebView(this);
        if (webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            webView.freeMemory();
            webView.pauseTimers();
            webView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
//        webView.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode){
            case SHOW_KEYBOARD:
                if (resultCode== Activity.RESULT_OK){
                    if (null!=intent){
                        try {
                            String code=intent.getStringExtra("engrypted");
                            String encryped = new CSIICypher().encryptWithJiamiJi(code,dbp,hms,timestamp,"UTF-8",2);
                            String url="javascript:getPassWord(\""+encryped.replace("'","")+"\")";
                            webView.loadUrl(url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (webView.canGoBack()) {
                    Trace.e(TAG, webView.getUrl());
                    if (webView.getUrl().equals(url)) {
                        super.onBackPressed();
                    } else {
                        webView.goBack();
                    }
                } else {
                    super.onBackPressed();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            Trace.e(TAG, webView.getUrl());
            if (webView.getUrl().equals(url)) {
                super.onBackPressed();
            } else {
                webView.loadUrl("javascript:goBack()");
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onInterceptUrl(String url) {
//        在此可以写拦截url的逻辑,注意,拦截以后,请返回true,可以解决事件冲突

        Trace.e(TAG, "onInterceptUrl: " + url);
//        if (url != null && url.contains("/MemberLogin.do")) {
//            webView.goBackOrForward(-1);
//            return true;
//        }
        return false;
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        Trace.e(TAG, "onPageStarted: " + url);
    }


    @Override
    public void onPageFinished(String url) {
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
    }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
    }

    @Override
    public void onExternalPageRequest(String url) {
    }

    public void synCookies(String url, WebView v) {

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        Trace.e(TAG, "synCookies: removeAllCookie");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(v, true);
        }
        Trace.e(" 將要同步的cookie==",DBHelper.getDataByKey(DataKey.cookie));
        cookieManager.setCookie(url,DBHelper.getDataByKey(DataKey.cookie));

        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }

//    *************************************************************************

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        }
    }

    public void setTitle(String title) {
        this.title = title;
        actionBar.setTitle(title);
    }

    public void openPDF(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    public static boolean isSSLCertOk(SslCertificate cert, String sha256Str) {
        byte[] SSLSHA256 = hexToBytes(sha256Str);
        Bundle bundle = SslCertificate.saveState(cert);
        if (bundle != null) {
            byte[] bytes = bundle.getByteArray("x509-certificate");
            if (bytes != null) {
                try {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    Certificate ca = cf.generateCertificate(new ByteArrayInputStream(bytes));
                    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                    byte[] key = sha256.digest(((X509Certificate) ca).getEncoded());
                    return Arrays.equals(key, SSLSHA256);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * hexString转byteArr
     * <p>例如：</p>
     * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
     *
     * @param hexString
     * @return 字节数组
     */
    public static byte[] hexToBytes(String hexString) {

        if (hexString == null || hexString.trim().length() == 0)
            return null;

        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789abcdef";
        for (int i = 0; i < length; i++) {
            int pos = i * 2; // 两个字符对应一个byte
            int h = hexDigits.indexOf(hexChars[pos]) << 4; // 注1
            int l = hexDigits.indexOf(hexChars[pos + 1]); // 注2
            if (h == -1 || l == -1) { // 非16进制字符
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }
}
