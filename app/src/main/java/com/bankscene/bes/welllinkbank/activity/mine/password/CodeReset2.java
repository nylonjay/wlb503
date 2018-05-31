package com.bankscene.bes.welllinkbank.activity.mine.password;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.Util.ButtonState;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.Util.dialog.DialogCallBack;
import com.bankscene.bes.welllinkbank.activity.gesture.LoginGestureActivity;
import com.bankscene.bes.welllinkbank.activity.mine.gesture.GestureSetResult;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.HttpActivity;
import com.bankscene.bes.welllinkbank.core.State;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.exception.WLBException;
import com.bankscene.bes.welllinkbank.view.WlbEditText;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.kh.keyboard.CSIICypher;
import com.kh.keyboard.KeyBoardDialogUtils;
import com.kh.keyboard.SecurityCypherException;
import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class CodeReset2 extends HttpActivity implements View.OnClickListener{
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.ed_1)
    WlbEditText ed_1;
    @BindView(R.id.ed_2)
    WlbEditText ed_2;
    @BindView(R.id.ed_3)
    WlbEditText ed_3;
    @BindView(R.id.btn_confirm)
    Button btn_confirml;
    KeyBoardDialogUtils keyBoardDialogUtil1;
    KeyBoardDialogUtils keyBoardDialogUtil2;
    KeyBoardDialogUtils keyBoardDialogUtil3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.LOGIN_CODE_RESET), R.string.wlb_arrow_l,"", 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
           Exit2LogOUt();
            }

            @Override
            public void onRightClick() {

            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Exit2LogOUt();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ed_1:
                GetTimeStampAndKey(ed_1);
                keyBoardDialogUtil1=new KeyBoardDialogUtils(this);
                keyBoardDialogUtil1.show(ed_1);
                break;
            case R.id.ed_2:
                GetTimeStampAndKey(ed_2);
                keyBoardDialogUtil2=new KeyBoardDialogUtils(this);
                keyBoardDialogUtil2.show(ed_2);
                break;
            case R.id.ed_3:
                GetTimeStampAndKey(ed_3);
                keyBoardDialogUtil3=new KeyBoardDialogUtils(this);
                keyBoardDialogUtil3.show(ed_3);
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();
//        ed_1.setInputType(InputType.TYPE_NULL);
//        ed_2.setInputType(InputType.TYPE_NULL);
//        ed_3.setInputType(InputType.TYPE_NULL);
//        GetTimeStampAndKey();
        ed_1.setOnClickListener(this);
        ed_2.setOnClickListener(this);
        ed_3.setOnClickListener(this);
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(ed_1);
        editTextList.add(ed_2);
        editTextList.add(ed_3);
        new ButtonState(editTextList, btn_confirml).onWatch();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_code_reset2;
    }

    public void onTapConfirm(View v){
//        确认修改
        if (checkPassword1()&&checkPassword2()&&checkPassword3())
        UploadPassword();
    }
    private boolean checkPassword1() {
        if (ed_1.getText().toString().equals("")||ed_1.getText().length()<8) {
            showNotice(getResources().getString(R.string.insert_8_atleast));
            return false;
        }
        return true;
    }
    private boolean checkPassword2() {
        if (ed_2.getText().toString().equals("")||ed_2.getText().length()<8) {
            showNotice(getResources().getString(R.string.insert_8_atleast));
            return false;
        }

        String e2=ed_2.getText().toString();
        if (e2.matches("[a-zA-Z]+")||e2.matches("[0-9]+")){
            showNotice(getResources().getString(R.string.no_same_type));
            return false;
        }
        return true;
    }
    private boolean checkPassword3() {
        if (ed_3.getText().toString().equals("")||ed_3.getText().length()<8) {
            showNotice(getResources().getString(R.string.insert_8_atleast));
            return false;
        }
        String e2=ed_3.getText().toString();
        if (e2.matches("[a-zA-Z]+")||e2.matches("[0-9]+")){
            showNotice(getResources().getString(R.string.no_same_type));
            return false;
        }

        return true;
    }
    private void UploadPassword() {
        String ed1 = null,ed2 = null,ed3 = null;
        try {
            Trace.e("ed1_timestamp",ed_1.getTimestamp()+"hms===="+ed_1.getHms()+"dbp==="+ed_1.getDbp());
            Trace.e("ed2_timestamp",ed_2.getTimestamp()+"hms===="+ed_2.getHms()+"dbp==="+ed_2.getDbp());
            Trace.e("ed3_timestamp",ed_3.getTimestamp()+"hms===="+ed_3.getHms()+"dbp==="+ed_3.getDbp());
            ed1= new CSIICypher().encryptWithJiamiJi(ed_1.getText().toString().trim(),ed_1.getDbp(),ed_1.getHms(),ed_1.getTimestamp(),"UTF-8",2);
            ed2= new CSIICypher().encryptWithJiamiJi(ed_2.getText().toString().trim(),ed_2.getDbp(),ed_2.getHms(),ed_2.getTimestamp(),"UTF-8",2);
            ed3= new CSIICypher().encryptWithJiamiJi(ed_3.getText().toString().trim(),ed_3.getDbp(),ed_3.getHms(),ed_3.getTimestamp(),"UTF-8",2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        params.put("OldPassword",ed1.replace("+","%2B"));
        params.put("NewPassword",ed2.replace("+","%2B"));
        params.put("ConfirmPassword",ed3.replace("+","%2B"));
        params.put("PasswordType", "01");
        if ("zh".equals(DBHelper.getDataByKey(DataKey.language))){
            params.put("_locale","zh_TW");
        }else {
            params.put("_locale","en_US");
        }
        if (State.isFirstLogin){
            params.put("FirstLoginFlag","Y");
        }
        setProgressDisplay(true);
        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .addHead("cookie", DBHelper.getDataByKey(DataKey.cookie))
                        .setUrl(CommDictAction.UpdatePassword)
                        .setRequestType(RequestType.POST)//设置请求方式
//                        .setNeedResponse(true)
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String result=info.getRetDetail();
                        try {
                            JSONObject json=new JSONObject(result);
                            if (json.has(_REJCODE)&&json.opt(_REJCODE).equals("000000")){

                                dialogUtils.ShowDialogOne(getResources().getString(R.string.promot), getResources().getString(R.string.update_password_ok), getResources().getString(R.string.confirm), new DialogCallBack() {
                                    @Override
                                    public void onPositive() {
                                        CodeReset2.this.finish();
                                        if (State.isFirstLogin)
                                        startActivity(new Intent(CodeReset2.this, TradeCodeReset.class));
                                    }

                                    @Override
                                    public void onNegative() {
//                                        startActivity(new Intent(CodeReset2.this, TradeCodeReset.class));
                                        CodeReset2.this.finish();
                                    }
                                });
                            }else {
                                try {
                                    JSONArray jsonError=json.getJSONArray("jsonError");
                                    JSONObject f1=jsonError.getJSONObject(0);
                                    noticeUtils.showNotice(f1.optString("_exceptionFieldName"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        noticeUtils.showNotice(getResources().getString(R.string.update_password_failed));
                    }
                });
    }

    private void Exit2LogOUt(){
        if (State.isFirstLogin){
            dialogUtils.ShowDialogTwo(getResources().getString(R.string.promot), getResources().getString(R.string.confim2logout), getResources().getString(R.string.confirm), getResources().getString(R.string.cancle_1), new DialogCallBack() {
                @Override
                public void onPositive() {
                    //登出
                    LogOut();
                    CodeReset2.this.finish();
                }

                @Override
                public void onNegative() {

                }
            });
        }else {
            CodeReset2.this.finish();
        }
    }
}
