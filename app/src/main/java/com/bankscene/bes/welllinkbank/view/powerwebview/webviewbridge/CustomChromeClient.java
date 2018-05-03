package com.bankscene.bes.welllinkbank.view.powerwebview.webviewbridge;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.widget.ProgressBar;


import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.common.Config;
import com.bankscene.bes.welllinkbank.core.BaseActivity;
import com.bankscene.bes.welllinkbank.core.State;
import com.bankscene.bes.welllinkbank.core.WebViewActivity;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by tianwei on 2017/4/25.
 */

public class CustomChromeClient extends InjectedChromeClient {

    private String TAG = "CustomChromeClient";
    private ProgressBar progressBar;
    private Activity activity;

    private boolean IsWebViewActivity() {
        return (activity != null && activity instanceof WebViewActivity);
    }

    public CustomChromeClient(String injectedName, Class injectedCls, ProgressBar progressBar,
                              Activity activity) {
        super(injectedName, injectedCls);
        this.progressBar = progressBar;
        this.activity = activity;
    }

    public CustomChromeClient(String injectedName, Class injectedCls, Activity activity) {
        super(injectedName, injectedCls);
        this.progressBar = null;
        this.activity = activity;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        Trace.e(TAG, "title:" + title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (progressBar != null) {
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
        }
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {

        try {

            Trace.e(TAG, "onJsPrompt: " + message);
            JSONObject jsonBean = new JSONObject(message);
            String method = jsonBean.getString("method");
            JSONArray argsArray = jsonBean.getJSONArray("args");

            String args = null;

            if (argsArray.length() > 0) {
                args = argsArray.getString(0);
                Trace.e(TAG, "onJsPrompt: " + "args=" + args);
            } else {
                Trace.e(TAG, "args is null ");
            }

            switch (method) {
                case "setTitle":
                    if (IsWebViewActivity()) {
                        ((WebViewActivity) activity).setTitle(args);
                    }
                    break;
                case "startView":
                    Intent intent = new Intent();
                    if (argsArray.getString(1).equals("1")) {
                        intent.putExtra("url", Config.TRANS_IP_PORT + args);
                    } else {
                        intent.putExtra("url", args);
                    }
                    intent.setClass(activity, WebViewActivity.class);
                    activity.startActivity(intent);
                    break;
//                case "inputPasswordView":
//                    if (IsWebViewActivity()) {
//                        ((WebViewActivity) activity).inputPasswordView(args);
//                    }
//                    break;
                case "login":
                    State.loginCalled = true;
                    ((BaseActivity) activity).gotoLogin();
                    break;
                case "openPDF":
                    if (IsWebViewActivity()) {
                        ((WebViewActivity) activity).openPDF(Config.TRANS_IP+args);
                    }
                    break;
                case "goToPayWithParameters":
//                    if (IsWebViewActivity()) {
//                        ((WebViewActivity) activity).showCashier(args,
//                                argsArray.getString(1),argsArray.getString(2));
//                    }
                    break;




                //添加银行卡
                case "addBankCard":
                    break;
                //找回支付密码
                case "findPayPassword":
                    break;
                //设置支付密码
                case "setPassword":
                    break;
                //选择卡列表
                case "selectBankCard":
                    break;
                //分享
                case "showShareViewWithParameters":
                    break;
                //拨打电话
                case "callWithphoneNum":
                    break;
                //理财购买
                case "BuyFinancing":
                    break;
                //我的理财
                case "MyFinancings":
                    break;
                //我要缴费
                case "MyPayments":
                    break;
                //我的贷款 申请进度
                case "MyLoans":
                    break;
                //设置登录密码
                case "setLoginPassword":
                    break;

            }

        } catch (Exception e) {
            Trace.e(TAG, "onJsPrompt: " + message);
            e.printStackTrace();
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}