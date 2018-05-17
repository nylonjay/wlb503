package com.bankscene.bes.welllinkbank.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.core.HttpActivity;
import com.kh.keyboard.CSIICypher;
import com.kh.keyboard.KeyBoardDialogUtils;
import com.kh.keyboard.SecurityCypherException;

public class PassWordDialogActivity extends Activity {
    EditText pwdedit;
    String timestamp;
    protected String pwTxt;
    protected int pwLen;
    private TextView[] peds;
    private TextView pe1,pe2,pe3,pe4,pe5,pe6;
    private boolean isNeedNumPwdSoftInput=true;
    KeyBoardDialogUtils keyBoardDialogUtils;
    private String encoding="UTF-8";
    LinearLayout ll_tvs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word_dialog);
//        calback=getIntent().getStringExtra("callback");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
//        trueUrl=getIntent().getStringExtra("trueUrl");
        timestamp=getIntent().getStringExtra("timestamp");
        Trace.e("timestamp",timestamp);

        initView();
//        InitWindouManager();
        keyBoardDialogUtils=new KeyBoardDialogUtils(PassWordDialogActivity.this);
        keyBoardDialogUtils.ForbiddenClose();
        keyBoardDialogUtils.show(pwdedit);
    }
    private void InitWindouManager(){
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight()*0.7);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 1.0);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.5f;      //设置黑暗度
        p.verticalMargin=d.getWidth()/5;
        getWindow().setAttributes(p);     //设置生效
        getWindow().setGravity(Gravity.CENTER);       //设置靠右对齐
    }



    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    for (TextView tv:peds){
//                        tv.setBackgroundResource(R.drawable.red_dot);
                        tv.setVisibility(View.GONE);
                    }
                    for (int i=0;i<pwLen;i++){
                        peds[i].setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };


    private void initView() {
//        initSecurityFieldsSet();
        pwdedit = findViewById(R.id.trans_password);
//        freshTimestamp();
        pwdedit.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
        InitWindouManager();
        ll_tvs=findViewById(R.id.ll_tvs);
        ll_tvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBoardDialogUtils=new KeyBoardDialogUtils(PassWordDialogActivity.this);
                keyBoardDialogUtils.show(pwdedit);
            }
        });
        peds=new TextView[6];
        pe1= (TextView) findViewById(R.id.password_edit1);
        pe2= (TextView) findViewById(R.id.password_edit2);
        pe3= (TextView) findViewById(R.id.password_edit3);
        pe4= (TextView) findViewById(R.id.password_edit4);
        pe5= (TextView) findViewById(R.id.password_edit5);
        pe6= (TextView) findViewById(R.id.password_edit6);
        peds[0]=pe1;
        peds[1]=pe2;
        peds[2]=pe3;
        peds[3]=pe4;
        peds[4]=pe5;
        peds[5]=pe6;
        pwdedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Trace.e("dialog_count----",s.length()+"" +"s===="+s.toString());
                for (int i=0;i<peds.length;i++){
                    if (i<s.length()){
                        peds[i].setText("●");
                    }else{
                        peds[i].setText("");
                    }
                }
                if (s.length()>=6){
                    GtoGetCode();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void GtoGetCode(){
//        String timestamp=System.currentTimeMillis()+"";
        //            String encrpre=new CSIICypher().encryptWithoutRemove(pwdedit.getText().toString().trim(),CommDictAction.SecurityPubKey,timestamp,"UTF-8",2);
//            String engryped="'"+encrpre.replace("+","%2B")+"'";//pwTxt已经加密过l
        Intent in=new Intent();
        in.putExtra("engrypted",pwdedit.getText().toString());
        setResult(RESULT_OK,in);
        PassWordDialogActivity.this.finish();

    }
    public void close(View v){//点击关闭也要回调 (以后改)
        Trace.e("close","138");
        String engryped="'"+"pwdViewRemoved"+"'";//pwTxt已经加密过l
        Intent in=new Intent();
        in.putExtra("engrypted",engryped);
        setResult(RESULT_OK,in);
        PassWordDialogActivity.this.finish();
    }



//    @Override
//    public void successHandle(JsonObject jobj, String transFlag, PagePushEvent pagePushEvent) {
//        super.successHandle(jobj, transFlag, pagePushEvent);
//    }
}
