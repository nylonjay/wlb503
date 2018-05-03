package com.bankscene.bes.welllinkbank.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.MessageCodeUtils;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.common.Constant;
import com.bankscene.bes.welllinkbank.core.HttpActivity;
import com.bankscene.bes.welllinkbank.core.State;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
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
import java.util.Scanner;

import de.greenrobot.event.EventBus;

import static com.tencent.smtt.sdk.TbsReaderView.ReaderCallback.SHOW_DIALOG;

public class LoginSMSVerify extends Activity {
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
    private TextView tv_resend;
    MessageCodeUtils mcu;
    public static final String _REJCODE="_RejCode";
    public static final String _REJMSG="_RejMsg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_to_getpassword);
//        calback=getIntent().getStringExtra("callback");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        keyBoardDialogUtils=new KeyBoardWithTextDialog(this);
        PrincipalSeq=getIntent().getStringExtra("PrincipalSeq");
        InitView();
//        InitWindouManager();
        keyBoardDialogUtils.ForbiddenClose();
        keyBoardDialogUtils.show(ppwdedit);
        keyBoardDialogUtils.SetTitle(getResources().getString(R.string.sms_verify));
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
    public void InitView() {
//        initSecurityFieldsSet();
        contentView=keyBoardDialogUtils.getContentView();
        ppwdedit = (EditText) findViewById(R.id.trans_password);
//        freshTimestamp();
        ppwdedit.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
        InitWindouManager();
        ll_tvs=contentView.findViewById(R.id.ll_tvs);
//        ll_tvs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                keyBoardDialogUtils=new KeyBoardWithTextDialog(LoginSMSVerify.this);
//                keyBoardDialogUtils.show(pwdedit);
//            }
//        });
        tv_resend=contentView.findViewById(R.id.tv_send);
        mcu= new MessageCodeUtils(60000, tv_resend,LoginSMSVerify.this);
        peds=new TextView[6];
        pe1= (TextView)contentView. findViewById(R.id.password_edit1);
        pe2= (TextView)contentView. findViewById(R.id.password_edit2);
        pe3= (TextView)contentView. findViewById(R.id.password_edit3);
        pe4= (TextView)contentView. findViewById(R.id.password_edit4);
        pe5= (TextView)contentView. findViewById(R.id.password_edit5);
        pe6= (TextView)contentView. findViewById(R.id.password_edit6);
        peds[0]=pe1;
        peds[1]=pe2;
        peds[2]=pe3;
        peds[3]=pe4;
        peds[4]=pe5;
        peds[5]=pe6;

        ppwdedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String [] arr=s.toString().split("");
                String[] ss = new String[arr.length-1];
                System.arraycopy(arr, 1, ss, 0, ss.length);
                for (int i=0;i<peds.length;i++){
                    if (i<ss.length){
                        peds[i].setText(ss[i]);
                    }else{
                        peds[i].setText("");
                    }
                }
                if (s.length()>=6){
                    pwTxt=s.toString();
                    GtoGetCode();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void GtoGetCode(){
        Intent in=new Intent();
        in.putExtra("code",pwTxt);
        setResult(RESULT_OK,in);
        LoginSMSVerify.this.finish();
    }
    public void close(View v){//点击关闭也要回调 (以后改)
        String engryped="'"+"pwdViewRemoved"+"'";//pwTxt已经加密过l
        Intent in=new Intent();
        in.putExtra("engrypted",engryped);
        setResult(RESULT_OK,in);
        LoginSMSVerify.this.finish();
    }
    public void resend(View v){
        Resend();
    }
    public void Resend(){
        Map params = new HashMap();
        params.put("_ChannelId","PMBS");
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
                        Trace.e("resend",info.getRetDetail().toString());
                        mcu.start();
//                        try {
//                            JSONObject result= new JSONObject(info.getRetDetail());
//                            if (result.has(_REJCODE)&&result.get(_REJCODE).equals("000000")){
//
//                            }else {
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
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
}
