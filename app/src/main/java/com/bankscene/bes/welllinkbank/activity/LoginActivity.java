package com.bankscene.bes.welllinkbank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;

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
import com.bankscene.bes.welllinkbank.core.WebViewActivity;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.module.User;
import com.bankscene.bes.welllinkbank.view.ActionSheet;
import com.bankscene.bes.welllinkbank.view.ClearableEditText;
import com.bankscene.bes.welllinkbank.view.IconFontTextView;
import com.bankscene.bes.welllinkbank.view.PhonPrePopWindow;
import com.bumptech.glide.Glide;
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

public class LoginActivity extends HttpActivity {

    @BindView(R.id.login_name)
    ClearableEditText login_name;
    @BindView(R.id.login_password)
    EditText pwdedit;
    @BindView(R.id.login_verifycode)
    EditText login_verifycode;
    @BindView(R.id.iv_verify)
    ImageView iv_verify;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.remerberuser)
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
    @BindView(R.id.tv_login_types)
    TextView tv_login_types;
    @BindView(R.id.gesture_container)
    FrameLayout mGestureContainer;
    @BindView(R.id.tv_tips)
    TextView mTextTip;
    @BindView(R.id.iv_logo)
    ImageView iv_logo;
    @BindView(R.id.tv_combine_login)
    ImageView tv_combine_login;
    private GestureContentView mGestureContentView;
    private boolean codeFlag=true;
    private String pwTxt;
    private int pwLen;
    protected String timestamp;
    private View pwd_invisiblebox;
    private boolean isNeedNumPwdSoftInput=true;
    private int DECODE_BYTEARRAY=1;
    private static final int SHOW_PRE_TEXT=2;
    private static String[] logintypes=new String[3];
    private ActionSheet mActionSheet;
    private static int LOGIN_TYPE=1;//1:密碼登陸 2：動態密碼登陸 3:手勢密碼登陸
    private String a1,a2,a3;
    private String[] types;
    private String Challenge;
    private String DynPwd;
    KeyBoardDialogUtils keyBoardDialogUtils;
    User user;

    private String RedirectclassName;
    static{
        ResId.connLineColorId = R.color.conn_line_color;
        ResId.gestureNodeNormalResId = R.mipmap.oval_select_nomal;
        ResId.gestureNodePressedResId = R.mipmap.group_all;
        ResId.gestureNodeSelectedWrong = R.mipmap.gesture_pattern_selected_wrong;
        ResId.patternNodeNormal = R.mipmap.oval_select_nomal;
        ResId.patternNodePressed = R.mipmap.wlb_circle;
    }
    private Gson gson;
    private final int LOGIN_M=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setActionBar() {
        initImmersionBar();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    public void onClickClose(View view){
        LoginActivity.this.finish();
    }

    @Override
    protected void initView() {
        super.initView();
        gson=new Gson();
        ResId.connLineColorId=R.color.transparent;
        EventBus.getDefault().register(this);
        user=BaseApplication.getInstance().getUser();
        InitKeyBoards();
        RedirectclassName=getIntent().getStringExtra(getResources().getString(R.string.className));
        iv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageCode();
            }
        });
        tv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhonePre();
            }
        });
        tv_login_types.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionSheet.show();
            }
        });
        getImageCode();
        types=new String[]{getResources().getString(R.string.verify_login),
//                getResources().getString(R.string.otp_login)
//                ,
                getResources().getString(R.string.cell_verify_login)
                ,getResources().getString(R.string.gesture_login)
        };
        LOGIN_TYPE=1;
        InitLoginTypeActionSheet();
//        Glide.with(this).load("http://seal.digicert.com/seals/cascade/?s=jNrVQDaD,3,m,127.0.0.1:8080").into(tv_combine_login);
        tv_combine_login.setOnClickListener(this);
        Trace.e("logintype",DBHelper.getDataByKey(DataKey.login_type));
        if (!TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.gesture_code))&&"true".equals(DBHelper.getDataByKey(DataKey.login_type))){
            InitGestureLogin();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_combine_login:
                Intent in=new Intent(LoginActivity.this, WebViewActivity.class);
                in.putExtra("url","https://seal.digicert.com/seals/popup/?tag=jNrVQDaD&url=127.0.0.1:8080&cbr="+System.currentTimeMillis());
                startActivity(in);
                break;
        }
    }

    private void MODE_VERIFY_LOGIN(){
        findViewById(R.id.ll_pwd_edits).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_gesture_login).setVisibility(View.GONE);
        findViewById(R.id.ll_verify).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_otp).setVisibility(View.GONE);
        ll_verify.setVisibility(View.VISIBLE);
        ll_otp.setVisibility(View.GONE);
        LOGIN_TYPE=1;
        btn_login.setText(getResources().getString(R.string.verify_login));
        InitLoginTypeActionSheet();
    }
    private void MODE_DYNAMIC_LOGIN(){
        findViewById(R.id.ll_pwd_edits).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_gesture_login).setVisibility(View.GONE);
        findViewById(R.id.ll_otp).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_verify).setVisibility(View.GONE);
        ll_verify.setVisibility(View.GONE);
        ll_otp.setVisibility(View.VISIBLE);
        LOGIN_TYPE=2;
        btn_login.setText(getResources().getString(R.string.otp_login));
        InitLoginTypeActionSheet();
    }
    private void MODE_CELL_LOGIN(){
        findViewById(R.id.ll_pwd_edits).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_gesture_login).setVisibility(View.GONE);
        findViewById(R.id.ll_verify).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_otp).setVisibility(View.GONE);
        btn_login.setText(getResources().getString(R.string.cell_verify_login));
        LOGIN_TYPE=3;
        InitLoginTypeActionSheet();
    }



    private void InitLoginTypeActionSheet() {
        switch (LOGIN_TYPE){
            case 1:
//                a1=types[1];
                a1=types[1];
                a2=types[2];
                break;
            case 2:
                a1=types[0];
                a2=types[1];
                a3=types[2];
                break;
            case 3:
                a1=types[0];
//                a2=types[1];
                a2=types[2];
                break;
            case 4:
                a1=types[0];
//                a2=types[1];
                a2=types[1];
                break;
        }
        mActionSheet=new ActionSheet(this);
        mActionSheet.addMenuItem(a1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a1.equals(types[0])){
                    MODE_VERIFY_LOGIN();
                }else if (a1.equals(types[1])){
                    MODE_CELL_LOGIN();
                }else if (a1.equals(types[2])){
                    InitGestureLogin();
                }
            }
        });
        mActionSheet.addMenuItem(a2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a2.equals(types[0])){
                    MODE_VERIFY_LOGIN();
                }else if (a2.equals(types[1])){
                    MODE_CELL_LOGIN();
                }else if (a2.equals(types[2])){
                    InitGestureLogin();
                }
            }
        });
//        mActionSheet.addMenuItem(a3, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (a3.equals(types[0])){
//                    MODE_VERIFY_LOGIN();
//                }else if (a3.equals(types[1])){
//                    MODE_DYNAMIC_LOGIN();
//                }else if (a3.equals(types[2])){
//                    MODE_CELL_LOGIN();
//                }else if (a3.equals(types[3])){
//                    InitGestureLogin();
//                }
//            }
//        });
//        mActionSheet.show();
    }

    private void InitGestureLogin() {
        if (TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.gesture_code))||!"true".equals(DBHelper.getDataByKey(DataKey.login_type))){
            noticeUtils.showNotice(getResources().getString(R.string.no_gs_setted));
            return;
        }

        findViewById(R.id.ll_pwd_edits).setVisibility(View.GONE);
        findViewById(R.id.ll_gesture_login).setVisibility(View.VISIBLE);
        mGestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {

            @Override
            public void onGestureCodeInput(String inputCode) {
                if (TextUtils.isEmpty(inputCode) || inputCode.length() < 6) {
                    mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>"+getResources().getString(R.string.atleast4)+"</font>"));
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }
                try {
                    String encryped=new CSIICypher().encryptWithoutRemove(inputCode,CommDictAction.SecurityPubKey,System.currentTimeMillis()+"","UTF-8",2);
                    Map params = new HashMap();
                    params.put("_ChannelId","PMBS");
                    params.put("LoginId",
                            DBHelper.getDataByKey(DataKey.userName));
                    params.put("Password",encryped.replace("+","%2B"));
                    params.put("DeviceId",BaseApplication.deviceId);
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
        mGestureContentView.setParentView(mGestureContainer);
        LOGIN_TYPE=4;
        InitLoginTypeActionSheet();
    }

    public void showPhonePre(){
        PhonPrePopWindow ppp=new PhonPrePopWindow(LoginActivity.this,tv_pre);
        ppp.showPopWin(tv_pre);
        icon_du.setText(getResources().getString(R.string.wlb_arrow));
    }

    private void getImageCode() {
        new GlideImageLoader().displayImage(this,CommDictAction.imageCode,iv_verify);
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

    private boolean checkUser() {
        if (login_name.getText().toString().equals("")) {
            showNotice("请输入用户名！");
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

    private boolean checkCode() {
        if (LOGIN_TYPE!=1)
            return true;
        if (login_verifycode.getText().toString().equals("")) {
            showNotice(getResources().getString(R.string.enter_vfc));
            return false;
        }
        return true;
    }

    private boolean checkOptCode() {
        if (LOGIN_TYPE!=2)
            return true;
        if (otp_verify.getText().toString().equals("")) {
            showNotice(getResources().getString(R.string.enter_opt));
            return false;
        }
        return true;
    }

    private void InitKeyBoards() {
        pwdedit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Trace.e("onfocus","pwd");
                    keyBoardDialogUtils=new KeyBoardDialogUtils(LoginActivity.this);
                    keyBoardDialogUtils.hideSystemSofeKeyboard(pwdedit);
//                    keyBoardDialogUtils.show(pwdedit);
                }
            }
        });
        pwdedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trace.e("onclick","pwd");
                keyBoardDialogUtils=new KeyBoardDialogUtils(LoginActivity.this);
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
                Trace.e("after changged==",s.toString());
            }
        });
        if (DBHelper.getDataByKey(DataKey.isRememberUser).equals("true")) {
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
    }
    private void OnLoginClicked(Map params) throws Exception {
        //验证码交易获取到的cookie

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
                                        DynPwd=result.optString("DynPwd");
                                        Challenge=result.optString("Challenge");
                                        String PrincipalSeq=result.optString("PrincipalSeq");
                                        Intent in=new Intent(LoginActivity.this,LoginSMSVerify.class);
                                        in.putExtra("PrincipalSeq",PrincipalSeq);
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

    private void QueryClientInfo() {
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
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
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
                                Intent in=new Intent(LoginActivity.this,Class.forName(RedirectclassName));
                                startActivity(in);
                                LoginActivity.this.finish();
                            }else {
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
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
                            Intent in=new Intent(LoginActivity.this, CodeReset2.class);
                            startActivity(in);
                        }else {
                            if (!TextUtils.isEmpty(RedirectclassName)){
                                Intent in=new Intent(LoginActivity.this,Class.forName(RedirectclassName));
                                startActivity(in);
                            }else {
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            }
                        }
                        LoginActivity.this.finish();
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

    public void onTapLogin(View v){
        try {
            String pre=tv_pre.getText().toString().equals(getResources().getString(R.string.others))?"":tv_pre.getText().toString().substring(1);
            String timestamp=System.currentTimeMillis()+"";
            Map params = new HashMap();
            params.put("_ChannelId","PMBS");
            if (login_name.getText().toString().contains("*")){
                params.put("LoginId",
                        DBHelper.getDataByKey(DataKey.userName).replace("+",""));
            }else {
                params.put("LoginId",
                        pre+login_name.getText().toString().trim());
            }
            String  encryped=new CSIICypher().encryptWithoutRemove(pwdedit.getText().toString().trim(),CommDictAction.SecurityPubKey,timestamp,"UTF-8",2);
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
            }else if (LOGIN_TYPE==3){
                params.put("LoginType","M");
            }else if (LOGIN_TYPE==2){
                params.put("LoginType","T");
                params.put("DynPwd",otp_verify.getText().toString().trim());
            }
            if (checkCode()&&checkPassword()){
                OnLoginClicked(params);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode){
            case LOGIN_M:
                if(null!=intent){
                    String code=intent.getStringExtra("code");
                    if (resultCode==RESULT_OK){
                        Back2Login(code);
                    }
                }

                break;
        }
    }

    private void Back2Login(String code) {
        try {
            String timestamp=System.currentTimeMillis()+"";

            Map params = new HashMap();
            params.put("_ChannelId","PMBS");
            params.put("LoginId", DBHelper.getDataByKey(DataKey.userName));
            String  encryped=new CSIICypher().encryptWithoutRemove(pwdedit.getText().toString().trim(),CommDictAction.SecurityPubKey,timestamp,"UTF-8",2);
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
