package com.bankscene.bes.welllinkbank.fragment.logintypes;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.ButtonState;
import com.bankscene.bes.welllinkbank.Util.Trace;
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

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Nylon on 2018/6/4.17:19
 */

public class DynamicLogin extends BaseTabFragment implements View.OnClickListener{
    static Activity mContext;
    ClearableEditText login_name;
    WlbEditText pwdedit;
    EditText login_verifycode;
    ImageView iv_verify_dyna;

    CheckBox remerberuser;
    TextView tv_pre;
    IconFontTextView icon_du;
    Button btn_login;
    ClearableEditText dynamic_pwd_dyna;

    private boolean clicked=false;
    private KeyBoardDialogUtils keyBoardDialogUtils;
    View contentview;
    private String encryped;
    private int LOGIN_M=3;
    private boolean mHasLoadedOnce=false;

//    public DynamicLogin(Activity context) {
//        mContext=context;
//    }

    public DynamicLogin() {
    }
    public static final DynamicLogin NewInstance(Activity activity){
        DynamicLogin dl=new DynamicLogin();
        mContext=activity;
        return dl;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        if (null!=contentview){
            return contentview;
        }
        EventBus.getDefault().register(this);
        contentview=inflater.inflate(R.layout.layout_login_dynamic, container, false);
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
//            getImageCode();
//    }


    private void InitKeyBoards(View contentview) {
        Trace.e("initKeyboard3","--------------");
        LOGIN_TYPE=3;
        dynamic_pwd_dyna=contentview.findViewById(R.id.dynamic_pwd_dyna);
        pwdedit= (WlbEditText)contentview.findViewById(R.id.login_password_dyna);
        login_name= (ClearableEditText) contentview.findViewById(R.id.login_name_dyna);
        login_verifycode= (EditText) contentview.findViewById(R.id.login_verifycode_dyna);
        btn_login= (Button) contentview.findViewById(R.id.btn_login_dyna);
        remerberuser= (CheckBox) contentview.findViewById(R.id.remerberuser_dyna);
        iv_verify_dyna= (ImageView) contentview.findViewById(R.id.iv_verify_dyna);
        iv_verify_dyna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageCode();
            }
        });
        icon_du=contentview.findViewById(R.id.icon_du);
        tv_pre= (TextView) contentview.findViewById(R.id.tv_pre_dyna);
        login_name.clearFocus();
        btn_login.setOnClickListener(this);
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
                Trace.e("focusechanged",hasFocus+"");
                if (hasFocus&&clicked){
                    Trace.e("onfocus","pwd");
                    keyBoardDialogUtils=new KeyBoardDialogUtils(activity);
                    keyBoardDialogUtils.hideSystemSofeKeyboard(pwdedit);
                    keyBoardDialogUtils.show(pwdedit);
                    Trace.e("focusechanged","focus2show");
                }
            }
        });
        pwdedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trace.e("onclick","pwd");

                GetTimeStampAndKeyWithoutEditor();
                keyBoardDialogUtils=new KeyBoardDialogUtils(activity);
                keyBoardDialogUtils.hideSystemSofeKeyboard(pwdedit);
                keyBoardDialogUtils.show(pwdedit);
                clicked=true;
                Trace.e("focusechanged","click2show");
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
        if (DBHelper.getDataByKey(DataKey.isRememberUser).equals("true")&&!TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.login_tab))&&DBHelper.getDataByKey(DataKey.login_tab).equals("3")) {
            remerberuser.setChecked(true);
            String un=DBHelper.getDataByKey(DataKey.userName);
            un=un.substring(DBHelper.getDataByKey(DataKey.pre).length()-1);
            String f3=un.substring(0,3);
            String l3=un.substring(un.length()-3,un.length());
            String showwords=f3+"***"+l3;
            login_name.setText(showwords);
            tv_pre.setText(DBHelper.getDataByKey(DataKey.pre));
        }
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(login_name);
        editTextList.add(pwdedit);
        editTextList.add(dynamic_pwd_dyna);
        editTextList.add(login_verifycode);
        new ButtonState(editTextList, btn_login).onWatch();

        tv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhonePre();
            }
        });
//        getImageCode();
    }
    public void showPhonePre(){
        PhonPrePopWindow ppp=new PhonPrePopWindow(getActivity(),tv_pre);
        ppp.showPopWin(tv_pre);
        icon_du.setText(getResources().getString(R.string.wlb_arrow));
    }
    private void getImageCode() {
        Trace.e("getimagecode","33333333");
        new GlideImageLoader().displayImage(mContext, CommDictAction.imageCode,iv_verify_dyna);
        mHasLoadedOnce = true;
    }

    public void onClickClose(View view){
        activity.finish();
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
            params.put("LoginType","T");
            params.put("DynPwd",dynamic_pwd_dyna.getText().toString().trim());
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
                                pwdedit.setText("");
                                DBHelper.insert(new Data(DataKey.userName,result.optString("LoginId")));
                                BaseApplication.getInstance().getUser().setUserId(result.optString("LoginId"));
                                DBHelper.insert(new Data(DataKey.pre,tv_pre.getText().toString()));
                                DBHelper.insert(new Data(DataKey.isRememberUser,remerberuser.isChecked()+""));
                                State.isFirstLogin=result.optString("FirstLoginFlag").equals("Y")?true:false;
                                State.isLogin=true;
                                login_verifycode.setText("");
                                for (Cookie cookie:cookies){
                                    if (!cookie.toString().startsWith("cookiesession1")){
                                        DBHelper.insert(new Data(DataKey.cookie,cookie.toString()));
                                    }
                                }
                                StockFlag=result.optString("StockFlag");
                                if (!TextUtils.isEmpty(StockFlag)){
                                    EventBus.getDefault().post(new MessageEvent(BaseFragment.REFRESHGRIDVIEW));
                                }
                                QueryClientInfo();
                            }else{
                                State.isLogin=false;
                                pwdedit.setText("");
                                dynamic_pwd_dyna.setText("");
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
    protected int setLayoutId() {
        return R.layout.layout_login_dynamic;
    }

    @Override
    public String getFragmentTitle() {
        return mContext.getResources().getString(R.string.sync_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_dyna:
                onTapLogin();
                break;
        }
    }
}
