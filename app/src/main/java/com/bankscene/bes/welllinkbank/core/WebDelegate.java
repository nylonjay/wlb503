package com.bankscene.bes.welllinkbank.core;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ProgressBar;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.view.APIWebviewTBS;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebDelegate extends AppCompatActivity {
    //    private String url="http://www.suomusic.com/dryCargoInfo.htm?mt=show&id=2";  //测试网址选择1
    private String url="https://v.qq.com/index.html";//测试网址选择2腾讯视频

    private WebView webView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_video);
        APIWebviewTBS.getAPIWebview().initTBSActivity(this);   //api借口注册二次封装
        url=getIntent().getStringExtra("url");
        loadWebvieUrl(url);
    }
//    private void initdata() {//获取手机版本信息
//        int tbsVersion = QbSdk.getTbsVersion(this);
//        String TID = QbSdk.getTID();
//        String qBVersion = QbSdk.getMiniQBVersion(this);
//        tvStatus.setText("TbsVersion:" + tbsVersion + "\nTID:" + TID + "\nMiniQBVersion:" + qBVersion);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        synCookies(url,webView);
    }

    private void loadWebvieUrl(String url) {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.webview_wechat);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
                progressBar.setVisibility(View.GONE);
            }
        });
        //进度条
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  确保注销配置能够被释放
//        if(this.webView!=null){
//            webView.destroy();
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void synCookies(String url, WebView v)  {

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        Trace.e("web", "synCookies: removeAllCookie");

        if (State.isLogin) {

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                cookieManager.setAcceptThirdPartyCookies(v, true);
//            }
            Trace.e(" 將要同步的cookie==", DBHelper.getDataByKey(DataKey.cookie));
            cookieManager.setCookie(url, DBHelper.getDataByKey(DataKey.cookie));
        }

        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }
}
