package com.bankscene.bes.welllinkbank.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.MessageCodeUtils;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.view.sms.OnPasswordInputFinish;
import com.bankscene.bes.welllinkbank.view.sms.PasswordView;
import com.bankscene.bes.welllinkbank.view.sms.PopEnterPassword;
import com.kh.keyboard.KeyBoardWithTextDialog;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nylon on 2018/5/17.16:34
 */

public class LoginSmsDialog extends Activity implements View.OnClickListener{
    EditText ppwdedit;
    String timestamp;
    protected String pwTxt;
    KeyBoardWithTextDialog keyBoardDialogUtils;
    private TextView pe1,pe2,pe3,pe4,pe5,pe6;
    private String encoding="UTF-8";
    private LinearLayout ll_tvs;
    private TextView[] peds;
    View contentView;
    private String PrincipalSeq;
    MessageCodeUtils mcu;
    public static final String _REJCODE="_RejCode";
    public static final String _REJMSG="_RejMsg";
    private String Challenge;
    private TextView tv_challenge;
    private TextView tv_cancle,tv_resend;
    private PasswordView passwordView;
    private View pwdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_to_sms);
//        calback=getIntent().getStringExtra("callback");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        PrincipalSeq=getIntent().getStringExtra("PrincipalSeq");
        Challenge=getIntent().getStringExtra("Challenge");
        InitWindouManager();
        InitView();
    }

    private void InitView() {
        passwordView=findViewById(R.id.pwd_view);
        pwdview=passwordView.getRootView();
        tv_challenge=pwdview.findViewById(R.id.tv_challenge);
        tv_challenge.setText(getResources().getString(R.string.challenge)+" "+Challenge);
        tv_cancle=pwdview.findViewById(R.id.cancel);
        tv_resend=pwdview.findViewById(R.id.submit);
        tv_cancle.setOnClickListener(this);
        tv_resend.setOnClickListener(this);
        mcu=new MessageCodeUtils(60000,tv_resend,this);
        mcu.start();
        passwordView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish(final String password) {
                pwTxt=password;
                GtoGetCode();
            }
        });
    }



    private void InitWindouManager(){
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = p.WRAP_CONTENT;  //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 1.0);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.5f;      //设置黑暗度
        p.verticalMargin=d.getWidth()/5;
        getWindow().setAttributes(p);     //设置生效
        getWindow().setGravity(Gravity.CENTER);       //设置靠右对齐
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 显示窗口
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void GtoGetCode(){
        Intent in=new Intent();
        in.putExtra("code",pwTxt);
        if (!TextUtils.isEmpty(Challenge)){
            in.putExtra("Challenge",Challenge);
        }
        setResult(RESULT_OK,in);
        LoginSmsDialog.this.finish();
    }
    public void close(View v){//点击关闭也要回调 (以后改)
        String engryped="'"+"pwdViewRemoved"+"'";//pwTxt已经加密过l
        Intent in=new Intent();
        in.putExtra("engrypted",engryped);
        setResult(RESULT_OK,in);
        LoginSmsDialog.this.finish();
    }
    public void Resend(){
        Map params = new HashMap();
        params.put("_ChannelId","PMBS");
        if ("zh".equals(DBHelper.getDataByKey(DataKey.language))){

            params.put("_locale","zh_TW");
        }else {
            params.put("_locale","en_US");
        } if ("zh".equals(DBHelper.getDataByKey(DataKey.language))){

            params.put("_locale","zh_TW");
        }else {
            params.put("_locale","en_US");
        }
        params.put("PrincipalSeq",PrincipalSeq);
        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .setUrl(CommDictAction.ReSendSMSOTP)
                        .setRequestType(RequestType.POST)//设置请求方式
                        .addHead("cookie", DBHelper.getDataByKey(DataKey.cookie))//添加头参数
                        .addParams(params)
//                        .setDelayExec(2, TimeUnit.SECONDS)//延迟2秒执行
                        .build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        try {
                            JSONObject result=new JSONObject(info.getRetDetail());
                            mcu.start();
                            // {"PrincipalSeq":"44","_ChannelId":"PMBS","Challenge":"0242"}
                            Challenge=result.optString("Challenge");
                            tv_challenge.setText(getResources().getString(R.string.challenge)+" "+Challenge);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }


    /**
     * 异步请求
     * @param info 请求信息体
     * @param callback 结果回调接口
     */
    protected void doHttpAsync(HttpInfo info, final Callback callback){
        OkHttpUtil.getDefault(this).doAsync(info, new Callback() {
            @Override
            public void onSuccess(HttpInfo info) throws IOException {
//                getLoadingDialog().dismiss();
                callback.onSuccess(info);
            }

            @Override
            public void onFailure(HttpInfo info) throws IOException {
                callback.onFailure(info);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                LoginSmsDialog.this.finish();
                break;
            case R.id.submit:
                Resend();
                break;
        }
    }
}
