package com.bankscene.bes.welllinkbank.activity.Webview;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.activity.LoginTablayoutActivity;
import com.bankscene.bes.welllinkbank.activity.PassWordDialogActivity;
import com.bankscene.bes.welllinkbank.core.HttpActivity;
import com.bankscene.bes.welllinkbank.core.WebViewActivity;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.kh.keyboard.CSIICypher;

import butterknife.BindView;

public class Html5Activity extends HttpActivity {

    private String mUrl;
    private ProgressBar mSeekBar;
    private FrameLayout mLayout;
    private Html5WebView mWebView;
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_web;
    }
    private final int SHOW_KEYBOARD=1;

    public void setActionBar() {
        actionBar.setStatusBarHeight();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏


        mLayout = (FrameLayout) findViewById(R.id.web_layout);
        mSeekBar= (ProgressBar) findViewById(R.id.progressBar);
        // 创建 WebView
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new Html5WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        mLayout.addView(mWebView);
        mWebView.addJavascriptInterface(new Html5Activity.JavacriptInterface(), "android");
        mWebView.setWebChromeClient(new Html5WebChromeClient());
        mWebView.setWebViewClient(new Html5WebClient());
        getData();
        mWebView.loadUrl(mUrl);
    }

    private void getData() {
        String showActionBar=getIntent().getStringExtra("showActionBar");
        String title=getIntent().getStringExtra("title");
        String localHtml=getIntent().getStringExtra("localHtml");
        if (!TextUtils.isEmpty(showActionBar)){
            IninActionBar(title);
        }else{
            actionBar.setStatusBarHeight();
        }

        if (!TextUtils.isEmpty(localHtml)){
            Trace.e("loclahtml:",localHtml);
            mWebView.loadUrl(localHtml);
            return;
        }
        String address=getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(address)){
            mUrl = address;
        }

        synCookies(mUrl,mWebView);
    }

    public void setTitle(String title) {
        actionBar.setTitle(title);
    }

    public void synCookies(String url, WebView v) {

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        Trace.e(TAG, "synCookies: removeAllCookie");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(v, true);
        }
        Trace.e(" 將要同步的cookie==", DBHelper.getDataByKey(DataKey.cookie));
        cookieManager.setCookie(url,DBHelper.getDataByKey(DataKey.cookie));

        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }
    public void IninActionBar(String title){
        actionBar.setVisibility(View.VISIBLE);
        actionBar.setActionBar(title, R.string.wlb_arrow_l, "",
                0, "",
                new ActionBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        Html5Activity.this.finish();
                    }

                    @Override
                    public void onRightClick() {
//                        showCashier("OMS1706212794139","0","");
                    }
                });
    }

    // 继承 WebView 里面实现的基类
    class Html5WebChromeClient extends Html5WebView.BaseWebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // 顶部显示网页加载进度
            if (null!=mSeekBar)
            mSeekBar.setProgress(newProgress);
        }



    }

    class Html5WebClient extends Html5WebView.BaseWebViewClient{
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            setStatusBarColor(Html5Activity.this,R.color.error_404);
            findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSeekBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        // 销毁 WebView
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
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
                            mWebView.loadUrl(url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
                break;
        }
    }

//    public void getParameter() {
//        Bundle bundle = getIntent().getBundleExtra("bundle");
//        if (bundle != null) {
//            mUrl = bundle.getString("url");
//        } else {
//            mUrl = "https://wing-li.github.io/";
//        }
//    }

    //============================= 下面是本 demo  的逻辑代码
    // ======================================================================================

    /**
     * 按目录键，弹出“关闭页面”的选项
     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int itemId = item.getItemId();
//        switch (itemId) {
//            case R.id.close:
//                Html5Activity.this.finish();
//                return true;
//            case R.id.copy:
//                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                String url = mWebView.getUrl();
//                ClipData clipData = ClipData.newPlainText("test", url);
//                if (clipboardManager != null) {
//                    clipboardManager.setPrimaryClip(clipData);
//                    Toast.makeText(getApplicationContext(), "本页网址复制成功", Toast.LENGTH_SHORT).show();
//                }
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private long mOldTime;

    /**
     * 点击“返回键”，返回上一层
     * 双击“返回键”，返回到最开始进来时的网页
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mOldTime < 1500) {
                mWebView.clearHistory();
                mWebView.loadUrl(mUrl);
            } else if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                Html5Activity.this.finish();
            }
            mOldTime = System.currentTimeMillis();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    final class JavacriptInterface{
        @JavascriptInterface
        public void Back2Homepage(){
            Trace.e(TAG,"BACK2HOME");
            Html5Activity.this.finish();
        }
        @JavascriptInterface
        public void Back2Login(){
            startActivity(new Intent(Html5Activity.this, LoginTablayoutActivity.class));
            Html5Activity.this.finish();
        }
        @JavascriptInterface
        public void ShowKeyBoard(String timestamp){
            GetTimeStampAndKeyWithoutEditor();
            Intent in =new Intent(Html5Activity.this, PassWordDialogActivity.class);
            in.putExtra("timestamp",timestamp);
            startActivityForResult(in,SHOW_KEYBOARD);

        }
        @JavascriptInterface
        public String langType(){
            Trace.e("langtype--","");
            return DBHelper.getDataByKey(DataKey.language);

        }

    }
}