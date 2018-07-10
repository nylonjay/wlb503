package com.bankscene.bes.welllinkbank.fragment.logintypes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.ButtonState;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.activity.LoginSmsDialog;
import com.bankscene.bes.welllinkbank.adapter.GlideImageLoader;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.common.Constant;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.BaseFragment;
import com.bankscene.bes.welllinkbank.core.State;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.view.ClearableEditText;
import com.bankscene.bes.welllinkbank.view.IconFontTextView;
import com.bankscene.bes.welllinkbank.view.PhonPrePopWindow;
import com.bankscene.bes.welllinkbank.view.WlbEditText;
import com.kh.keyboard.CSIICypher;
import com.kh.keyboard.KeyBoardDialogUtils;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Nylon on 2018/6/4.17:18
 */

public class SMSLogin extends BaseTabFragment implements View.OnClickListener{
    static Activity mContext;
    ClearableEditText login_name;
    WlbEditText pwdedit;
    EditText login_verifycode;
    ImageView iv_verify_cell;
    TextView tv_pre;
    CheckBox remerberuser;
    IconFontTextView icon_du;
    Button btn_login;

    private KeyBoardDialogUtils keyBoardDialogUtils;
    View contentview;
    private String encryped;
    private final int LOGIN_M=3;
    private String Challenge="";
    private boolean clicked2=false;
    private boolean mHasLoadedOnce=false;

//    public SMSLogin(Activity context) {
//        mContext=context;
//    }

    public SMSLogin() {
    }
    public static final SMSLogin NewInstance(Activity activity){
        SMSLogin sl=new SMSLogin();
        mContext=activity;
        return sl;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (null!=contentview){
            return contentview;
        }
        EventBus.getDefault().register(this);
        contentview=inflater.inflate(R.layout.layout_login_cell, container, false);
        InitKeyBoards(contentview);
        getImageCode();
        return contentview;
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(MessageEvent messageEvent){
        Trace.e(TAG,"MAIN_messageEvent==="+messageEvent.getMessage());
        switch (messageEvent.getWhat()){
            case 1:
                setProgressDisplay(false);
//                refreshSelf();
                break;
            case SHOW_PRE_TEXT:
                tv_pre.setText(messageEvent.getMessage());
                icon_du.setText(getResources().getString(R.string.wlb_arrow_d));
                break;
            case Constant.DISMISS:
                icon_du.setText(getResources().getString(R.string.wlb_arrow_d));
                break;
        }
    }
    @Override
    protected void initView() {
        super.initView();

    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden){
//            getImageCode();
//        }
//    }

//    @Override
//    protected void onVisible() {
//        super.onVisible();
//        if (!mHasLoadedOnce)
//        getImageCode();
//    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && !mHasLoadedOnce) {
//            Log.i("TestData", "FoundFragment 加载请求网络数据");
//            //TO-DO 执行网络数据请求
//            InitKeyBoards(contentview);
//
//            mHasLoadedOnce = true;
//        }
//    }

    private void InitKeyBoards(View contentview) {
        Trace.e("initKeyboard2","--------------");
        LOGIN_TYPE=2;
        pwdedit= (WlbEditText)contentview. findViewById(R.id.login_password_cell);
        login_name= (ClearableEditText)contentview. findViewById(R.id.login_name_cell);
        login_verifycode= (EditText)contentview. findViewById(R.id.login_verifycode_cell);
        remerberuser= (CheckBox)contentview. findViewById(R.id.remerberuser_cell);
        iv_verify_cell= (ImageView)contentview. findViewById(R.id.iv_verify_celll);
        tv_pre=contentview.findViewById(R.id.tv_pre_cell);
        iv_verify_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageCode();
            }
        });
        btn_login=contentview.findViewById(R.id.btn_login_cell);
        icon_du=contentview.findViewById(R.id.icon_du);
        btn_login.setOnClickListener(this);
        login_name.clearFocus();
        login_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus&&login_name.getText().toString().contains("*")){
                    login_name.setText("");
                }
            }
        });
        pwdedit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    GetTimeStampAndKeyWithoutEditor();
                }
                if (hasFocus&&clicked2){
                    Trace.e("onfocus","pwd");
                    keyBoardDialogUtils=new KeyBoardDialogUtils(activity);
                    keyBoardDialogUtils.hideSystemSofeKeyboard(pwdedit);
                    keyBoardDialogUtils.show(pwdedit);
                }
            }
        });
        pwdedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trace.e("onclick","pwd");

                keyBoardDialogUtils=new KeyBoardDialogUtils(activity);
                keyBoardDialogUtils.hideSystemSofeKeyboard(pwdedit);
                keyBoardDialogUtils.show(pwdedit);
                clicked2=true;
            }
        });
        pwdedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (DBHelper.getDataByKey(DataKey.isRememberUser).equals("true")&&!TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.login_tab))&&DBHelper.getDataByKey(DataKey.login_tab).equals("2")) {
            remerberuser.setChecked(true);
            String un=DBHelper.getDataByKey(DataKey.userName);
            un=un.substring(DBHelper.getDataByKey(DataKey.pre).length()-1);
            String f3=un.substring(0,3);
            String l3=un.substring(un.length()-3,un.length());
            String showwords=f3+"***"+l3;
            login_name.setText(showwords);
            tv_pre.setText(DBHelper.getDataByKey(DataKey.pre));
        }
        tv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhonePre();
            }
        });
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(login_name);
        editTextList.add(pwdedit);
//        editTextList.add(login_verifycode);
        new ButtonState(editTextList, btn_login).onWatch();

    }
    @Override
    protected int setLayoutId() {
        return R.layout.layout_login_cell;
    }

    @Override
    public String getFragmentTitle() {
        return mContext.getResources().getString(R.string.cell_verify_login);
    }

    private void getImageCode() {

        Trace.e("getimagecode","22222222222");
        new GlideImageLoader().displayImage(mContext, CommDictAction.imageCode,iv_verify_cell);
        mHasLoadedOnce = true;
    }

    public void showPhonePre(){
        PhonPrePopWindow ppp=new PhonPrePopWindow(getActivity(),tv_pre);
        ppp.showPopWin(tv_pre);
        icon_du.setText(getResources().getString(R.string.wlb_arrow));
    }

    public void onTapLogin(){

        try {
            String pre=tv_pre.getText().toString().equals(getResources().getString(R.string.others))?"":tv_pre.getText().toString().substring(1);
            Map params = new HashMap();
            params.put("_ChannelId","PMBS");
            if (login_name.getText().toString().contains("*")){
                params.put("LoginId",
                        DBHelper.getDataByKey(DataKey.userName).replace("+",""));
            }else {
                params.put("LoginId",
                        pre+login_name.getText().toString().trim());
            }
            encryped = new CSIICypher().encryptWithJiamiJi(pwdedit.getText().toString().trim(),dbp,hms,timestamp,"UTF-8",2);
            params.put("Password",encryped
                    .replace("+","%2B")
            );
            if ("zh".equals(DBHelper.getDataByKey(DataKey.language))){
                params.put("_locale","zh_TW");
            }else {
                params.put("_locale","en_US");
            }
            params.put("_vTokenName", login_verifycode.getText().toString().trim());
            params.put("LoginType","M");
            if (checkCode()&&checkPassword()){
                OnLoginClicked(params);
            }

        } catch (Exception e) {
            pwdedit.setText("");
            noticeUtils.showNotice(getResources().getString(R.string.retry));
            e.printStackTrace();
        }
    }
    private boolean checkCode() {
        if (LOGIN_TYPE!=1)
            return true;
        if (login_verifycode.getText().toString().equals("")) {
            showNotice(getResources().getString(R.string.enter_vfc));
            return false;
        }
        return true;
    }
    private boolean checkPassword() {
        if (pwdedit.getText().toString().equals("")||pwdedit.getText().length()<8) {
            showNotice(getResources().getString(R.string.insert_8_atleast));
            return false;
        }
        return true;
    }
    private void OnLoginClicked(Map params) throws Exception {
        //验证码交易获取到的cookie
        Trace.e("password==",pwdedit.getText().toString());
        List<Cookie> coos=
                OkHttpUtil.getDefault().getDefaultClient().cookieJar().loadForRequest(HttpUrl.parse(CommDictAction.imageCode));

        for (Cookie cookie:coos){
            Trace.e("getImageCodeCookie==",cookie.toString());
            DBHelper.insert(new Data(DataKey.cookie,cookie.toString()));
        }

        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .addHead("cookie",DBHelper.getDataByKey(DataKey.cookie))
                        .setUrl(CommDictAction.loginNormal)
                        .setRequestType(RequestType.POST)//设置请求方式
//                        .setNeedResponse(true)
//                        .setContentType(ContentType.JSON)
//                        .addHead("head", "test")//添加头参数
//                        .addParam("param", "test")//添加接口参数
                        .addParams(params)
//                        .setDelayExec(2, TimeUnit.SECONDS)//延迟2秒执行
                        .build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        login_verifycode.setText("");
                        pwdedit.setText("");
                        getImageCode();
                        noticeUtils.showNotice(info.getRetDetail());
//                        showNotice(error);
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String retDetail=info.getRetDetail();
                        String resultString=retDetail;
                        List<Cookie> cookies=
                                OkHttpUtil.getDefault().getDefaultClient().cookieJar().loadForRequest(HttpUrl.parse(CommDictAction.loginNormal));
                        try {
                            JSONObject result=new JSONObject(resultString);
                            if (result.get(_REJCODE).equals("000000")){

                                DBHelper.insert(new Data(DataKey.userName,result.optString("LoginId")));
                                BaseApplication.getInstance().getUser().setUserId(result.optString("LoginId"));
                                DBHelper.insert(new Data(DataKey.pre,tv_pre.getText().toString()));
                                DBHelper.insert(new Data(DataKey.isRememberUser,remerberuser.isChecked()+""));
                                if (result.has("Challenge")){
                                    if (result.opt("LoginType").equals("S")){
                                        State.isFirstLogin=result.optString("FirstLoginFlag").equals("Y")?true:false;
                                        State.isLogin=true;
                                        login_verifycode.setText("");
                                        for (Cookie cookie:cookies){
                                            if (!cookie.toString().startsWith("cookiesession1")){
                                                DBHelper.insert(new Data(DataKey.cookie,cookie.toString()));
                                            }
                                        }
                                        pwdedit.setText("");
                                        StockFlag=result.optString("StockFlag");
                                        if (!TextUtils.isEmpty(StockFlag)){
                                            EventBus.getDefault().post(new MessageEvent(BaseFragment.REFRESHGRIDVIEW));
                                        }
                                        QueryClientInfo();
                                    }else {
//                                        DynPwd=result.optString("DynPwd");
                                        GetTimeStampAndKeyWithoutEditor();
                                        Challenge=result.optString("Challenge");
                                        String PrincipalSeq=result.optString("PrincipalSeq");
                                        Intent in=new Intent(activity,LoginSmsDialog.class);
                                        in.putExtra("PrincipalSeq",PrincipalSeq);
                                        in.putExtra("Challenge",Challenge);
                                        startActivityForResult(in,LOGIN_M);
                                    }
                                }
                            }else{
                                State.isLogin=false;
                                pwdedit.setText("");
                                noticeUtils.showNotice(result.getString(_REJMSG));
                                getImageCode();
                                login_verifycode.setText("");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_cell:
                onTapLogin();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode){
            case LOGIN_M:
                if(null!=intent){
                    String code=intent.getStringExtra("code");
                    if (!TextUtils.isEmpty(intent.getStringExtra("Challenge"))){
                        Challenge=intent.getStringExtra("Challenge");
                    }
                    if (resultCode==activity.RESULT_OK){
                        Back2Login(code);
                    }
                }

                break;
        }
    }
    private void Back2Login(String code) {
        try {

            Map params = new HashMap();
            params.put("_ChannelId","PMBS");
            params.put("LoginId", DBHelper.getDataByKey(DataKey.userName));
//            String  encryped=new CSIICypher().encryptWithoutRemove(pwdedit.getText().toString().trim(),CommDictAction.SecurityPubKey,timestamp,"UTF-8",2);
            String encryped = new CSIICypher().encryptWithJiamiJi(pwdedit.getText().toString().trim(),dbp,hms,timestamp,"UTF-8",2);
            params.put("Password",encryped.replace("+","%2B"));
            params.put("_vTokenName", login_verifycode.getText().toString().trim());
            params.put("LoginType","S");
            params.put("DynPwd",code);
            if ("zh".equals(DBHelper.getDataByKey(DataKey.language))){

                params.put("_locale","zh_TW");
            }else {
                params.put("_locale","en_US");
            }
            params.put("Challenge",Challenge);
            if (checkCode()){
                OnLoginClicked(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
