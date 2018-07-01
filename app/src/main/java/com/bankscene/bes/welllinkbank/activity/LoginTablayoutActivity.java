package com.bankscene.bes.welllinkbank.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.FileUtil;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.activity.mine.password.CodeReset2;
import com.bankscene.bes.welllinkbank.adapter.FragmentAdapter;
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
import com.bankscene.bes.welllinkbank.fragment.logintypes.BaseTabFragment;
import com.bankscene.bes.welllinkbank.fragment.logintypes.DynamicLogin;
import com.bankscene.bes.welllinkbank.fragment.logintypes.NormalLogin;
import com.bankscene.bes.welllinkbank.fragment.logintypes.SMSLogin;
import com.bankscene.bes.welllinkbank.module.User;
import com.bankscene.bes.welllinkbank.view.NoscrollViewpage;
import com.bankscene.bes.welllinkbank.view.MyTabLayout;
import com.csii.gesturekeyboard.GestureContentView;
import com.csii.gesturekeyboard.GestureDrawline;
import com.csii.gesturekeyboard.ResId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kh.keyboard.CSIICypher;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class LoginTablayoutActivity extends HttpActivity {
    private MyTabLayout myTab;
    private NoscrollViewpage myVp;
    private List<BaseTabFragment> fragments;
    private FragmentAdapter adapter;
    FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    TextView mTextTip;
    static{
        ResId.connLineColorId = R.color.conn_line_color;
        ResId.gestureNodeNormalResId = R.mipmap.oval_select_nomal;
        ResId.gestureNodePressedResId = R.mipmap.group_all;
        ResId.gestureNodeSelectedWrong = R.mipmap.gesture_pattern_selected_wrong;
        ResId.patternNodeNormal = R.mipmap.oval_select_nomal;
        ResId.patternNodePressed = R.mipmap.wlb_circle;
    }

    private String RedirectclassName="";
    private Gson gson;
    private User user;
    private int tab=1;

    @Override
    public void setActionBar(@Nullable Toolbar toolbar) {
        initImmersionBar();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
//    @Subscribe(threadMode = ThreadMode.MainThread)
//    public void onMessageEventMainThread(MessageEvent messageEvent){
//        Trace.e(TAG,"MAIN_messageEvent==="+messageEvent.getMessage());
//        switch (messageEvent.getWhat()){
//            case 1:
//                setProgressDisplay(false);
////                refreshSelf();
//                break;
//            case SHOW_PRE_TEXT:
//                tv_pre.setText(messageEvent.getMessage());
//                icon_du.setText(getResources().getString(R.string.wlb_arrow_d));
//                break;
//            case Constant.DISMISS:
//                icon_du.setText(getResources().getString(R.string.wlb_arrow_d));
//                break;
//        }
//    }
    @Override
    protected void initView() {
        super.initView();
        gson=new Gson();

//        EventBus.getDefault().register(this);
        user=BaseApplication.getInstance().getUser();
        RedirectclassName=getIntent().getStringExtra(getResources().getString(R.string.className));
        initMyTabLayout();
        User u=getUserState(DBHelper.getDataByKey(DataKey.userName));
        if (u.isGestureOpen()&&u.isGestureSetted()){
            CastGestureLogin();
        }


    }
    public void onClickClose(View view){
        LoginTablayoutActivity.this.finish();
    }
    private void initMyTabLayout() {

        fragments = new LinkedList<BaseTabFragment>(); //方便 遍历

        //初始化  数据
        BaseTabFragment bf1=NormalLogin.NewInstance(this);
        BaseTabFragment bf2=SMSLogin.NewInstance(this);
        BaseTabFragment bf3=DynamicLogin.NewInstance(this);
        Bundle b=new Bundle();
        b.putString("redirect",RedirectclassName);
        if (!TextUtils.isEmpty(RedirectclassName)){
            bf1.setArguments(b);
            bf2.setArguments(b);
            bf3.setArguments(b);

        }

        fragments.add(bf1);
        fragments.add(bf2);
        fragments.add(bf3);

        //第一步：实现自定义  TabLayout  初始化 数据
        myTab = (MyTabLayout) findViewById(R.id.my_tab);
        myVp = (NoscrollViewpage) findViewById(R.id.my_viewPager);
        myVp.setCanScroll(false);
        FragmentPagerAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),this,fragments);
        //第二步：初始化  导航
        //获取   表头数据
        myTab.setTabData(fragments);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BaseApplication.WIDTH);
        myVp.setLayoutParams(layoutParams);
        myVp.setAdapter(adapter);
        if (!TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.login_tab))){
            tab=Integer.parseInt(DBHelper.getDataByKey(DataKey.login_tab));
        }
//        myVp.setCurrentItem(tab-1);
        //第三步：分别设置监听事件：
        myTab.setOnTabSelectListener(new MyTabLayout.OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                myVp.setCurrentItem(position);
            }
            @Override
            public void onTabReselect(int position) {
            }
        });
        myVp.setOnPageChangeListener(new NoscrollViewpage.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                myTab.setCurrentTab(position);
                DBHelper.insert(new Data(DataKey.login_tab,position+1+""));
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }
    @Override
    protected int setLayoutId() {
        return R.layout.activity_login_tablayout;
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
//                        login_verifycode.setText("");
//                        pwdedit.setText("");
//                        getImageCode();
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
                                State.isLogin=true;
                                for (Cookie cookie:cookies){
                                    if (!cookie.toString().startsWith("cookiesession1")){
                                        DBHelper.insert(new Data(DataKey.cookie,cookie.toString()));
                                    }
                                }
                                QueryClientInfo();
                            }else{
                                if (State.isGestureLogin){
                                    GetTimeStampAndKeyWithoutEditor();
                                }

                                State.isLogin=false;
                                noticeUtils.showNotice(resultString);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    protected void QueryClientInfo() {
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
                                startActivity(new Intent(LoginTablayoutActivity.this,MainActivity.class));
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
                                Intent in=new Intent(LoginTablayoutActivity.this,Class.forName(RedirectclassName));
                                startActivity(in);
                                LoginTablayoutActivity.this.finish();
                            }else {
                                startActivity(new Intent(LoginTablayoutActivity.this,MainActivity.class));
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
                            Intent in=new Intent(LoginTablayoutActivity.this, CodeReset2.class);
                            startActivity(in);
                        }else {
                            if (!TextUtils.isEmpty(RedirectclassName)){
                                Intent in=new Intent(LoginTablayoutActivity.this,Class.forName(RedirectclassName));
                                startActivity(in);
                            }else {
                                startActivity(new Intent(LoginTablayoutActivity.this,MainActivity.class));
                            }
                        }
                        LoginTablayoutActivity.this.finish();
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
    public void SwitchLoginType(View v){
        if (!State.isGestureLogin){
            if (TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.gesture_code))||!"true".equals(DBHelper.getDataByKey(DataKey.login_type))){
                noticeUtils.showNotice(getResources().getString(R.string.no_gs_setted));
                return;
            }
            GetTimeStampAndKeyWithoutEditor();
            State.isGestureLogin=true;
            ((TextView)findViewById(R.id.tv_login_types)).setText(getResources().getString(R.string.use_pwd_login));
            findViewById(R.id.ll_pwd_edits).setVisibility(View.GONE);
            findViewById(R.id.ll_gesture_login).setVisibility(View.VISIBLE);
            InitGestureLogin();
        }else {
            State.isGestureLogin=false;

            findViewById(R.id.ll_pwd_edits).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_gesture_login).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.tv_login_types)).setText(getResources().getString(R.string.use_gesture_login));
        }

    }

    public void CastGestureLogin(){
        GetTimeStampAndKeyWithoutEditor();
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
}
