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
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.kh.keyboard.CSIICypher;
import com.kh.keyboard.KeyBoardDialogUtils;
import com.kh.keyboard.KeyBoardTransCodeDialog;
import com.kh.keyboard.KeyBoardWithTextDialog;
import com.kh.keyboard.SecurityCypherException;

import utils.CSIISecurityUtil;

public class Dialog2GetPassWord extends Activity {
    EditText pwdedit;
    String timestamp;
    protected String pwTxt;
    KeyBoardTransCodeDialog keyBoardDialogUtils;
    private TextView pe1,pe2,pe3,pe4,pe5,pe6;
    private String encoding="UTF-8";
    private LinearLayout ll_tvs;
    private TextView[] peds;
    View contentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_to_getpassword);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        keyBoardDialogUtils=new KeyBoardTransCodeDialog(this);
        timestamp=System.currentTimeMillis()+"";
        initView();
        keyBoardDialogUtils.ForbiddenClose();
        keyBoardDialogUtils.show(pwdedit);
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
    private void initView() {
//        initSecurityFieldsSet();
        contentView=keyBoardDialogUtils.getContentView();
        pwdedit = findViewById(R.id.trans_password);
//        freshTimestamp();
        pwdedit.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
        InitWindouManager();
        ll_tvs=contentView.findViewById(R.id.ll_tvs);
//        ll_tvs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                keyBoardDialogUtils.show(pwdedit);
//            }
//        });
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

        pwdedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Trace.e("transcode",s.toString());
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

    @Override
    public void finish() {
        super.finish();
    }

    private void GtoGetCode(){
        String ens= null;
        try {
            ens = new CSIICypher().encryptWithoutRemove(pwdedit.getText().toString().trim(), CommDictAction.SecurityPubKey,timestamp,"UTF-8",2).replace("+","%2B");
//            String engryped="'"+ens+"'";//pwTxt已经加密过l
            Intent in=new Intent();
            in.putExtra("engrypted",ens);
            setResult(RESULT_OK,in);
            Dialog2GetPassWord.this.finish();
        } catch (SecurityCypherException e) {
            e.printStackTrace();
        }

    }
    public void close(View v){//点击关闭也要回调 (以后改)
        Trace.e("close","138");
        String engryped="'"+"pwdViewRemoved"+"'";//pwTxt已经加密过l
        Intent in=new Intent();
        in.putExtra("engrypted",engryped);
        setResult(RESULT_OK,in);
        Dialog2GetPassWord.this.finish();
    }



//    @Override
//    public void successHandle(JsonObject jobj, String transFlag, PagePushEvent pagePushEvent) {
//        super.successHandle(jobj, transFlag, pagePushEvent);
//    }
}
