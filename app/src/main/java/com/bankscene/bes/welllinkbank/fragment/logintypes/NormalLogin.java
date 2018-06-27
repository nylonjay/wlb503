package com.bankscene.bes.welllinkbank.fragment.logintypes;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
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
import com.okhttplib.annotation.ContentType;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Nylon on 2018/6/4.17:15
 */

public class NormalLogin extends BaseTabFragment implements View.OnClickListener{
    static Activity mContext;
    ClearableEditText login_name;
    WlbEditText pwdedit;
    EditText login_verifycode;
    ImageView iv_verify;

    CheckBox remerberuser;
    LinearLayout ll_verify;
    LinearLayout ll_otp;
    TextView tv_pre;
    EditText otp_verify;
    IconFontTextView icon_du;
    Button btn_login;

    private boolean clicked=false;
    private KeyBoardDialogUtils keyBoardDialogUtils;
    View contentview;
    private String encryped;
    private int LOGIN_M=3;
    private boolean mHasLoadedOnce=false;


//    public NormalLogin(Activity context) {
//        mContext=context;
//
//    }

    public NormalLogin() {
    }
    public static final NormalLogin NewInstance(Activity activity){
        NormalLogin nl=new NormalLogin();
        mContext=activity;
        return nl;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (null!=contentview){
            return contentview;
        }
        EventBus.getDefault().register(this);
        contentview=inflater.inflate(R.layout.layout_login_normal, container, false);
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
    protected int setLayoutId() {
        return R.layout.layout_login_normal;
    }

    @Override
    public String getFragmentTitle() {
        return mContext.getResources().getString(R.string.verify_login);
    }

    @Override
    protected void initView() {
        super.initView();

    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        Trace.e("hidden",hidden+"");
//        if (hidden){
//            Trace.e("hidden",hidden+"");
//            getImageCode();
//        }
//    }
    //    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        getImageCode();
//    }
    //    @Override
//    protected void onVisible() {
//        super.onVisible();
//        if (mHasLoadedOnce)
//            getImageCode();
//    }
    //
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && !mHasLoadedOnce) {
//            Log.i("TestData", "FoundFragment 加载请求网络数据");
//            //TO-DO 执行网络数据请求
//
//
//            mHasLoadedOnce = true;
//        }
//    }

    private void InitKeyBoards(View contentview) {
        Trace.e("initKeyboard1","--------------");
        LOGIN_TYPE=1;
        pwdedit= (WlbEditText)contentview.findViewById(R.id.login_password);
        login_name= (ClearableEditText) contentview.findViewById(R.id.login_name);
        login_verifycode= (EditText) contentview.findViewById(R.id.login_verifycode);
        btn_login= (Button) contentview.findViewById(R.id.btn_login);
        remerberuser= (CheckBox) contentview.findViewById(R.id.remerberuser);
        iv_verify= (ImageView) contentview.findViewById(R.id.iv_verify);
        iv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageCode();
            }
        });
        tv_pre= (TextView) contentview.findViewById(R.id.tv_pre);
        icon_du=contentview.findViewById(R.id.icon_du);
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
        if (DBHelper.getDataByKey(DataKey.isRememberUser).equals("true")&&!TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.login_tab))&&DBHelper.getDataByKey(DataKey.login_tab).equals("1")) {
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
//        editTextList.add(login_verifycode);
        new ButtonState(editTextList, btn_login).onWatch();

        tv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhonePre();
            }
        });

    }
    public void showPhonePre(){
        PhonPrePopWindow ppp=new PhonPrePopWindow(getActivity(),tv_pre);
        ppp.showPopWin(tv_pre);
        icon_du.setText(getResources().getString(R.string.wlb_arrow));
    }
    private void getImageCode() {
        Trace.e("getimagecode","111111111111");
        new GlideImageLoader().displayImage(mContext, CommDictAction.imageCode,iv_verify);
        mHasLoadedOnce = true;
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
//            if (LOGIN_TYPE==1){
            params.put("LoginType","R");
//            }else if (LOGIN_TYPE==2){
//                params.put("LoginType","M");
//            }else if (LOGIN_TYPE==3){
//                params.put("LoginType","T");
//                params.put("DynPwd",dynamic_pwd_dyna.getText().toString().trim());
//            }
//            else if (LOGIN_TYPE==2){
//                params.put("LoginType","T");
//                params.put("DynPwd",otp_verify.getText().toString().trim());
//            }
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

    @TargetApi(21)
    private void forceSendRequestByMobileData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)activity. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(12);
        //强制使用蜂窝数据网络-移动数据
        builder.addTransportType(0);//使用手机流量
        NetworkRequest build = builder.build();
        connectivityManager.requestNetwork(build, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                try {
                    URL url = new URL("");
                    HttpURLConnection connection = (HttpURLConnection)network.openConnection(url);
                    /*******省略参数配置*******/
                    connection.connect();
                    /*******数据流处理*******/
                } catch (Exception e) {

                }

            }
        });
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
                        .addHead("Accept","text/mobilejson")
                        .addHead("cookie",DBHelper.getDataByKey(DataKey.cookie))
//                        .addHead("connection","keep-alive")
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
                                StockFlag=result.optString("StockFlag");
                                if (!TextUtils.isEmpty(StockFlag)){
                                    EventBus.getDefault().post(new MessageEvent(BaseFragment.REFRESHGRIDVIEW));
                                }
                                State.isLogin=true;
                                login_verifycode.setText("");
                                for (Cookie cookie:cookies){
                                    Trace.e("logincookie===",cookie.toString());
                                    if (!cookie.toString().startsWith("cookiesession1")){
                                        DBHelper.insert(new Data(DataKey.cookie,cookie.toString()));
                                    }
                                }
                                QueryClientInfo();
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
            case R.id.btn_login:
                onTapLogin();
                break;
        }
    }
}
