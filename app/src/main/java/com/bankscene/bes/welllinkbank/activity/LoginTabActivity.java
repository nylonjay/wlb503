package com.bankscene.bes.welllinkbank.activity;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.ButtonState;
import com.bankscene.bes.welllinkbank.Util.FileUtil;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.activity.mine.password.CodeReset2;
import com.bankscene.bes.welllinkbank.adapter.GlideImageLoader;
import com.bankscene.bes.welllinkbank.biz.ClientInfoBiz;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.biz.NameImgPair;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.common.Constant;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.HttpActivity;
import com.bankscene.bes.welllinkbank.core.State;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.exception.WLBException;
import com.bankscene.bes.welllinkbank.module.User;
import com.bankscene.bes.welllinkbank.view.ActionSheet;
import com.bankscene.bes.welllinkbank.view.ClearableEditText;
import com.bankscene.bes.welllinkbank.view.IconFontTextView;
import com.bankscene.bes.welllinkbank.view.PhonPrePopWindow;
import com.bankscene.bes.welllinkbank.view.TabHostClass;
import com.bankscene.bes.welllinkbank.view.TabHostSettingsClass;
import com.bankscene.bes.welllinkbank.view.WlbEditText;
import com.csii.gesturekeyboard.GestureContentView;
import com.csii.gesturekeyboard.GestureDrawline;
import com.csii.gesturekeyboard.ResId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kh.keyboard.CSIICypher;
import com.kh.keyboard.KeyBoardDialogUtils;
import com.kh.keyboard.SecurityCypherException;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
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

public class LoginTabActivity extends HttpActivity {
    @BindView(android.R.id.tabhost)
    TabHost tabHost;
    @BindView(android.R.id.tabs)
    TabWidget tabs;
    ClearableEditText login_name;
    WlbEditText pwdedit;
    EditText login_verifycode;
    ImageView iv_verify;
    Button btn_login;
    CheckBox remerberuser;
    @BindView(R.id.ll_verify)
    LinearLayout ll_verify;
    @BindView(R.id.ll_otp)
    LinearLayout ll_otp;
    @BindView(R.id.tv_pre)
    TextView tv_pre;
    @BindView(R.id.otp_verify)
    EditText otp_verify;
    @BindView(R.id.icon_du)
    IconFontTextView icon_du;

    @BindView(R.id.iv_logo)
    ImageView iv_logo;
    private TabHostSettingsClass tabHostSettingsClass;
    private GestureContentView mGestureContentView;
    private int LOGIN_TYPE=1;
    private Gson gson;
    private User user;
    private KeyBoardDialogUtils keyBoardDialogUtils;
    private String RedirectclassName;
    private String Challenge;
    private final int LOGIN_M=3;
    private final int SHOW_PRE_TEXT=2;
    FrameLayout mGestureContainer;
    TextView mTextTip;
    static{
        ResId.connLineColorId = R.color.conn_line_color;
        ResId.gestureNodeNormalResId = R.mipmap.oval_select_nomal;
        ResId.gestureNodePressedResId = R.mipmap.group_all;
        ResId.gestureNodeSelectedWrong = R.mipmap.gesture_pattern_selected_wrong;
        ResId.patternNodeNormal = R.mipmap.oval_select_nomal;
        ResId.patternNodePressed = R.mipmap.wlb_circle;
    }

    int tab=1;
    private String encryped="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login_tab;
    }

    @Override
    public void setActionBar(@Nullable Toolbar toolbar) {
        initImmersionBar();
    }

    @Override
    protected void initView() {
        gson=new Gson();
        EventBus.getDefault().register(this);
        user=BaseApplication.getInstance().getUser();
        if (!TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.login_tab))){
            tab=Integer.parseInt(DBHelper.getDataByKey(DataKey.login_tab));
        }
        tabHost.setCurrentTab(tab-1);
        if (tab==1){
            InitKeyBoards();
        }else if (tab==2){
            InitKeyBoards2();
        }
        RedirectclassName=getIntent().getStringExtra(getResources().getString(R.string.className));

        TabHostClass tabHostClass=new TabHostClass(tabHost,R.id.now_update,R.id.today_choose,getResources().getString(R.string.verify_login),getResources().getString(R.string.cell_verify_login));
        tabHostClass.setTabHost();
        tabHostSettingsClass=new TabHostSettingsClass(LoginTabActivity.this,tabHost,tabs);
        tabHostSettingsClass.setChangeTabSetting();
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                Trace.e("tabName",s);
                tabHostSettingsClass.setChangeTabSetting();
                if (s.equals("1")){
                    InitKeyBoards();
                    btn_login.setText(getResources().getString(R.string.verify_login));
                    LOGIN_TYPE=1;
                }else if (s.equals("2")){
                    InitKeyBoards2();
                    btn_login.setText(getResources().getString(R.string.cell_verify_login));
                    LOGIN_TYPE=2;

                }

            }
        });
        GetTimeStampAndKeyWithoutEditor();
        if (!TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.gesture_code))&&"true".equals(DBHelper.getDataByKey(DataKey.login_type))){
            CastGestureLogin();
        }
    }


    public void onClickClose(View view){
        LoginTabActivity.this.finish();
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
    public void showPhonePre(){
        PhonPrePopWindow ppp=new PhonPrePopWindow(LoginTabActivity.this,tv_pre);
        ppp.showPopWin(tv_pre);
        icon_du.setText(getResources().getString(R.string.wlb_arrow));
    }
    private void InitGestureLogin() {

        mGestureContainer= (FrameLayout) findViewById(R.id.gesture_container);
        mTextTip= (TextView) findViewById(R.id.tv_tips);
        mGestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {

            @Override
            public void onGestureCodeInput(String inputCode) {
                if (TextUtils.isEmpty(inputCode) || inputCode.length() < 6) {
                    mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>"+getResources().getString(R.string.atleast4)+"</font>"));
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }
                try {
                    String encryped=new CSIICypher().encryptWithJiamiJi(inputCode, dbp,hms,timestamp,"UTF-8",2);
                    Map params = new HashMap();
                    params.put("_ChannelId","PMBS");
                    params.put("LoginId",
                            DBHelper.getDataByKey(DataKey.userName));
                    params.put("Password",encryped.replace("+","%2B"));
                    params.put("DeviceId", BaseApplication.deviceId);
                    params.put("LoginType","K");
                    OnLoginClicked(params);
                } catch (SecurityCypherException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mGestureContentView.clearDrawlineState(0L);

            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });
        mGestureContentView.setVisibility(View.VISIBLE);
        mGestureContentView.setParentView(mGestureContainer);

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
                                DBHelper.insert(new Data(DataKey.pre,tv_pre.getText().toString()));
                                DBHelper.insert(new Data(DataKey.isRememberUser,remerberuser.isChecked()+""));

                                State.isFirstLogin=result.optString("FirstLoginFlag").equals("Y")?true:false;
                                if (result.has("Challenge")){
                                    if (result.opt("LoginType").equals("S")){
                                        State.isLogin=true;
                                        login_verifycode.setText("");
                                        for (Cookie cookie:cookies){
                                            Trace.e("登陆获取到的cookie==",cookie.toString());
                                            DBHelper.insert(new Data(DataKey.cookie,cookie.toString()));
                                        }
                                        QueryClientInfo();
                                    }else {
//                                        DynPwd=result.optString("DynPwd");
                                        Challenge=result.optString("Challenge");
                                        String PrincipalSeq=result.optString("PrincipalSeq");
                                        Intent in=new Intent(LoginTabActivity.this,LoginSmsDialog.class);
                                        in.putExtra("PrincipalSeq",PrincipalSeq);
                                        in.putExtra("Challenge",Challenge);
                                        startActivityForResult(in,LOGIN_M);
                                    }
                                }else {
                                    State.isLogin=true;
                                    login_verifycode.setText("");
                                    for (Cookie cookie:cookies){
                                        DBHelper.insert(new Data(DataKey.cookie,cookie.toString()));
                                    }
                                    QueryClientInfo();
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
    private void getImageCode() {
        new GlideImageLoader().displayImage(this,CommDictAction.imageCode,iv_verify);
    }
    private void QueryClientInfo() {
        DBHelper.insert(new Data(DataKey.login_tab,LOGIN_TYPE+""));
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        setProgressDisplay(true);
        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .addHead("cookie",DBHelper.getDataByKey(DataKey.cookie))
                        .setUrl(CommDictAction.QryCifInfo)
                        .setRequestType(RequestType.POST)//设置请求方式
//                        .setNeedResponse(true)
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        try {

                            String result=info.getRetDetail();
                            JSONObject json=new JSONObject(result);
                            if (!json.has("jsonError")){
                                ClientInfoBiz ciz=gson.fromJson(result,ClientInfoBiz.class);
                                user.setName(ciz.getCHN_NAME());
                                user.setMobileNum(ciz.getTEL_NO());
                                user.setEnglish_name(ciz.getENG_NAME());
                                Message msg=new Message();
                                msg.what=1;
                                msg.obj=ciz;
                                handler.sendMessage(msg);
                            }else {
                                JSONArray jar=json.getJSONArray("jsonError");
                                JSONObject error=jar.getJSONObject(0);
                                ToastUtils.showShortToast(getResources().getString(R.string.get_client_info));
                                startActivity(new Intent(LoginTabActivity.this,MainActivity.class));
                            }

                        } catch (Exception e) {
                            Trace.e("saveimgfailed",e.toString());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        try{
                            ToastUtils.showShortToast(info.getRetDetail());
                            if (!TextUtils.isEmpty(RedirectclassName)){
                                Intent in=new Intent(LoginTabActivity.this,Class.forName(RedirectclassName));
                                startActivity(in);
                                LoginTabActivity.this.finish();
                            }else {
                                startActivity(new Intent(LoginTabActivity.this,MainActivity.class));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    final ClientInfoBiz ciz= (ClientInfoBiz) msg.obj;
                    try {
                        if (TextUtils.isEmpty(getNameValueImagepath(DBHelper.getDataByKey(DataKey.userName)))) {
                            //缓存里有过登陆过的用户的头像
                            String imagepath = null;
                            if (!TextUtils.isEmpty(ciz.getImage())){
                                imagepath = FileUtil.stringToFile(ciz.getImage());
                                Trace.e("转换后的path--", imagepath);
                                DBHelper.insert(new Data(DataKey.userIcon, imagepath));
                                saveName_Image(DBHelper.getDataByKey(DataKey.userName),imagepath);
                            }
                        }else {
                            Trace.e("从内存中取到的--",  getNameValueImagepath(DBHelper.getDataByKey(DataKey.userName)));
                            DBHelper.insert(new Data(DataKey.userIcon, getNameValueImagepath(DBHelper.getDataByKey(DataKey.userName))));
                        }
                    } catch (Exception e) {
                        Trace.e("exception 528",e.toString());
                        e.printStackTrace();
                    }
                    try{
                        if (State.isFirstLogin){
                            Intent in=new Intent(LoginTabActivity.this, CodeReset2.class);
                            startActivity(in);
                        }else {
                            if (!TextUtils.isEmpty(RedirectclassName)){
                                Intent in=new Intent(LoginTabActivity.this,Class.forName(RedirectclassName));
                                startActivity(in);
                            }else {
                                startActivity(new Intent(LoginTabActivity.this,MainActivity.class));
                            }
                        }
                        LoginTabActivity.this.finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    break;
            }
        }
    };

    private String getNameValueImagepath(String loginid){

        String Nips=DBHelper.getDataByKey(DataKey.nips);
        if (!TextUtils.isEmpty(Nips)){
            String imagePath="";
            Type type=new TypeToken<List<NameImgPair>>(){}.getType();
            List<NameImgPair> nips=gson.fromJson(Nips,type);
            for (NameImgPair n:nips){
                if (n.getLoginId().equals(loginid)){
                    imagePath=n.getImagePath();
                }
            }
            return imagePath;
        }else {
            return "";
        }

    }

    private void saveName_Image(String loginId, String imagepath) {
        List<NameImgPair> nips;
        Type type=new TypeToken<List<NameImgPair>>(){}.getType();
        NameImgPair nip=new NameImgPair();
        nip.setLoginId(loginId);
        nip.setImagePath(imagepath);
        String Nips=DBHelper.getDataByKey(DataKey.nips);
        if (TextUtils.isEmpty(Nips)){
            nips=new ArrayList<>();
        }else {
            nips=gson.fromJson(Nips,type);
        }
        if (!Nips.contains(loginId)){
            nips.add(nip);
        }
        String FinalNipstr=gson.toJson(nips,type);
        DBHelper.insert(new Data(DataKey.nips,FinalNipstr));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode){
            case LOGIN_M:
                if(null!=intent){
                    String code=intent.getStringExtra("code");
                    if (!TextUtils.isEmpty(intent.getStringExtra("Challenge"))){
                        Challenge=intent.getStringExtra("Challenge");
                    }
                    if (resultCode==RESULT_OK){
                        Back2Login(code);
                    }
                }

                break;
        }
    }
    private void InitKeyBoards() {
        pwdedit= (WlbEditText) findViewById(R.id.login_password);
        login_name= (ClearableEditText) findViewById(R.id.login_name);
        login_verifycode= (EditText) findViewById(R.id.login_verifycode);
        btn_login= (Button) findViewById(R.id.btn_login);
        remerberuser= (CheckBox) findViewById(R.id.remerberuser);
        iv_verify= (ImageView) findViewById(R.id.iv_verify);
        iv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageCode();
            }
        });
        tv_pre= (TextView) findViewById(R.id.tv_pre);
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
                    Trace.e("onfocus","pwd");
                    keyBoardDialogUtils=new KeyBoardDialogUtils(LoginTabActivity.this);
                    keyBoardDialogUtils.hideSystemSofeKeyboard(pwdedit);
//                    keyBoardDialogUtils.show(pwdedit);
                }
            }
        });
        pwdedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trace.e("onclick","pwd");
                keyBoardDialogUtils=new KeyBoardDialogUtils(LoginTabActivity.this);
                keyBoardDialogUtils.hideSystemSofeKeyboard(pwdedit);
                keyBoardDialogUtils.show(pwdedit);
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
        getImageCode();
    }
    private void InitKeyBoards2() {
        pwdedit= (WlbEditText) findViewById(R.id.login_password_cell);
        login_name= (ClearableEditText) findViewById(R.id.login_name_cell);
        login_verifycode= (EditText) findViewById(R.id.login_verifycode_cell);
        btn_login= (Button) findViewById(R.id.btn_login_cell);
        remerberuser= (CheckBox) findViewById(R.id.remerberuser_cell);
        iv_verify= (ImageView) findViewById(R.id.iv_verify_cell);
        iv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageCode();
            }
        });
        tv_pre= (TextView) findViewById(R.id.tv_pre_cell);
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
                    Trace.e("onfocus","pwd");
                    keyBoardDialogUtils=new KeyBoardDialogUtils(LoginTabActivity.this);
                    keyBoardDialogUtils.hideSystemSofeKeyboard(pwdedit);
//                    keyBoardDialogUtils.show(pwdedit);
                }
            }
        });
        pwdedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trace.e("onclick","pwd");
                keyBoardDialogUtils=new KeyBoardDialogUtils(LoginTabActivity.this);
                keyBoardDialogUtils.hideSystemSofeKeyboard(pwdedit);
                keyBoardDialogUtils.show(pwdedit);
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
        getImageCode();
    }

    public void onTapLogin(View v){
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
            if (LOGIN_TYPE==1){
                params.put("LoginType","R");
            }else if (LOGIN_TYPE==2){
                params.put("LoginType","M");
            }
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

    private boolean checkPassword() {
        if (pwdedit.getText().toString().equals("")||pwdedit.getText().length()<8) {
            showNotice(getResources().getString(R.string.insert_8_atleast));
            return false;
        }
        return true;
    }

    private void Back2Login(String code) {
        try {
            String timestamp=System.currentTimeMillis()+"";

            Map params = new HashMap();
            params.put("_ChannelId","PMBS");
            params.put("LoginId", DBHelper.getDataByKey(DataKey.userName));
//            String  encryped=new CSIICypher().encryptWithoutRemove(pwdedit.getText().toString().trim(),CommDictAction.SecurityPubKey,timestamp,"UTF-8",2);
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

    private boolean checkCode() {
        if (LOGIN_TYPE!=1)
            return true;
        if (login_verifycode.getText().toString().equals("")) {
            showNotice(getResources().getString(R.string.enter_vfc));
            return false;
        }
        return true;
    }
    public void CastGestureLogin(){
        if (TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.gesture_code))||!"true".equals(DBHelper.getDataByKey(DataKey.login_type))){
            noticeUtils.showNotice(getResources().getString(R.string.no_gs_setted));
            return;
        }
        State.isGestureLogin=true;
        ((TextView)findViewById(R.id.tv_login_types)).setText(getResources().getString(R.string.use_pwd_login));
        findViewById(R.id.ll_pwd_edits).setVisibility(View.GONE);
        findViewById(R.id.ll_gesture_login).setVisibility(View.VISIBLE);
        InitGestureLogin();
    }

    public void SwitchLoginType(View v){
        if (!State.isGestureLogin){
            if (TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.gesture_code))||!"true".equals(DBHelper.getDataByKey(DataKey.login_type))){
                noticeUtils.showNotice(getResources().getString(R.string.no_gs_setted));
                return;
            }
            State.isGestureLogin=true;
            ((TextView)findViewById(R.id.tv_login_types)).setText(getResources().getString(R.string.use_pwd_login));
            findViewById(R.id.ll_pwd_edits).setVisibility(View.GONE);
            findViewById(R.id.ll_gesture_login).setVisibility(View.VISIBLE);
            InitGestureLogin();
        }else {
            State.isGestureLogin=false;
            ((TextView)findViewById(R.id.tv_login_types)).setText(getResources().getString(R.string.use_gesture_login));
            findViewById(R.id.ll_pwd_edits).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_gesture_login).setVisibility(View.GONE);
        }

    }

}

